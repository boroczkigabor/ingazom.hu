package org.atos.commutermap.users.network.service;

public class UserNotFoundException extends UserLookupException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
