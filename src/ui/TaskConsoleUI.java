package ui;

import tasks.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class TaskConsoleUI {
    private TaskManager taskManager;

    public TaskConsoleUI(TaskManager taskManager){
        this.taskManager = taskManager;
    }
    public TaskManager getTaskManager() {
        return taskManager;
    }

    Scanner sc = new Scanner(System.in);

    public void run(){
        int choice = -1;
        while (choice != 0){
            System.out.println
                    ("""
                    === Task Organizer ===
                    1. Add task
                    2. List tasks by date
                    3. List tasks by urgency
                    4. Complete a task
                    5. Reschedule a task
                    6. Undo last action
                    0. Exit
                    """);

            choice = readInt("Choose your option ");
            switch (choice){
                case 1 -> handleAddTask(); // for now only simple tasks
                case 2 -> handleListByDate();
                case 3 -> handleListByUrgency();
                case 4 -> handleComplete();
                case 5 -> handleReschedule();
                case 6 -> handleUndo();
                case 0 -> System.out.println("Bye!");
                default -> System.out.println("Choose a number between 0 and 6!");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = sc.nextLine().trim();
            try { return Integer.parseInt(input); }
            catch (NumberFormatException e) { System.out.println("Please enter a number."); }
        }
    }
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String input = sc.nextLine().trim();
            try { return LocalDate.parse(input);}
            catch (DateTimeParseException e) { System.out.println("Invalid date. Try again."); }
        }
    }
    private LocalDateTime readDateTime(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-ddTHH:mm): ");
            String input = sc.nextLine().trim();
            try {return LocalDateTime.parse(input); }
            catch (DateTimeParseException e) { System.out.println("Invalid date time. Try again."); }
        }
    }
    private String readLine(String prompt) {
        System.out.print(prompt + ": ");
        return sc.nextLine().trim();
    }

    private void buildSimpleTask(){
        String title = readLine("Task title");
        LocalDate dueDate = readDate("Task date");
        Task task = new SimpleTask(title, dueDate);
        taskManager.addTask(task);
        System.out.println("Task added.");
    }

    private void buildChecklistTask(){
        String title = readLine("Task title");
        LocalDate dueDate = readDate("Task date");
        ChecklistTask task = new ChecklistTask(title, dueDate);

        while (true) {
            String more = readLine("Add an item? (y/n)");
            if (!more.equalsIgnoreCase("y")) break;
            addItem(task);
        }

        taskManager.addTask(task);
        System.out.println("Task added.");
    }
    private void addItem(ChecklistTask task){
        String desc = readLine("Item description");
        task.addItem(desc);
    }

    private void buildMeetingTask(){
        String title = readLine("Task title");
        LocalDateTime startTime = readDateTime("Start time");
        LocalDateTime endTime = readDateTime("End time");
        String location = readLine("Location (leave blank for none)");
        if (location.isEmpty()) location = null;

        Task task = new MeetingTask(title, startTime, endTime, location);
        taskManager.addTask(task);
        System.out.println("Task added.");
    }

    private void buildProjectTask(){
        String title = readLine("Task title");
        ProjectTask task = new ProjectTask(title);

        while (true) {
            String more = readLine("Add a milestone? (y/n)");
            if (!more.equalsIgnoreCase("y")) break;
            addMilestone(task);
        }

        taskManager.addTask(task);
        System.out.println("Task added.");
    }
    private void addMilestone(ProjectTask task){
        String milestoneTitle = readLine("Milestone title");
        LocalDate milestoneDueDate = readDate("Milestone date");
        task.addMilestone(milestoneTitle, milestoneDueDate);
    }

    private void handleAddTask(){
        int choice;

        while (true){
            System.out.println
                    ("""
                    === Task Type ===
                    1. Simple
                    2. Checklist
                    3. Meeting
                    4. Project
                    0. Back
                    """);

            choice = readInt("Enter your option");

            switch (choice) {
                case 1 -> buildSimpleTask();
                case 2 -> buildChecklistTask();
                case 3 -> buildMeetingTask();
                case 4 -> buildProjectTask();
                case 0 -> { return; }
                default -> System.out.println("Choose a number between 0 and 4!");
            }
        }
    }

    private void handleListByDate(){
        List<Task> tasks = taskManager.listByDate();
        printTasks(tasks);
    }

    private void handleListByUrgency(){
        List<Task> tasks = taskManager.listByUrgency();
        printTasks(tasks);
    }

    private void handleComplete(){
        int id = readInt("Task id");
        try {
            taskManager.completeTask(id);
            System.out.println("Task complete.");
        } catch (TaskNotFoundException e){
            System.out.println("Task not found.");
        }
    }

    private void handleReschedule(){
        int id = readInt("Task id");
        LocalDate newDueDate = readDate("New date");
        try {
            taskManager.rescheduleTask(id, newDueDate);
            System.out.println("Rescheduled.");
        } catch (TaskNotFoundException | NotReschedulableException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleUndo(){
        taskManager.undo();
        System.out.println("Undone.");
    }

    // TODO (upgrade to show type specific detail
    private void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) { System.out.println("(no tasks)"); return; }
        for (Task t : tasks) {
            System.out.println("[" + t.getId() + "] " + t.getTitle()
                    + " | " + t.getScheduledDate()
                    + " | urgency=" + t.calculateUrgency()
                    + (t.isDone() ? " | DONE" : ""));
        }
    }
}
