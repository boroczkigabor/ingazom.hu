package org.atos.commutermap.batch.steps;

import com.google.common.collect.ImmutableList;
import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.atos.commutermap.batch.steps.TestUtil.createTravelOffer;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MostCommonTimeOfferSelectorTest {

    private OfferSelector selector;

    @BeforeEach
    void setUp() {
        selector =  new MostCommonTimeOfferSelector();
    }

    @Test
    void selectWhenResponseHasNoOffersMustThrowException() {
        assertThrows(NoSuchElementException.class, () ->
                selector.selectBestOffer(new TravelOfferResponse(
                        ImmutableList.of(),
                        ImmutableList.of()
                )));
    }

    @Test
    void selectWhenThereIsOnlyOneItemInResponse_MustReturnThatItem() {
        TravelOffer travelOffer = createTravelOffer("00:23");

        TravelOffer bestOffer = selector.selectBestOffer(new TravelOfferResponse(
                ImmutableList.of(travelOffer),
                ImmutableList.of()
        ));

        assertThat(bestOffer)
                .isSameAs(travelOffer);
    }

    @Test
    void selectWhenThereAreItemsWithSameTimeMustReturnOneOfThem() {
        TravelOfferResponse travelOfferResponse = createOfferResponse(
                createTravelOffer("00:22"),
                createTravelOffer("00:22"),
                createTravelOffer("00:22"),
                createTravelOffer("00:22"),
                createTravelOffer("00:22"),
                createTravelOffer("00:22"),
                createTravelOffer("00:22")
                );

        TravelOffer bestOffer = selector.selectBestOffer(travelOfferResponse);

        assertThat(bestOffer.travelTime.toMinutes())
                .isEqualTo(22);
    }

    @Test
    void selectWhenShortestIsNotTheMostCommon_MustReturnCommonItem() {
        TravelOfferResponse offerResponse = createOfferResponse(
                createTravelOffer("00:19"),
                createTravelOffer("00:23"),
                createTravelOffer("00:23"),
                createTravelOffer("00:23"),
                createTravelOffer("00:21"),
                createTravelOffer("00:23"),
                createTravelOffer("00:23"),
                createTravelOffer("00:23")
        );

        TravelOffer bestOffer = selector.selectBestOffer(offerResponse);
        assertThat(bestOffer.travelTime.toMinutes())
                .isEqualTo(23);
    }

    private TravelOfferResponse createOfferResponse(TravelOffer... travelOffers) {
        return new TravelOfferResponse(
                ImmutableList.copyOf(travelOffers),
                ImmutableList.of()
        );
    }

    @Test
    void selectWhenResponseHasTwoDifferentItems_MustReturnShorter() {
        TravelOfferResponse travelOfferResponse = createOfferResponse(
                createTravelOffer("00:23"),
                createTravelOffer("00:22"),
                createTravelOffer("00:23"),
                createTravelOffer("00:22")
        );

        TravelOffer bestOffer = selector.selectBestOffer(travelOfferResponse);

        assertThat(bestOffer.travelTime.toMinutes())
                .isEqualTo(22);
    }
}