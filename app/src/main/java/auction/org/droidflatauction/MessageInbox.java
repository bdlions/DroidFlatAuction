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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.bdlions.dto.response.ClientListResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.auction.udp.BackgroundWork;
import org.bdlions.auction.dto.DTOMessageBody;
import org.bdlions.auction.entity.EntityMessageBody;
import java.util.ArrayList;
import java.util.List;

public class MessageInbox extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    ListView messageListView;
    ArrayList<Integer> user_images;
    ArrayList<String> user_list,message_subject_list;
    MessageInboxAdapter messageInboxAdapter;

    public ArrayList<Integer> messageIdList = new ArrayList<Integer>();
    public ArrayList<String> userNameList = new ArrayList<String>();
    public ArrayList<String> subjectList = new ArrayList<String>();
    ArrayList<Integer> imageList = new ArrayList<Integer>();
    ArrayList<String> imgList = new ArrayList<String>();

    SessionManager session;
    public int fetchMessageInfoCounter = 0;
    public Dialog progressBarDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onClickButtonBackArrowListener();

        // Session Manager
        session = new SessionManager(getApplicationContext());

        messageIdList = (ArrayList<Integer>)getIntent().getExtras().get("messageIdList");
        userNameList = (ArrayList<String>)getIntent().getExtras().get("userNameList");
        subjectList = (ArrayList<String>)getIntent().getExtras().get("subjectList");
        imageList = (ArrayList<Integer>)getIntent().getExtras().get("imageList");
        imgList = (ArrayList<String>)getIntent().getExtras().get("imgList");

        messageListView = (ListView) findViewById(R.id.message_listview);

        user_images = new ArrayList<>();
        //user_iamges = getUserIamges();
        //user_list = getMessageSenderList();
        //message_subject_list = getMessageSubjectList();
        //messageInboxAdapter = new MessageInboxAdapter(MessageInbox.this,user_images,user_list,message_subject_list);
        messageInboxAdapter = new MessageInboxAdapter(MessageInbox.this, session.getSessionId(), messageIdList, imageList, imgList, userNameList, subjectList);
        messageListView.setAdapter(messageInboxAdapter);
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int messageId = messageIdList.get(position);
                progressBarDialog = new Dialog(MessageInbox.this);
                progressBarDialog.setContentView(R.layout.progressbar);
                progressBarDialog.show();
                fetchMessageInfo(messageId);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void fetchMessageInfo(final int messageId)
    {
        DTOMessageBody dtoMessageBody = new DTOMessageBody();
        EntityMessageBody entityMessageBody = new EntityMessageBody();
        entityMessageBody.setMessageHeaderId(messageId);
        dtoMessageBody.setEntityMessageBody(entityMessageBody);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String messageString = gson.toJson(dtoMessageBody);
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_MESSAGE_BODY_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, messageString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    String resultString = (String)msg.obj;
                    Gson gson = new Gson();
                    ClientListResponse response = gson.fromJson(resultString, ClientListResponse.class);
                    System.out.println(response);
                    List<DTOMessageBody> messageTextList = (List<DTOMessageBody>)response.getList();

                    int messageTextListCounter = messageTextList.size();
                    ArrayList<String> messageBodyList = new ArrayList<String>();
                    ArrayList<String> userNameList = new ArrayList<String>();
                    ArrayList<Integer> imageList = new ArrayList<Integer>();
                    ArrayList<String> imgList = new ArrayList<String>();
                    ArrayList<String> timeList = new ArrayList<String>();
                    for(int counter = 0 ; counter < messageTextListCounter ; counter++)
                    {
                        DTOMessageBody messageText = messageTextList.get(counter);
                        messageBodyList.add(messageText.getEntityMessageBody().getMessage());
                        if(messageText.getEntityUser() != null)
                        {
                            userNameList.add(messageText.getEntityUser().getFirstName() + messageText.getEntityUser().getLastName());
                            imgList.add(messageText.getEntityUser().getImg());
                        }
                        else
                        {
                            userNameList.add("");
                            imgList.add("");
                        }
                        imageList.add(R.drawable.user);
                        timeList.add(messageText.getCreatedTime());
                    }
                    Intent message_inbox_row_intent = new Intent(MessageInbox.this, MessageShow.class);
                    message_inbox_row_intent.putExtra("userNameList", userNameList);
                    message_inbox_row_intent.putExtra("imageList", imageList);
                    message_inbox_row_intent.putExtra("imgList", imgList);
                    message_inbox_row_intent.putExtra("messageBodyList", messageBodyList);
                    message_inbox_row_intent.putExtra("timeList", timeList);
                    message_inbox_row_intent.putExtra("messageId", messageId);
                    startActivity(message_inbox_row_intent);

                    progressBarDialog.dismiss();
                }
                catch(Exception ex)
                {
                    fetchMessageInfoCounter++;
                    if (fetchMessageInfoCounter <= Constants.MAX_REPEAT_SERVER_REQUEST)
                    {
                        fetchMessageInfo(messageId);
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.message_inbox_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent message_inbox_back_arrow_intent = new Intent(getBaseContext(), MessageDashboard.class);
                        startActivity(message_inbox_back_arrow_intent);
                    }
                }
        );
    }

    public ArrayList<Integer> getUserIamges(){
        user_images = new ArrayList<>();
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        user_images.add(R.drawable.user);
        return user_images;
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
    public ArrayList<String> getMessageSubjectList(){
        message_subject_list = new ArrayList<>();
        message_subject_list.add("I need 1 Flat");
        message_subject_list.add("I need 2 Flats");
        message_subject_list.add("I need 3 Flats");
        message_subject_list.add("I need 4 Flats");
        message_subject_list.add("I need 5 Flats");
        message_subject_list.add("I need 6 Flats");
        message_subject_list.add("I need 7 Flats");
        message_subject_list.add("I need 8 Flats");
        message_subject_list.add("I need 9 Flats");
        message_subject_list.add("I need 10 Flats");
        return message_subject_list;
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
        getMenuInflater().inflate(R.menu.message_inbox, menu);
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
      //      return true;
       // }

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
