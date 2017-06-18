package auction.org.droidflatauction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by bdlions on 18/06/2017.
 */

public class ManageAdvertRankingAdapter extends ArrayAdapter<String> {
    int vg;
    String []ranking_list_items;
    Context context;

    public ManageAdvertRankingAdapter(Context context, int vg, int id, String[] ranking_list_items){
            super(context, vg, id, ranking_list_items);
        this.context = context;
        this.ranking_list_items = ranking_list_items;
        this.vg = vg;
    }

   public static class ViewHolder{
        public TextView txrank;
        public TextView txadvert;
       public TextView txcurrentbid;
       public TextView txtopbid;
    }
public View getView(int position, View convertView, ViewGroup parent){
View rowView = convertView;
    if(rowView == null){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(vg, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.txrank = (TextView) rowView.findViewById(R.id.txrank);
        holder.txadvert = (TextView) rowView.findViewById(R.id.txadvert);
        holder.txcurrentbid = (TextView) rowView.findViewById(R.id.txcurrentbid);
        holder.txtopbid = (TextView) rowView.findViewById(R.id.txtopbid);
        rowView.setTag(holder);
    }

    String [] items = ranking_list_items[position].split("__");
    ViewHolder holder = (ViewHolder)rowView.getTag();
    holder.txrank.setText(items[0]);
    holder.txadvert.setText(items[1]);
    holder.txcurrentbid.setText(items[2]);
    holder.txtopbid.setText(items[3]);
    return rowView;
}
}
