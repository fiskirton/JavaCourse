package org.elevators.containers;

import org.elevators.enums.Direction;

public class Request {
	private int callingFloor;
	private Direction direction;
	private int floor;

	public Request(int floor, int callingFloor, Direction direction) {
		this.floor = floor;
		this.direction = direction;
		this.callingFloor = callingFloor;
	}

	public int getFloor() {
		return floor;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getCallingFloor() {
		return callingFloor;
	}
}