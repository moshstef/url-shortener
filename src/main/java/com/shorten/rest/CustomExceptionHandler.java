package com.shorten.rest;


import com.shorten.exception.QuotaReachedException;
import com.shorten.exception.UnknownUrlException;
import com.shorten.exception.UserNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<Exception> {

    static class Error {
        String error;

        public String getError() {
            return error;
        }

        public Error(String error) {
            this.error = error;
        }
    }

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof QuotaReachedException) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new Error("Quota limit reached")).build();
        } else if (e instanceof UnknownUrlException) {
            return Response.status(Response.Status.NOT_FOUND).entity("Unknown url").build();
        } else if (e instanceof UserNotFoundException) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new Error("Unknown api key")).build();
        } else {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Error("Oops... something broke")).build();
        }
    }
}