package bbqcreations.drinkiit;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 26/04/15.
 * Gère tous les différents URL de l'api du site drinkiit.
 */
public class ApiURL {

    /*
    URLs qui requièrent une méthode POST
     */
    final public static String KEY_CONNECT = "postLogformURL";
    final public static String KEY_ORDER = "postOrderURL";
    final public static String KEY_DELETEORDER = "postDeleteOrderURL";
    /*
    URLs qui requièrent une méthode GET
     */
    final public static String KEY_CHECKTOKEN = "getTokenCheckURL";
    final public static String KEY_MENU = "getMenuURL";
    final public static String KEY_USERINFO = "getUserInfoURL";
    final public static String KEY_USERORDERS = "getUserOrdersURL";

    /*
    Valeurs possibles pour la méthode
     */
    final public static String METHOD_POST = "POST";
    final public static String METHOD_GET = "GET";

    /*
    Attributs
     */
    private Context c;  // Contexte de l'application
    private String url; // url cible
    private String method; // type de la méthode (GET ou POST)
    private String key; // clé de l'url (cf. variables statiques)

    /**
     * Créée un URL en fonction de la clé passée en paramètre.
     * @param key clé de l'URL, cf. variables statiques., jette une IllegalArgumentException si la clé n'est pas valable
     * @param context contexte de l'application
     */
    public ApiURL(String key, Context context){
        this.key = key;
        this.c = context;
        this.url = context.getString(R.string.baseURL);
        if (this.getStringFromKey(key) != null)
            this.url += getStringFromKey(key);
        else
            throw new IllegalArgumentException();

    }

    /**
     * Fixe la méthode et l'url en fonction de la clé
     * @param key clé déterminant le type de l'url (cf. variables statiques)
     * @return La portion de l'url correspondant à la clé.
     */
    public String getStringFromKey(String key){
        String res;
        switch(key){
            case KEY_CONNECT:
                res = c.getString(R.string.postLogformURL);
                this.method = METHOD_POST;
                break;
            case KEY_ORDER:
                res = c.getString(R.string.postOrderURL);
                this.method = METHOD_POST;
                break;
            case KEY_DELETEORDER:
                res = c.getString(R.string.postDeleteOrderURL);
                this.method = METHOD_POST;
                break;
            case KEY_CHECKTOKEN:
                res = c.getString(R.string.getTokenCheckURL);
                this.method = METHOD_GET;
                break;
            case KEY_MENU:
                res = c.getString(R.string.getMenuURL);
                this.method = METHOD_GET;
                break;
            case KEY_USERINFO:
                res = c.getString(R.string.getUserInfoURL);
                this.method = METHOD_GET;
                break;
            case KEY_USERORDERS:
                res = c.getString(R.string.getUserOrdersURL);
                this.method = METHOD_GET;
                break;
            default:
                res = null;
        }
        return res;
    }

    public String getMethod(){
        return this.method;
    }

   /*Getters & Setters*/

    public String getURL(){
        return this.url;
    }

    public Context getContext(){
        return this.c;
    }

    public void setContext (Context context){
        this.c = context;
    }

    public String getKey(){
        return this.key;
    }

    /**
     *
     * @param params Ensemble des arguments à passer dans la méthode POST.
     * @return La liste de NameValuePair en fonction de la clé prête à envoyer au serveur.
     */
    public List<NameValuePair> getPOSTinfo(String... params){
        if (!(getMethod().equals(METHOD_POST))){
            Log.v("Erreur: ", "La méthode pour l\'URL actuel n'est pas POST");
            return null;
        }
        List<NameValuePair> nameValuePairs;
        switch(key){
            case KEY_CONNECT:
                nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("aresID", params[0]));
                nameValuePairs.add(new BasicNameValuePair("password", params[1]));
                break;
            case KEY_ORDER:
                nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("token", params[0]));
                nameValuePairs.add(new BasicNameValuePair("meal_id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("qty", params[2]));
                nameValuePairs.add(new BasicNameValuePair("comment", params[3]));
                break;
            case KEY_DELETEORDER:
                nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("token", params[0]));
                nameValuePairs.add(new BasicNameValuePair("o_id", params[1]));
                break;
            default:
                nameValuePairs = null;
        }

        return nameValuePairs;
    }

    /**
     * Fixe l'url pour une méthode get en raccrochant le token passé en paramètre
     * @param token token de connexion
     * @return l'url complété du token
     */
    public String getGETinfo(String token){
        if (!(getMethod().equals(METHOD_GET))){
            Log.v("Erreur: ", "La méthode pour l\'URL actuel n'est pas GET");
            return null;
        }
        return this.url + token;
    }
}
