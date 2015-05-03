package bbqcreations.drinkiit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by lucas on 01/05/15.
 */
public class PreOrderAdapter extends BaseAdapter {

    public static class ViewHolder{
        TextView qty;
        TextView meal;
        TextView price;
        Button delete;
    }

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
        ViewHolder holder;
        Order cur = data.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.preorder_row, null);
            holder = new ViewHolder();
            holder.meal = (TextView)convertView.findViewById(R.id.txt_preorder_meal);
            holder.qty = (TextView)convertView.findViewById(R.id.txt_preorder_qty);
            holder.price = (TextView)convertView.findViewById(R.id.txt_preorder_price);
            holder.delete = (Button)convertView.findViewById(R.id.btn_preorder_remove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.qty.setText(cur.getQty() + "x");
        holder.meal.setText(cur.getMeal().getName());
        double total = cur.getMeal().getPrice() * (double) cur.getQty();
        holder.price.setText(total + "€");
        holder.delete.setTag(position);

        return convertView;
    }
}
