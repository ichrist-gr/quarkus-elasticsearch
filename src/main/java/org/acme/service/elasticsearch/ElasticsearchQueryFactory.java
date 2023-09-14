package org.acme.service.elasticsearch;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.acme.config.ElasticConfigs;
import org.acme.model.Event;

@Dependent
public class ElasticsearchQueryFactory {
    private final ElasticConfigs elasticConfigs;

    @Inject
    public ElasticsearchQueryFactory(ElasticConfigs elasticConfigs) {
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
