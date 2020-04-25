package org.elevators.containers;

public class Elevator {
	private int currentFloor;
	private int elevatorName;

	public Elevator(int startFloor, int name) {
		this.currentFloor = startFloor;
		this.elevatorName = name;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public int getElevatorName() {
		return elevatorName;
	}
}
