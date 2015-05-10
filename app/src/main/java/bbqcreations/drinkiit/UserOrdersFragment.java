package bbqcreations.drinkiit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserOrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserOrdersFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int sectionNumber;
    private ArrayList<PendingOrder> orders;

    private OnFragmentInteractionListener mListener;
    private ListView lv_orders;
    private MenuItem pb_actionbar;
    private MenuItem btn_refresh;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param section_number Parameter 1.
     * @return A new instance of fragment UserOrdersFragment.
     */
    public static UserOrdersFragment newInstance(int section_number) {
        UserOrdersFragment fragment = new UserOrdersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, section_number);
        fragment.setArguments(args);
        return fragment;
    }

    public UserOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        setHasOptionsMenu(true);
        orders = PendingOrder.getPendingOrdersList(mainActivity.currentOrdersData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_orders, container, false);
        lv_orders = (ListView) rootView.findViewById(R.id.lv_pending);
        lv_orders.setAdapter(new PendingOrderAdapter(orders, getActivity()));
        lv_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder suppression = new AlertDialog.Builder(getActivity());
                suppression.setTitle("Supprimer " + orders.get(position).toString() + " ?");
                suppression.setMessage("La commande ne vous sera pas délivrée");
                suppression.setPositiveButton(getActivity().getString(R.string.ok), new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Token current = new Token(mainActivity.tokenData, getActivity());
                        String token = current.getValue();
                        String id = orders.get(position).getId() + "";
                        String args[] = new String[]{token, id};
                        new AsyncTask<String, Void, Void>() {
                            @Override
                            protected Void doInBackground(String... params) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn_refresh.setVisible(false);
                                        pb_actionbar.setVisible(true);
                                    }
                                });
                                boolean result = false;
                                try {
                                    result = current.postDeleteOrderData(params);
                                } catch (IOException e) {
                                    this.cancel(true);
                                    e.printStackTrace();
                                }
                                if (result){
                                    orders.remove(position);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            refresh();
                                            Toast.makeText(getActivity(), "Commande supprimée", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    current.getUserOrders();
                                }
                                else{
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Echec de la suppression de la commande", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                return null;
                            }

                            @Override
                            protected void onCancelled(){
                                mainActivity.showConnexionErrorDialog(getActivity());
                            }

                            @Override
                            protected void onPostExecute(Void result){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb_actionbar.setVisible(false);
                                        btn_refresh.setVisible(true);
                                    }
                                });
                            }

                        }.execute(args);
                    }

                });
                suppression.setNegativeButton(getActivity().getString(R.string.cancel), new Dialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                suppression.show();
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        pb_actionbar = menu.findItem(R.id.menu_progress);
        btn_refresh = menu.findItem(R.id.action_refresh);
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

    public void refresh(){
        lv_orders.setAdapter(new PendingOrderAdapter(orders, getActivity()));
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
