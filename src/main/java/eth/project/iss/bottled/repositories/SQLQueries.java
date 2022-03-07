package eth.project.iss.bottled.repositories;

public class SQLQueries {
    // UserRepository queries
    public static final String GET_USER_QUERY = "SELECT * FROM users WHERE public_address like ?;";

    public static final String CREATE_USER_QUERY = "INSERT INTO users VALUE (?) ;";

    // MessageRepository queries
    public static final String INSERT_MESSAGE_QUERY = "INSERT INTO messages (msg, public_address, timestamp) VALUES (?, ?, ?);";

    public static final String GET_MESSAGE_QUERY = "SELECT * FROM messages WHERE public_address = ? AND timestamp > NOW() - INTERVAL 1 DAY;";

    public static final String GET_MESSAGE_BY_ID_QUERY = "SELECT * FROM messages WHERE msg_id = ? AND public_address = ? AND timestamp > NOW() - INTERVAL 1 DAY;";

    public static final String DELETE_EXPIRED_MESSAGES = "DELETE FROM messages WHERE timestamp < NOW() - INTERVAL 1 DAY;";
}
