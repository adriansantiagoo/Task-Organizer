# Task Organizer

A console-based task management application written in plain Java. It models several distinct kinds of tasks — each with its own urgency and rescheduling rules — and lets you add, list, complete, reschedule, and undo actions through an interactive menu.

## Features

- **Multiple task types**, each with its own urgency calculation and scheduled-date logic (see below).
- **Undo** for the last add, complete, or reschedule action.
- **Two list views**: by scheduled date or by computed urgency.
- Input validation with retry loops for dates, date-times, and numeric input.

## Task types

| Type | Key fields | Urgency driven by |
|---|---|---|
| `SimpleTask` | due date | days remaining until the due date |
| `ChecklistTask` | due date, checklist items | due date proximity + fraction of undone items |
| `MeetingTask` | start/end time, optional location | whether the meeting is upcoming, in progress, or over |
| `ProjectTask` | milestones (title + due date each) | next undone milestone's due date + overall progress |

`SimpleTask`, `ChecklistTask`, and `MeetingTask` can be rescheduled directly. `ProjectTask` has no due date of its own — its schedule follows its next undone milestone.

## Project structure

```
src/
├── datastructures/   # Hand-rolled generic BST and array-backed stack used internally
├── tasks/            # Domain model: Task hierarchy, TaskManager, exceptions
└── ui/                # Console entry point and interactive menu
```

- `tasks.Task` is the abstract base for all task types, tracking id, title, and completion state.
- `tasks.TaskManager` is the in-memory service that owns all tasks, keeps them indexed by id and by scheduled date (via a custom BST), and maintains an undo history (via a custom fixed-capacity stack).
- `ui.TaskConsoleUI` drives everything through a `Scanner`-based menu; `ui.Main` is the entry point.

This project is also an exercise in implementing core data structures (a binary search tree and a generic stack) from scratch rather than relying on the standard library equivalents.

## Getting started

No build tool is configured — the project compiles directly from `src/`.

**Requires JDK 21+** (the codebase uses modern Java features: text blocks, pattern-matching `switch` and `instanceof`).

### Run from IntelliJ IDEA

Open the project folder in IntelliJ, let it index the existing module (`Task Organizer.iml`), and run `ui.Main`.

### Run from the command line

```bash
# from the project root
find src -name "*.java" > sources.txt
javac -d out -encoding UTF-8 @sources.txt
java -cp out ui.Main
```

## Usage

Running the app presents a menu:

```
1. Add task
2. List by date
3. List by urgency
4. Complete task
5. Reschedule task
6. Undo
7. Exit
```

Adding a task prompts for the task type (simple, checklist, meeting, or project) and its type-specific fields. Completing, rescheduling, and undoing operate on a task's id, shown in the list views.

## Notes

- State is entirely in-memory and is lost when the program exits.
- The undo history holds up to the last 100 actions.
