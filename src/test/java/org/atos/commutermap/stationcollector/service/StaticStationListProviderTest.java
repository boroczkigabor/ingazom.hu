package org.atos.commutermap.stationcollector.service;

import org.atos.commutermap.stationcollector.service.StaticStationListProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StaticStationListProviderTest {

    private StaticStationListProvider provider;

    @BeforeEach
    void setUp() {
        provider = new StaticStationListProvider();
    }

    @Test
    void getStationsMustReturnNonEmptyCollections() {
        assertThat(provider.getStations()).isNotEmpty();
    }
}