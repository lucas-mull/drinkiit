package bbqcreations.drinkiit;

/**
 * Created by lucas on 30/04/15.
 */
public class Order {

    String token;
    Meal meal;
    int qty;
    String comment;

    public Order(String token, Meal meal, int qty, String comment) {
        this.token = token;
        this.meal = meal;
        this.qty = qty;
        this.comment = comment;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMealId(Meal meal) {
        this.meal = meal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
