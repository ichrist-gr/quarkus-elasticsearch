package org.acme.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.ResponseBody;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.acme.config.ElasticConfigs;
import org.acme.model.Event;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Dependent
public class EventFactory {
    private final ElasticConfigs elasticConfigs;

    @Inject
    public EventFactory(ElasticConfigs elasticConfigs) {
        this.elasticConfigs = elasticConfigs;
    }

    public CreateRequest<Event> createEventRequest(Event event) {
        return CreateRequest.of(
                builder -> builder
                        .index(elasticConfigs.indexName())
                        .id(event.getEventId())
                        .document(event)
        );
    }

    public SearchRequest searchEventsRequest() {
        return SearchRequest.of(
                builder -> builder
                        .index(elasticConfigs.indexName())
                        .query(QueryBuilders.matchAll().build()._toQuery())
        );
    }

    public SearchRequest searchEventByEventIdRequest(String eventId) {
        return SearchRequest.of(
                builder -> builder
                        .index(elasticConfigs.indexName())
                        .query(QueryBuilders.match().field("eventId").query(FieldValue.of(eventId)).build()._toQuery())
        );
    }

    public List<Event> extractEventList(SearchResponse<Event> response) {
        return Optional.ofNullable(response)
                .map(ResponseBody::hits)
                .map(HitsMetadata::hits)
                .stream()
                .flatMap(Collection::stream)
                .map(Hit::source)
                .filter(Objects::nonNull)
                .toList();
    }

    public Event extractEvent(SearchResponse<Event> response) {
        return Optional.ofNullable(response)
                .map(ResponseBody::hits)
                .map(HitsMetadata::hits)
                .stream()
                .flatMap(Collection::stream)
                .map(Hit::source)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public UpdateRequest<Event, Object> updateEventRequest(Event event) {
        return UpdateRequest.of(
                builder -> builder
                        .index(elasticConfigs.indexName())
                        .id(event.getEventId())
                        .doc(event)
                        .docAsUpsert(true)
        );
    }

    public DeleteRequest deleteEventByEventIdRequest(String eventId) {
        return DeleteRequest.of(
                builder -> builder
                        .index(elasticConfigs.indexName())
                        .id(eventId)
        );
    }
}
