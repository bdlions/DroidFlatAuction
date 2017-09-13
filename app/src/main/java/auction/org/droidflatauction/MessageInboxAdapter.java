package auction.org.droidflatauction;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.auction.dto.MessageText;
import com.auction.dto.Product;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdlions on 14/06/2017.
 */

public class MessageInboxAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> messageIdList, listId;
    private ArrayList<String>message_sender_list,message_subject_list, imgList;

    public String sessionId;

    public MessageInboxAdapter(Context context, String sessionId, ArrayList<Integer> messageIdList, ArrayList<Integer> listId, ArrayList<String> imgList, ArrayList<String> message_sender_list,ArrayList<String> message_subject_list) {
        this.context = context;
        this.sessionId = sessionId;
        this.messageIdList = messageIdList;
        this.listId = listId;
        this.imgList = imgList;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = View.inflate(context,R.layout.message_inbox_row, null);
        }

        ImageView userImage = (ImageView) convertView.findViewById(R.id.user_profile_image);
        TextView message_sender_name = (TextView) convertView.findViewById(R.id.message_sender);
        TextView message_subject = (TextView) convertView.findViewById(R.id.message_subject);
        //userImage.setImageResource(listId.get(position));
        String imageName = imgList.get(position);
        Picasso.with(convertView.getContext()).load(Constants.baseUrl+Constants.profilePicturePath_50_50+imageName).into(userImage);
        message_sender_name.setText(message_sender_list.get(position));
        message_subject.setText(message_subject_list.get(position));
        return convertView;
    }


}
