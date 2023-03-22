package com.vitaquest.eventservice.Domain.Service;

import com.vitaquest.eventservice.Database.Repository.IEventRepository;
import com.vitaquest.eventservice.Domain.DTO.AddEventDTO;
import com.vitaquest.eventservice.Domain.DTO.AllUserNotificationDTO;
import com.vitaquest.eventservice.Domain.DTO.UpdateEventDTO;
import com.vitaquest.eventservice.Domain.Models.Event;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventService {

    private final List<Event> events = new ArrayList<>();
    private IEventRepository repository;
    private final DaprClient daprClient;

    public EventService(IEventRepository repository) {
        this.repository = repository;
        this.daprClient = new DaprClientBuilder().build();
    }

    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    public Event getEventByID(String eventId) {
        Optional<Event> foundEvent = repository.findById(eventId);
        if (foundEvent.isEmpty()) {
            //throw not found exception
            throw new IllegalArgumentException("Event not found");
        }
        return foundEvent.get();
    }

    public Event addEvent(AddEventDTO DTO) {
        Event event = Event.builder()
                .title(DTO.getTitle())
                .description(DTO.getDescription())
                .date(new Date())
                .userIds(new ArrayList<String>())
                .build();

        event = repository.save(event);
        events.add(event);

        AllUserNotificationDTO dto = AllUserNotificationDTO.builder()
                .title("New event created")
                .message("The event \"" + event.getTitle() + "\" has been created, check it now")
                .build();
        daprClient.publishEvent("pubsub", "notifyAllUsers", dto).block();
        return event;
    }

    public boolean deleteEvent(String eventId, Authentication authContext) throws IllegalAccessException {
        Optional<Event> event = repository.findById(eventId);
        if (event.isPresent()) {
            repository.delete(event.get());
            return true;
        }
        return false;
    }

    public Event acceptEvent(String eventId, Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        String userId = claims.get("oid").toString();

        Optional<Event> existingEvent = repository.findById(eventId);

        Event event = existingEvent.stream().findFirst().orElse(null);

        if (event.getUserIds() == null) {
            event.setUserIds(new ArrayList<>());
            event.getUserIds().add(userId);

            return repository.save(event);
        }
        if (!event.getUserIds().contains(userId)) {
            event.getUserIds().add(userId);
        }

        return repository.save(event);
    }

    public Event removeFromEvent(String eventId, Authentication authContext) throws IllegalAccessException {

        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        String userId = claims.get("oid").toString();

        Optional<Event> existingEvent = repository.findById(eventId);

        Event event = existingEvent.stream().findFirst().orElse(null);

        event.getUserIds().remove(userId);

        return repository.save(event);
    }

    public Event CheckIfUserIsInEvent(String eventId, Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        String userId = claims.get("oid").toString();

        Optional<Event> existingEvent = repository.findById(eventId);

        Event event = existingEvent.stream().findFirst().orElse(null);

        if (event.getUserIds() == null) {
            event.setUserIds(new ArrayList<>());
            event.getUserIds().add(userId);
        } else if (!event.getUserIds().contains(userId)) {
            event.getUserIds().add(userId);
        } else if (event.getUserIds().contains(userId)) {
            event.getUserIds().remove(userId);
        }
        return repository.save(event);
    }
    public Event updateEvent(UpdateEventDTO DTO){
        // get existing event by id
        Event existingEvent = getEventByID(DTO.getId());
        // update event fields
        existingEvent.setTitle(DTO.getTitle());
        existingEvent.setDescription(DTO.getDescription());
        // update event in db
        repository.save(existingEvent);
        return existingEvent;
    }
}
