package com.shorten.rest;

import com.shorten.api.ShortenRequest;
import com.shorten.service.ShortenService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

@Path("/")
public class ShortenResource {

    @Inject
    ShortenService shortenService;

    @Path("/api/shorten")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response shorten(ShortenRequest request, @HeaderParam("X-API-Key") String apiKey) throws Exception {
        String shortUrl = shortenService.generateShortUrl(request.getUrl(), apiKey);
        return Response.ok(Map.of("shortUrl", shortUrl)).build();
    }

    @Path("/{urlHash}")
    @GET
    public Response redirect(@PathParam("urlHash") String urlHash) throws Exception {
        String realUrl = shortenService.retrieveRealUrl(urlHash);
        return Response.temporaryRedirect(URI.create(realUrl)).build();
    }
}
