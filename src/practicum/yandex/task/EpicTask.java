package practicum.yandex.task;

import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private List<SubTask> subtasks;

    public EpicTask(String name, String description, String status, List<SubTask> subtasks) {
        super(name, description, status);

        this.subtasks = subtasks;
    }

    public List<SubTask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<SubTask> subtasks) {
        this.subtasks = subtasks;
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
        String result = "EpicTask{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'';

        result += subtasks != null
                ? ", subtasks=" + subtasks + '\''
                : ", subtasks='null" + '\'';

        return result + '}';
    }
}
