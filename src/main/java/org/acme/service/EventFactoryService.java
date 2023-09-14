package org.acme.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.ResponseBody;
import jakarta.enterprise.context.Dependent;
import org.acme.model.Event;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Dependent
public class EventFactoryService {
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
}
