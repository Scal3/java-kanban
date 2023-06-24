package practicum.yandex.task;

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
    public String toString() {
        String result = "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'';

        result += epicTaskReference != null
                ? ", epicTaskReferenceName=" + epicTaskReference.name + '\''
                : ", epicTaskReferenceName='null" + '\'';

        return result + '}';
    }
}
