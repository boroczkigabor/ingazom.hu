package org.atos.commutermap.users.network.service;

public class UserLookupException extends RuntimeException {
    public UserLookupException(String message) {
        super(message);
    }

    public UserLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
