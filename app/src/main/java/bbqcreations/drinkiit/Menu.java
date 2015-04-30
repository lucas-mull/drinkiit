package bbqcreations.drinkiit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 30/04/15.
 */
public class Menu {

    Meal[] menu;

    public Menu(JSONObject data){
        try {
            JSONArray innerData = data.getJSONArray("data");
            menu = new Meal[innerData.length()];
            for (int i = 0; i < menu.length; i++){
               JSONObject temp = innerData.getJSONObject(i);
               this.menu[i] = new Meal(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Meal getMeal(int position){
        if (this.menu == null)
            return null;
        return this.menu[position];
    }

    public int getLength(){
        return menu.length;
    }
}
