package practicum.yandex.task;

import java.util.Objects;

public class SubTask extends Task {
    private Integer epicId;
    private final TaskTypes type = TaskTypes.SUBTASK;

    public SubTask(String name, String description, String status, Integer epicId) {
        super(name, description, status);

        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskTypes getType() { return type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicId, subTask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return id + ","
                + type + ","
                + name + ","
                + status + ","
                + description + ","
                + startTime + ","
                + duration + ","
                + epicId;
    }
}
