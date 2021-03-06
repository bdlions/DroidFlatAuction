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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import org.auction.udp.BackgroundWork;
import org.bdlions.auction.dto.DTOUser;
import org.bdlions.auction.entity.EntityRole;
import org.json.JSONObject;

import java.util.List;

public class UserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    SessionManager session;
    public static TextView tvProfileFullName, tvProfileEmail, tvProfileTelephone,tvProfileBusinessName,tvProfileBusinessNameHeader,tvProfileAddress,tvProfileAddressHeader,tvProfileRole;
    private static ImageView ivProfilePhoto, ivProfileAgentLogo,ivProfileDocument, ivUserProfileVerificationYes, ivUserProfileVerificationNo;
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
        ivProfileDocument = (ImageView) findViewById(R.id.iv_profile_document);

        ivUserProfileVerificationYes = (ImageView) findViewById(R.id.iv_user_profile_verification_yes);
        ivUserProfileVerificationNo = (ImageView) findViewById(R.id.iv_user_profile_verification_no);

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
                    DTOUser user = null;
                    ClientResponse clientResponse = null;
                    String clientResponseString = null;
                    Gson gson = new Gson();
                    if(msg != null  && msg.obj != null)
                    {
                        clientResponseString = (String) msg.obj;
                    }
                    if(clientResponseString != null)
                    {
                        clientResponse = gson.fromJson(clientResponseString, ClientResponse.class);
                    }
                    if(clientResponse != null && clientResponse.isSuccess())
                    {
                        progressBarDialog.dismiss();
                        try
                        {
                            JSONObject obj = new JSONObject(clientResponseString);
                            user = gson.fromJson(obj.get("result").toString(), DTOUser.class);
                            if(user == null || user.getEntityUser() == null || user.getEntityUser().getId() == 0)
                            {
                                return;
                            }
                        }
                        catch(Exception ex)
                        {
                            return;
                        }
                        String roleString = "";
                        String agentString = "";
                        List<EntityRole> roleList = user.getRoles();
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

                        tvProfileBusinessName.setText(user.getEntityUser().getBusinessName());
                        tvProfileAddress.setText(user.getEntityUser().getAddress());

                        tvProfileFullName.setText(user.getEntityUser().getFirstName()+" "+user.getEntityUser().getLastName());
                        tvProfileEmail.setText(user.getEntityUser().getEmail());
                        tvProfileTelephone.setText(user.getEntityUser().getCell());
                        tvProfileRole.setText(roleString);

                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getEntityUser().getImg()).into(ivProfilePhoto);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getEntityUser().getAgentLogo()).into(ivProfileAgentLogo);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profileDocument+user.getEntityUser().getDocument()).into(ivProfileDocument);

                        if(user.getEntityUser().isIsVerified())
                        {
                            ivUserProfileVerificationYes.setVisibility(View.VISIBLE);
                            ivUserProfileVerificationNo.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            ivUserProfileVerificationYes.setVisibility(View.INVISIBLE);
                            ivUserProfileVerificationNo.setVisibility(View.VISIBLE);
                        }
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
