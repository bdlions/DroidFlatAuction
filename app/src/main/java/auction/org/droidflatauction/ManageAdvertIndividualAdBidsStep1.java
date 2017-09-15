package auction.org.droidflatauction;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ManageAdvertIndividualAdBidsStep1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    ListView individualAdBidsPropertyListView;
    ArrayList<String> property_title_list,property_postcode_list;
    ManageAdvertIndividualAdBidsStep1ProductAdapter individualAdBidsPropertyAdapter;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_advert_individual_ad_bids_step1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        onClickButtonBackArrowListener();

        property_title_list = new ArrayList<>();
        property_postcode_list = new ArrayList<>();


        try
        {
            property_title_list = (ArrayList<String>)getIntent().getExtras().get("titleList");
            property_postcode_list = (ArrayList<String>)getIntent().getExtras().get("postCodeList");
        }
        catch(Exception ex)
        {

        }

        individualAdBidsPropertyListView = (ListView) findViewById(R.id.ad_bids_listview);
        //property_title_list = getPropertyTitileList();
        //property_postcode_list = getPropertyPostCodeList();
        individualAdBidsPropertyAdapter = new ManageAdvertIndividualAdBidsStep1ProductAdapter(ManageAdvertIndividualAdBidsStep1.this,property_title_list,property_postcode_list);

        individualAdBidsPropertyListView.setAdapter(individualAdBidsPropertyAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.individual_ad_bids_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent individual_ad_bids_back_arrow_intent = new Intent(getBaseContext(), ManageAdvertDashboard.class);
                        startActivity(individual_ad_bids_back_arrow_intent);
                    }
                }
        );
    }

    public ArrayList<String> getPropertyTitileList(){
        property_title_list = new ArrayList<>();
        property_title_list.add("23 PLANET ST, london EC4N 6AJ, UK");
        property_title_list.add("10 Trinity Square, OXFORD EC3N 4AJ, UK");
        property_title_list.add("23 PLANET ST, london EC4N 6AJ, UK");
        property_title_list.add("100 Trinity Square, OXFORD EC3N 4AJ, UK");
        property_title_list.add("223 PLANET ST, london EC4N 6AJ, UK");
        return property_title_list;
    }
    public ArrayList<String> getPropertyPostCodeList(){
        property_postcode_list = new ArrayList<>();
        property_postcode_list.add("LU4 0HL");
        property_postcode_list.add("LU4 0HL");
        property_postcode_list.add("LU4 0HL");
        property_postcode_list.add("LU4 0HL");
        property_postcode_list.add("LU4 0HL");
        return property_postcode_list;
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
        getMenuInflater().inflate(R.menu.manage_advert_individual_ad_bids_step1, menu);
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
         //   return true;
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
