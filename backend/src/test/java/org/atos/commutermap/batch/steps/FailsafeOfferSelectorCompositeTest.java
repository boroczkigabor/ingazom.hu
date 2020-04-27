package org.atos.commutermap.batch.steps;

import com.google.common.collect.ImmutableList;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FailsafeOfferSelectorCompositeTest {

    @Mock
    private OfferSelector delegate;

    @Test
    void selectWhenOnlyOneSelectorIsProvidedReturningObject_MustReturnObject() {
        TravelOfferResponse response = new TravelOfferResponse(ImmutableList.of(), ImmutableList.of());
        Mockito.when(delegate.selectBestOffer(response))
                .thenReturn(TestUtil.createTravelOffer("00:21"));

        FailsafeOfferSelectorComposite offerSelector = new FailsafeOfferSelectorComposite(delegate);

        assertThat(offerSelector.selectBestOffer(response)).isNotNull();
        Mockito.verify(delegate, only()).selectBestOffer(response);
    }

    @Test
    void selectWhenTwoSelectorsAreProvidedFirstAlreadyReturningObject_MustReturnObject_NotCallSecondDelegate() {
        TravelOfferResponse response = new TravelOfferResponse(ImmutableList.of(), ImmutableList.of());
        Mockito.when(delegate.selectBestOffer(response))
                .thenReturn(TestUtil.createTravelOffer("00:21"));

        FailsafeOfferSelectorComposite offerSelector = new FailsafeOfferSelectorComposite(delegate, delegate);

        assertThat(offerSelector.selectBestOffer(response)).isNotNull();
        Mockito.verify(delegate, only()).selectBestOffer(response);
    }

    @Test
    void selectWhenTwoSelectorsAreProvidedFirstThrowingSecondReturningObject_MustReturnObject_NotCallSecondDelegate() {
        TravelOfferResponse response = new TravelOfferResponse(ImmutableList.of(), ImmutableList.of());
        Mockito.when(delegate.selectBestOffer(response))
                .thenThrow(NoSuchElementException.class)
                .thenReturn(TestUtil.createTravelOffer("00:21"));

        FailsafeOfferSelectorComposite offerSelector = new FailsafeOfferSelectorComposite(delegate, delegate);

        assertThat(offerSelector.selectBestOffer(response)).isNotNull();
        Mockito.verify(delegate, times(2)).selectBestOffer(response);
    }

    @Test
    void selectWhenTwoSelectorsAreProvidedBothThrowing_MustThrow() {
        TravelOfferResponse response = new TravelOfferResponse(ImmutableList.of(), ImmutableList.of());
        Mockito.when(delegate.selectBestOffer(response))
                .thenThrow(NoSuchElementException.class);
        OfferSelector secondMock = Mockito.mock(FailsafeOfferSelectorComposite.class); //making sure Class is of different type here

        Mockito.when(secondMock.selectBestOffer(response))
                .thenThrow(NoSuchElementException.class);

        FailsafeOfferSelectorComposite offerSelector = new FailsafeOfferSelectorComposite(delegate, secondMock);

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> offerSelector.selectBestOffer(response),
                "Unable to select best offer using the delegate offer selectors.");

        Mockito.verify(delegate, only()).selectBestOffer(response);
        Mockito.verify(secondMock, only()).selectBestOffer(response);
    }

}