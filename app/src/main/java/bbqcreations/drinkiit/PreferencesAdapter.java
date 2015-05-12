package bbqcreations.drinkiit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;

/**
 * Created by lucas on 10/05/15.
 *
 */
public class PreferencesAdapter extends BaseAdapter {

    static class ViewHolder{
        TextView title;
        TextView pref;
    }

    NameValuePair[] data;
    Context context;
    private static LayoutInflater inflater = null;

    public PreferencesAdapter(NameValuePair[] data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.preferences_row, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.txt_product_name);
            holder.pref = (TextView) convertView.findViewById(R.id.txt_product_comment);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(data[position].getName().toUpperCase());
        holder.pref.setText(data[position].getValue());

        return convertView;
    }
}
