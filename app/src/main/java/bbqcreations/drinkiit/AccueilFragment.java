package bbqcreations.drinkiit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Fragment accueil.
 */
public class AccueilFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    int section_number;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber the fragment section's number.
     * @return A new instance of fragment AccueilFragment.
     */
    public static AccueilFragment newInstance(int sectionNumber) {
        AccueilFragment fragment = new AccueilFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public AccueilFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);
        TextView user = (TextView)rootView.findViewById(R.id.txt_accueil_name);
        if (MainActivity.isConnected){
            UserInfo ui = new UserInfo(MainActivity.userInfoData);
            user.setText(ui.getSurname());
            Button btn_login = (Button) rootView.findViewById(R.id.btn_accueil_connexion);
            Button btn_order = (Button) rootView.findViewById(R.id.btn_accueil_commander);
            btn_login.setVisibility(View.GONE);
            btn_order.setVisibility(View.VISIBLE);
        }
        else
        user.setText("bel(le) inconnu(e)");
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
