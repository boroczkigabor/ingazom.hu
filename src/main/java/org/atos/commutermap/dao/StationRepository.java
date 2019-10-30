package org.atos.commutermap.dao;

import org.atos.commutermap.dao.model.Station;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StationRepository extends CrudRepository<Station, String> {

    default Optional<Station> findByName(String name) {
        return findById(name);
    }
}
