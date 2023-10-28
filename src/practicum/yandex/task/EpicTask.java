package practicum.yandex.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private List<SubTask> subtasks;
    private LocalDateTime endTime;

    public EpicTask(String name, String description, String status, List<SubTask> subtasks) {
        super(name, description, status);

        this.subtasks = subtasks;
        this.type = TaskTypes.EPIC;
        this.calculateTimes();
    }

    public List<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void setSubtasks(List<SubTask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public TaskTypes getType() { return type; }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void calculateTimes() {
        Duration duration = Duration.ofSeconds(0);

        for (SubTask sub : getSubtasks()) {
            if (sub.getDuration() != null) {
                duration = duration.plus(sub.getDuration());
            }
        }

        setDuration(duration);

        LocalDateTime epicStartTime = null;

        for (SubTask sub : getSubtasks()) {
            if (sub.getStartTime() != null && epicStartTime == null) epicStartTime = sub.getStartTime();

            if (sub.getStartTime() != null && epicStartTime.isAfter(sub.getStartTime())) {
                epicStartTime = sub.getStartTime();
            }
        }

        setStartTime(epicStartTime);

        LocalDateTime epicEndTime = null;

        for (SubTask sub : getSubtasks()) {
            if (sub.getEndTime() != null && epicEndTime == null) epicEndTime = sub.getEndTime();

            if (sub.getEndTime() != null && epicEndTime.isBefore(sub.getEndTime())) epicEndTime = sub.getEndTime();
        }

        setEndTime(epicEndTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(subtasks, epicTask.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description;
    }
}
