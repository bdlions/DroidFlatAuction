package auction.org.droidflatauction;

import android.widget.BaseAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bdlions on 18/06/2017.
 */

public class ManageAdvertIndividualAdBidsStep1ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> property_title_list, property_postcode_list;


    public ManageAdvertIndividualAdBidsStep1ProductAdapter(Context context, ArrayList<String> property_title_list,ArrayList<String> property_postcode_list) {
        this.context = context;
        this.property_title_list = property_title_list;
        this.property_postcode_list = property_postcode_list;
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
            convertView = View.inflate(context,R.layout.manage_advert_individual_ad_bids_step1_row, null);
            /*convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent saved_advert_property_step2_intent = new Intent(context, SavedAdvertStep2.class);
                    context.startActivity(saved_advert_property_step2_intent);
                }
            });*/
        }

        ImageView property_images = (ImageView) convertView.findViewById(R.id.saved_advert_property_image);
        TextView property_title = (TextView) convertView.findViewById(R.id.property_title);
        TextView property_bedroom = (TextView) convertView.findViewById(R.id.property_postcode);
        property_title.setText(property_title_list.get(position));
        property_bedroom.setText(property_postcode_list.get(position));
        return convertView;
    }


}

