package auction.org.droidflatauction;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bdlions on 29/08/2017.
 */

public class AmenityAdapter extends BaseAdapter {
    Activity activity;
    List<DTOAmenity> amenities;
    LayoutInflater layoutInflater;

    public AmenityAdapter(Activity activity){

    }

    public AmenityAdapter(Activity activity, List<DTOAmenity> amenities) {
        this.activity = activity;
        this.amenities = amenities;

        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return amenities.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AmenityAdapter.ViewHolder holder = null;
        if(view == null){
            view = layoutInflater.inflate(R.layout.amenities_list_view_item,viewGroup, false);
            holder = new AmenityAdapter.ViewHolder();
            holder.tvAmenity = (TextView)view.findViewById(R.id.tv_amenity);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);
            view.setTag(holder);
        }
        else
            holder = (AmenityAdapter.ViewHolder)view.getTag();
        DTOAmenity model = amenities.get(i);

        holder.tvAmenity.setText(model.getAmenity());

        if(model.isSelected()){
            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);
        }
        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.check);

        return view;
    }

    public void updateRecords(List<DTOAmenity> amenities){
        this.amenities = amenities;
        notifyDataSetChanged();

    }

    public class ViewHolder{
        TextView tvAmenity;
        ImageView ivCheckBox;
    }
}
