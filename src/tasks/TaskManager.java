package tasks;

import datastructures.BST;
import datastructures.GenericStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final int stackCapacity = 100; // only for be useful
    private final BST<TaskDay> daysBst = new BST<>();
    private final HashMap<Integer, Task> tasksById = new HashMap<>();
    private final GenericStack<UndoAction> undoStack = new GenericStack<>(stackCapacity);

    public void addTask(Task task){
        tasksById.put(task.getId(), task);
        findFirstThenAppend(task);
    }

    public void findFirstThenAppend(Task task){
        TaskDay taskDay = new TaskDay(task.getScheduledDate());
        TaskDay existing = daysBst.find(taskDay);
        if (existing != null) {
            existing.addTask(task);
        } else {
            taskDay.addTask(task);
            daysBst.insert(taskDay);
        }
    }

    public List<Task> listByDate(){
        List<Task> result = new ArrayList<>();
        for (TaskDay day : daysBst.inorderTraversal()) {
            result.addAll(day.getTasks());
        }
        return result;
    }
}
