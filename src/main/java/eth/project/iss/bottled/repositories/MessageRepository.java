package eth.project.iss.bottled.repositories;

import static eth.project.iss.bottled.repositories.SQLQueries.DELETE_EXPIRED_MESSAGES;
import static eth.project.iss.bottled.repositories.SQLQueries.GET_MESSAGE_BY_ID_QUERY;
import static eth.project.iss.bottled.repositories.SQLQueries.GET_MESSAGE_QUERY;
import static eth.project.iss.bottled.repositories.SQLQueries.INSERT_MESSAGE_QUERY;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import eth.project.iss.bottled.models.Message;

@Repository
public class MessageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // add new message for walletAddress
    public boolean addMessage(Message message, String walletAddress) {
        jdbcTemplate.update(INSERT_MESSAGE_QUERY, message.getMsg(), walletAddress,
                Timestamp.from(message.getTimestamp()));
        return true;
    }

    // get all messages belonging to a walletAddress
    public SqlRowSet getMsgByWallet(String walletAddress) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_MESSAGE_QUERY, walletAddress);
        return rs;
    }

    // get a single message with an ID for a walletAddress
    public SqlRowSet getMsgById(Integer id, String walletAddress) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_MESSAGE_BY_ID_QUERY, id, walletAddress);
        return rs;
    }

    // deletes entries in messages table that are longer than 24 HRS
    public Integer cleanAllExpiredMessages() {
        Integer cleaned = jdbcTemplate.update(DELETE_EXPIRED_MESSAGES);
        return cleaned;
    }
}
