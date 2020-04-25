package org.elevators.observers;

import org.openjfx.listeners.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElevatorEventManager {
	private Map<String, List<org.openjfx.listeners.EventListener>> listeners = new HashMap<>();

	public ElevatorEventManager(String... operations) {
		for (String operation : operations) {
			this.listeners.put(operation, new ArrayList<>());
		}
	}

	public void subscribe(String eventType, org.openjfx.listeners.EventListener listener) {
		List<org.openjfx.listeners.EventListener> users = listeners.get(eventType);
		users.add(listener);
	}

	public void unsubscribe(String eventType, org.openjfx.listeners.EventListener listener) {
		List<org.openjfx.listeners.EventListener> users = listeners.get(eventType);
		users.remove(listener);
	}

	public void notify(String eventType, String message) {
		List<org.openjfx.listeners.EventListener> users = listeners.get(eventType);
		for (EventListener listener : users) {
			listener.update(message);
		}
	}

}
