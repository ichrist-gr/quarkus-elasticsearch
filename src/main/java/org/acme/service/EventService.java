package org.acme.service;

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
    private final ElasticService elasticService;
    private final ElasticFactory elasticFactory;

    @Inject
    public EventService(KafkaProducer kafkaProducer, ElasticService elasticService, ElasticFactory elasticFactory) {
        this.kafkaProducer = kafkaProducer;
        this.elasticService = elasticService;
        this.elasticFactory = elasticFactory;
    }

    public Uni<Response> createEvent(Event event) {
        Log.info("Publishing event: " + event.getEventId());

        return kafkaProducer.publishKafkaEvent(event)
                .onItem()
                .transform(unused -> Response.ok().build());
    }

    public Uni<List<Event>> retrieveEvents() {
        Log.info("Retrieving all events");

        SearchRequest searchRequest = elasticFactory.searchEventsRequest();
        return Uni.createFrom().completionStage(() -> elasticService.searchOperation(searchRequest))
                .onItem()
                .transform(elasticFactory::extractEventList);
    }

    public Uni<Event> retrieveEventByEventId(String eventId) {
        Log.info("Retrieving event: " + eventId);

        SearchRequest searchRequest = elasticFactory.searchEventByEventIdRequest(eventId);
        return Uni.createFrom().completionStage(() -> elasticService.searchOperation(searchRequest))
                .onItem()
                .transform(elasticFactory::extractEvent);
    }

    public Uni<UpdateResponse<Event>> updateEvent(Event event) {
        Log.info("Updating event: " + event.getEventId());

        UpdateRequest<Event, Object> updateRequest = elasticFactory.updateEventRequest(event);
        return Uni.createFrom().completionStage(() -> elasticService.updateEvent(updateRequest));
    }
}
