package bbqcreations.drinkiit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class mainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoginFragment.OnFragmentInteractionListener,
        AccueilFragment.OnFragmentInteractionListener, OrderFragment.OnFragmentInteractionListener,
        UserOrdersFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener {

    public static JSONObject tokenData;
    public static JSONObject postOrderData;
    public static JSONObject userInfoData;
    public static JSONObject menuData;
    public static boolean isTokenValid;


    private boolean isConnected;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        isConnected = false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment new_fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Token current = null;
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
        switch (position){
            case 0:
                new_fragment = AccueilFragment.newInstance(position+1);
                break;
            case 1:
                if (!isConnected)
                    new_fragment = LoginFragment.newInstance(position + 1);
                else{
                    if (!isTokenValid)
                        new_fragment = this.connexionExpiredFragment(position);
                    else
                        new_fragment = OrderFragment.newInstance(position + 1, current.getTokenValue());
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
                    new_fragment = this.connexionExpiredFragment(position);
                else
                    new_fragment = AccountFragment.newInstance(position + 1, current.getTokenValue());
                break;
            default:
                new_fragment = PlaceholderFragment.newInstance(position + 1);
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, new_fragment)
                .commit();
    }

    private Fragment connexionExpiredFragment(int position){
        Fragment new_fragment = AccueilFragment.newInstance(position + 1);
        mNavigationDrawerFragment.restoreDrawerLayout();
        Toast.makeText(this, "Votre connexion a expirée...", Toast.LENGTH_SHORT).show();
        return new_fragment;
    }

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

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


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

    public void onFragmentInteraction(Uri uri){

    }

    public void httprequest(View v) {
        final Context current_context = this;
        EditText form_email = (EditText)(findViewById(R.id.txt_email));
        EditText form_passwd = (EditText)(findViewById(R.id.txt_passwd));
        String email = form_email.getText().toString();
        String passwd = form_passwd.getText().toString();
        Log.v("email: ", email);
        Log.v("password: ", passwd);
        String args[] = new String[]{email, passwd};

        new AsyncTask<String, Void, Void>(){

            @Override
            protected Void doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoginFragment();
                    }
                });
                Token response = new Token();
                try{
                    response.GetToken(current_context, params);
                }
                // If IOException is thrown, it means the connection attempt failed
                catch (IOException e){
                    // Cancel the connection attempt and reset the fragment
                    this.cancel(true);
                }

                if (response.getTokenValue() != null){
                    isTokenValid = true;
                    isConnected = true;
                    response.getUserInfo();
                }
                else
                    isTokenValid = false;
                return null;
            }

            @Override
            protected void onCancelled(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetLoginFragment();
                    }
                });
                new AlertDialog.Builder(current_context)
                        .setTitle("Wooooops!")
                        .setMessage("Il semblerait que la requête n'ait pas atteint le serveur. Vérifiez que vous êtes bien connectés à Internet ou réessayez plus tard")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                try {
                    String res = tokenData.getString("type");
                    if (res.equals("success")){
                        Toast.makeText(current_context, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, AccueilFragment.newInstance(1)).commit();
                        mNavigationDrawerFragment.changeDrawerLayout();
                        mTitle = getString(R.string.title_section1);
                        restoreActionBar();
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() { resetLoginFragment(); }});
                        Toast.makeText(current_context, "Email ou mot de passe invalide !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute(args);

    }

    private void resetLoginFragment(){
        FrameLayout frame_log = (FrameLayout)(findViewById(R.id.fl_login));
        FrameLayout frame_load = (FrameLayout)(findViewById(R.id.fl_loading));
        frame_log.setVisibility(View.VISIBLE);
        frame_load.setVisibility(View.GONE);
    }

    private void hideLoginFragment(){
        FrameLayout frame_log = (FrameLayout)(findViewById(R.id.fl_login));
        FrameLayout frame_load = (FrameLayout)(findViewById(R.id.fl_loading));
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
