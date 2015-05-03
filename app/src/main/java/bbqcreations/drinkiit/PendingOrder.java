package bbqcreations.drinkiit;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lucas on 02/05/15.
 */
public class PendingOrder {

    Calendar date;
    String name;
    double price;
    String comment;
    int quantity;
    int total;
    boolean done;

    public PendingOrder(JSONObject data){
        try {
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

    public Calendar parseString(String s){
        int year = Integer.parseInt(s.substring(0, 3));
        int month = Integer.parseInt(s.substring(5, 6));
        int day = Integer.parseInt(s.substring(8, 9));
        int hours = Integer.parseInt(s.substring(11, 12));
        int minutes = Integer.parseInt(s.substring(14, 15));
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hours, minutes);
        return cal;
    }

    public static ArrayList<PendingOrder> getPendingOrdersList(JSONObject response){
        ArrayList<PendingOrder> ordersList = new ArrayList<PendingOrder>();
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

    public void setDate(Calendar date) {
        this.date = date;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
