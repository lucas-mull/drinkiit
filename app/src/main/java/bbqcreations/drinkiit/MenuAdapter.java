package bbqcreations.drinkiit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import bbqcreations.drinkiit.R;

/**
 * Created by lucas on 30/04/15.
 */
public class MenuAdapter extends BaseAdapter {

    Context context;
    Menu data;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        TextView name = (TextView)vi.findViewById(R.id.txt_product_name);
        TextView description = (TextView)vi.findViewById(R.id.txt_product_description);
        TextView price = (TextView)vi.findViewById(R.id.txt_product_price);
        name.setText(data.getMeal(position).getName().toUpperCase());
        description.setText(data.getMeal(position).getDescription());
        price.setText(data.getMeal(position).getPrice() + "€");
        return vi;
    }
}
