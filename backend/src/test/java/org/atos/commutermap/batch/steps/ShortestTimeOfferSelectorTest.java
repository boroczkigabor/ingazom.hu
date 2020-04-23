package org.atos.commutermap.batch.steps;

import com.google.common.collect.ImmutableList;
import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferDetails;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShortestTimeOfferSelectorTest {

    private OfferSelector offerSelector;

    @BeforeEach
    void setUp() {
        offerSelector = new ShortestTimeOfferSelector();
    }

    @Test
    void selectWhenThereIsOneOfferOnlyMustReturnThat() {
        TravelOffer onlyTravelOffer = createTravelOffer("00:23");

        TravelOffer travelOffer = offerSelector.selectBestOffer(new TravelOfferResponse(
                ImmutableList.of(onlyTravelOffer),
                ImmutableList.of()
        ));
        assertThat(travelOffer).isSameAs(onlyTravelOffer);
    }

    @Test
    void selectWhenThereAreMultipleOffersMustReturnOneWithShortestTime() {
        TravelOfferResponse travelOfferResponse = new TravelOfferResponse(
                ImmutableList.of(
                        createTravelOffer("00:05"),
                        createTravelOffer("00:03"),
                        createTravelOffer("00:02"),
                        createTravelOffer("00:04"),
                        createTravelOffer("00:01"),
                        createTravelOffer("00:06")
                ),
                ImmutableList.of()
        );

        TravelOffer bestOffer = offerSelector.selectBestOffer(travelOfferResponse);
        assertThat(bestOffer.travelTime.toMinutes()).isEqualTo(1L);
    }

    @Test
    void selectWhenThereAreMultipleOffersWithSameValueMustReturnOneOfThem() {
        TravelOfferResponse travelOfferResponse = new TravelOfferResponse(
                ImmutableList.of(
                        createTravelOffer("00:05"),
                        createTravelOffer("00:05"),
                        createTravelOffer("00:05"),
                        createTravelOffer("00:05"),
                        createTravelOffer("00:05")
                ),
                ImmutableList.of()
        );

        TravelOffer bestOffer = offerSelector.selectBestOffer(travelOfferResponse);
        assertThat(bestOffer.travelTime.toMinutes()).isEqualTo(5L);
    }

    @Test
    void selectWhenResponseHasNoOffersMustThrowException() {
        assertThrows(NoSuchElementException.class, () ->
                offerSelector.selectBestOffer(new TravelOfferResponse(
                        ImmutableList.of(),
                        ImmutableList.of()
                )));
    }

    private TravelOffer createTravelOffer(String travelTime) {
        return new TravelOffer(1L, 2L, travelTime, 5, 450, true, new TravelOfferDetails[]{});
    }
}