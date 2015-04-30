package bbqcreations.drinkiit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 30/04/15.
 */
public class Meal {

    int id;
    String name;
    String description;
    double price;

    public Meal(JSONObject data){
        try {
            this.id = data.getInt("id");
            this.name = data.getString("name");
            this.description = data.getString("description");
            this.price = data.getDouble("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
