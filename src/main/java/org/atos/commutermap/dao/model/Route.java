package org.atos.commutermap.dao.model;

import org.atos.commutermap.dao.util.MonetaryAmountConverter;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
public class Route {
    @Id
    public Long id;
    @Column(name = "destinationID")
    public final Station destinationStation;
    @Column(name = "departureID")
    public final Station departureStation;
    @Convert(converter = MonetaryAmountConverter.class)
    @Column(name = "priceHUF")
    public final MonetaryAmount price;
    public final Duration duration;
    public final Integer distanceKm;
    public final LocalDateTime updateTime;

    protected Route() {
        this.departureStation = null;
        this.destinationStation = null;
        this.price = null;
        this.duration = null;
        distanceKm = null;
        this.updateTime = null;
    }

    public Route(Station departureStation, Station destinationStation, MonetaryAmount price, Duration duration, Integer distanceKm, LocalDateTime updateTime) {
        this.departureStation = departureStation;
        this.destinationStation = destinationStation;
        this.price = price;
        this.duration = duration;
        this.distanceKm = distanceKm;
        this.updateTime = updateTime;
    }
}
