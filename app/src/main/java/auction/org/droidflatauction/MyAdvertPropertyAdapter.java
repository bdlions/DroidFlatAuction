package auction.org.droidflatauction;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by bdlions on 07/06/2017.
 */

public class MyAdvertPropertyAdapter extends BaseAdapter {
    private static Context context;
    private ArrayList<Integer> listId;
    private ArrayList<String> property_title_list, property_bedroom_list,property_bathroom_list,property_price_list;


    public MyAdvertPropertyAdapter(Context context, ArrayList<Integer> listId, ArrayList<String> property_title_list,ArrayList<String> property_bedroom_list,ArrayList<String> property_bathroom_list,ArrayList<String> property_price_list) {
        this.context = context;
        this.listId = listId;
        this.property_title_list = property_title_list;
        this.property_bedroom_list = property_bedroom_list;
        this.property_bathroom_list = property_bathroom_list;
        this.property_price_list = property_price_list;
    }

    @Override
    public int getCount() {
        return property_title_list.size();
    }

    @Override
    public Object getItem(int position) {
        return property_title_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = View.inflate(context,R.layout.my_advert_property_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent my_advert_property_intent = new Intent(context, ManageAdvertDashboard.class);
                    context.startActivity(my_advert_property_intent);
                }
            });
        }
        ImageView property_images = (ImageView) convertView.findViewById(R.id.my_advert_property_image);

        TextView property_title = (TextView) convertView.findViewById(R.id.my_advert_property_title);
        TextView property_bedroom = (TextView) convertView.findViewById(R.id.my_advert_property_bedroom);
        TextView property_bathroom = (TextView) convertView.findViewById(R.id.my_advert_property_bathroom);
        TextView property_price = (TextView) convertView.findViewById(R.id.my_advert_property_price);
        property_images.setImageResource(listId.get(position));
        property_title.setText(property_title_list.get(position));
        property_bedroom.setText(property_bedroom_list.get(position));
        property_bathroom.setText(property_bathroom_list.get(position));
        property_price.setText(property_price_list.get(position));

        return convertView;
    }


}
