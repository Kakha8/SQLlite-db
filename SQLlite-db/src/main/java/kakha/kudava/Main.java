package kakha.kudava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {

        String url = "jdbc:sqlite:tasks.db";

        TaskDAO dao = new TaskDAO(url);
        Task task = new Task(0, "clean", "Clean the rooom", false);
        Task task2 = new Task(0, "Gym", "Go to the gym", false);
        Task task3 = new Task(0, "Study", "Study for exam", false);

        dao.createTable();
        dao.insertTask(task);
        dao.insertTask(task2);
        dao.insertTask(task3);


        ArrayList<Task> allTasks = dao.getAllTasks();

        System.out.println("All tasks: " + allTasks.size());
        printTasks(allTasks);

        dao.updateTask(1, true);
        dao.updateTask(3, true);


        ArrayList<Task> completedTasks = dao.getCompletedTasks();
        System.out.println("Completed tasks: " + completedTasks.size());
        printTasks(completedTasks);

        dao.deleteTask(1);
        System.out.println("Deleted task: " + task.getId());
        System.out.println("Showing all :");
        printTasks(dao.getAllTasks());

    }

    private static void printTasks(ArrayList<Task> tasks) {
        for(Task tsk : tasks){
            System.out.println("id" + tsk.getId() +" " + tsk.getTitle() + " " + tsk.isCompleted());
        }
    }







}