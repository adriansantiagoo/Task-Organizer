package ui;

import tasks.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        TaskConsoleUI ui = new TaskConsoleUI(manager);
        ui.run();
    }
}
