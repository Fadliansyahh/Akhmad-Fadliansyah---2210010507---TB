import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    public static Connection connect() {
        // Gantilah dengan informasi database Anda
        String url = "jdbc:mysql://localhost:3306/rental_mobil"; 
        String user = "root";  
        String password = "";  

        try {
            // Membuat koneksi ke database
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi berhasil!");
            return conn; 
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Koneksi gagal!");
            return null;
        }
    }
}
