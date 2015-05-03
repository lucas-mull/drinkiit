package bbqcreations.drinkiit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    private UserInfo userInfo;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_TOKEN = "token";

    private int sectionNumber;
    private String token;


    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private Menu menu;
    public static View selectedItem;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param section_number number of section in navigation drawer
     * @param token value of the current user's token
     * @return A new instance of fragment OrderFragment.
     */
    public static OrderFragment newInstance(int section_number, String token) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, section_number);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            token = getArguments().getString(ARG_TOKEN);
        }
        this.userInfo = new UserInfo(mainActivity.userInfoData);
        this.menu = new Menu(mainActivity.menuData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        TextView solde = (TextView)rootView.findViewById(R.id.txt_solde);
        solde.setText(userInfo.getCredit() + "€");
        if (userInfo.getCredit() > 0)
            solde.setTextColor(Color.parseColor("#27ae60"));
        else
            solde.setTextColor(Color.parseColor("#c0392b"));
        listView = (ListView)rootView.findViewById(R.id.lv_menu);
        listView.setAdapter(new MenuAdapter(getActivity(), menu));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(view);
            }
        });
        return rootView;
    }

    public void selectItem(View v){
        if (selectedItem != null)
            unSelectItem(selectedItem);
        selectedItem = v;
        LinearLayout base_ll = (LinearLayout)v.findViewById(R.id.ll_product);
        LinearLayout ll_clicked = (LinearLayout)v.findViewById(R.id.ll_product_clicked);
        base_ll.setVisibility(View.GONE);
        ll_clicked.setVisibility(View.VISIBLE);
    }

    public void unSelectItem(View v){
        LinearLayout base_ll = (LinearLayout)v.findViewById(R.id.ll_product);
        LinearLayout ll_clicked = (LinearLayout)v.findViewById(R.id.ll_product_clicked);
        ll_clicked.setVisibility(View.GONE);
        base_ll.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard(){
        Activity a = getActivity();
        View v = a.getCurrentFocus();
        if (v != null){
            InputMethodManager inputManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
        ((mainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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
