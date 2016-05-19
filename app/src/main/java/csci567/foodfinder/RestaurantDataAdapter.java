package csci567.foodfinder;

/**
 * Created by bradley on 5/18/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brad on 5/6/2016.
 * https://www.javacodegeeks.com/2013/09/android-listview-with-adapter-example.html
 */
public class RestaurantDataAdapter extends BaseAdapter {
    List<Restaurant> data;
    Context mContext;

    RestaurantDataAdapter(Context c, List<Restaurant> data)
    {
        mContext = c;
        this.data = new ArrayList<Restaurant>(data);
    }

    public int getCount()
    {
        return data.size();
    }

    public Object getItem(int arg0)
    {
        return data.get(arg0);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {
        View row = view;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.rest_data_item, parent, false);
        }
        TextView name = (TextView) row.findViewById(R.id.name_text);
        TextView addr = (TextView) row.findViewById(R.id.addr_text);
        TextView phone = (TextView) row.findViewById(R.id.phone_text);


        Button remove = (Button) row.findViewById(R.id.remove_button);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).getM_id();
                /*
                TODO remove from Database
                 */
            }
        });


        String name_str = "Name: " + data.get(position).getM_name();
        String addr_str = "Address: " + data.get(position).getM_address();
        String phone_str = "Phone: " + data.get(position).getM_phone_num();


        name.setText(name_str);
        addr.setText(addr_str);
        phone.setText(phone_str);


        return row;
    }




}