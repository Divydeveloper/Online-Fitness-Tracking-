import java.sql.SQLException;
import java.util.List;

public interface WorkoutDAO {
    void addWorkout(Workout workout) throws SQLException;
    List<Workout> getAllWorkouts() throws SQLException;
    Workout getWorkoutById(int id) throws SQLException;
    void updateWorkout(Workout workout) throws SQLException;
    void deleteWorkout(int id) throws SQLException;
}
