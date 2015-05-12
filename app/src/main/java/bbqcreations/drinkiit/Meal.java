package bbqcreations.drinkiit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 30/04/15.
 * Correspond à une catégorie de plat drinkiit
 */
public class Meal {
    /*
    Attributs
     */
    int id; // identifiant du plat
    String name;    // nom du plat
    String description; // description du plat
    double price;   // prix du plat

    /**
     * Créée un nouveau plat à partir d'un JSONObject. (Typiquement renvoyé par une requête http).
     * @param data JSON à partir duquel créer le plat.
     */
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

    /*
    Getters & Setters
     */
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

    public double getPrice() {
        return price;
    }
}
