package org.openjfx.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import org.elevators.containers.Building;
import org.elevators.containers.Request;
import org.elevators.enums.Direction;
import org.elevators.models.ElevatorManager;
import org.elevators.threads.RequestsHandler;
import org.elevators.util.Config;
import org.elevators.util.RequestGenerator;
import org.openjfx.listeners.ElevatorManagerChangeListener;
import org.openjfx.listeners.ElevatorProcessChangeListener;


public class ElevatorController implements Initializable {
	public TextArea handler;
	public TextArea process;
	public TextArea sender;
	private ElevatorManager manager;
	private RequestGenerator rg;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Building building = new Building(Config.getFloorsNum());
		manager = new ElevatorManager(building);
		RequestsHandler requestsHandler = new RequestsHandler(manager);
		
		rg = new RequestGenerator(1, building.getFloorsNum());

		manager.events.subscribe("signalHandled", new ElevatorManagerChangeListener(handler));
		manager.events.subscribe("processChanged", new ElevatorProcessChangeListener(process));
		
		requestsHandler.getThread().start();
		
	}

	@FXML
	private void pushUpButton(ActionEvent actionEvent) {
		Request new_request = rg.generate(Direction.UP);
		sender.appendText(String.format("Отправлена наверх заявка с %d этажа на %d этаж\n",
				new_request.getCallingFloor(),
				new_request.getFloor()
		));
		manager.addRequest(new_request);

	}

	@FXML
	private void pushDownButton(ActionEvent actionEvent) {
		Request new_request = rg.generate(Direction.DOWN);
		sender.appendText(String.format("Отправлена вниз заявка с %d этажа на %d этаж\n",
				new_request.getCallingFloor(),
				new_request.getFloor()
		));
		manager.addRequest(new_request);
	}
	
	@FXML
	private synchronized void pushOffButton(ActionEvent actionEvent) {
		manager.turnOff();
		notify();
		Platform.exit();
	}
	
	
}

