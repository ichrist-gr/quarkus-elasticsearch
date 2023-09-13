package org.acme.messaging;


import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.transactions.KafkaTransactions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.model.Event;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

@ApplicationScoped
public class KafkaProducer {
    @Inject
    @Channel("eventOut")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 250)
    KafkaTransactions<Event> txProducer;

    public Uni<Void> publishKafkaEvent(Event event) {
        return txProducer.withTransaction(emitter -> {
                    emitter.send(event);
                    return Uni.createFrom().voidItem();
                })
                .onItem()
                .invoke(() -> Log.info("Successfully published event: " + event.getEventId()))
                .onFailure()
                .invoke(throwable -> Log.error("Failed to publish event: " + event.getEventId() + "because " + throwable.getMessage()));
    }
}
