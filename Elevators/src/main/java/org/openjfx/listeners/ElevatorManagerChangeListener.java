package org.openjfx.listeners;

import javafx.scene.control.TextArea;

public class ElevatorManagerChangeListener implements EventListener {

	private TextArea handler;
	private int rowCount;

	public ElevatorManagerChangeListener(TextArea handler) {
		this.handler = handler;
	}

	@Override
	public void update(String message) {
		handler.appendText(message);
		rowCount++;
		if (rowCount == 20){
			handler.clear();
		}
	}
}
