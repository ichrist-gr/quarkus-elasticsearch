package org.acme.model;

public class Event {
    private String eventType;
    private String eventId;
    private String eventTime;
    private String indexName;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    @Override
    public String toString() {
        final StringBuilder sb;
        sb = new StringBuilder("Event{");
        sb.append("eventType='").append(eventType).append('\'');
        sb.append(", eventId='").append(eventId).append('\'');
        sb.append(", eventTime='").append(eventTime).append('\'');
        sb.append(", indexName='").append(indexName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
