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

public class AvailablabilityAdapter extends BaseAdapter {
    Activity activity;
    List<AvailablabilityModel> availablabilities;
    LayoutInflater layoutInflater;

    public AvailablabilityAdapter(Activity activity){

    }

    public AvailablabilityAdapter(Activity activity, List<AvailablabilityModel> availablabilities) {
        this.activity = activity;
        this.availablabilities = availablabilities;

        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return availablabilities.size();
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
        ViewHolder holder = null;
        if(view == null){
            view = layoutInflater.inflate(R.layout.availabilities_list_view_item,viewGroup, false);
            holder = new ViewHolder();
            holder.tvAvailablability = (TextView)view.findViewById(R.id.tv_availablability);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder)view.getTag();
        AvailablabilityModel model = availablabilities.get(i);

        holder.tvAvailablability.setText(model.getAvailablability());

        if(model.isSelected()){
            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);
        }
        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.check);

        return view;
    }

    public void updateRecords(List<AvailablabilityModel> availablabilities){
        this.availablabilities = availablabilities;
        notifyDataSetChanged();

    }

   public class ViewHolder{
        TextView tvAvailablability;
        ImageView ivCheckBox;
    }
}
