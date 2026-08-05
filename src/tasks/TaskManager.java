package tasks;

import datastructures.BST;
import datastructures.GenericStack;

import java.time.LocalDate;
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
        undoStack.push(new UndoAction(ActionType.ADD, task));
    }

    public void completeTask(int id){
        Task target = getTaskById(id);
        if (target.isDone()) return;
        target.setDone(true);
        undoStack.push(new UndoAction(ActionType.COMPLETE, target));
    }

    public void rescheduleTask(int id, LocalDate newDate){
        Task task = getTaskById(id);
        LocalDate oldDate = task.getScheduledDate();

        TaskDay oldTaskDay = new TaskDay(task.getScheduledDate());
        throwIfNotReschedulable(task);

        moveTaskToDate(task, newDate);
        undoStack.push(new UndoAction(ActionType.RESCHEDULE, task, oldDate));
    }

    public void undo(){
        if (undoStack.isEmpty()) return;

        Task task = undoStack.peek().getTask();
        LocalDate previousDate = undoStack.peek().getPreviousDate();
        tasks.ActionType type = undoStack.pop().getType();

        TaskDay taskDay = new TaskDay(task.getScheduledDate());

        if (type.equals(ActionType.ADD)){
            tasksById.remove(task.getId());
            daysBst.find(taskDay).removeTask(task);
        } else if (type.equals(ActionType.COMPLETE)){
            task.setDone(false);
        } else {
            moveTaskToDate(task, previousDate);
        }
    }

    private void moveTaskToDate(Task task, LocalDate newDate){
        TaskDay taskDay = new TaskDay(task.getScheduledDate());
        TaskDay currentDay = daysBst.find(taskDay);

        currentDay.removeTask(task);
        if (task instanceof Reschedulable){
            ((Reschedulable) task).reschedule(newDate);
        }
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

    private void throwIfNotReschedulable(Task t){
        if (!(t instanceof Reschedulable r)){
            throw new NotReschedulableException("Not a reschedulable task!");
        }
    }
}
