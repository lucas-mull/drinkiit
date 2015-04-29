package bbqcreations.drinkiit;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 27/04/15.
 */
public class UserInfo {
    private int id;
    private String name;
    private String surname;
    private String email;
    private int credit;
    private boolean activated;

    public UserInfo(JSONObject data){
        try {
            this.id = data.getInt("id");
            this.name = data.getString("name");
            this.surname = data.getString("surname");
            this.email = data.getString("email");
            this.credit = data.getInt("credit");
            this.activated = data.getBoolean("activated");
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

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
