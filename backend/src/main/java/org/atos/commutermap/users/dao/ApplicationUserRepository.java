package org.atos.commutermap.users.dao;

import org.atos.commutermap.users.model.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
}
