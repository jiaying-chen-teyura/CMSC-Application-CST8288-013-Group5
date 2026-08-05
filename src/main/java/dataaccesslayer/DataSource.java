package dataaccesslayer;

/* File: DataSource.java
 * Description: DAO Design Pattern - connection factory for the Data tier.
 * Based on the course example (AuthorsDAOProj/dataaccesslayer/DataSource.java),
 * adapted for a multi-user web app: instead of one shared static Connection
 * (not thread-safe, breaks under concurrent servlet requests), this opens
 * a fresh short-lived Connection per call. Every DAO method uses
 * try-with-resources so the connection is returned immediately after the
 * query/update finishes.
 */

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {

    private static final String PROPERTIES_FILE = "/database.properties";
    private static Properties props;

    private DataSource() { }

    private static synchronized Properties loadProps() {
        if (props == null) {
            props = new Properties();
            try (InputStream in = DataSource.class.getResourceAsStream(PROPERTIES_FILE)) {
                if (in == null) {
                    throw new IllegalStateException(
                        "database.properties not found on classpath at " + PROPERTIES_FILE);
                }
                props.load(in);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load database.properties", e);
            }
        }
        return props;
    }

    /** Opens a brand-new JDBC connection. Callers MUST close it (try-with-resources). */
    public static Connection getConnection() throws SQLException {
        Properties p = loadProps();
        String url = p.getProperty("jdbc.url");
        String user = p.getProperty("jdbc.username");
        String pass = p.getProperty("jdbc.password");
        return DriverManager.getConnection(url, user, pass);
    }
}
