package bbqcreations.drinkiit;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 26/04/15.
 */
public class ApiURL {

    private Context c;
    private String url;
    private String method;
    private String key;

    public ApiURL(String key, Context context){
        this.key = key;
        this.c = context;
        this.url = context.getString(R.string.baseURL);
        if (this.getStringFromKey(key) != null)
            this.url += getStringFromKey(key);
        else
            throw new IllegalArgumentException();

    }

    /*Determines the URL and the method depending on the parameter @key
    * Returns null if @key is not recognized*/
    public String getStringFromKey(String key){
        String res;
        switch(key){
            case "postLogformURL":
                res = c.getString(R.string.postLogformURL);
                this.method = "POST";
                break;
            case "postOrderURL":
                res = c.getString(R.string.postOrderURL);
                this.method = "POST";
                break;
            case "getTokenCheckURL":
                res = c.getString(R.string.getTokenCheckURL);
                this.method = "GET";
                break;
            case "getMenuURL":
                res = c.getString(R.string.getMenuURL);
                this.method = "GET";
                break;
            case "getUserInfoURL":
                res = c.getString(R.string.getUserInfoURL);
                this.method = "GET";
                break;
            case "getUserOrdersURL":
                res = c.getString(R.string.getUserOrdersURL);
                this.method = "GET";
                break;
            case "postDeleteOrderURL":
                res = c.getString(R.string.postDeleteOrderURL);
                this.method = "POST";
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
    public void setMethod(String method){
        if (method != "POST" && method != "GET")
            Log.v("Erreur: ", "Méthode invalide ({POST ou GET})");
        else
            this.method = method;
    }

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

    /*Gets the list of {key, value} that has to be given to the POST request for the current URL
    * Returns an exception if the current method is not POST*/
    public List<NameValuePair> getPOSTinfo(String... params){
        if (getMethod() != "POST"){
            Log.v("Erreur: ", "La méthode pour l\'URL actuel n'est pas POST");
            return null;
        }
        List<NameValuePair> nameValuePairs;
        if (key.equals("postLogformURL")){
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", params[0]));
            nameValuePairs.add(new BasicNameValuePair("password", params[1]));
        }
        else if (key.equals("postOrderURL")){
            nameValuePairs = new ArrayList<>(4);
            nameValuePairs.add(new BasicNameValuePair("token", params[0]));
            nameValuePairs.add(new BasicNameValuePair("meal_id", params[1]));
            nameValuePairs.add(new BasicNameValuePair("qty", params[2]));
            nameValuePairs.add(new BasicNameValuePair("comment", params[3]));
        }
        else{
            nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("token", params[0]));
            nameValuePairs.add(new BasicNameValuePair("order_id", params[1]));
        }
        return nameValuePairs;
    }

    /*Gets the complete URL for a GET request
    * Basically adds the given token at the end of the url
    * Throws an exception if the current method is not GET*/

    public String getGETinfo(String token){
        if (getMethod() != "GET"){
            Log.v("Erreur: ", "La méthode pour l\'URL actuel n'est pas GET");
            return null;
        }
        return this.url + token;
    }
}
