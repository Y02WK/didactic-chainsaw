package eth.project.iss.bottled.controllers;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eth.project.iss.bottled.models.Message;
import eth.project.iss.bottled.services.MessageService;
import eth.project.iss.bottled.services.UserService;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    // endpoint to get all non-expired messages
    @GetMapping
    public ResponseEntity<String> getUserMessages(@RequestParam String walletAddress) {
        // Register wallet into database if doesn't exist
        userService.createUserIfNotExist(walletAddress);
        // call service
        JsonArray response = messageService.getMessages(walletAddress);

        return ResponseEntity.ok(response.toString());
    }

    // endpoint to post new message
    @PostMapping(path = "{walletAddress}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postMessage(@RequestBody HashMap<String, String> map,
            @PathVariable String walletAddress) {
        Message postedMessage = new Message(map.get("message"));
        System.out.println(map);
        JsonArray response = messageService.postMessage(postedMessage, walletAddress);

        return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
    }

    // endpoint to generate NFT SVG
    @PostMapping(path = "{walletAddress}/mint", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> mintNFT(@RequestBody HashMap<String, String> map,
            @PathVariable String walletAddress) {
        Integer id = Integer.parseInt(map.get("msgId"));
        Optional<JsonObject> response = messageService.generateImageFromMessage(id, walletAddress);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get().toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error generating NFT Image.");
    }

    // endpoint to delete expired message from database
    @DeleteMapping(path = "{walletAddress}")
    public ResponseEntity<String> deleteMessages(@PathVariable String walletAddress) {
        Integer cleaned = messageService.deleteMessages();
        return ResponseEntity.ok(String.format("cleaned %d messages", cleaned));
    }
}
