package bbqcreations.drinkiit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;


/**
 * Fragment login
 */
public class LoginFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    int section_number;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param section_number number of the fragment's section.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(int section_number) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, section_number);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section_number = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_login, container, false);

        final SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedEmail = preferences.getString(getString(R.string.field_email), null);
        if (savedEmail != null){
            EditText form_email = (EditText)(rootView.findViewById(R.id.txt_email));
            form_email.setText(savedEmail);
            final EditText form_passwd = (EditText)(rootView.findViewById(R.id.txt_passwd));
            String passwd = preferences.getString(getString(R.string.field_password), null);
            if (passwd != null){
                final CheckBox cb_remember_me = (CheckBox)rootView.findViewById(R.id.cb_remember_passwd);
                cb_remember_me.setChecked(true);
                form_passwd.setText(passwd);
            }
            form_passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        form_passwd.setText(null);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(getString(R.string.field_password), null);
                        editor.apply();
                    }

                }
            });
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}
