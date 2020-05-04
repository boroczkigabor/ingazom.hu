package org.atos.commutermap.users.dao;

import org.atos.commutermap.users.model.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByEmail(String email);
}
