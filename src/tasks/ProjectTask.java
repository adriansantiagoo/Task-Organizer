package tasks;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProjectTask extends Task{
    private final List<Milestone> milestones = new ArrayList<>();

    public ProjectTask(String title) {
        super(title);
    }
    public List<Milestone> getMilestones(){
        return Collections.unmodifiableList(milestones);
    }

    public void addMilestone(String title, LocalDate dueDate){
        milestones.add(new Milestone(title, dueDate));
    }

    public void markMilestone(int index, boolean done){
        milestones.get(index).setDone(done);
    }

    public Optional<LocalDate> findNextDueDate(){
        return  milestones.stream()
                .filter(m -> !m.isDone())
                .map(Milestone::getDueDate)
                .min(LocalDate::compareTo);
    }

    public double calculateUrgencyByDueDate(){
        Optional<LocalDate> next = findNextDueDate();
        if (next.isEmpty()) return 0;

        long daysUntilDueDate = ChronoUnit.DAYS.between(LocalDate.now(), next.get());

        if (daysUntilDueDate > 30) return 0.0;
        if (daysUntilDueDate > 15) return 0.5;
        if (daysUntilDueDate > 7) return 1.0;
        if (daysUntilDueDate > 3) return 1.5;
        return 2.0;
    }

    @Override
    public int calculateUrgency() {
        if (milestones.isEmpty()) return 0;

        long undoneMilestones = milestones.stream()
                .filter(m -> !m.isDone())
                .count();
        if (undoneMilestones == 0) return 0;

        double undonePercentage = (double) undoneMilestones / milestones.size();

        double overallProgressWeight;
        if (undonePercentage < 0.25)        overallProgressWeight = 0.5;
        else if (undonePercentage < 0.5)    overallProgressWeight = 1.0;
        else if (undonePercentage < 0.75)   overallProgressWeight = 1.5;
        else                                overallProgressWeight = 2.0;

        double urgency = overallProgressWeight + calculateUrgencyByDueDate();
        return (int) Math.round(urgency);
    }

    @Override
    public LocalDate getScheduledDate() {
        return findNextDueDate().orElse(LocalDate.now());    }
}
