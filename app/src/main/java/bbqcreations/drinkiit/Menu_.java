package bbqcreations.drinkiit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 30/04/15.
 * Correspond à un ensemble de plats -> constitue le menu Drinkiit
 */
public class Menu_ {

    /*
    Attributs
     */
    Meal[] menu;    // Ensemble des plats constituants le menu

    /**
     * Créée un menu à partir d'un JSONObject (Typiquement envoyé par une requête http).
     * @param data JSONObject à partir duquel créer notre menu.
     */
    public Menu_(JSONObject data){
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

    /*
    Getters & Setters
     */
    public Meal getMeal(int position){
        if (this.menu == null)
            return null;
        return this.menu[position];
    }

    public int getLength(){
        return menu.length;
    }

    /**
     *
     * @return tableaux comprenant les noms de chaque plat du menu
     */
    public String[] toArray(){
        String array[] = new String[menu.length];
        for (int i = 0; i < menu.length; i++){
            array[i] = menu[i].getName();
        }
        return array;
    }
}
