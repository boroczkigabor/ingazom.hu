package org.atos.commutermap.users.network.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserApiControllerAdvice {

    @ExceptionHandler({
            UserPermissionMissingException.class,
            UserNotFoundException.class
    })
    public <E extends UserLookupException> ResponseEntity<String> handleUserLookupClientException(E clientException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(clientException.getMessage());
    }

    @ExceptionHandler({
            UserLookupException.class,
    })
    public ResponseEntity<String> handleUserLookupServerException(UserLookupException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

}
