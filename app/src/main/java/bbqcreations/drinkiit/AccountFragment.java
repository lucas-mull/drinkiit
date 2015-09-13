package bbqcreations.drinkiit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Fragment Mon compte
 */
public class AccountFragment extends Fragment {

    private UserInfo userInfo;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    int sectionNumber;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param section_number number of section in drawerLayout.
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance(int section_number) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, section_number);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        this.userInfo = new UserInfo(MainActivity.userInfoData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        TextView name = (TextView)rootView.findViewById(R.id.txt_account_name);
        TextView email = (TextView)rootView.findViewById(R.id.txt_account_email);
        TextView solde = (TextView)rootView.findViewById(R.id.txt_account_solde);
        TextView statut = (TextView)rootView.findViewById(R.id.txt_account_activated);
        name.setText(userInfo.getName() + " " + userInfo.getSurname());
        email.setText(userInfo.getEmail());
        solde.setText("" + userInfo.getCredit() + "€");
        if (userInfo.isActivated()){
            statut.setTextColor(Color.parseColor("#27ae60"));
            statut.setText("Activé");
        }

        else{
            statut.setTextColor(Color.parseColor("#c0392b"));
            statut.setText("En attente d'activation");
        }
        if (userInfo.getCredit() <= 0)
            solde.setTextColor(Color.parseColor("#c0392b"));
        else
            solde.setTextColor(Color.parseColor("#27ae60"));

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

}
