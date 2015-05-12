package bbqcreations.drinkiit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by lucas on 30/04/15.
 *
 */
public class MenuAdapter extends BaseAdapter {

    public static class ViewHolder{
        TextView name;
        TextView description;
        TextView price;
        TextView comment;
        Button add;
        Button cancel;
    }

    Context context;
    Menu_ data;

    private ArrayList<Order> orders = new ArrayList<>();
    private static LayoutInflater inflater = null;

    public MenuAdapter(Context context, Menu_ data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.getLength();
    }

    @Override
    public Object getItem(int position) {
        return data.getMeal(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Order> getOrders(){
        return this.orders;
    }

    public double getCurrentTotal(){
        double sum = 0;
        if (orders.size() != 0){
            for (int i = 0; i < orders.size(); i++){
                Order cur = orders.get(i);
                sum += (cur.getMeal().getPrice() * (double) cur.getQty());
            }
        }
        BigDecimal d = new BigDecimal(sum);
        sum = d.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        Log.v("sum", "" + sum);
        return sum;
    }

    public boolean hasEnoughMoneyFor(Order order){
        UserInfo ui = new UserInfo(mainActivity.userInfoData);
        double sum = getCurrentTotal();
        sum += (order.getMeal().getPrice() * order.getQty());
        return (ui.getCredit() >= sum);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Meal cur = data.getMeal(position);
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.menu_row, null);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.txt_product_name);
            holder.description = (TextView)convertView.findViewById(R.id.txt_product_description);
            holder.price = (TextView)convertView.findViewById(R.id.txt_product_price);
            holder.add = (Button)convertView.findViewById(R.id.btn_order_add);
            holder.cancel = (Button)convertView.findViewById(R.id.btn_order_cancel);
            holder.comment = (TextView)convertView.findViewById(R.id.edtxt_order_comment);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        final View fView = convertView;
        holder.name.setText(cur.getName().toUpperCase());
        holder.description.setText(cur.getDescription());
        holder.price.setText(cur.getPrice() + "€");

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Token token = new Token(mainActivity.tokenData, context);
                int quantity = getQuantity(fView);
                String comment = getComment(fView);
                Order o = new Order(token.getValue(), cur, quantity, comment);
                if (hasEnoughMoneyFor(o)){
                    orders.add(o);
                    mainActivity.commandes = getOrders();
                    Toast.makeText(context, quantity + " " + cur.getName() + " ajouté(s)", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(context, "Pas assez d'argent !", Toast.LENGTH_SHORT).show();

                resetCommentAndQty(fView);
                unSelectItem(fView);

            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectItem(fView);
            }
        });

        final TextView comment = holder.comment;
        final SharedPreferences preferences = ((mainActivity)context).getPreferences(Context.MODE_PRIVATE);
        String commentaire = preferences.getString(cur.getName(), null);
        if (commentaire != null)
            comment.setText(commentaire);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder message = new AlertDialog.Builder(context);
                message.setTitle("Saisissez le commentaire");
                final EditText input = new EditText(context);

                message.setView(input);
                message.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        comment.setText(value);
                        // hide keyboard
                        Activity a = (mainActivity) context;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(cur.getName(), value);
                        editor.apply();

                        // fermeture du clavier
                        InputMethodManager inputManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                });

                message.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Activity a = (mainActivity) context;
                        InputMethodManager inputManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        // Canceled.
                    }
                });
                showKeyboard();
                message.show();
            }
        });

        return convertView;
    }

    private String getComment(View item){
        TextView comment = (TextView)item.findViewById(R.id.edtxt_order_comment);
        String result = comment.getText().toString();
        if (result == null)
            result = "";
        return result;
    }

    private int getQuantity(View item){
        TextView qty = (TextView)item.findViewById(R.id.txt_order_qty);
        return Integer.parseInt(qty.getText().toString());
    }

    private void resetCommentAndQty(View item){
        TextView comment = (TextView)item.findViewById(R.id.edtxt_order_comment);
        TextView qty = (TextView)item.findViewById(R.id.txt_order_qty);
        comment.setText(null);
        qty.setText("1");
    }

    public void unSelectItem(View v){

        LinearLayout base_ll = (LinearLayout)v.findViewById(R.id.ll_product);
        LinearLayout ll_clicked = (LinearLayout)v.findViewById(R.id.ll_product_clicked);
        ll_clicked.setVisibility(View.GONE);
        base_ll.setVisibility(View.VISIBLE);
        if (getCurrentTotal() != 0){
            TextView total = (TextView)((mainActivity)context).findViewById(R.id.txt_order_total);
            total.setTag(getCurrentTotal());
            total.setText(getCurrentTotal() + "€");
        }
        OrderFragment.selectedItem = null;
    }

    public void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

}
