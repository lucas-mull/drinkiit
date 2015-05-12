package bbqcreations.drinkiit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lucas on 02/05/15.
 * Correspond à une commande en cours (déjà postée sur le site)
 */
public class PendingOrder {
    /*
    Attributs
     */
    int id; // identifiant de la commande (pour une éventuelle suppression)
    Calendar date; // date de la commande
    String name; // nom du plat
    double price; // prix du plat
    String comment; // commentaire de la commande
    int quantity; // quantité demandée
    int total; // prix total de la commande (prix * quantité)
    boolean done; // si la commande a été servie ou pas

    /**
     * Créée une commande à partir d'un JSONObject (typiquement renvoyé par une requête http)
     * @param data JSONObject à partir duquel créer la commande
     */
    public PendingOrder(JSONObject data){
        try {
            this.id = data.getInt("id");
            this.date = this.parseString(data.getString("date"));
            this.total = data.getInt("total");
            this.done = data.getBoolean("done");

            JSONArray contentArray = data.getJSONArray("content");
            JSONObject content = contentArray.getJSONObject(0);
            this.name = content.getString("name");
            this.price = content.getDouble("price");
            this.comment = content.getString("comment");
            this.quantity = content.getInt("quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convertit la date renvoyée par le serveur en date (type Calendar)
     * @param s chaîne à partir du quel convertir une date
     * @return l'objet Calendar créé
     */
    public Calendar parseString(String s){
        int year = Integer.parseInt(s.substring(0, 4));
        int month = Integer.parseInt(s.substring(5, 7));
        int day = Integer.parseInt(s.substring(8, 10));
        int hours = Integer.parseInt(s.substring(11, 13));
        int minutes = Integer.parseInt(s.substring(14, 16));
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hours, minutes);
        return cal;
    }

    /**
     *
     * @param response renvoie la liste des commandes créée à partir de la réponse http
     * @return la liste des commandes
     */
    public static ArrayList<PendingOrder> getPendingOrdersList(JSONObject response){
        ArrayList<PendingOrder> ordersList = new ArrayList<>();
        try {
            JSONArray list = response.getJSONArray("data");
            for (int i = 0; i < list.length(); i++){
                JSONObject order = list.getJSONObject(i);
                ordersList.add(new PendingOrder(order));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    public Calendar getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public String getComment() {
        return comment;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isDone() {
        return done;
    }

    public int getId() {
        return id;
    }

    public String toString(){
        String commande = this.getQuantity() + "x " + this.getName();
        if (this.getComment().length() != 0)
            commande += (" (" + this.getComment() + ")");
        return commande;
    }
}
