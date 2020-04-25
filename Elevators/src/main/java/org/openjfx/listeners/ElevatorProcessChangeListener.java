package org.openjfx.listeners;

import javafx.scene.control.TextArea;

public class ElevatorProcessChangeListener implements EventListener {

	private TextArea process;

	public ElevatorProcessChangeListener(TextArea process) {
		this.process = process;
	}

	@Override
	public void update(String message) {
		if (message.equals("")){
			process.clear();
		} else {
			process.appendText(message);
		}
	}
}
