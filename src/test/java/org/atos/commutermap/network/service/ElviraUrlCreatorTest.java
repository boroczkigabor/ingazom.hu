package org.atos.commutermap.network.service;

import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.network.model.TestData;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ElviraUrlCreatorTest {

    private ElviraUrlCreator creator;

    @BeforeEach
    void setUp() {
        creator = new ElviraUrlCreator();
        ReflectionTestUtils.setField(creator, "elviraBaseUrl", "http://localhost/elvira.dll");
    }

    @Test
    void createUrlFromARouteReturnsProperUrl() {
        Route route = new Route(TestData.STATION_BUDAPEST_STAR, TestData.STATION_MAGLOD, TestData.STATION_BUDAPEST_NYUGATI, Money.of(465, "HUF"),
                Duration.of(27, ChronoUnit.MINUTES), 17, LocalDateTime.now());

        String elviraUrl = creator.createElviraUrlFor(route);

        assertThat(elviraUrl).startsWith("http://localhost/elvira.dll/x/uf?_charset_=UTF-8&mikor=-1&i=BUDAPEST*&e=Magl%C3%B3d&d=");
    }
}