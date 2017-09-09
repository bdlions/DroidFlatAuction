package auction.org.droidflatauction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.auction.dto.Message;
import com.auction.dto.MessageText;
import com.auction.dto.Product;
import com.auction.dto.User;
import com.auction.dto.response.GeneralResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;

public class ContactThoughMessage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private static TextView tvPropertyOwner, tvMessageSubject;
    private EditText etMessageText;
    private  static Button btnMessageSend;
    SessionManager session;
    NavigationManager navigationManager;
    Product product;
    String productString;
    int adIdentity;
    public int sendContactMessageCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_though_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());
        tvPropertyOwner = (TextView)findViewById(R.id.property_owner);
        tvMessageSubject = (TextView)findViewById(R.id.message_subject);
        btnMessageSend = (Button)findViewById(R.id.btn_send_message);
        etMessageText = (EditText) findViewById(R.id.message_text);


        try
        {
            adIdentity = getIntent().getExtras().getInt("adIdentity");
            productString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, Product.class);

            //-------------------------set user name and subject
            tvPropertyOwner.setText(product.getUser().getFirstName() + " " + product.getUser().getLastName());
            tvMessageSubject.setText("Re : "+product.getTitle());
        }
        catch(Exception ex)
        {
            //handle exception
        }

        onClickButtonBackArrowListener();
        onClickButtonMessageSendListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.send_message_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent send_message_back_arrow_intent = new Intent(getBaseContext(), SavedAdvertStep2.class);
                        //startActivity(send_message_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonMessageSendListener(){
        btnMessageSend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String body = etMessageText.getText().toString();
                        if(body.equals(""))
                        {
                            Toast.makeText(ContactThoughMessage.this, "Please add message!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sendContactMessage();
                    }
                }
        );
    }

    public void sendContactMessage()
    {
        String body = etMessageText.getText().toString();
        MessageText messageText = new MessageText();
        //set message text
        messageText.setBody(body);

        Message message = new Message();
        Product tempProduct = new Product();
        tempProduct.setUser(new User());
        tempProduct.setId(product.getId());
        tempProduct.getUser().setId(product.getUser().getId());
        message.setProduct(tempProduct);
        message.setSubject("Re : "+product.getTitle());
        message.setMessageTextList(new ArrayList<MessageText>());
        message.getMessageTextList().add(messageText);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String tempMessageString = gson.toJson(message);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.ADD_MESSAGE_INFO);
        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, tempMessageString, new Handler(){
            @Override
            public void handleMessage(android.os.Message msg) {
                try
                {
                    GeneralResponse response = null;
                    String responseString = null;
                    if(msg != null && msg.obj != null)
                    {
                        responseString = (String) msg.obj;
                    }
                    if(responseString != null)
                    {
                        Gson gson = new Gson();
                        response = gson.fromJson(responseString, GeneralResponse.class);
                    }
                    if(response != null)
                    {
                        //show success message and reset input fields
                        if(response.isSuccess())
                        {
                            Toast.makeText(ContactThoughMessage.this, response.getMessage(),Toast.LENGTH_SHORT).show();
                            //redirect to product details page
                            Intent productDetailsIntent = new Intent(ContactThoughMessage.this, ShowAdvertProductDetails.class);
                            productDetailsIntent.putExtra("productString", productString);
                            productDetailsIntent.putExtra("adIdentity", Constants.MY_AD_IDENTITY);
                            startActivity(productDetailsIntent);
                        }
                        else
                        {
                            //show error message
                            Toast.makeText(ContactThoughMessage.this, response.getMessage(),Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else
                    {
                        sendContactMessageCounter++;
                        if (sendContactMessageCounter <= 5)
                        {
                            sendContactMessage();
                        }
                    }
                }
                catch(Exception ex)
                {
                    sendContactMessageCounter++;
                    if (sendContactMessageCounter <= 5)
                    {
                        sendContactMessage();
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
        getMenuInflater().inflate(R.menu.contact_though_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //navigationManager.navigateTo(id);

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
        }
        else if (id == R.id.nav_logout) {
            session.logoutUser();
            //Intent member_logout_intent = new Intent(getBaseContext(), NonMemberHome.class);
            //startActivity(member_logout_intent);
        }
        else if (id == R.id.nav_email) {

        } else if (id == R.id.nav_phone) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
