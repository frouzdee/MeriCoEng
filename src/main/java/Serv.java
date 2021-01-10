import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import java.sql.*;

public class Serv{
    public static void main(String[] args) {
        String user = "root";
        String password = "root";
        String URL = "jdbc:mysql://localhost:3306/mysql?useSLL=false";
        try (Connection connection = DriverManager.getConnection(URL, user, password);){
            System.out.println("connected");
            Statement statement = connection.createStatement();

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
