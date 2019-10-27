package org.atos.commutermap.network.model;

public interface FieldNames {
    // Request keys
    String WITHOUT_CACHE = "CacheNelkul";
    String DATE = "Datum";
    String IS_DEPARTURE_TIME = "IndulasiIdo";
    String SEARCH_CONDITIONS = "KeresesiFeltetelek";
    String LANGUAGE = "Nyelv";
    String SERVICES = "Szolgaltatasok";
    String UAID = "UAID";
    String PASSENGERS = "Utasok";
    String DEPARTURE_STATION = "IndAllomasID";
    String DESTINATION_STATION = "CelAllomasID";
    String ONLY_WITH_EXTRA_PAYMENT = "CsakFelarJel";
    String DISABLED_PERSON = "FogyatekkalElo";
    String DISCOUNTS = "Kedvezmenyek";
    String NUMBER_OF_TICKETS = "Mennyiseg";
    String PASSENGER_TYPE = "UtasTipus";

    // Response keys
    String TRAVEL_OFFERS = "UtazasiAjanlatok";
    String DEPARTURE_DATE = "IndDatum";
    String ARRIVAL_DATE = "ErkDatum";
    String TRAVEL_DURATION = "Idotartam";
    String TRAVEL_DISTANCE = "Km";
    String PRICE = "Ar";
    String TICKET_IS_AVAILABLE = "JegyAdhato";
}
