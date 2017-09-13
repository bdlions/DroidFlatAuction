package auction.org.droidflatauction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.auction.dto.MessageList;
import com.auction.dto.Product;
import com.auction.dto.ProductList;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.List;

public class MessageDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static Button btn_msg_inbox, btn_msg_sent;
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
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
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_MESSAGE_INBOX_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    String resultString = (String)msg.obj;
                    Gson gson = new Gson();
                    MessageList response = gson.fromJson(resultString, MessageList.class);

                    List<com.auction.dto.Message> messageList = response.getMessageList();


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
                            com.auction.dto.Message message = messageList.get(messageCounter);
                            messageIdList.add(message.getId());
                            userNameList.add(message.getFrom().getFirstName() + " " + message.getFrom().getLastName());
                            subjectList.add(message.getSubject());
                            imageList.add(R.drawable.user);
                            imgList.add(message.getFrom().getImg());
                        }
                    }
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
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_MESSAGE_SENT_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    String resultString = (String)msg.obj;
                    Gson gson = new Gson();
                    MessageList response = gson.fromJson(resultString, MessageList.class);

                    List<com.auction.dto.Message> messageList = response.getMessageList();


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
                            com.auction.dto.Message message = messageList.get(messageCounter);
                            messageIdList.add(message.getId());
                            userNameList.add(message.getFrom().getFirstName() + " " + message.getFrom().getLastName());
                            subjectList.add(message.getSubject());
                            imageList.add(R.drawable.user);
                            imgList.add(message.getFrom().getImg());
                        }
                    }
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

        } else if (id == R.id.nav_phone) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
