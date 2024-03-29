package org.atos.commutermap.batch.steps;

import org.atos.commutermap.batch.JobStatistics;
import org.atos.commutermap.batch.Util;
import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.network.model.ErrorMessage;
import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.hibernate.jdbc.BatchFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CallMavProcessor extends StepExecutionAware implements ItemProcessor<TravelOfferRequest, Route> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallMavProcessor.class);
    private static final String NUM_OF_EMPTIES = "numOfEmpties";

    private final MavinfoServerCaller serverCaller;
    private final OfferSelector offerSelector;
    private final RouteRepository routeRepository;

    public CallMavProcessor(MavinfoServerCaller serverCaller, OfferSelector offerSelector, RouteRepository routeRepository) {
        this.serverCaller = serverCaller;
        this.offerSelector = offerSelector;
        this.routeRepository = routeRepository;
    }

    @Override
    public void stepExecutionInitialized(StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().putInt(CallMavProcessor.NUM_OF_EMPTIES, 0);
    }

    @Override
    public Route process(TravelOfferRequest request) {
        LOGGER.info("Calling MAV for {} - {}.", request.departure.name, request.destination.name);
        TravelOfferResponse response = serverCaller.callServerWith(request);
        JobStatistics.mavRequestIssued();
        if (!response.errorMessages.isEmpty() || response.travelOffers.isEmpty()) {
            ErrorType errorType = ErrorType.fromErrorMessages(response.errorMessages);
            errorType.handleError(stepExecution);
            if (ErrorType.EXHAUSTED.equals(errorType)) {
                return process(request);
            } else {
                return null;
            }
        }
        Optional<Route> existingRoute = routeRepository.findById(new Route.RoutePK(request.departure.id, request.destination.id));
        TravelOffer bestOffer = offerSelector.selectBestOffer(response);
        return existingRoute.map(route -> route.updateWith(bestOffer))
                .orElseGet(() -> new Route(request.departure, request.destination, bestOffer.details.realDepartureStation, bestOffer.price, bestOffer.travelTime, bestOffer.distance, LocalDateTime.now()))
;
    }

    private enum ErrorType {
        EXHAUSTED("0") {
            @Override
            void handleError(StepExecution stepExecution) {
                LOGGER.warn("MAV server got exhausted, waiting 30 seconds...");
                Util.sleep(30_000);
            }
        },
        OUTDATED_VERSION("460") {
            @Override
            void handleError(StepExecution stepExecution) {
                LOGGER.error("MAV server integration is outdated, update required!");
                throw new BatchFailedException("MAV server integration is outdated, update required!");
            }
        },
        MISSING_PARAMETER("100") {
            @Override
            void handleError(StepExecution stepExecution) {
                OUTDATED_VERSION.handleError(stepExecution);
            }
        },
        OFFER_NOT_AVAILABLE("333") {
            @Override
            void handleError(StepExecution stepExecution) {
                int numOfErrorResponses = 1 + stepExecution.getJobExecution().getExecutionContext().getInt(NUM_OF_EMPTIES);
                stepExecution.getJobExecution().getExecutionContext().putInt(NUM_OF_EMPTIES, numOfErrorResponses);
                if (numOfErrorResponses > 50) {
                    throw new BatchFailedException("Too many empty responses came back, failing the job...");
                } else {
                    LOGGER.warn("{} empty responses came back so far.", numOfErrorResponses);
                }
            }
        },
        UNKNOWN("") {
            @Override
            void handleError(StepExecution stepExecution) {

            }
        };

        private final String messageID;

        ErrorType(String messageID) {
            this.messageID = messageID;
        }

        abstract void handleError(StepExecution stepExecution);

        static ErrorType fromErrorMessages(List<ErrorMessage> errorMessages) {
            String errorCode = extractErrorCode(errorMessages).orElse("");
            for (ErrorType errorType : values()) {
                if (errorType.messageID.equals(errorCode)) {
                    return errorType;
                }
            }
            return valueOf(errorCode);
        }

        private static Optional<String> extractErrorCode(List<ErrorMessage> list) {
            return list.stream().map(errorMessage -> errorMessage.ID).findFirst();
        }
    }
}
