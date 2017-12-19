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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bdlions.dto.AccountSettingFA;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

public class ManageAdvertAccountSettingsStep extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private  static Button btn_submit;
    public EditText etSettingDefaultBidPerClick, etSettingDailyBudget;
    public AccountSettingFA accountSettingFA;
    SessionManager session;
    NavigationManager navigationManager;

    public Dialog progressBarDialog;
    public int fetchAccountSettingFACounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_advert_account_settings_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        accountSettingFA = new AccountSettingFA();

        etSettingDefaultBidPerClick = (EditText) findViewById(R.id.et_setting_default_bid_per_click);
        etSettingDailyBudget = (EditText) findViewById(R.id.et_setting_daily_budget);

        progressBarDialog = new Dialog(ManageAdvertAccountSettingsStep.this);
        progressBarDialog.setContentView(R.layout.progressbar);
        progressBarDialog.show();
        fetchAccountSettingFA();

        onClickButtonBackArrowListener();
        onClickButtonSubmitListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void fetchAccountSettingFA()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_ACCOUNT_SETTING_FA);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    String resultString = (String)msg.obj;
                    Gson gson = new Gson();
                    accountSettingFA = gson.fromJson(resultString, AccountSettingFA.class);
                    if(accountSettingFA.isSuccess())
                    {
                        //converting pound into p
                        etSettingDefaultBidPerClick.setText(accountSettingFA.getDefaultBidPerClick()*100 + "");
                        etSettingDailyBudget.setText(accountSettingFA.getDailyBudget()+"");
                    }
                    else
                    {
                        //show error message in toast
                    }
                    progressBarDialog.dismiss();
                }
                catch(Exception ex)
                {
                    fetchAccountSettingFACounter++;
                    if (fetchAccountSettingFACounter <= 5)
                    {
                        fetchAccountSettingFA();
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
        ib_back_arrow = (ImageButton) findViewById(R.id.account_settings_advert_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent account_settings_advert_back_arrow_intent = new Intent(getBaseContext(), ManageAdvertDashboard.class);
                        startActivity(account_settings_advert_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonSubmitListener(){
        btn_submit = (Button) findViewById(R.id.account_settings_advert_submit_button);
        btn_submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBarDialog = new Dialog(ManageAdvertAccountSettingsStep.this);
                        progressBarDialog.setContentView(R.layout.progressbar);
                        progressBarDialog.show();
                        //set amount here
                        try
                        {
                            accountSettingFA.setDailyBudget(Double.parseDouble(etSettingDailyBudget.getText().toString()));
                            accountSettingFA.setDefaultBidPerClick(Double.parseDouble(etSettingDefaultBidPerClick.getText().toString()));
                            //converting p into pound
                            accountSettingFA.setDefaultBidPerClick(accountSettingFA.getDefaultBidPerClick()/100);
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(getBaseContext(),   "Please assign correct number.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String sessionId = session.getSessionId();
                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        packetHeader.setAction(ACTION.SAVE_ACCOUNT_SETTING_FA);
                        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
                        packetHeader.setSessionId(sessionId);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String accountSettingFAString = gson.toJson(accountSettingFA);
                        new BackgroundWork().execute(packetHeader, accountSettingFAString, new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                progressBarDialog.dismiss();
                                try
                                {
                                    String resultString = (String)msg.obj;
                                    Gson gson = new Gson();
                                    accountSettingFA = gson.fromJson(resultString, AccountSettingFA.class);
                                    if(accountSettingFA.isSuccess())
                                    {
                                        Toast.makeText(getBaseContext(),   "Updated successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getBaseContext(),   accountSettingFA.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch(Exception ex)
                                {
                                    System.out.println(ex.toString());
                                    Toast.makeText(getBaseContext(),   "Unable to save. Please try again.", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.manage_advert_account_settings_step, menu);
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
       //     return true;
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
