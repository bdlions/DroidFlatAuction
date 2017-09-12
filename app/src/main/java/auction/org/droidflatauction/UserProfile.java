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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auction.dto.Role;
import com.auction.dto.User;
import com.auction.dto.response.SignInResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;
import org.bdlions.client.reqeust.threads.IServerCallback;
import org.bdlions.transport.packet.IPacketHeader;

import java.util.List;

public class UserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    SessionManager session;
    public static TextView tvProfileFullName, tvProfileEmail, tvProfileTelephone,tvProfileBusinessName,tvProfileBusinessNameHeader,tvProfileAddress,tvProfileAddressHeader,tvProfileRole;
    private static ImageView ivProfilePhoto, ivProfileAgentLogo,tvProfileAgentLogoHeader;
    public int fetchProfileCounter = 0;
    public Dialog progressBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvProfileFullName = (TextView) findViewById(R.id.tv_profile_full_name);
        tvProfileEmail = (TextView) findViewById(R.id.tv_profile_email);
        tvProfileTelephone = (TextView) findViewById(R.id.tv_profile_telephone);
        tvProfileBusinessName = (TextView) findViewById(R.id.tv_profile_business_name);
        tvProfileAddress = (TextView) findViewById(R.id.tv_profile_address);
        tvProfileRole = (TextView) findViewById(R.id.tv_profile_role);

        ivProfilePhoto = (ImageView) findViewById(R.id.iv_profile_photo);
        ivProfileAgentLogo = (ImageView) findViewById(R.id.iv_profile_agent_logo);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        onClickButtonBackArrowListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //retrieve user profile from the database
        fetchUserProfile();
    }

    public void fetchUserProfile()
    {
        progressBarDialog = new Dialog(UserProfile.this);
        progressBarDialog.setContentView(R.layout.progressbar);
        progressBarDialog.show();

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_USER_INFO);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
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
                        String roleString = "";
                        String agentString = "";
                        List<Role> roleList = user.getRoleList();

                        if(roleList != null && roleList.size() > 0)
                        {
                            for(int counter = 0; counter < roleList.size(); counter++)
                            {
                                if(counter == 0)
                                {
                                    roleString = roleList.get(counter).getDescription();
                                }
                                else
                                {
                                    roleString = roleString + ", " +roleList.get(counter).getDescription();
                                }
                            }
                        }

                        tvProfileBusinessName.setText(user.getBusinessName());
                        tvProfileAddress.setText(user.getAddress());



                        tvProfileFullName.setText(user.getFirstName()+" "+user.getLastName());
                        tvProfileEmail.setText(user.getEmail());
                        tvProfileTelephone.setText(user.getCellNo());
                        tvProfileRole.setText(roleString);

                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getImg()).into(ivProfilePhoto);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getAgentLogo()).into(ivProfileAgentLogo);
                        progressBarDialog.dismiss();
                    }
                    else
                    {
                        fetchProfileCounter++;
                        if (fetchProfileCounter <= 5)
                        {
                            fetchUserProfile();
                        }
                        else
                        {
                            progressBarDialog.dismiss();
                        }
                    }
                }
                catch(Exception ex)
                {
                    fetchProfileCounter++;
                    if (fetchProfileCounter <= 5)
                    {
                        fetchUserProfile();
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
        ib_back_arrow = (ImageButton)findViewById(R.id.user_profile_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent user_profile_back_arrow_intent = new Intent(getBaseContext(), ProfileDashboard.class);
                        startActivity(user_profile_back_arrow_intent);
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
        getMenuInflater().inflate(R.menu.user_profile, menu);
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
