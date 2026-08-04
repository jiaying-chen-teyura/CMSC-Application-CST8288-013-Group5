
package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Thread-safe Singleton responsible for providing a JDBC {@link Connection}
 * to the CMSC MySQL database.
 * <p>
 * This did not exist anywhere in the project yet — no DAO, connection
 * class, or credentials flow had been committed before this module — so it
 * is added here as a shared utility rather than duplicated per-DAO. Any
 * other module's DAO (Ledger, Consumable, Maintenance, WorkOrder, User)
 * should reuse this same class instead of opening its own connection.
 * <p>
 * Uses double-checked locking with a {@code volatile} instance field so
 * only the very first call pays the synchronization cost — once
 * {@code instance} is initialized, subsequent calls skip the
 * {@code synchronized} block entirely. This is the exact Singleton shape
 * used for {@code DataSource} in CST8288 Assignment 2, reused here so the
 * whole team is working from one already-vetted pattern instead of a new
 * one per module.
 * <p>
 * Connection details resolve in this order:
 * <ol>
 *   <li>Explicit {@link #init(String, String, String, String, String)} call
 *       (e.g. from a future DB-credentials servlet/filter) — always wins.</li>
 *   <li>Otherwise, on first {@link #getConnection()} call, values are loaded
 *       from {@code database.properties} on the classpath
 *       ({@code src/main/resources/database.properties}) so the Equipment
 *       module (and anyone else's DAO) works standalone before that
 *       credentials flow exists.</li>
 * </ol>
 *
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public final class DataSource {

    /** The single shared instance. Volatile prevents unsafe publication of a
     *  partially-constructed object across threads. */
    private static volatile DataSource instance;

    private static final String PROPERTIES_RESOURCE = "database.properties";

    private String url;
    private String user;
    private String password;
    private boolean initialized;

    /** Private constructor — prevents external instantiation. */
    private DataSource() {
    }

    /**
     * Returns the single shared {@code DataSource} instance, creating it on
     * first call.
     *
     * @return the Singleton instance
     */
    public static DataSource getInstance() {
        if (instance == null) {                    // 1st check (no lock - fast path)
            synchronized (DataSource.class) {
                if (instance == null) {             // 2nd check (inside lock)
                    instance = new DataSource();
                }
            }
        }
        return instance;
    }

    /**
     * Explicitly initializes the JDBC connection URL and credentials.
     * Intended for a future DB-credentials collection flow; calling this
     * always overrides whatever {@code database.properties} supplied.
     *
     * @param host     database host, e.g. {@code localhost}
     * @param port     database port, e.g. {@code 3306}
     * @param db       database/schema name, e.g. {@code cmsc}
     * @param user     database username
     * @param password database password
     */
    public synchronized void init(String host, String port, String db, String user, String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        this.user = user;
        this.password = password;
        this.initialized = true;
    }

    /**
     * Loads connection details from {@code database.properties} on the
     * classpath if {@link #init} has not already been called explicitly.
     * Safe to call repeatedly — only loads once.
     *
     * @throws SQLException if the properties file is missing or unreadable
     */
    private synchronized void loadFromPropertiesIfNeeded() throws SQLException {
        if (initialized) {
            return;
        }
        Properties props = new Properties();
        try (InputStream in = DataSource.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_RESOURCE)) {
            if (in == null) {
                throw new SQLException(
                        "database.properties not found on classpath and init() was never "
                        + "called — add src/main/resources/database.properties or call "
                        + "DataSource.getInstance().init(...) before the first getConnection().");
            }
            props.load(in);
        } catch (IOException e) {
            throw new SQLException("Failed to read database.properties", e);
        }

        String host = props.getProperty("db.host", "localhost");
        String port = props.getProperty("db.port", "3306");
        String db = props.getProperty("db.name", "cmsc");
        String propUser = props.getProperty("db.user");
        String propPassword = props.getProperty("db.password");

        init(host, port, db, propUser, propPassword);
    }

    /**
     * Opens and returns a new JDBC {@link Connection} using the credentials
     * supplied to {@link #init} (directly, or indirectly via
     * {@code database.properties}).
     * <p>
     * A new {@code Connection} is returned on every call rather than reused,
     * because {@code Connection} objects are not thread-safe — sharing one
     * across requests would reintroduce the concurrency problem this
     * Singleton is meant to avoid. This mirrors the Assignment 2
     * {@code DataSource} design note: the Singleton itself is safe to
     * share; the {@code Connection} it produces is not.
     *
     * @return an open database connection
     * @throws SQLException if the connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        loadFromPropertiesIfNeeded();
        return DriverManager.getConnection(url, user, password);
    }
}
