package auction.org.droidflatauction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bdlions on 15/06/2017.
 */

public class MemberPropertySearchAdapter extends RecyclerView.Adapter<MemberPropertySearchAdapter.MyViewHolder> {
    ArrayList<Place> placeArrayList = new ArrayList<>();

    MemberPropertySearchAdapter(ArrayList<Place> placeArrayList) {
        this.placeArrayList = placeArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_property_place_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.p_name.setText(placeArrayList.get(position).getPlace());
    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView p_name;

        public MyViewHolder(View view) {
            super(view);

            p_name = (TextView) itemView.findViewById(R.id.search_place);
        }
    }

}
