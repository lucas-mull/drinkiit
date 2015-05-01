package bbqcreations.drinkiit;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lucas on 26/04/15.
 */
public class Token {

    private String value;
    private Context c;
    private JSONObject data;

    public Token(){
        //empty constructor, required to use getTokenFromRequest()
    }

    public Token(JSONObject data){
        try {
            this.value = data.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Token(String token, Context context) {
        this.value = token;
        this.c = context;
    }

    public String getValue(){
        return this.value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public Context getContext() {
        return c;
    }

    public void setContext(Context c) {
        this.c = c;
    }

    private JSONObject getData() {
        return data;
    }

    private void setData(JSONObject data) {
        this.data = data;
    }

    public void getTokenFromRequest(Context context, String... params) throws IOException{
        this.setContext(context);
        try {
            sendData(new apiURL("postLogformURL", context), params);
            if (mainActivity.tokenData.getString("type").equals("success"))
                this.setValue(this.getData().getString("data"));
            else
                this.value = null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean postOrderData(Context context, String... params) throws IOException{
        this.setContext(context);
        try {
            sendData(new apiURL("postOrderURL", context), params);
            if (getData().getString("type").equals("success"))
                return true;
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getMenu(){
        try {
            sendData(new apiURL("getMenuURL", this.c), getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.data;
    }

    public boolean isValid(){
        try {
            sendData(new apiURL("getTokenCheckURL", this.c), getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mainActivity.isTokenValid;
    }

    public JSONObject getUserInfo(){
        try {
            sendData(new apiURL("getUserInfoURL", this.c), getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.data;
    }

    private DefaultHttpClient getHttpClient(){
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        DefaultHttpClient client = new DefaultHttpClient();

        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        return new DefaultHttpClient(mgr, client.getParams());
    }

    private void sendData(apiURL url, String...params) throws JSONException, IOException {

        // Create a new HttpClient and Post Header
        DefaultHttpClient httpclient = getHttpClient();
        String json;
        HttpResponse response;

        if (url.getMethod().equals("POST")){
            HttpPost httppost = new HttpPost(url.getURL());
            // Add your data
            httppost.setEntity(new UrlEncodedFormEntity(url.getPOSTinfo(params), "UTF-8"));
            // Execute HTTP Post Request
            response = httpclient.execute(httppost);
        }
        else{
            HttpGet httpget = new HttpGet(url.getGETinfo(params[0]));
            response = httpclient.execute(httpget);
        }
        json = EntityUtils.toString(response.getEntity());
        Log.v("réponse du serveur: ", json);
        this.setData(new JSONObject(json));
        switch(url.getKey()){
            case "postLogformURL":
                mainActivity.tokenData = getData();
                break;
            case "getTokenCheckURL":
                mainActivity.isTokenValid = Boolean.valueOf(getData().getString("value"));
                break;
            case "getUserInfoURL":
                mainActivity.userInfoData = getData();
                break;
            case "getMenuURL":
                mainActivity.menuData = getData();
                break;
            default:
                break;
        }


    }

}
