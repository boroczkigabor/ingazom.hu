package org.atos.commutermap.dao;

import org.atos.commutermap.dao.model.BaseStation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BaseStationRepository extends CrudRepository<BaseStation, String> {
    Optional<BaseStation> findByName(String name);
}
