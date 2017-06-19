package auction.org.droidflatauction;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bdlions on 19/06/2017.
 */

public class ManageAdvertStatsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> property_date_list,property_click_list,property_impression_list,property_ctr_list,property_cost_list;


    public ManageAdvertStatsAdapter(Context context, ArrayList<String> property_date_list,ArrayList<String> property_click_list,ArrayList<String> property_impression_list,ArrayList<String> property_ctr_list,ArrayList<String> property_cost_list) {
        this.context = context;
        this.property_date_list = property_date_list;
        this.property_click_list = property_click_list;
        this.property_impression_list = property_impression_list;
        this.property_ctr_list = property_ctr_list;
        this.property_cost_list = property_cost_list;
    }

    @Override
    public int getCount() {
        return property_date_list.size();
    }

    @Override
    public Object getItem(int position) {
        return property_date_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = View.inflate(context,R.layout.manage_advert_stats_row, null);
        }

        TextView property_date = (TextView) convertView.findViewById(R.id.property_stat_date);
        TextView property_click = (TextView) convertView.findViewById(R.id.property_stat_clicks);
        TextView property_impression = (TextView) convertView.findViewById(R.id.property_stat_impression);
        TextView property_ctr = (TextView) convertView.findViewById(R.id.property_stat_ctr);
        TextView property_cost = (TextView) convertView.findViewById(R.id.property_stat_cost);
        property_date.setText(property_date_list.get(position));
        property_click.setText(property_click_list.get(position));
        property_impression.setText(property_date_list.get(position));
        property_ctr.setText(property_click_list.get(position));
        property_cost.setText(property_date_list.get(position));
        return convertView;
    }


}

