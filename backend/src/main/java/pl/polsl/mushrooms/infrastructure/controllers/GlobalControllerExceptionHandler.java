package pl.polsl.mushrooms.infrastructure.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.polsl.mushrooms.application.exceptions.EntityAlreadyExistException;
import pl.polsl.mushrooms.application.exceptions.NoRequiredPermissions;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.NotFoundException;

/**
 * Created by pawel_zaqkxkn on 25.04.2017.
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ExceptionHandler(EntityAlreadyExistException.class)
    public void handleConflict() {
        //
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @ExceptionHandler(EntityNotFoundException.class)
    public void entityNotFound() {
        //
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(NoRequiredPermissions.class)
    public void noRequiredPermission() {
        //
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    @ExceptionHandler(NotFoundException.class)
    public void notFound() {
        //
    }
}
