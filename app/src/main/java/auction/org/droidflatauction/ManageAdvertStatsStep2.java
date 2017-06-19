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

public class ManageAdvertStatsStep2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    ListView statsPropertyListView;
    ArrayList<String> property_date_list,property_click_list,property_impression_list,property_ctr_list,property_cost_list;
    ManageAdvertStatsAdapter statsPropertyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_advert_stats_step2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onClickButtonBackArrowListener();

        statsPropertyListView = (ListView) findViewById(R.id.stats_listview);
        property_date_list = new ArrayList<>();
        property_date_list = getPropertyDateList();
        property_click_list = new ArrayList<>();
        property_click_list = getPropertyClickList();
        property_impression_list = new ArrayList<>();
        property_impression_list = getPropertyImpressionList();
        property_ctr_list = new ArrayList<>();
        property_ctr_list = getPropertyCtrList();
        property_cost_list = new ArrayList<>();
        property_cost_list = getPropertyCostList();
        statsPropertyAdapter = new ManageAdvertStatsAdapter(ManageAdvertStatsStep2.this,property_date_list,property_click_list,property_impression_list,property_ctr_list,property_cost_list);

        statsPropertyListView.setAdapter(statsPropertyAdapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.stat_advert_step2_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent stat_advert_back_arrow_intent = new Intent(getBaseContext(), ManageAdvertStatsStep1.class);
                        startActivity(stat_advert_back_arrow_intent);
                    }
                }
        );
    }
    public ArrayList<String> getPropertyDateList(){
        property_date_list = new ArrayList<>();
        property_date_list.add("17 April 2017 08:45AM");
        property_date_list.add("18 April 2017 07:45AM");
        property_date_list.add("19 April 2017 06:45AM");
        property_date_list.add("20 April 2017 05:45AM");
        property_date_list.add("21 April 2017 04:45AM");
        return property_date_list;
    }
    public ArrayList<String> getPropertyClickList(){
        property_click_list = new ArrayList<>();
        property_click_list.add("100");
        property_click_list.add("200");
        property_click_list.add("300");
        property_click_list.add("400");
        property_click_list.add("500");
        return property_click_list;
    }
    public ArrayList<String> getPropertyImpressionList(){
        property_click_list = new ArrayList<>();
        property_click_list.add("Bad");
        property_click_list.add("Good");
        property_click_list.add("Very Good");
        property_click_list.add("Excellent");
        property_click_list.add("Awesome");
        return property_click_list;
    }
    public ArrayList<String> getPropertyCtrList(){
        property_click_list = new ArrayList<>();
        property_click_list.add("Ctr 1");
        property_click_list.add("Ctr 2");
        property_click_list.add("Ctr 3");
        property_click_list.add("Ctr 4");
        property_click_list.add("Ctr 5");
        return property_click_list;
    }
    public ArrayList<String> getPropertyCostList(){
        property_click_list = new ArrayList<>();
        property_click_list.add("$100");
        property_click_list.add("$200");
        property_click_list.add("$300");
        property_click_list.add("$400");
        property_click_list.add("$500");
        return property_click_list;
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
        getMenuInflater().inflate(R.menu.manage_advert_stats_step2, menu);
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
            Intent member_logout_intent = new Intent(getBaseContext(), SignIn.class);
            startActivity(member_logout_intent);
        } else if (id == R.id.nav_email) {

        } else if (id == R.id.nav_phone) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
