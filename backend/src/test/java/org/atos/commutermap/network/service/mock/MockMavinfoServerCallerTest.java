package org.atos.commutermap.network.service.mock;

import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MockMavinfoServerCallerTest {

    private MavinfoServerCaller mockCaller;

    @BeforeEach
    void setUp() {
        mockCaller = new MockMavinfoServerCaller();
    }

    @Test
    void callShouldReturnAProperObject() {
        assertThat(mockCaller.callServerWith(null))
                .isNotNull();
    }
}