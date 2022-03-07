package eth.project.iss.bottled.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import eth.project.iss.bottled.models.Message;
import eth.project.iss.bottled.repositories.MessageRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Service
public class MessageService {
    @Autowired
    private NftService nftSvc;

    @Autowired
    private MessageRepository messageRepository;

    public JsonArray getMessages(String walletAddress) {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        SqlRowSet rs = messageRepository.getMsgByWallet(walletAddress);
        while (rs.next()) {
            arrBuilder.add(Message.createJson(rs));
        }

        return arrBuilder.build();
    }

    public JsonArray postMessage(Message message, String walletAddress) {
        messageRepository.addMessage(message, walletAddress);
        return this.getMessages(walletAddress);
    }

    private SqlRowSet getMessageById(Integer id, String walletAddress) {
        SqlRowSet rs = messageRepository.getMsgById(id, walletAddress);
        return rs;
    }

    public Optional<JsonObject> generateImageFromMessage(Integer id, String walletAddress) {
        SqlRowSet rs = this.getMessageById(id, walletAddress);
        if (rs.next()) {
            Message message = new Message((Integer) rs.getInt("msg_id"), rs.getString("msg"),
                    rs.getTimestamp("timestamp"));
            String svgImage = nftSvc.generateSvg(message);
            return Optional.ofNullable(Json.createObjectBuilder().add("svg", svgImage).build());
        }
        return Optional.empty();
    }

    public Integer deleteMessages() {
        return messageRepository.cleanAllExpiredMessages();
    }
}
