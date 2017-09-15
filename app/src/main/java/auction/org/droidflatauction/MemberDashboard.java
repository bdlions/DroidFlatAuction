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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auction.dto.Location;
import com.auction.dto.LocationList;
import com.auction.dto.Product;
import com.auction.dto.ProductList;
import com.auction.dto.User;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;
import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;

public class MemberDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static ImageView iv_profile_photo;
    private  static LinearLayout manage_advert_layout, message_layout, profile_layout, account_settings_layout, property_search_layout;
    private static TextView tv_md_user_full_name;

    public int fetchProfileCounter = 0;

    SessionManager session;
    NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        onClickButtonManageAdvertDashboardListener();
        onClickButtonMessageDashboardListener();
        onClickButtonProfileDashboardListener();
        onClickButtonAccountSettingsDashboardListener();
        onClickButtonPropertySearchListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tv_md_user_full_name = (TextView)  findViewById(R.id.tv_md_user_full_name);
        iv_profile_photo = (ImageView) findViewById(R.id.profile_image);
        this.initUserProfile();
    }

    public void initUserProfile()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_USER_INFO);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                User user = null;
                String userString = null;
                if(msg != null  && msg.obj != null)
                {
                    userString = (String) msg.obj;
                }
                if(userString != null)
                {
                    Gson gson = new Gson();
                    user = gson.fromJson(userString, User.class);
                }
                if(user != null && user.isSuccess())
                {
                    if(session.getUserId() == 0)
                    {
                        session.setUserId(user.getId());
                    }
                    tv_md_user_full_name.setText(user.getFirstName()+" "+user.getLastName());
                    Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getImg()).into(iv_profile_photo);
                }
                else
                {
                    fetchProfileCounter++;
                    if (fetchProfileCounter <= 5)
                    {
                        initUserProfile();
                    }
                }
            }
        });

    }

    public void onClickButtonManageAdvertDashboardListener(){
        manage_advert_layout = (LinearLayout) findViewById(R.id.manage_advert);
        manage_advert_layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent manage_advert_intent = new Intent(getBaseContext(), ManageAdvertDashboard.class);
                        startActivity(manage_advert_intent);
                    }
                }
        );
    }

    public void onClickButtonMessageDashboardListener(){
        message_layout = (LinearLayout) findViewById(R.id.message);
        message_layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent message_intent = new Intent(getBaseContext(), MessageDashboard.class);
                        startActivity(message_intent);
                    }
                }
        );
    }

    public void onClickButtonProfileDashboardListener(){
        profile_layout = (LinearLayout) findViewById(R.id.profile);
        profile_layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profile_dashboard_intent = new Intent(getBaseContext(), ProfileDashboard.class);
                        startActivity(profile_dashboard_intent);
                    }
                }
        );
    }

    public void onClickButtonAccountSettingsDashboardListener(){
        account_settings_layout = (LinearLayout) findViewById(R.id.account_settings);
        account_settings_layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent account_settings_intent = new Intent(getBaseContext(), AccountSettingsDashboard.class);
                        startActivity(account_settings_intent);
                    }
                }
        );
    }

    public void onClickButtonPropertySearchListener(){
        property_search_layout = (LinearLayout) findViewById(R.id.search);
        property_search_layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sessionId = session.getSessionId();
                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        packetHeader.setAction(ACTION.FETCH_LOCATION_LIST);
                        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
                        packetHeader.setSessionId(sessionId);
                        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                try
                                {
                                    String resultString = (String)msg.obj;
                                    Gson gson = new Gson();
                                    LocationList response = gson.fromJson(resultString, LocationList.class);

                                    ArrayList<String> locationList = new ArrayList<String>();
                                    if(response != null)
                                    {

                                        int totalLocations = response.getLocations().size();
                                        String[] items = new String[totalLocations];
                                        for(int counter = 0; counter < totalLocations; counter++)
                                        {
                                            Location location = response.getLocations().get(counter);
                                            locationList.add(location.getSearchString());
                                            items[counter] = location.getSearchString();
                                        }
                                        Intent property_search_intent = new Intent(getBaseContext(), MemberPropertySearch.class);
                                        property_search_intent.putExtra("locationList", locationList);
                                        property_search_intent.putExtra("items", items);
                                        startActivity(property_search_intent);
                                    }
                                }
                                catch(Exception ex)
                                {
                                    System.out.println(ex.toString());
                                }
                            }
                        });

                    }
                }
        );
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
        getMenuInflater().inflate(R.menu.member_dashboard, menu);
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
       // }

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
