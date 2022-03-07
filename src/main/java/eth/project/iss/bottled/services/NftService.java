package eth.project.iss.bottled.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import eth.project.iss.bottled.models.ApiOptions;
import eth.project.iss.bottled.models.Message;
import eth.project.iss.bottled.models.RGB;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class NftService {
    private String svgTemplate = "<svg xmlns='http://www.w3.org/2000/svg' preserveAspectRatio='xMinYMin meet' viewBox='0 0 350 350'><style>.base { fill: %s; font-family: serif; font-size: 14px; }</style><rect width='100%%' height='100%%' fill='%s' /><text x='5%%' y='%d%%' class='base' dominant-baseline='middle' text-anchor='start'>";
    private final String SVG_CLOSING = "</text></svg>";
    private final String SVG_SPAN_CONST = "<tspan x='5%%' dy='1.6em'>%s</tspan>";
    private final String COLOR_API = "http://colormind.io/api/";

    private String generateRawSvg(Message message) {
        // fallback colors
        String fontColor = "white";
        String bgColor = "blue";

        // select 2 random completary colors from Colormind API
        Optional<List<RGB>> colorOpt = this.colorSelector();
        if (colorOpt.isPresent()) {
            List<RGB> color = colorOpt.get();
            fontColor = color.get(0).toString();
            bgColor = color.get(1).toString();
        }

        // randomly generate the y offset for the image
        Integer yOffset = this.randomIntGen(1, 51);

        // format all strings for the final SVG
        String templateFormatted = String.format(svgTemplate, fontColor, bgColor, yOffset);
        String messageText = this.splitMessage(message.getMsg());

        String svgImage = templateFormatted + messageText + SVG_CLOSING;

        return svgImage;
    }

    public String generateSvg(Message message) {
        String rawSvg = this.generateRawSvg(message);
        return Base64.getEncoder().encodeToString(rawSvg.getBytes());
    }

    // function to get complementary colors
    private Optional<List<RGB>> colorSelector() {
        ApiOptions apiOptions = new ApiOptions();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.postForEntity(COLOR_API, apiOptions, String.class);

        if (resp.getStatusCode() != HttpStatus.OK) {
            return Optional.empty();
        }

        try {
            List<RGB> colorList = this.jsonToList(resp.getBody());
            return Optional.ofNullable(colorList);
        } catch (IOException e) {
            System.err.println(e);
            return Optional.empty();
        }
    }

    // randomly select two colors from the list of colors
    private List<RGB> jsonToList(String body) throws IOException {
        try (InputStream is = new ByteArrayInputStream(body.getBytes())) {
            System.out.println("Starting conversion");
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            JsonArray colorArray = data.getJsonArray("result");

            List<RGB> RGBList = new LinkedList<>();
            Set<Integer> rngSet = new HashSet<>();
            while (RGBList.size() != 2) {
                Integer index = this.randomIntGen(0, 4);
                if (!rngSet.contains(index)) {
                    rngSet.add(index);
                    RGB tempRGB = new RGB();
                    tempRGB.populateFromJsonArray(colorArray.getJsonArray(index));
                    RGBList.add(tempRGB);
                }
            }
            System.out.println("Finished getting RGBs" + RGBList);
            return RGBList;
        }
    }

    // simple random number generator
    private Integer randomIntGen(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    // function to split the message into different <tspan> for word wrapping in svg
    private String splitMessage(String message) {
        String svgSpan = "";

        Integer charCount = message.length();
        Integer movingCount = 0;

        while (movingCount < charCount) {
            if (charCount - movingCount > 53) {
                svgSpan += String.format(SVG_SPAN_CONST, message.substring(movingCount, movingCount + 53));
                movingCount += 53;
            } else {
                svgSpan += String.format(SVG_SPAN_CONST, message.substring(movingCount, charCount));
                break;
            }
        }

        return svgSpan;
    }
}