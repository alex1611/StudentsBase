import utils.MySqlConnection;
import view.TableStudents;

import java.sql.Connection;
import java.sql.SQLException;

public class Main{
    public static void main(String[] args) throws SQLException {
        // write your code here
        Connection connection = MySqlConnection.getConnection();
        TableStudents.run(args,connection);

    }
}
