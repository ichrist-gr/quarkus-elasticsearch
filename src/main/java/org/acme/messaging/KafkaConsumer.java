package org.acme.messaging;

import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.service.EventFactory;
import org.acme.service.ElasticsearchService;
import org.acme.model.Event;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class KafkaConsumer {
    private final ElasticsearchService elasticsearchService;
    private final EventFactory eventFactory;

    @Inject
    public KafkaConsumer(ElasticsearchService elasticsearchService, EventFactory eventFactory) {
        this.elasticsearchService = elasticsearchService;
        this.eventFactory = eventFactory;
    }

    @Incoming("eventIn")
    public Uni<CreateResponse> incomingEvent(Event event) {
        Log.info("Received event: " + event.getEventId());

        CreateRequest<Event> createRequest = eventFactory.createEventRequest(event);
        return Uni.createFrom().completionStage(() -> elasticsearchService.createOperation(createRequest));
    }
}
