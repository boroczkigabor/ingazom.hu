package org.atos.commutermap.batch;

import com.google.common.collect.ImmutableSortedSet;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.network.model.ErrorMessage;
import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.hibernate.jdbc.BatchFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class CallMavProcessor implements ItemProcessor<TravelOfferRequest, Route> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallMavProcessor.class);

    private final MavinfoServerCaller serverCaller;

    public CallMavProcessor(MavinfoServerCaller serverCaller) {
        this.serverCaller = serverCaller;
    }

    @Override
    public Route process(TravelOfferRequest request) {
        LOGGER.info("Calling MAV for {} - {}.", request.departure.name, request.destination.name);
        TravelOfferResponse response = serverCaller.callServerWith(request);
        if (!response.errorMessages.isEmpty() || response.travelOffers.isEmpty()) {
            ErrorType errorType = ErrorType.fromErrorMessages(response.errorMessages);
            errorType.handleError();
            if (ErrorType.EXHAUSTED.equals(errorType)) {
                return process(request);
            }
        }
        ImmutableSortedSet<TravelOffer> sortedOffers = ImmutableSortedSet
                .<TravelOffer>orderedBy(Comparator.comparing(o -> o.travelTime))
                .addAll(response.travelOffers)
                .build();

        TravelOffer firstOffer = sortedOffers.first();

        return new Route(request.departure, request.destination, firstOffer.details.realDepartureStation, firstOffer.price, firstOffer.travelTime, firstOffer.distance, LocalDateTime.now());
    }

    private enum ErrorType {
        EXHAUSTED {
            @Override
            void handleError() {
                LOGGER.warn("MAV server got exhausted, waiting 30 seconds...");
                Util.sleep(30_000);
            }
        },
        OUTDATED_VERSION {
            @Override
            void handleError() {
                LOGGER.error("MAV server integration is outdated, update required!");
                throw new BatchFailedException("MAV server integration is outdated, update required!");
            }
        },
        MISSING_PARAMETER {
            @Override
            void handleError() {
                OUTDATED_VERSION.handleError();
            }
        },
        UNKNOWN {
            @Override
            void handleError() {

            }
        };

        abstract void handleError();

        static ErrorType fromErrorMessages(List<ErrorMessage> errorMessages) {
            Stream<ErrorMessage> stream = errorMessages.stream();
            if (hasErrorCode(stream, "0")) {
                return EXHAUSTED;
            } else if (hasErrorCode(stream, "460")) {
                return OUTDATED_VERSION;
            } else if (hasErrorCode(stream, "100")) {
                return MISSING_PARAMETER;
            } else {
                return UNKNOWN;
            }
        }

        private static boolean hasErrorCode(Stream<ErrorMessage> stream, String errorCode) {
            return stream.anyMatch(msg -> errorCode.equals(msg.ID));
        }
    }
}
