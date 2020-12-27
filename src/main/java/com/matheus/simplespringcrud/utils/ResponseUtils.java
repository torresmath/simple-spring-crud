package com.matheus.simplespringcrud.utils;

import com.matheus.simplespringcrud.exceptions.HttpException;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

public class ResponseUtils {

    private ResponseUtils() { throw new IllegalStateException("Utility Class"); }

    public static <T> Optional<List<T>> listToOptional(List<T> list) {
        return list.isEmpty() ? Optional.empty() : Optional.of(list);
    }

    public static ResponseEntity<Entity> errorToResponse(BindingResult bindingResult) {
        ObjectError objectError = bindingResult.getAllErrors().get(0);
        return ResponseEntity.badRequest().body((new ErrorEntity(
                objectError.getDefaultMessage(),
                HttpStatus.BAD_REQUEST)));
    }

    public static <T extends HttpException> ResponseEntity<Entity> errorToResponse(T e) {
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorEntity(e.getMessage(), e.getStatusCode()));
    }

    public static <T extends Entity> ResponseEntity<Entity> toOkResponse(T obj) {
        return ResponseEntity.ok(obj);
    }

    public static <T extends Entity> ResponseEntity<Entity> listToOkResponse(List<T> obj) {
        return ResponseEntity.ok(new ListEntity<>(obj));
    }

    public static <T extends Either<HttpException, ? extends Entity>> ResponseEntity<Entity> toCreatedResponse(T obj) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(obj.get());
    }
}
