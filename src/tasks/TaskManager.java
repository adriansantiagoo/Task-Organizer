package tasks;

import datastructures.BST;
import datastructures.GenericStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private final int stackCapacity = 100; // only to be useful
    private final BST<TaskDay> daysBst = new BST<>();
    private final HashMap<Integer, Task> tasksById = new HashMap<>();
    private final GenericStack<UndoAction> undoStack = new GenericStack<>(stackCapacity);

    public void addTask(Task task){
        tasksById.put(task.getId(), task);
        findFirstThenAppend(task);
    }

    private void findFirstThenAppend(Task task){
        TaskDay taskDay = new TaskDay(task.getScheduledDate());
        TaskDay existing = daysBst.find(taskDay);
        if (existing != null) {
            existing.addTask(task);
        } else {
            taskDay.addTask(task);
            daysBst.insert(taskDay);
        }
    }

    private List<Task> collectAllTasks() {
        List<Task> result = new ArrayList<>();
        for (TaskDay day : daysBst.inorderTraversal()) {
            result.addAll(day.getTasks());
        }
        return result;
    }

    public List<Task> listByDate() {
        return collectAllTasks();
    }

    public List<Task> listByUrgency() {
        return collectAllTasks().stream()
                .sorted(Comparator.comparingInt(Task::calculateUrgency).reversed())
                .collect(Collectors.toCollection(ArrayList::new)); // mutable result
    }

    public Task getTaskById(int id){
        throwIfIdNotFound(id);
        return tasksById.get(id);
    }

    private void throwIfIdNotFound(int id){
        if (!tasksById.containsKey(id))
            throw new TaskNotFoundException("Id not found!");
    }
}
