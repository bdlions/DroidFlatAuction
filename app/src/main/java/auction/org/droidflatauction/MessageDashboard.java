package auction.org.droidflatauction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import com.bdlions.dto.response.ClientListResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.auction.udp.BackgroundWork;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static Button btn_msg_inbox, btn_msg_sent,demo_pagination;
    SessionManager session;
    public Dialog progressBarDialog;

    public int fetchMessageInboxListCounter = 0;
    public int fetchMessageSentListCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        onClickButtonMessageInboxListener();
        onClickButtonMessageSentListener();
        //onClickButtonPaginationListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /*public void onClickButtonPaginationListener(){
        demo_pagination = (Button) findViewById(R.id.demo_pagination);
        demo_pagination.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent demo_pagination_intent = new Intent(getBaseContext(), DemoPagination.class);
                        startActivity(demo_pagination_intent);
                    }
                }
        );
    }*/
    public void onClickButtonMessageInboxListener(){
        btn_msg_inbox = (Button) findViewById(R.id.inbox_button);
        btn_msg_inbox.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressBarDialog = new Dialog(MessageDashboard.this);
                        progressBarDialog.setContentView(R.layout.progressbar);
                        progressBarDialog.show();

                        fetchMessageInboxList();

                    }
                }
        );
    }

    public void fetchMessageInboxList()
    {
        DTOMessageHeader dtoMessageHeader = new DTOMessageHeader();
        dtoMessageHeader.setOffset(0);
        dtoMessageHeader.setLimit(10);
        dtoMessageHeader.setSender(new EntityUser());
        dtoMessageHeader.getSender().setId(session.getUserId());
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String dtoMessageHeaderString = gson.toJson(dtoMessageHeader);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_MESSAGE_INBOX_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, dtoMessageHeaderString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    List<DTOMessageHeader> messageList = new ArrayList<>();
                    ClientListResponse clientListResponse = null;
                    String clientListResponseString = null;
                    Gson gson = new Gson();
                    if(msg != null  && msg.obj != null)
                    {
                        clientListResponseString = (String) msg.obj;
                    }
                    if(clientListResponseString != null)
                    {
                        clientListResponse = gson.fromJson(clientListResponseString, ClientListResponse.class);
                    }
                    if(clientListResponse != null && clientListResponse.isSuccess())
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(clientListResponseString);
                            messageList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<DTOMessageHeader>>(){}.getType());
                            if(messageList == null)
                            {
                                return;
                            }
                        }
                        catch(Exception ex)
                        {
                            return;
                        }
                    }

                    ArrayList<Integer> messageIdList = new ArrayList<Integer>();
                    ArrayList<String> userNameList = new ArrayList<String>();
                    ArrayList<String> subjectList = new ArrayList<String>();
                    ArrayList<Integer> imageList = new ArrayList<Integer>();
                    ArrayList<String> imgList = new ArrayList<String>();

                    if(messageList != null)
                    {
                        int totalMessages = messageList.size();
                        for(int messageCounter = 0; messageCounter < totalMessages; messageCounter++)
                        {
                            DTOMessageHeader message = messageList.get(messageCounter);
                            messageIdList.add(message.getEntityMessageHeader().getId());
                            userNameList.add(message.getReceiver().getFirstName() + " " + message.getReceiver().getLastName());
                            subjectList.add(message.getEntityMessageHeader().getSubject());
                            imageList.add(R.drawable.user);
                            imgList.add(message.getReceiver().getImg());
                        }
                    }
                    progressBarDialog.dismiss();
                    Intent inbox_button_intent = new Intent(getBaseContext(), MessageInbox.class);
                    inbox_button_intent.putExtra("messageIdList", messageIdList);
                    inbox_button_intent.putExtra("userNameList", userNameList);
                    inbox_button_intent.putExtra("subjectList", subjectList);
                    inbox_button_intent.putExtra("imageList", imageList);
                    inbox_button_intent.putExtra("imgList", imgList);
                    startActivity(inbox_button_intent);
                }
                catch(Exception ex)
                {
                    fetchMessageInboxListCounter++;
                    if (fetchMessageInboxListCounter <= Constants.MAX_REPEAT_SERVER_REQUEST)
                    {
                        fetchMessageInboxList();
                    }
                    else
                    {
                        //display a popup to show error message
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    public void onClickButtonMessageSentListener(){
        btn_msg_sent = (Button) findViewById(R.id.Sent_message_button);
        btn_msg_sent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBarDialog = new Dialog(MessageDashboard.this);
                        progressBarDialog.setContentView(R.layout.progressbar);
                        progressBarDialog.show();

                        fetchMessageSentList();
                    }
                }
        );
    }

    public void fetchMessageSentList()
    {
        DTOMessageHeader dtoMessageHeader = new DTOMessageHeader();
        dtoMessageHeader.setOffset(0);
        dtoMessageHeader.setLimit(10);
        dtoMessageHeader.setSender(new EntityUser());
        dtoMessageHeader.getSender().setId(session.getUserId());
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String dtoMessageHeaderString = gson.toJson(dtoMessageHeader);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_MESSAGE_SENT_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, dtoMessageHeaderString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    List<DTOMessageHeader> messageList = new ArrayList<>();
                    ClientListResponse clientListResponse = null;
                    String clientListResponseString = null;
                    Gson gson = new Gson();
                    if(msg != null  && msg.obj != null)
                    {
                        clientListResponseString = (String) msg.obj;
                    }
                    if(clientListResponseString != null)
                    {
                        clientListResponse = gson.fromJson(clientListResponseString, ClientListResponse.class);
                    }
                    if(clientListResponse != null && clientListResponse.isSuccess())
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(clientListResponseString);
                            messageList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<DTOMessageHeader>>(){}.getType());
                            if(messageList == null)
                            {
                                return;
                            }
                        }
                        catch(Exception ex)
                        {
                            return;
                        }
                    }


                    ArrayList<Integer> messageIdList = new ArrayList<Integer>();
                    ArrayList<String> userNameList = new ArrayList<String>();
                    ArrayList<String> subjectList = new ArrayList<String>();
                    ArrayList<Integer> imageList = new ArrayList<Integer>();
                    ArrayList<String> imgList = new ArrayList<String>();

                    if(messageList != null)
                    {
                        int totalMessages = messageList.size();
                        for(int messageCounter = 0; messageCounter < totalMessages; messageCounter++)
                        {
                            DTOMessageHeader message = messageList.get(messageCounter);
                            messageIdList.add(message.getEntityMessageHeader().getId());
                            userNameList.add(message.getReceiver().getFirstName() + " " + message.getReceiver().getLastName());
                            subjectList.add(message.getEntityMessageHeader().getSubject());
                            imageList.add(R.drawable.user);
                            imgList.add(message.getReceiver().getImg());
                        }
                    }
                    progressBarDialog.dismiss();
                    Intent inbox_button_intent = new Intent(getBaseContext(), MessageInbox.class);
                    inbox_button_intent.putExtra("messageIdList", messageIdList);
                    inbox_button_intent.putExtra("userNameList", userNameList);
                    inbox_button_intent.putExtra("subjectList", subjectList);
                    inbox_button_intent.putExtra("imageList", imageList);
                    inbox_button_intent.putExtra("imgList", imgList);
                    startActivity(inbox_button_intent);
                }
                catch(Exception ex)
                {
                    fetchMessageSentListCounter++;
                    if (fetchMessageSentListCounter <= Constants.MAX_REPEAT_SERVER_REQUEST)
                    {
                        fetchMessageSentList();
                    }
                    else
                    {
                        //display a popup to show error message
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
       //     return true;
      //  }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Intent member_bashboard_intent = new Intent(getBaseContext(), MemberDashboard.class);
            startActivity(member_bashboard_intent);
        } else if (id == R.id.nav_manage_advert) {
            Intent member_manage_advert_intent = new Intent(getBaseContext(), ManageAdvertDashboard.class);
            startActivity(member_manage_advert_intent);
        } else if (id == R.id.nav_message) {
            Intent member_message_intent = new Intent(getBaseContext(), MessageDashboard.class);
            startActivity(member_message_intent);
        } else if (id == R.id.nav_profile) {
            Intent member_bashboard_intent = new Intent(getBaseContext(), ProfileDashboard.class);
            startActivity(member_bashboard_intent);

        }else if (id == R.id.nav_account_settings) {
            Intent member_account_settings_intent = new Intent(getBaseContext(), AccountSettingsDashboard.class);
            startActivity(member_account_settings_intent);
        }else if (id == R.id.nav_search) {
            Intent member_account_settings_intent = new Intent(getBaseContext(), MemberPropertySearch.class);
            startActivity(member_account_settings_intent);
        } else if (id == R.id.nav_logout) {
            session.logoutUser();
            //Intent member_logout_intent = new Intent(getBaseContext(), NonMemberHome.class);
            //startActivity(member_logout_intent);
        } else if (id == R.id.nav_email) {
            Intent member_email_intent = new Intent(getBaseContext(), Email.class);
            startActivity(member_email_intent);
        } else if (id == R.id.nav_phone) {
            Intent member_phone_intent = new Intent(getBaseContext(), Phone.class);
            startActivity(member_phone_intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
