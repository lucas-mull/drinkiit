package bbqcreations.drinkiit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreferencesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferencesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Menu menu;
    ListView lv_preferences;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static PreferencesFragment newInstance() {
        PreferencesFragment fragment = new PreferencesFragment();
        return fragment;
    }

    public PreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.menu = new Menu(mainActivity.menuData);
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
                        editor.commit();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
