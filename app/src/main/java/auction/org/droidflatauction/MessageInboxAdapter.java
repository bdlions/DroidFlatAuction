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

public class MessageInboxAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> listId;
    private ArrayList<String>message_sender_list,message_subject_list;


    public MessageInboxAdapter(Context context, ArrayList<Integer> listId, ArrayList<String> message_sender_list,ArrayList<String> message_subject_list) {
        this.context = context;
        this.listId = listId;
        this.message_sender_list = message_sender_list;
        this.message_subject_list = message_subject_list;
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
            convertView = View.inflate(context,R.layout.message_inbox_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent message_inbox_row_intent = new Intent(context, MessageShow.class);
                    context.startActivity(message_inbox_row_intent);
                }
            });
        }

        ImageView user_image = (ImageView) convertView.findViewById(R.id.user_profile_image);
        TextView message_sender_name = (TextView) convertView.findViewById(R.id.message_sender);
        TextView message_subject = (TextView) convertView.findViewById(R.id.message_subject);
        user_image.setImageResource(listId.get(position));
        message_sender_name.setText(message_sender_list.get(position));
        message_subject.setText(message_subject_list.get(position));
        return convertView;
    }


}
