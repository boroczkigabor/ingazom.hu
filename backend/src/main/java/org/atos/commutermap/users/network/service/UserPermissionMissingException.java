package org.atos.commutermap.users.network.service;

public class UserPermissionMissingException extends UserLookupException {
    public UserPermissionMissingException(String message) {
        super(message);
    }
}
