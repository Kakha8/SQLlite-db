package kakha.kudava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TaskDAO {
    private static final Logger log = LoggerFactory.getLogger(TaskDAO.class);
    private static String url;
    public TaskDAO(String url) {
        this.url = url;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }
    public void createTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY,
                title TEXT,
                description TEXT,
                completed BOOLEAN
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("Table created");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public void insertTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks(title, description, completed) VALUES(?,?,?)";
        try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isCompleted());
            pstmt.executeUpdate();
            log.info("Task inserted");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public Task getTask(int id) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Task(id, rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("completed"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
        return null;
    }

    public void updateTask(int id, boolean state) throws SQLException {
        String sql = "UPDATE tasks SET completed = ? WHERE id = ?";
        try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, state);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            log.info("Task {} deleted", id);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public ArrayList<Task> getAllTasks() throws SQLException {
        String sql = "SELECT * FROM tasks";
        try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                tasks.add(new Task(rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("completed")));
            }
            return tasks;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public ArrayList<Task> getCompletedTasks() throws SQLException {
        String sql = "SELECT * FROM tasks WHERE completed = ?";
        try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                    tasks.add(new Task(rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getBoolean("completed")));
            }
            return tasks;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
