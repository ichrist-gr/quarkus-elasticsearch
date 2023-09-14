package org.acme.service;

import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.messaging.KafkaProducer;
import org.acme.model.Event;

import java.util.List;

@ApplicationScoped
public class EventService {
    private final KafkaProducer kafkaProducer;
    private final ElasticsearchService elasticsearchService;
    private final EventFactory eventFactory;

    @Inject
    public EventService(KafkaProducer kafkaProducer,
                        ElasticsearchService elasticsearchService,
                        EventFactory eventFactory) {
        this.kafkaProducer = kafkaProducer;
        this.elasticsearchService = elasticsearchService;
        this.eventFactory = eventFactory;
    }

    public Uni<Response> createEvent(Event event) {
        Log.info("Publishing event: " + event.getEventId());

        return kafkaProducer.publishKafkaEvent(event)
                .onItem()
                .transform(unused -> Response.ok().build());
    }

    public Uni<List<Event>> retrieveEvents() {
        Log.info("Retrieving all events");

        SearchRequest searchRequest = eventFactory.searchEventsRequest();
        return Uni.createFrom().completionStage(() -> elasticsearchService.searchOperation(searchRequest))
                .onItem()
                .transform(eventFactory::extractEventList);
    }

    public Uni<Event> retrieveEventByEventId(String eventId) {
        Log.info("Retrieving event: " + eventId);

        SearchRequest searchRequest = eventFactory.searchEventByEventIdRequest(eventId);
        return Uni.createFrom().completionStage(() -> elasticsearchService.searchOperation(searchRequest))
                .onItem()
                .transform(eventFactory::extractEvent);
    }

    public Uni<UpdateResponse<Event>> updateEvent(Event event) {
        Log.info("Updating event: " + event.getEventId());

        UpdateRequest<Event, Object> updateRequest = eventFactory.updateEventRequest(event);
        return Uni.createFrom().completionStage(() -> elasticsearchService.updateEvent(updateRequest));
    }

    public Uni<Response> deleteEventByEventId(String eventId) {
        Log.info("Deleting event: " + eventId);

        DeleteRequest deleteRequest = eventFactory.deleteEventByEventIdRequest(eventId);
        return Uni.createFrom().completionStage(() -> elasticsearchService.deleteOperation(deleteRequest))
                .onItem()
                .transform(deleteResponse -> Response.ok().build());
    }
}
