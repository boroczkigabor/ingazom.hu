package org.atos.commutermap.network.service;

import org.atos.commutermap.dao.model.Route;
import org.springframework.beans.factory.annotation.Value;

public class RouteDurationColorizer {

    @Value("${categories.green.upto}")
    private int greenUpto;
    @Value("${categories.yellow.upto}")
    private int yellowUpto;
    @Value("${categories.blue.upto}")
    private int blueUpto;

    public String getColorFor(Route route) {
        long minutesOfTravel = route.getDuration().toMinutes();
        String color;
        if (minutesOfTravel <= greenUpto) {
            color = "green";
        } else if (minutesOfTravel <= blueUpto) {
            color = "blue";
        } else if (minutesOfTravel <= yellowUpto) {
            color = "yellow";
        } else {
            color = "red";
        }

        return color;
    }
}
