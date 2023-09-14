package org.acme.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Event;
import org.acme.service.EventService;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;


@Path("/event")
public class EventResource {
    private final EventService eventService;

    @Inject
    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @GET
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<List<Event>> retrieveEvents(@RestQuery("index") String index) {
        return eventService.retrieveEvents(index);
    }

    @GET
    @Path("/retrieve/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Event> retrieveEventByEventId(@RestPath("id") String eventId, @RestQuery("index") String index) {
        return eventService.retrieveEventByEventId(index, eventId);
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> retrieveIndexes(Event event) {
        return eventService.updateEvent(event);
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteEventByEventId(@RestPath("id") String eventId, @RestQuery("index") String index) {
        return eventService.deleteEventByEventId(index, eventId);
    }
}
