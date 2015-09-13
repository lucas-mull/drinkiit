package bbqcreations.drinkiit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


/**
 * Fragment des préférences
 */
public class PreferencesFragment extends Fragment {

    private Menu_ menu;
    ListView lv_preferences;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static PreferencesFragment newInstance() {
        return new PreferencesFragment();
    }

    public PreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.menu = new Menu_(MainActivity.menuData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_preferences, container, false);
        lv_preferences = (ListView) rootView.findViewById(R.id.lv_preferences);
        lv_preferences.setAdapter(new PreferencesAdapter(getAdapterInfo(), getActivity()));
        lv_preferences.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final TextView comment = (TextView)getActivity().findViewById(R.id.txt_product_comment);
                final SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                AlertDialog.Builder message = new AlertDialog.Builder(getActivity());
                message.setTitle("Saisissez le commentaire");
                final EditText input = new EditText(getActivity());
                final PreferencesAdapter adapter = (PreferencesAdapter) lv_preferences.getAdapter();

                message.setView(input);
                message.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        NameValuePair cur = (NameValuePair) adapter.getItem(position);
                        comment.setText(value);
                        // hide keyboard
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(cur.getName(), value);
                        editor.apply();
                        adapter.data[position] = new BasicNameValuePair(cur.getName(), value);

                        // fermeture du clavier
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                });

                message.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        // Canceled.
                    }
                });
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                message.show();
            }
        });
        return rootView;
    }

    private NameValuePair[] getAdapterInfo(){
        NameValuePair[] info = new NameValuePair[menu.getLength()];
        String[] names = menu.toArray();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < info.length; i++){
            String value = preferences.getString(names[i], null);
            info[i] = new BasicNameValuePair(names[i], value);
        }
        return info;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
