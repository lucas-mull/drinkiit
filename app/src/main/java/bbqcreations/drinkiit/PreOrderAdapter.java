package bbqcreations.drinkiit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by lucas on 01/05/15.
 */
public class PreOrderAdapter extends BaseAdapter {

    ArrayList<Order> data = new ArrayList<Order>();
    Context context;
    private static LayoutInflater inflater = null;

    public PreOrderAdapter(Context context, ArrayList<Order> data){
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Order cur = data.get(position);
        if (vi == null)
            vi = inflater.inflate(R.layout.preorder_row, null);
        TextView qty = (TextView)vi.findViewById(R.id.txt_preorder_qty);
        TextView meal = (TextView)vi.findViewById(R.id.txt_preorder_meal);
        TextView price = (TextView)vi.findViewById(R.id.txt_preorder_price);
        qty.setText(cur.getQty() + "x");
        meal.setText(cur.getMeal().getName());
        double total = cur.getMeal().getPrice() * (double) cur.getQty();
        /*BigDecimal arrondi = new BigDecimal(total);
        total = arrondi.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();*/
        price.setText(total + "€");
        Button delete = (Button)vi.findViewById(R.id.btn_preorder_remove);
        delete.setTag(position);

        return vi;
    }
}
