package bbqcreations.drinkiit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by lucas on 02/05/15.
 */
public class PendingOrderAdapter extends BaseAdapter
{
    static class ViewHolder{
        TextView date;
        TextView commande;
        TextView total;
        TextView done;
    }

    ArrayList<PendingOrder> data = new ArrayList<PendingOrder>();
    Context context;
    private static LayoutInflater inflater = null;

    public PendingOrderAdapter(ArrayList<PendingOrder> data, Context context) {
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
        if (convertView == null){
            convertView = inflater.inflate(R.layout.pendingorder_row, null);
            holder = new ViewHolder();
            holder.date = (TextView)convertView.findViewById(R.id.txt_pending_date);
            holder.commande = (TextView)convertView.findViewById(R.id.txt_pending_name);
            holder.total = (TextView)convertView.findViewById(R.id.txt_pending_total);
            holder.done = (TextView)convertView.findViewById(R.id.txt_pending_done);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        PendingOrder cur = data.get(position);
        DateFormat df = new SimpleDateFormat("dd-MM");

        String commande = cur.getQuantity() + "x " + cur.getName();
        if (cur.getComment().length() != 0)
            commande += (" (" + cur.getComment() + ")");

        String done;
        if (cur.isDone())
            done = "Oui";
        else
            done = "Non";

        holder.date.setText(df.format(cur.getDate().getTime()));
        holder.commande.setText(commande);
        holder.total.setText(cur.getPrice() + "€");
        holder.done.setText(done);

        return convertView;
    }
}
