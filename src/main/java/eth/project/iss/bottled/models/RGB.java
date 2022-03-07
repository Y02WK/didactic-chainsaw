package eth.project.iss.bottled.models;

import jakarta.json.JsonArray;

public class RGB {
    private Integer red;
    private Integer green;
    private Integer blue;

    public Integer getRed() {
        return this.red;
    }

    public Integer getGreen() {
        return this.green;
    }

    public Integer getBlue() {
        return this.blue;
    }

    @Override
    public String toString() {
        return String.format("rgb(%d, %d, %d)", red, green, blue);
    }

    public void populateFromJsonArray(JsonArray jsonArray) {
        this.red = jsonArray.getInt(0);
        this.green = jsonArray.getInt(1);
        this.blue = jsonArray.getInt(2);
    }
}
