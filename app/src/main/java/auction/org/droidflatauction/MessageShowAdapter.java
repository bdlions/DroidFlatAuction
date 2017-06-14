package auction.org.droidflatauction;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bdlions on 14/06/2017.
 */

public class MessageShowAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> listId;
    private ArrayList<String>message_sender_list,message_time_list,message_text_list;


    public MessageShowAdapter(Context context, ArrayList<Integer> listId, ArrayList<String> message_sender_list,ArrayList<String> message_time_list,ArrayList<String> message_text_list) {
        this.context = context;
        this.listId = listId;
        this.message_sender_list = message_sender_list;
        this.message_time_list = message_time_list;
        this.message_text_list = message_text_list;
    }

    @Override
    public int getCount() {
        return message_sender_list.size();
    }

    @Override
    public Object getItem(int position) {
        return message_sender_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = View.inflate(context,R.layout.message_show_row, null);
        }

        ImageView user_image = (ImageView) convertView.findViewById(R.id.user_profile_image);
        TextView message_sender_name = (TextView) convertView.findViewById(R.id.message_sender);
        TextView message_time = (TextView) convertView.findViewById(R.id.message_time);
        TextView message_text = (TextView) convertView.findViewById(R.id.message_text);
        user_image.setImageResource(listId.get(position));
        message_sender_name.setText(message_sender_list.get(position));
        message_time.setText(message_time_list.get(position));
        message_text.setText(message_text_list.get(position));
        return convertView;
    }
}
