package org.acme.messaging;

import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.service.ElasticFactory;
import org.acme.service.ElasticService;
import org.acme.model.Event;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class KafkaConsumer {
    private final ElasticService elasticService;
    private final ElasticFactory elasticFactory;

    @Inject
    public KafkaConsumer(ElasticService elasticService, ElasticFactory elasticFactory) {
        this.elasticService = elasticService;
        this.elasticFactory = elasticFactory;
    }

    @Incoming("eventIn")
    public Uni<CreateResponse> incomingEvent(Event event) {
        Log.info("Received event: " + event.getEventId());

        CreateRequest<Event> createRequest = elasticFactory.createEventRequest(event);
        return Uni.createFrom().completionStage(() -> elasticService.createOperation(createRequest));
    }
}
