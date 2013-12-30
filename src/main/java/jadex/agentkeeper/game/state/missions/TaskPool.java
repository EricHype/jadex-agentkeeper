package jadex.agentkeeper.game.state.missions;

import jadex.agentkeeper.util.Neighborcase;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector1Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TaskPool extends HashMap<Integer, List<Task>> {

	private static final long serialVersionUID = -3812527463024196310L;
	private static int countedTasks = 0;

	public synchronized void addTask(TaskType taskType, Task task) {
		int priority = taskType.getPriority();
		if (this.containsKey(priority)) {
			List<Task> taskList = this.get(priority);
			taskList.add(task);
			countedTasks++;
		} else {
			this.put(priority, new ArrayList<Task>(Arrays.asList(task)));
			countedTasks++;
		}
	}

	public synchronized Task getNextTaskToPositionButDependOnPriority(Vector2Int position) {
		Task result = null;
		for (Integer priority : this.keySet()) {
			List<Task> listOfTasksInPriority = this.get(priority);
			if (listOfTasksInPriority.size() > 0) {
				Vector1Double dist = new Vector1Double(Double.MAX_VALUE);
				for (Task task : listOfTasksInPriority) {
					if (task.isConnectedToDungeon()) {
						Vector2Int ziel = task.getTargetPosition();
						Vector1Double tmpdist = (Vector1Double) ziel.getDistance(position);
						if (tmpdist.less(dist)) {
							dist = tmpdist;
							result = task;
						}
					}
				}
				System.out.println("getNextTaskToPositionButDependOnPriority" + result);
				// gleich entfernen? eigentlich sollte das sp�ter passieren.
				listOfTasksInPriority.remove(result);
				countedTasks--;
				break; // springen bei der h�chsten Prio raus
			}
		}
		return result;
	}

	public synchronized Task checkNeighborfields(Vector2Int position) {
		for (Neighborcase neighborcase : Neighborcase.values()) {
			for (Integer priority : this.keySet()) {
				List<Task> listOfTasksInPriority = this.get(priority);
				if (listOfTasksInPriority.size() > 0) {
					for (Task task : listOfTasksInPriority) {
						if (task.isConnectedToDungeon()) {
							Vector2Int firstFoundedPositionInNeighbourHood = (Vector2Int) position.copy().add(neighborcase.getVector());
							if (task.getTargetPosition().equals(firstFoundedPositionInNeighbourHood)) {
								listOfTasksInPriority.remove(task);
								System.out.println("checkNeighborfields" + task);
								countedTasks--;
								return task;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public synchronized void removeTask(Task taskToRemove) {
		for (Integer priority : this.keySet()) {
			List<Task> listOfTasksInPriority = this.get(priority);
			if (!listOfTasksInPriority.isEmpty()) {
				if (listOfTasksInPriority.contains(taskToRemove)) {
					listOfTasksInPriority.remove(taskToRemove);
					countedTasks--;
				}
			}
		}
	}

	public synchronized static int getCountedTasks() {
		return countedTasks;
	}

	public synchronized void setReachable(TaskType digSector, IVector2 position) {
		for (Task selectedTask : this.get(digSector.getPriority())) {
			selectedTask.setConnectedToDungeon(true);
		}
	}

}
