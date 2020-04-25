package org.elevators.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Building {


	private final ArrayList<Elevator> elevators;
	private HashMap<Integer, List<Request>> floors;
	private int floorsNum;

	public Building(int floorsNum) {
		this.floors = new HashMap<>();
		this.floorsNum = floorsNum;
		this.elevators = new ArrayList<>();
		init();
	}

	public void init() {
		for (int i = 0; i < 3; i++) {
			plugElevator(new Elevator(i + 2, i + 1));
		}
	}

	public void plugElevator(Elevator elevator) {
		elevators.add(elevator);
	}

	public HashMap<Integer, List<Request>> getFloors() {
		return floors;
	}

	public int getFloorsNum() {
		return floorsNum;
	}

	public ArrayList<Elevator> getElevators() {
		return elevators;
	}
}
