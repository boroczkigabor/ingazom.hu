package org.atos.commutermap.dao.model;

import org.atos.commutermap.common.model.BaseClass;
import org.atos.commutermap.dao.util.DurationConverter;
import org.atos.commutermap.dao.util.LocalDateTimeToStringConverter;
import org.atos.commutermap.dao.util.MonetaryAmountConverter;
import org.atos.commutermap.network.model.TravelOffer;

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
    private Station destinationStation;

    @Id
    @Nonnull
    @JoinColumn(name = "departureStation")
    @ManyToOne(targetEntity = Station.class)
    private Station departureStation;

    @JoinColumn(name = "realDepartureStation")
    @ManyToOne(targetEntity = Station.class)
    private Station realDepartureStation;

    @Convert(converter = MonetaryAmountConverter.class)
    @Column(name = "priceHUF")
    private MonetaryAmount price;

    @Convert(converter = DurationConverter.class)
    private Duration duration;

    private Integer distanceKm;

    @Convert(converter = LocalDateTimeToStringConverter.class)
    private LocalDateTime updateTime;

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

    @Nonnull
    public Station getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(@Nonnull Station destinationStation) {
        this.destinationStation = destinationStation;
    }

    @Nonnull
    public Station getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(@Nonnull Station departureStation) {
        this.departureStation = departureStation;
    }

    public Station getRealDepartureStation() {
        return realDepartureStation;
    }

    public void setRealDepartureStation(Station realDepartureStation) {
        this.realDepartureStation = realDepartureStation;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public void setPrice(MonetaryAmount price) {
        this.price = price;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Integer getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Integer distanceKm) {
        this.distanceKm = distanceKm;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isInScope() {
        return inScope;
    }

    public void setInScope(boolean inScope) {
        this.inScope = inScope;
    }

    public Route updateWith(TravelOffer bestOffer) {
        setDistanceKm(bestOffer.distance);
        setDuration(bestOffer.travelTime);
        setPrice(bestOffer.price);
        setUpdateTime(LocalDateTime.now());
        return this;
    }

    public RoutePK privateKey() {
        return new RoutePK(this.departureStation.id, this.destinationStation.id);
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
