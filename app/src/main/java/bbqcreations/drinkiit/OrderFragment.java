package bbqcreations.drinkiit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Fragment utilisé pour passer une commande
 */
public class OrderFragment extends Fragment {

    private UserInfo userInfo;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    int sectionNumber;

    ListView listView;
    private Menu_ menu;
    public static View selectedItem;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param section_number number of section in navigation drawer
     * @return A new instance of fragment OrderFragment.
     */
    public static OrderFragment newInstance(int section_number) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, section_number);
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
        }
        this.userInfo = new UserInfo(MainActivity.userInfoData);
        this.menu = new Menu_(MainActivity.menuData);
        setHasOptionsMenu(true);
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
