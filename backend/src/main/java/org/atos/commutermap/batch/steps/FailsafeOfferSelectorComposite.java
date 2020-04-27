package org.atos.commutermap.batch.steps;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.NoSuchElementException;

public class FailsafeOfferSelectorComposite implements OfferSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferSelector.class);

    private final Collection<OfferSelector> delegates;

    public FailsafeOfferSelectorComposite(OfferSelector... delegates) {
        this.delegates = ImmutableList.copyOf(Validate.notEmpty(delegates, "There must be at least one delegate provided"));
    }

    @Override
    public TravelOffer selectBestOffer(TravelOfferResponse response) {
        ImmutableMap.Builder<Class<? extends OfferSelector>, RuntimeException> builder = ImmutableMap.builder();
        for (OfferSelector delegate : delegates) {
            try {
                return delegate.selectBestOffer(response);
            } catch (RuntimeException re) {
                LOGGER.warn("Delegate {} has thrown exception during offer selection:\n{}", delegate.getClass().getSimpleName(), ExceptionUtils.getMessage(re));
                builder.put(delegate.getClass(), re);
            }
        }

        LOGGER.error("Delegates returned no best offer due to exceptions:\n{}", builder.build());
        throw new NoSuchElementException("Unable to select best offer using the delegate offer selectors.");
    }
}
