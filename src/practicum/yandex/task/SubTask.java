package practicum.yandex.task;

import practicum.yandex.manager.TaskTypes;

import java.util.Objects;

public class SubTask extends Task {
    private EpicTask epicTaskReference;

    public SubTask(String name, String description, String status, EpicTask epicTaskReference) {
        super(name, description, status);

        this.epicTaskReference = epicTaskReference;
    }

    public EpicTask getEpicTaskReference() {
        return epicTaskReference;
    }

    public void setEpicTaskReference(EpicTask epicTaskReference) {
        this.epicTaskReference = epicTaskReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicTaskReference, subTask.epicTaskReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicTaskReference);
    }

    @Override
    public String toString() {
        return id + ","
                + TaskTypes.SUBTASK.name() + ","
                + name + ","
                + status + ","
                + description + ","
                + epicTaskReference.id;
    }
}
