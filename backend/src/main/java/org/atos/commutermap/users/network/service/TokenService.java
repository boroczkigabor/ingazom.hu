package org.atos.commutermap.users.network.service;

import org.atos.commutermap.users.model.ApplicationUser;

@FunctionalInterface
public interface TokenService {
    ApplicationUser retrieveUserDetailsWithToken(String authToken) throws UserLookupException;
}
