import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDAOImpl implements WorkoutDAO {
    private final Connection connection;

    public WorkoutDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addWorkout(Workout workout) throws SQLException {
        String sql = "INSERT INTO workouts (type, duration, calories_burned, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, workout.getType());
            stmt.setInt(2, workout.getDuration());
            stmt.setInt(3, workout.getCaloriesBurned());
            stmt.setDate(4, workout.getDate());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Workout> getAllWorkouts() throws SQLException {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workouts";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Workout workout = new Workout(
                    rs.getString("type"),
                    rs.getInt("duration"),
                    rs.getInt("calories_burned"),
                    rs.getDate("date")
                );
                workout.setId(rs.getInt("id"));
                workouts.add(workout);
            }
        }
        return workouts;
    }

    @Override
    public Workout getWorkoutById(int id) throws SQLException {
        String sql = "SELECT * FROM workouts WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Workout(
                        rs.getString("type"),
                        rs.getInt("duration"),
                        rs.getInt("calories_burned"),
                        rs.getDate("date")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void updateWorkout(Workout workout) throws SQLException {
        String sql = "UPDATE workouts SET type = ?, duration = ?, calories_burned = ?, date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, workout.getType());
            stmt.setInt(2, workout.getDuration());
            stmt.setInt(3, workout.getCaloriesBurned());
            stmt.setDate(4, workout.getDate());
            stmt.setInt(5, workout.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteWorkout(int id) throws SQLException {
        String sql = "DELETE FROM workouts WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
