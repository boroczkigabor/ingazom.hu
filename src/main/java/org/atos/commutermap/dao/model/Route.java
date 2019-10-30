package org.atos.commutermap.dao.model;

import org.atos.commutermap.dao.util.MonetaryAmountConverter;

import javax.money.MonetaryAmount;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class Route {
    @Id
    public Long id;
    public final Station departureStation;
    public final Station destinationStation;
    @Convert(converter = MonetaryAmountConverter.class)
    public final MonetaryAmount price;
    public final Duration travelDuration;
    public final LocalDateTime updateTime;

    public Route(Station departureStation, Station destinationStation, MonetaryAmount price, Duration travelDuration, LocalDateTime updateTime) {
        this.departureStation = departureStation;
        this.destinationStation = destinationStation;
        this.price = price;
        this.travelDuration = travelDuration;
        this.updateTime = updateTime;
    }
}
