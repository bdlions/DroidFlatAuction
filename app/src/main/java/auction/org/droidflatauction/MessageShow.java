package auction.org.droidflatauction;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.auction.dto.Image;
import com.auction.dto.Location;
import com.auction.dto.MessageText;
import com.auction.dto.Occupation;
import com.auction.dto.Pet;
import com.auction.dto.ProductCategory;
import com.auction.dto.ProductSize;
import com.auction.dto.ProductType;
import com.auction.dto.Smoking;
import com.auction.dto.Stay;
import com.auction.dto.response.SignInResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.List;

public class MessageShow extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private EditText etInboxSendMessageBody;
    private Button btnInboxSendMessage;
    ListView messageListView;
    ArrayList<Integer> user_iamges;
    ArrayList<String> user_list,message_time_list,message_text_list;
    MessageShowAdapter messageInboxAdapter;

    ArrayList<String> messageBodyList = new ArrayList<String>();
    ArrayList<String> userNameList = new ArrayList<String>();
    ArrayList<Integer> imageList = new ArrayList<Integer>();
    ArrayList<String> imgList = new ArrayList<String>();
    ArrayList<String> timeList = new ArrayList<String>();

    public com.auction.dto.Message message;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onClickButtonBackArrowListener();

        // Session Manager
        session = new SessionManager(getApplicationContext());

        etInboxSendMessageBody = (EditText) findViewById(R.id.et_inbox_send_message_body);
        btnInboxSendMessage = (Button) findViewById(R.id.btn_inbox_send_message);
        onClickButtonSendMessageListener();

        message = new com.auction.dto.Message();
        message.setId(getIntent().getExtras().getInt("messageId"));

        messageBodyList = (ArrayList<String>)getIntent().getExtras().get("messageBodyList");
        userNameList = (ArrayList<String>)getIntent().getExtras().get("userNameList");
        imageList = (ArrayList<Integer>)getIntent().getExtras().get("imageList");
        imgList = (ArrayList<String>)getIntent().getExtras().get("imgList");
        timeList = (ArrayList<String>)getIntent().getExtras().get("timeList");

        messageListView = (ListView) findViewById(R.id.message_show_listview);

        user_iamges = new ArrayList<>();
        //user_iamges = getUserIamges();
        //user_list = getMessageSenderList();
        //message_time_list = getMessageTimeList();
        //message_text_list = getMessageTextList();

        //messageInboxAdapter = new MessageShowAdapter(MessageShow.this,user_iamges,user_list,message_time_list,message_text_list);
        messageInboxAdapter = new MessageShowAdapter(MessageShow.this,imageList, imgList, userNameList, timeList, messageBodyList);

        messageListView.setAdapter(messageInboxAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onClickButtonSendMessageListener(){
        btnInboxSendMessage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageText messageText = new MessageText();
                        messageText.setBody(etInboxSendMessageBody.getText().toString());
                        messageText.setMessageId(message.getId());
                        List<MessageText> messageTextList = new ArrayList<>();
                        messageTextList.add(messageText);
                        message.setMessageTextList(messageTextList);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String messageString = gson.toJson(message);


                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        packetHeader.setAction(ACTION.ADD_MESSAGE_TEXT);
                        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
                        packetHeader.setSessionId(session.getSessionId());
                        new BackgroundWork().execute(packetHeader, messageString, new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                String resultString = (String)msg.obj;
                                Gson gson = new Gson();
                                com.auction.dto.Message response = gson.fromJson(resultString, com.auction.dto.Message.class);
                                if(response.isSuccess())
                                {
                                    Toast.makeText(getApplicationContext(), "Message is sent successfully.", Toast.LENGTH_LONG).show();
                                    etInboxSendMessageBody.setText("");
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Error while sending message Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
        );
    }

    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.message_show_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent message_show_back_arrow_intent = new Intent(getBaseContext(), MessageDashboard.class);
                        startActivity(message_show_back_arrow_intent);
                    }
                }
        );
    }

    public ArrayList<Integer> getUserIamges(){
        user_iamges = new ArrayList<>();
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        user_iamges.add(R.drawable.user);
        return user_iamges;
    }

    public ArrayList<String> getMessageSenderList(){
        user_list = new ArrayList<>();
        user_list.add("Nazmul Hasan");
        user_list.add("Alamgir Kabir");
        user_list.add("Nazmul Hasan");
        user_list.add("Alamgir Kabir");
        user_list.add("Nazmul Hasan");
        user_list.add("Alamgir Kabir");
        user_list.add("Nazmul Hasan");
        user_list.add("Alamgir Kabir");
        user_list.add("Nazmul Hasan");
        user_list.add("Alamgir Kabir");
        return user_list;
    }
    public ArrayList<String> getMessageTimeList(){
        message_time_list = new ArrayList<>();
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        message_time_list.add("2017-04-26 8:00AM");
        return message_time_list;
    }
    public ArrayList<String> getMessageTextList(){
        message_text_list = new ArrayList<>();
        message_text_list.add("when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
        message_text_list.add("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour");
        message_text_list.add("when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
        message_text_list.add("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour");
        message_text_list.add("when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
        message_text_list.add("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour");
        message_text_list.add("when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
        message_text_list.add("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour");
        message_text_list.add("when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
        message_text_list.add("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour");
        return message_text_list;
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
        getMenuInflater().inflate(R.menu.message_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
        //    return true;
        //}

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
