package org.elevators.threads;

import org.elevators.enums.State;
import org.elevators.models.ElevatorManager;

public class RequestsHandler implements Runnable {
	private ElevatorManager manager;
	private Thread thread;

	public RequestsHandler(ElevatorManager manager) {
		this.manager = manager;
		this.thread = new Thread(this);
		this.thread.setName("Handler");
	}

	public Thread getThread() {
		return thread;
	}

	@Override
	public void run() {
		launch();
	}

	public void launch() {
		manager.turnOn();
		while (manager.getState() == State.ON) {
			manager.processRequest();
		}
		System.out.println("exit");
	}
}
