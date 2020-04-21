package org.atos.commutermap.dao;

import org.atos.commutermap.dao.model.Station;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StationRepository extends PagingAndSortingRepository<Station, String> {

    default Optional<Station> findByNameOrId(@Param("nameOrId") String nameOrID) {
        return findByNameOrId(nameOrID, nameOrID);
    }

    Optional<Station> findByNameOrId(@Param("name") String name, @Param("id") String id);
}
