package bbqcreations.drinkiit;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 27/04/15.
 *
 */
public class UserInfo {
    private int id;
    private String name;
    private String surname;
    private String email;
    private double credit;
    private boolean activated;

    public UserInfo(JSONObject data){
        try {
            JSONObject container = data.getJSONObject("data");
            this.id = container.getInt("id");
            this.name = container.getString("name");
            this.surname = container.getString("surname");
            this.email = container.getString("email");
            this.credit = container.getDouble("credit");
            this.activated = container.getBoolean("activated");
        } catch (JSONException e) {
            Log.v("Error: ", "field not found inside JSONObject");
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

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public double getCredit() {
        return credit;
    }

    public boolean isActivated() {
        return activated;
    }

}
