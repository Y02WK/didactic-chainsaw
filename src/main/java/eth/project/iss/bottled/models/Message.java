package eth.project.iss.bottled.models;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Message {
    private Integer msgId;
    private String msg;
    private Instant timestamp = Instant.now();

    public Message() {
    }

    public Message(String msg, Instant timestamp) {
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public Message(Integer msgId, String msg, Timestamp timestamp) {
        this.msgId = msgId;
        this.msg = msg;
        this.timestamp = timestamp.toInstant();
    }

    public Message(String msg) {
        this.msg = msg;
    }

    public Integer getMsgId() {
        return this.msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public static JsonObject createJson(SqlRowSet rs) {
        Message message = new Message(rs.getInt("msg_id"), rs.getString("msg"), rs.getTimestamp("timestamp"));
        JsonObject jsonObj = Json.createObjectBuilder()
                .add("msgId", message.getMsgId())
                .add("msg", message.getMsg())
                .add("timestamp", message.getTimestamp().toString())
                .build();

        return jsonObj;
    }

}
