package bbqcreations.drinkiit;

/**
 * Created by lucas on 30/04/15.
 * Correspond à une commande que l'utilisateur veut passer.
 */
public class Order {

    /*
    Attributs
     */
    String token;   // token de connexion de l'utilisateur
    Meal meal;  // Plat de la commande
    int qty;    // Quantité du plat pour la commande
    String comment; // Commentaire pour la commande

    /**
     * Constructeur classique
     * @param token token
     * @param meal meal
     * @param qty qty
     * @param comment comment
     */
    public Order(String token, Meal meal, int qty, String comment) {
        this.token = token;
        this.meal = meal;
        this.qty = qty;
        this.comment = comment;
    }

    /*
    Getters & Setters
     */
    public String getToken() {
        return token;
    }

    public int getQty() {
        return qty;
    }

    public Meal getMeal() {
        return meal;
    }

    public String getComment() {
        return comment;
    }

    /**
     * Renvoie les données nécessaires à l'envoi d'une commande, à savoir:
     * - le token
     * - l'id du plat
     * - la quantité
     * - le commentaire
     * @return le tableau de string comprenant toutes les données nécessaires à l'envoi d'une commande
     */
    public String[] getFormData(){
        String meal_id = this.getMeal().getId() + "";
        return new String[]{getToken(), meal_id, getQty() + "", getComment()};
    }

}
