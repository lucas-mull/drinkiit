package bbqcreations.drinkiit;

import android.content.Context;
import android.graphics.Color;
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
 *
 */
public class PendingOrderAdapter extends BaseAdapter
{
    static class ViewHolder{
        TextView date;
        TextView commande;
        TextView total;
        TextView done;
    }

    ArrayList<PendingOrder> data = new ArrayList<>();
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
        PendingOrder cur = data.get(position);
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

        DateFormat df = new SimpleDateFormat("dd-MM");
        String commande = cur.toString();

        String done;
        if (cur.isDone())
            done = "Oui";
        else
            done = "Non";

        holder.date.setText(df.format(cur.getDate().getTime()));
        holder.commande.setText(commande);
        holder.total.setText(cur.getPrice() + "€");
        holder.done.setText(done);
        if (position % 2 == 0)
            convertView.setBackgroundColor(Color.parseColor("#9920221F"));
        else
            convertView.setBackgroundColor(Color.parseColor("#99c95743"));

        return convertView;
    }
}
