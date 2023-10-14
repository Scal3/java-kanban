package practicum.yandex.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private List<SubTask> subtasks;
    private final TaskTypes type = TaskTypes.EPIC;
    private LocalDateTime endTime;

    public EpicTask(String name, String description, String status, List<SubTask> subtasks) {
        super(name, description, status);

        this.subtasks = subtasks;
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
