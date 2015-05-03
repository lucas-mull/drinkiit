package bbqcreations.drinkiit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * La seule activité de l'application. C'est elle qui se lance au démarrage de l'application. Tous les changements d'écran se font ensuite
 * par le biais du menu coulissant (navigation drawer) mis en place avec cette activité (auto généré). Chaque clic sur un item du menu coulissant
 * aura pour effet de charger un nouveau fragment dans la fenêtre. Un fragment est une sous-activité en gros. Il représente une fonctionnalité
 * précise de notre application et possède un layout (une vue, ce qui s'affiche sur le téléphone) propre.
 */
public class mainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoginFragment.OnFragmentInteractionListener,
        AccueilFragment.OnFragmentInteractionListener, OrderFragment.OnFragmentInteractionListener,
        UserOrdersFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener {

    /*
    Paramètres static. Le mot clé static autorise ces variables à être utilisées depuis partout dans l'application
    (depuis un fragment par exemple).
     */
    public static JSONObject tokenData;     // JSON contenant les infos sur le token (essentiellement la valeur).
    public static JSONObject userInfoData;  // JSON contenant les infos de l'utilisateur.
    public static JSONObject menuData;      // JSON contenant les infos sur le menu.
    public static JSONObject currentOrdersData; // JSON contenant les commandes récentes passées (validées) par l'utilisateur
    public static boolean isTokenValid = false; // true si le token courant est valide, false sinon
    public static boolean isConnected = false;  //true si l'utilisateur est connecté, false sinon.
    public static ArrayList<Order> commandes = new ArrayList<Order>(); // Liste contenant la commande actuelle.

    private int backCount = 0;  // Compteur représentant le nombre de fois où l'utilisateur a appuyé sur la touche "back" du téléphone d'affilée
    private int currentPosition = 0; // Position du fragment actif dans l'activité

    /**
     * Fragment gérant les comportements, intéractions et présentation du menu coulissant
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Utilisé pour stocker le titre du dernier écran. Est utilisé dans {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Appelé à la création de l'activité
     * On met en place le menu coulissant (navigation drawer)
     * Toute cette partie est autogénérée par android studio.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        final Context context = this;

        if (isConnected){
            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... params) {
                    Token current = new Token(tokenData, context);
                    isTokenValid = current.isValid();
                    return null;
                }
            }.execute();
        }

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /**
     * Méthode appelée lorsque l'utilisateur sélectionne un item du menu coulissant.
     * Elle gère tous les changements de vues.
     * @param position position de l'item sur lequel on a cliqué (comme les index d'un tableau)
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Si on est à la même position, on ferme juste le tiroir pour éviter de recharger un fragment pour rien.
        // On teste également que le menu existe, sinon cela signifie que c'est le lancement de l'appli.
        if (mNavigationDrawerFragment != null){
            if (position == currentPosition)
                return;
        }
        // On créée le fragment qui va être insérer dans la fenêtre
        Fragment new_fragment;
        // On crée un token (pour rappel un token permet de se connecter via le site drinkiit)
        Token current = null;
        /* Si on est déjà connecté et que l'utilisateur a cliqué sur "commander", "mes commandes", ou "mon compte"
        on va lancer un processus en arrière plan (AsyncTask dans android pour tâche asynchrone) qui va faire une requête http sur le site drinkiit
        pour savoir si le token utilisé préalablement pour la connexion est toujours valable (expire au bout de 5 min).
        On lance uniquement l'AsyncTask pour les 3 cas cités précédemment puisque ce sont les seuls qui requièrent une requête http et donc
        un token valide. Si le token n'est plus valide, la variable static isTokenValid est passée à false. On utilise une AsyncTask pour s'assurer
        de ne pas ralentir la navigation pour rien.
         */
        if (isConnected && (position == 1 || position == 2 || position == 3)){
            try {
                current = new Token(tokenData.getString("data"), this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final Token inter = current;
                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        isTokenValid = inter.isValid();
                        return null;
                    }
                }.execute();
            }

        /*
        On assigne le fragment en fonction de la position, càd:
        0 -> fragment "accueil" dans tous les cas
        1 -> fragment "login" si on est pas encore connecté, fragment "commander" sinon.
        2 -> fragment "A propos" si on est pas connecté (pas encore codé), fragment "Mes commandes" sinon
        3 -> fragment "Mon compte" dans tous les cas (la session non connecté possède seulement 3 items, donc position s'arrête à 2).
        4 -> defaut : fragment "A propos" (pas encore codé)
        On vérifie pour chaque section si le token a expiré. Dans ce cas, on redirige vers l'accueil.
         */
        switch (position){
            case 0:
                new_fragment = AccueilFragment.newInstance(position+1);
                break;
            case 1:
                if (!isConnected)
                    new_fragment = LoginFragment.newInstance(position + 1);
                else{
                    if (!isTokenValid)
                        new_fragment = this.connexionExpiredFragment();
                    else
                        new_fragment = OrderFragment.newInstance(position + 1, current.getValue());
                }
                break;
            case 2:
                if (!isConnected)
                    new_fragment = PlaceholderFragment.newInstance(position + 1);
                else
                    new_fragment = UserOrdersFragment.newInstance(position + 1);
                break;
            case 3:
                if (!isTokenValid)
                    new_fragment = this.connexionExpiredFragment();
                else
                    new_fragment = AccountFragment.newInstance(position + 1, current.getValue());
                break;
            default:
                new_fragment = PlaceholderFragment.newInstance(position + 1);
                break;
        }
        // Et on remplace le fragment actuel par le nouveau.
        this.replaceFragment(new_fragment, position);

    }


    /**
     * Simple méthode pour remplacer le fragment de la fenêtre actuelle.
     * @param new_fragment fragment qui va remplacer l'actuel.
     */
    private void replaceFragment(Fragment new_fragment, int position){
        // On réinitialise backCount à 0 puisque le max est de 2 PAR FRAGMENT. Si on change de fragment, on réinitialise.
        this.backCount = 0;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new_fragment)
                .commit();
        currentPosition = position;
    }

    /**
     * Utilisé dans le cas d'une connexion expirée. Reset le menu avec les items de base (Accueil, login, a propos), notifie l'utilisateur
     * de l'expiration, reset les paramètres static, et renvoie une instance du fragment accueil.
     * @return Une instance de AccueilFragment
     */
    private Fragment connexionExpiredFragment(){
        Fragment new_fragment = AccueilFragment.newInstance(1);
        mNavigationDrawerFragment.notConnectedDrawerLayout();
        Toast.makeText(this, "Votre connexion a expirée...", Toast.LENGTH_SHORT).show();
        resetAllStaticInfo();
        return new_fragment;
    }

    /**
     * remet tous les paramètres dans leur état de départ.
     */
    private void resetAllStaticInfo(){
        tokenData = null;
        userInfoData = null;
        menuData = null;
        backCount = 0;
        isConnected = false;
        isTokenValid = false;
        commandes = new ArrayList<Order>();
    }

    /**
     * Change le titre de la section lorsqu'un nouveau fragment est rattaché à l'activité.
     * @param number numéro de la section (index + 1)
     */
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                if (!isConnected)
                    mTitle = getString(R.string.title_section2);
                else
                    mTitle = getString(R.string.title_section4);
                break;
            case 3:
                if (!isConnected)
                    mTitle = getString(R.string.title_section3);
                else
                    mTitle = getString(R.string.title_section5);
                break;
            case 4:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }


    /**
     * Autogénérée, restaure l'action bar, et fixe notamment le titre de la section.
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * Méthode lancée lorsque l'utilisateur appuie sur le bouton 'back' du téléphone.
     * En gros, requiert à l'utilisateur d'appuyer deux fois sur back pour quitter l'application (super.onBackPressed())
     * Cas particulier : si l'utilisateur est sur l'écran de validation d'une commande, la touche back le renvoie sur l'écran "Commander" classique.
     */
    @Override
    public void onBackPressed(){
        int position = currentPosition;
        if (isConnected && position == 1){
            Button submit = (Button)findViewById(R.id.btn_order_submit);
            // Si submit est visible, cela signifie que l'utilisateur est sur l'écran de validation d'une commande
            if (submit.getVisibility() == View.VISIBLE){
                this.hidePreOrder();
                this.backCount = 0;
            }
            else if (this.backCount == 0){
                Toast.makeText(this, "Rappuyez pour quitter (toute commande non validée sera perdue)", Toast.LENGTH_SHORT).show();
                this.backCount = 1;
            }
            else
                super.onBackPressed();
        }
        else if (this.backCount == 0){
            Toast.makeText(this, "Rappuyez pour quitter", Toast.LENGTH_SHORT).show();
            this.backCount = 1;
        }
        else
            super.onBackPressed();
    }

    /**
     * Autogénéré
     * @param menu liste des items du menu coulissant, rien à voir avec la classe Menu de l'appli.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * TODO
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * TODO
     * @param uri
     */
    public void onFragmentInteraction(Uri uri){

    }

    /**
     * Exécute une requête http avec l'email et le mdp du formulaire du fragment Login pour récupérer le token
     * @param v bouton login du fragment LoginFragment.(Vue appelante)
     */
    public void httprequest(View v) {
        final Context current_context = this;
        /*
        Récupération de l'email et mdp rentrés par l'utilisateur.
         */
        EditText form_email = (EditText)(findViewById(R.id.txt_email));
        EditText form_passwd = (EditText)(findViewById(R.id.txt_passwd));
        final String email = form_email.getText().toString();
        final String passwd = form_passwd.getText().toString();
        if (email.equals("") || passwd.equals("")){
            Toast.makeText(this, "Vous n'avez pas rempli tous les champs !", Toast.LENGTH_SHORT).show();
            return;
        }

        // On les met dans un tableau à passer en paramètre de l'AsyncTask.
        String args[] = new String[]{email, passwd};
        // Récupération de la valeur de la checkbox (si l'utilisateur veut save son mdp)
        CheckBox cb_passwd = (CheckBox)findViewById(R.id.cb_remember_passwd);
        final boolean isChecked = cb_passwd.isChecked();

        new AsyncTask<String, Void, Void>(){

            /* partie à effectuer en arrière plan */
            @Override
            protected Void doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoginFragment();    // On affiche la partie "chargement" du fragment ("connexion en cours...")
                    }
                });
                // on lance une requête post avec les paramètres email et mdp et on récupère la réponse.
                Token response = new Token();
                try{
                    response.getTokenFromRequest(current_context, params);
                }
                // Si IOException est lancée, cela signifie que la connexion a échoué..
                catch (IOException e){
                    // Annule la tentative de connexion et reset le fragment.
                    this.cancel(true);
                }

                // Si la valeur du token n'est pas nulle, la connexion a réussi. On initialise les paramètres static en conséquence
                if (response.getValue() != null){
                    /* On en profite pour récupérer directement les infos de l'utilisateur et le menu pour éviter d'avoir à les récupérer plus tard
                      (En effet cela fait beaucoup travailler l'application et ralentirait la navigation inutilement)
                     */
                    response.getUserInfo();
                    response.getMenu();
                    response.getUserOrders();
                    isTokenValid = true;
                    isConnected = true;
                    SharedPreferences preferences = ((mainActivity)current_context).getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email);
                    if (isChecked)
                        editor.putString("passwd", passwd);
                    editor.commit();

                }
                // Sinon la connexion a échoué, on ne change rien

                return null;
            }

            /**
             * appelée lorsque cancel(true) est lancé.
             * Reset le fragment login a son été d'origine (avec le formulaire de connexion) et notifie l'utilisateur de l'échec de connexion.
             */
            @Override
            protected void onCancelled(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetLoginFragment();
                    }
                });
                showConnexionErrorDialog();
            }

            /**
             * Appelée après doInBackground(), teste si la connexion a réussi:
             * si oui -> l'utilisateur est redirigé vers l'accueil, le menu coulissant passe en mode connecté, et l'actionbar est restaurée
             * (màj du titre de section, la redirection vers l'accueil s'étant faite manuellement)
             * si non -> l'utilisateur est notifié et le fragment réinitialisé.
             * @param result résultat (null ici)
             */
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (isConnected){
                    Toast.makeText(current_context, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                    replaceFragment(AccueilFragment.newInstance(1), 0);
                    mNavigationDrawerFragment.connectedDrawerLayout();
                    mTitle = getString(R.string.title_section1);
                    restoreActionBar();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { resetLoginFragment(); }});
                    Toast.makeText(current_context, "Email ou mot de passe invalide !", Toast.LENGTH_SHORT).show();
                }
            }

        }.execute(args);

    }

    public void goTo(View v){
        Button caller = (Button)v;
        if (caller.getId() == R.id.btn_accueil_connexion)
            this.replaceFragment(LoginFragment.newInstance(2), 1);
        else{
            Token token = new Token(tokenData, this);
            this.replaceFragment(OrderFragment.newInstance(2, token.getValue()), 1);
        }
    }

    public void showConnexionErrorDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Wooooops!")
                .setMessage("Il semblerait que la requête n'ait pas atteint le serveur. Vérifiez que vous êtes bien connectés à Internet ou réessayez plus tard")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // événement nul, le clic sur le bouton ne fait que dismiss la boite de dialogue (ce qui nous suffit)
                    }
                })
                .show();
    }

    /**
     * Appelée au clic sur le bouton déconnexion dans la rubrique "mon compte".
     * Réinitialise l'application.
     * @param v bouton deconnexion
     */
    public void logOut(View v){
        this.replaceFragment(AccueilFragment.newInstance(1), 0);
        this.mNavigationDrawerFragment.notConnectedDrawerLayout();
        Toast.makeText(this, "Vous vous êtes déconnecté", Toast.LENGTH_SHORT).show();
        resetAllStaticInfo();
    }

    /**
     * Effectue le changement d'écran lorsque l'utilisateur appuie sur le bouton pour voir ses commandes à valider (fragment OrderFragment)
     * @param v
     */
    public void checkOrder(View v){
        // La liste contenant le menu est remplacée par celle contenant les commandes passées.
        ListView orders = (ListView)findViewById(R.id.lv_preorder);
        orders.setAdapter(new PreOrderAdapter(this, commandes));
        ListView menu = (ListView)findViewById(R.id.lv_menu);
        Button check = (Button)findViewById(R.id.btn_order_check);
        Button submit = (Button)findViewById(R.id.btn_order_submit);
        check.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
        menu.setVisibility(View.GONE);
        orders.setVisibility(View.VISIBLE);
    }

    /**
     * Fait l'inverse de checkOrder()
     */
    public void hidePreOrder(){
        ListView orders = (ListView)findViewById(R.id.lv_preorder);
        orders.setAdapter(new PreOrderAdapter(this, commandes));
        ListView menu = (ListView)findViewById(R.id.lv_menu);
        Button check = (Button)findViewById(R.id.btn_order_check);
        Button submit = (Button)findViewById(R.id.btn_order_submit);
        submit.setVisibility(View.GONE);
        check.setVisibility(View.VISIBLE);
        orders.setVisibility(View.GONE);
        menu.setVisibility(View.VISIBLE);
    }

    public void removeOrder(View v){
        Button caller = (Button)v;
        int position = (int)v.getTag();
        double priceToSubtract = commandes.get(position).getMeal().getPrice() * commandes.get(position).getQty();
        TextView total = (TextView)findViewById(R.id.txt_order_total);
        double updatedPrice = ((double)total.getTag()) - priceToSubtract;
        BigDecimal inter = new BigDecimal(updatedPrice);
        updatedPrice = inter.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        total.setTag(updatedPrice);
        total.setText(updatedPrice + "€");
        commandes.remove(position);
        ListView orders = (ListView)findViewById(R.id.lv_preorder);
        orders.setAdapter(new PreOrderAdapter(this, commandes));
        Toast.makeText(this, "Plat supprimé", Toast.LENGTH_SHORT).show();
    }

    public void removeOrder(int position){
        double priceToSubtract = commandes.get(position).getMeal().getPrice() * commandes.get(position).getQty();
        TextView total = (TextView)findViewById(R.id.txt_order_total);
        double updatedPrice = ((double)total.getTag()) - priceToSubtract;
        BigDecimal inter = new BigDecimal(updatedPrice);
        updatedPrice = inter.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        total.setTag(updatedPrice);
        total.setText(updatedPrice + "€");
        commandes.remove(position);
        ListView orders = (ListView)findViewById(R.id.lv_preorder);
        orders.setAdapter(new PreOrderAdapter(this, commandes));
        Toast.makeText(this, "Commande envoyée avec succès", Toast.LENGTH_SHORT).show();
    }

    public void sendOrder(View v){
        if (commandes.size() == 0){
            Toast.makeText(this, "Vous n'avez rien commandé !", Toast.LENGTH_SHORT).show();
            return;
        }
        final Context context = this;
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayOrdersLoading();
                    }
                });
                Token current_token= new Token(tokenData, context);
                int size = commandes.size();
                int erreur = 0;
                Log.v("nbCommandes", "" + commandes.size());
                for (int i = 0; i < size; i++){
                    Order current_order = commandes.get(erreur);
                    boolean result = false;
                    try {
                        result = current_token.postOrderData(current_order.getFormData());
                    } catch (IOException e) {
                        this.cancel(true);
                    }
                    final int position = erreur;
                    if (result)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                removeOrder(position);
                            }
                        });

                    else{
                        Toast.makeText(context, "Echec de l'envoi de la commande", Toast.LENGTH_SHORT).show();
                        erreur++;
                    }
                }
                // On récupère directement les commandes de l'utilisateur puisqu'elles ont changé
                current_token.getUserOrders();

                return null;
            }

            @Override
            protected void onCancelled(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideOrdersLoading();
                    }
                });
                showConnexionErrorDialog();
            }

            @Override
            protected void onPostExecute(Void result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Commandes mises à jour !", Toast.LENGTH_SHORT).show();
                        hideOrdersLoading();
                    }
                });
            }

        }.execute();
    }

    public void displayOrdersLoading(){
        LinearLayout ll_bottom = (LinearLayout)findViewById(R.id.ll_order_bottom);
        LinearLayout ll_bottom_loading = (LinearLayout)findViewById(R.id.ll_order_bottom_loading);
        ll_bottom.setVisibility(View.GONE);
        ll_bottom_loading.setVisibility(View.VISIBLE);
    }

    public void hideOrdersLoading(){
        LinearLayout ll_bottom = (LinearLayout)findViewById(R.id.ll_order_bottom);
        LinearLayout ll_bottom_loading = (LinearLayout)findViewById(R.id.ll_order_bottom_loading);
        ll_bottom_loading.setVisibility(View.GONE);
        ll_bottom.setVisibility(View.VISIBLE);
    }

    public void updateQty(View v){
        TextView caller = (TextView)v;
        TextView txtQty = (TextView)OrderFragment.selectedItem.findViewById(R.id.txt_order_qty);
        int qty = Integer.parseInt(txtQty.getText().toString());
        if (caller.getText().toString().equals("+"))
            qty++;
        else if (qty > 1)
            qty--;
        txtQty.setText(qty + "");
    }


    private void resetLoginFragment(){
        LinearLayout frame_log = (LinearLayout)(findViewById(R.id.ll_login));
        LinearLayout frame_load = (LinearLayout)(findViewById(R.id.ll_loading));
        frame_log.setVisibility(View.VISIBLE);
        frame_load.setVisibility(View.GONE);
    }

    private void hideLoginFragment(){
        LinearLayout frame_log = (LinearLayout)(findViewById(R.id.ll_login));
        LinearLayout frame_load = (LinearLayout)(findViewById(R.id.ll_loading));
        frame_log.setVisibility(View.GONE);
        frame_load.setVisibility(View.VISIBLE);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((mainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
