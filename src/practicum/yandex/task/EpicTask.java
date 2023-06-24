package practicum.yandex.task;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<SubTask> subtasks;

    public EpicTask(String name, String description, String status, ArrayList<SubTask> subtasks) {
        super(name, description, status);

        this.subtasks = subtasks;
    }

    public ArrayList<SubTask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<SubTask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        String result = "EpicTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'';

        result += subtasks != null
                ? ", subtasks.length=" + subtasks.size() + '\''
                : ", subtasks.length='null" + '\'';

        return result + '}';
    }
}
