package org.acme.resource;

import co.elastic.clients.elasticsearch.core.UpdateResponse;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Event;
import org.acme.service.EventService;
import org.jboss.resteasy.reactive.RestPath;

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
    public Uni<List<Event>> retrieveEvents() {
        return eventService.retrieveEvents();
    }

    @GET
    @Path("/retrieve/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Event> retrieveEventByEventId(@RestPath("id") String eventId) {
        return eventService.retrieveEventByEventId(eventId);
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<UpdateResponse<Event>> retrieveIndexes(Event event) {
        return eventService.updateEvent(event);
    }
}
