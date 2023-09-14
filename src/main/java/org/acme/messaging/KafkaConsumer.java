package org.acme.messaging;

import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.model.Event;
import org.acme.service.elasticsearch.ElasticsearchQueryFactory;
import org.acme.service.elasticsearch.ElasticsearchService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class KafkaConsumer {
    private final ElasticsearchService elasticsearchService;
    private final ElasticsearchQueryFactory elasticsearchQueryFactory;

    @Inject
    public KafkaConsumer(ElasticsearchService elasticsearchService, ElasticsearchQueryFactory elasticsearchQueryFactory) {
        this.elasticsearchService = elasticsearchService;
        this.elasticsearchQueryFactory = elasticsearchQueryFactory;
    }

    @Incoming("eventIn")
    public Uni<CreateResponse> incomingEvent(Event event) {
        Log.info("Received event: " + event.getEventId());

        CreateRequest<Event> createRequest = elasticsearchQueryFactory.createEventRequest(event);
        return Uni.createFrom().completionStage(() -> elasticsearchService.createOperation(createRequest));
    }
}
