package org.elevators.util;

import org.elevators.containers.Request;
import org.elevators.enums.Direction;

import java.util.concurrent.ThreadLocalRandom;

public class RequestGenerator {
	private int min, max;

	public RequestGenerator(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public Request generate(Direction direction) {
		int floor = ThreadLocalRandom.current().nextInt(min, max + 1);
		int callingFloor;
		if (direction == Direction.UP) {
			do {
				callingFloor = ThreadLocalRandom.current().nextInt(1, max + 1);
			} while (callingFloor > floor);
		} else {
			do {
				callingFloor = ThreadLocalRandom.current().nextInt(1, max + 1);
			} while (callingFloor < floor);
		}
		return new Request(floor, callingFloor, direction);
	}

}
