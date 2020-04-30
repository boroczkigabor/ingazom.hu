package org.atos.commutermap.dao.model;

import org.atos.commutermap.common.model.BaseClass;
import org.atos.commutermap.dao.util.DurationConverter;
import org.atos.commutermap.dao.util.LocalDateTimeToStringConverter;
import org.atos.commutermap.dao.util.MonetaryAmountConverter;

import javax.annotation.Nonnull;
import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@IdClass(Route.RoutePK.class)
public class Route extends BaseClass {
    @Id
    @Nonnull
    @JoinColumn(name = "destinationStation")
    @ManyToOne(targetEntity = Station.class)
    public final Station destinationStation;
    @Id
    @Nonnull
    @JoinColumn(name = "departureStation")
    @ManyToOne(targetEntity = Station.class)
    public final Station departureStation;
    @JoinColumn(name = "realDepartureStation")
    @ManyToOne(targetEntity = Station.class)
    public final Station realDepartureStation;
    @Convert(converter = MonetaryAmountConverter.class)
    @Column(name = "priceHUF")
    public final MonetaryAmount price;
    @Convert(converter = DurationConverter.class)
    public final Duration duration;
    public final Integer distanceKm;
    @Convert(converter = LocalDateTimeToStringConverter.class)
    public final LocalDateTime updateTime;
    @Column(columnDefinition = "BIT")
    private boolean inScope = true;

    protected Route() {
        this.departureStation = null;
        this.destinationStation = null;
        this.realDepartureStation = null;
        this.price = null;
        this.duration = null;
        distanceKm = null;
        this.updateTime = null;
    }

    public Route(Station departureStation, Station destinationStation, Station realDepartureStation, MonetaryAmount price, Duration duration, Integer distanceKm, LocalDateTime updateTime) {
        this.departureStation = departureStation;
        this.destinationStation = destinationStation;
        this.realDepartureStation = realDepartureStation;
        this.price = price;
        this.duration = duration;
        this.distanceKm = distanceKm;
        this.updateTime = updateTime;
    }

    public boolean isReachableWithinTime() {
        return duration != null && inScope;
    }

    public void markFarAway() {
        this.inScope = false;
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
