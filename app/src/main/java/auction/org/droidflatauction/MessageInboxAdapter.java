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

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdlions on 14/06/2017.
 */

public class MessageInboxAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> messageIdList, listId;
    private ArrayList<String>message_sender_list,message_subject_list;

    public String sessionId;

    public MessageInboxAdapter(Context context, String sessionId, ArrayList<Integer> messageIdList, ArrayList<Integer> listId, ArrayList<String> message_sender_list,ArrayList<String> message_subject_list) {
        this.context = context;
        this.sessionId = sessionId;
        this.messageIdList = messageIdList;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = View.inflate(context,R.layout.message_inbox_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int messageId = messageIdList.get(position);

                    com.auction.dto.Message requestMessage = new com.auction.dto.Message();
                    requestMessage.setId(messageId);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    String messageString = gson.toJson(requestMessage);


                    org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                    packetHeader.setAction(ACTION.FETCH_MESSAGE_INFO);
                    packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
                    packetHeader.setSessionId(sessionId);
                    new BackgroundWork().execute(packetHeader, messageString, new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            try
                            {
                                String resultString = (String)msg.obj;
                                Gson gson = new Gson();
                                com.auction.dto.Message responseMessage = gson.fromJson(resultString, com.auction.dto.Message.class);
                                System.out.println(responseMessage.getSubject());
                                if(responseMessage.getMessageTextList() != null)
                                {
                                    List<com.auction.dto.MessageText> messageTextList = responseMessage.getMessageTextList();
                                    int messageTextListCounter = messageTextList.size();
                                    ArrayList<String> messageBodyList = new ArrayList<String>();
                                    ArrayList<String> userNameList = new ArrayList<String>();
                                    ArrayList<Integer> imageList = new ArrayList<Integer>();
                                    ArrayList<String> timeList = new ArrayList<String>();
                                    for(int counter = 0 ; counter < messageTextListCounter ; counter++)
                                    {
                                        MessageText messageText = messageTextList.get(counter);
                                        messageBodyList.add(messageText.getBody());
                                        if(messageText.getUser() != null)
                                        {
                                            userNameList.add(messageText.getUser().getFirstName() + messageText.getUser().getLastName());
                                        }
                                        else
                                        {
                                            userNameList.add("");
                                        }
                                        imageList.add(R.drawable.user);
                                        timeList.add("2017-06-20 10:00AM");
                                    }
                                    Intent message_inbox_row_intent = new Intent(context, MessageShow.class);
                                    message_inbox_row_intent.putExtra("userNameList", userNameList);
                                    message_inbox_row_intent.putExtra("imageList", imageList);
                                    message_inbox_row_intent.putExtra("messageBodyList", messageBodyList);
                                    message_inbox_row_intent.putExtra("timeList", timeList);
                                    message_inbox_row_intent.putExtra("messageId", responseMessage.getId());
                                    context.startActivity(message_inbox_row_intent);
                                }



                            }
                            catch(Exception ex)
                            {
                                System.out.println(ex.toString());
                            }
                        }
                    });
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
