package org.atos.commutermap.dao.model;

import org.atos.commutermap.dao.util.DurationConverter;
import org.atos.commutermap.dao.util.LocalDateTimeToStringConverter;
import org.atos.commutermap.dao.util.MonetaryAmountConverter;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@IdClass(Route.RoutePK.class)
public class Route extends BaseClass {
    @Id
    @JoinColumn(name = "destinationStation")
    @ManyToOne(targetEntity = Station.class)
    public final Station destinationStation;
    @Id
    @JoinColumn(name = "departureStation")
    @ManyToOne(targetEntity = Station.class)
    public final Station departureStation;
    @Convert(converter = MonetaryAmountConverter.class)
    @Column(name = "priceHUF")
    public final MonetaryAmount price;
    @Convert(converter = DurationConverter.class)
    public final Duration duration;
    public final Integer distanceKm;
    @Convert(converter = LocalDateTimeToStringConverter.class)
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

    public static class RoutePK extends BaseClass {
        private String departureStation;
        private String destinationStation;

        private RoutePK() {}

        public RoutePK(String departureStation, String destinationStation) {
            this.departureStation = departureStation;
            this.destinationStation = destinationStation;
        }
    }

}
