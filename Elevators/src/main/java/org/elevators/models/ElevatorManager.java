package org.elevators.models;

import org.elevators.containers.Building;
import org.elevators.containers.Elevator;
import org.elevators.containers.Request;
import org.elevators.enums.Direction;
import org.elevators.enums.State;
import org.elevators.observers.ElevatorEventManager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ElevatorManager {
	public ElevatorEventManager events;
	private Building building;
	private Elevator currentElevator;
	private Request currentRequest;
	private boolean isWait;
	private LinkedList<Request> requests;
	private State state;

	public ElevatorManager(Building building) {
		this.state = State.OFF;
		this.isWait = true;
		this.building = building;
		this.requests = new LinkedList<>();
		this.events = new ElevatorEventManager("signalHandled", "processChanged");
	}

	public void turnOn() {
		state = State.ON;
	}

	public void turnOff() {
		state = State.OFF;
	}

	private void findNearest(int callingFloor, Direction direction) {
		Elevator nearest = null;
		int topEdge = building.getFloorsNum();
		int bottomEdge = 0;
		/*
		 *
		 * ищем ближайший к запросу лифт. он будет находится между запросом и границей(в зависимости от направления вызо-
		 * ва). если лифт не нашелся значит произошла ситуация когда все лифты находятся с одной стороны и пользователь
		 * посылает сигнал в ту же сторону. Например, все лифты находятся на 10 этаже, пользователь делает запрос наверх
		 * с 5 этажа. Очевидно, что к нему никто не приедет т.к лифтов ниже его этажа нету.
		 *
		 * */
		for (Elevator elevator :
				building.getElevators()) {
			if (direction == Direction.UP) {
				if (bottomEdge <= elevator.getCurrentFloor() && elevator.getCurrentFloor() <= callingFloor) {
					nearest = elevator;
					bottomEdge = nearest.getCurrentFloor();
				}
			} else {
				if (callingFloor <= elevator.getCurrentFloor() && elevator.getCurrentFloor() <= topEdge) {
					nearest = elevator;
					topEdge = nearest.getCurrentFloor();
				}
			}
		}

		currentElevator = nearest;

		/*
		 * если лифт нашелся, отправляем его к пользователю
		 * */

		if (nearest != null) {
			moveElevator(callingFloor, direction);
		}
	}

	private void moveElevator(int callingFloor, Direction direction) {
		String dir = (direction == Direction.UP) ? "наверх" : "вниз";
		events.notify("processChanged",
				String.format("Текующая заявка: %s с %d этажа\n",
						dir, callingFloor
				)
		);
		if (direction == Direction.UP) {
			up(callingFloor, true);
		} else {
			down(callingFloor, true);
		}
	}

	private void up(int floor, boolean search) {
		events.notify("processChanged",
				String.format("Лифт %d отправился\n",
						currentElevator.getElevatorName()
				)
		);
		while (currentElevator.getCurrentFloor() < floor) {
			// собираем попутные заявки
			checkAssociated(search);
			currentElevator.setCurrentFloor(currentElevator.getCurrentFloor() + 1);
			events.notify("processChanged",
					String.format("Лифт %d на %d этаже\n",
							currentElevator.getElevatorName(),
							currentElevator.getCurrentFloor())
			);
			sleep(1000);
		}
		finishElevatorTask(floor, search);
		
	}

	private void down(int floor, boolean search) {
		events.notify("processChanged",
				String.format("Лифт %d отправился\n",
						currentElevator.getElevatorName()
				)
		);
		while (currentElevator.getCurrentFloor() > floor) {
			checkAssociated(search);
			currentElevator.setCurrentFloor(currentElevator.getCurrentFloor() - 1);
			events.notify("processChanged",
					String.format("Лифт %d на %d этаже\n",
							currentElevator.getElevatorName(),
							currentElevator.getCurrentFloor()
					)
			);
			sleep(1000);
		}
		finishElevatorTask(floor, search);
	}
	
	private void finishElevatorTask(int floor, boolean search) {
		if (requests.contains(currentRequest)) {
			building.getFloors().get(floor).remove(currentRequest);
		}
		if (search){
			events.notify("processChanged",
					String.format("Загрузка на %d этаже\n",
							currentElevator.getCurrentFloor()
					)
			);
		} else {
			events.notify("processChanged",
					String.format("Высадка на %d этаже\n",
							currentElevator.getCurrentFloor()
					)
			);
		}
		events.notify("processChanged",
				String.format("Лифт прибыл на %d этаж\n",
						floor
				)
		);
		sleep(1000);
		if (!search) {
			sleep(5000);
			events.notify("processChanged", "");
		}
	}
	
	private void checkAssociated(boolean search) {
		List<Request> associatedRequests = building.getFloors().get(currentElevator.getCurrentFloor());
		if (associatedRequests == null) {
			return;
		}
		LinkedList<Request> toRemove = new LinkedList<>();
		
		boolean loading = false;

		if (!associatedRequests.isEmpty()) {
			synchronized (associatedRequests) {
				for (Request ar : associatedRequests) {
					if (ar.getDirection() == currentRequest.getDirection() &&
							ar.getFloor() == currentRequest.getFloor()) {
						toRemove.add(ar);
					}
					if (ar.getDirection() == currentRequest.getDirection() &&
							ar.getCallingFloor() == currentElevator.getCurrentFloor()) {
						loading = true;
					}
				}
			}
			if (loading) {
				events.notify("processChanged",
						String.format("Загрузка на %d этаже\n",
								currentElevator.getCurrentFloor()
						)
				);
			}
			if (!toRemove.isEmpty() && !search) {
				events.notify("processChanged",
						String.format("Высадка на %d этаже\n",
								currentElevator.getCurrentFloor()
						)
				);
			}
			for (Request tr :
					toRemove) {
				requests.remove(tr);
				associatedRequests.remove(tr);
			}
		}
	}

	public void addRequest(Request request) {
		requests.addFirst(request);
		building.getFloors().computeIfAbsent(request.getFloor(),
				b -> Collections.synchronizedList(new LinkedList<>()));
		building.getFloors().get(request.getFloor()).add(request);
		if (isWait) {
			synchronized (this) {
				isWait = false;
				notify();
			}
		}

	}

	public synchronized void processRequest() {
		while (requests.isEmpty() && state == State.ON) {
			try {
				events.notify("signalHandled",
						"Лифты ожидают заявок\n"
				);
				wait();
			} catch (InterruptedException ex) {
				System.out.println("Interrupted");
			}
		}
		
		currentRequest = requests.removeFirst();
		events.notify("signalHandled",
				String.format(
						"Получен запрос с %d этажа\n",
						currentRequest.getCallingFloor()
				)
		);
		
		findNearest(currentRequest.getCallingFloor(), currentRequest.getDirection());

		if (currentElevator == null) {
			String direction = (currentRequest.getDirection() == Direction.UP) ? "наверх" : "вниз";
			events.notify("signalHandled",
					String.format(
							"Нет доступных лифтов %s по заявке с %d этажа\n",
							direction,
							currentRequest.getCallingFloor()
					)
			);
			if (requests.isEmpty()) {
				isWait = true;
			}
			return;
		}

		if (currentRequest.getDirection() == Direction.UP) {
			up(currentRequest.getFloor(), false);
		} else {
			down(currentRequest.getFloor(), false);
		}

		if (requests.isEmpty()) {
			isWait = true;
		}
	}
	
	private void sleep(int timeout){
		try {
			Thread.sleep(timeout);
			
		} catch (InterruptedException ex) {
			System.out.println("Interrupted");
		}
	}

	public State getState() {
		return state;
	}

	public LinkedList<Request> getRequests() {
		return requests;
	}
}
