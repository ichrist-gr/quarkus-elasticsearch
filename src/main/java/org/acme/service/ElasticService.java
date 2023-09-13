package org.acme.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.model.Event;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class ElasticService {
    private final ElasticsearchClient elasticsearchClient;

    @Inject
    public ElasticService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public CompletableFuture<CreateResponse> createOperation(CreateRequest<Event> createRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return elasticsearchClient.create(createRequest);
            } catch (IOException e) {
                return null;
            }
        });
    }

    public CompletableFuture<SearchResponse<Event>> searchOperation(SearchRequest searchRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return elasticsearchClient.search(searchRequest, Event.class);
            } catch (IOException e) {
                return null;
            }
        });
    }

    public CompletableFuture<UpdateResponse<Event>> updateEvent(UpdateRequest<Event, Object> updateRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return elasticsearchClient.update(updateRequest, Event.class);
            } catch (IOException e) {
                return null;
            }
        });
    }
}
