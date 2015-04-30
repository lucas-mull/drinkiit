package bbqcreations.drinkiit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import bbqcreations.drinkiit.R;

/**
 * Created by lucas on 30/04/15.
 */
public class MenuAdapter extends BaseAdapter {

    Context context;
    Menu data;

    private ArrayList<Order> orders = new ArrayList<Order>();
    private static LayoutInflater inflater = null;

    public MenuAdapter(Context context, Menu data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public boolean hasEnoughMoneyFor(Order order){
        UserInfo ui = new UserInfo(mainActivity.userInfoData);
        double sum = 0;
        if (orders.size() != 0){
            for (int i = 0; i < orders.size(); i++){
                Order cur = orders.get(i);
                sum += (cur.getMeal().getPrice() * cur.getQty());
            }
        }
        sum += (order.getMeal().getPrice() * order.getQty());
        if (ui.getCredit() >= sum)
            return true;
        else
            return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final Meal cur = data.getMeal(position);
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        vi.setTag(position);
        final View fView = vi;
        TextView name = (TextView)vi.findViewById(R.id.txt_product_name);
        TextView description = (TextView)vi.findViewById(R.id.txt_product_description);
        TextView price = (TextView)vi.findViewById(R.id.txt_product_price);
        name.setText(cur.getName().toUpperCase());
        description.setText(cur.getDescription());
        price.setText(cur.getPrice() + "€");
        Button addBtn = (Button)vi.findViewById(R.id.btn_order_add);
        Button cancelBtn = (Button)vi.findViewById(R.id.btn_order_cancel);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Token token = new Token(mainActivity.tokenData);
                int quantity = getQuantity(fView);
                String comment = getComment(fView);
                if (quantity != 0)
                {
                    Order o = new Order(token.getValue(), cur, quantity, comment);
                    if (hasEnoughMoneyFor(o)){
                        orders.add(o);
                        Toast.makeText(context, quantity + " " + cur.getName() + " ajouté(s)", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(context, "Pas assez d'argent !", Toast.LENGTH_SHORT).show();

                    unSelectItem(fView);
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectItem(fView);
            }
        });
        return vi;
    }

    private String getComment(View item){
        EditText comment = (EditText)item.findViewById(R.id.edtxt_order_comment);
        String result = comment.getText().toString();
        if (result == null)
            result = "";
        return result;
    }

    private int getQuantity(View item){
        EditText qty = (EditText)item.findViewById(R.id.edtxt_order_qty);
        try{
            return Integer.parseInt(qty.getText().toString());
        } catch (Exception e){
            Toast.makeText(context, "Veuillez rentrez une quantité valide", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    public void unSelectItem(View v){
        LinearLayout base_ll = (LinearLayout)v.findViewById(R.id.ll_product);
        LinearLayout ll_clicked = (LinearLayout)v.findViewById(R.id.ll_product_clicked);
        ll_clicked.setVisibility(View.GONE);
        base_ll.setVisibility(View.VISIBLE);
        OrderFragment.selectedItem = null;
    }
}
