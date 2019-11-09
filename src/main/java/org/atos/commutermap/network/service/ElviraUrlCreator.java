package org.atos.commutermap.network.service;

import com.google.common.escape.Escapers;
import com.google.common.net.UrlEscapers;
import org.atos.commutermap.dao.model.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ElviraUrlCreator {

    @Value("")
    private String elviraBaseUrl;

    /*
    * http://elvira.mav-start.hu/elvira.dll/x/uf?_charset_=UTF-8&i=BUDAPEST*&e=Magl%C3%B3d&v=&d=19.11.09
    */
    public String createElviraUrlFor(Route route) {
        return UriComponentsBuilder.fromHttpUrl(elviraBaseUrl)
                .pathSegment("x")
                .pathSegment("uf")
                .queryParam("_charset_", StandardCharsets.UTF_8.displayName())
                .queryParam("i", UrlEscapers.urlFragmentEscaper().escape(route.departureStation.name))
                .queryParam("e", UrlEscapers.urlFragmentEscaper().escape(route.destinationStation.name))
                .queryParam("d", LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                .build(true)
                .toUriString();
    }
}
