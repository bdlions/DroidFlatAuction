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
 * Created by bdlions on 09/09/2017.
 */

public class RoleAdapter extends BaseAdapter {
    Activity activity;
    List<RoleDTO> roles;
    LayoutInflater layoutInflater;

    public RoleAdapter(Activity activity){

    }

    public RoleAdapter(Activity activity, List<RoleDTO> roles) {
        this.activity = activity;
        this.roles = roles;

        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return roles.size();
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
        RoleAdapter.ViewHolder holder = null;
        if(view == null){
            view = layoutInflater.inflate(R.layout.roles_list_view_item,viewGroup, false);
            holder = new RoleAdapter.ViewHolder();
            holder.tvRole = (TextView)view.findViewById(R.id.tv_role);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);
            view.setTag(holder);
        }
        else
            holder = (RoleAdapter.ViewHolder)view.getTag();
        RoleDTO model = roles.get(i);

        holder.tvRole.setText(model.getRole());

        if(model.isSelected()){
            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);
        }
        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.check);

        return view;
    }

    public void updateRecords(List<RoleDTO> roles){
        this.roles = roles;
        notifyDataSetChanged();

    }

    public class ViewHolder{
        TextView tvRole;
        ImageView ivCheckBox;
    }
}
