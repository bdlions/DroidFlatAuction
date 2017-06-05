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
import android.widget.Button;

public class ManageAdvertDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static Button create_ad_btn, my_ad_btn, saved_ad_btn, ad_account_settings_btn,
                            individual_ad_bids_btn,ad_stats_btn,ad_ranking,ad_faq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_advert_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onClickButtonCreateAdListener();
        onClickButtonMyAdListener();
        onClickButtonSavedAdListener();
        onClickButtonAccountSettingsAdListener();
        onClickButtonIndividualAdBidsListener();
        onClickButtonStatsAdListener();
        onClickButtonRankingAdListener();
        onClickButtonFaqAdListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onClickButtonCreateAdListener(){
        create_ad_btn = (Button) findViewById(R.id.create_advert);
        create_ad_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent create_advert_intent = new Intent(getBaseContext(), CreateAdvertStep1.class);
                        startActivity(create_advert_intent);
                    }
                }
        );
    }
    public void onClickButtonMyAdListener(){
        my_ad_btn = (Button) findViewById(R.id.my_advert);
        my_ad_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent my_advert_intent = new Intent(getBaseContext(), MyAdvertStep1.class);
                        startActivity(my_advert_intent);
                    }
                }
        );
    }
    public void onClickButtonSavedAdListener(){
        saved_ad_btn = (Button) findViewById(R.id.saved_advert);
        saved_ad_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent saved_advert_intent = new Intent(getBaseContext(), SavedAdvertStep1.class);
                        startActivity(saved_advert_intent);
                    }
                }
        );
    }
    public void onClickButtonAccountSettingsAdListener(){
        ad_account_settings_btn = (Button) findViewById(R.id.account_settings_advert);
        ad_account_settings_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent account_settings_advert_intent = new Intent(getBaseContext(), ManageAdvertAccountSettingsStep1.class);
                        startActivity(account_settings_advert_intent);
                    }
                }
        );
    }
    public void onClickButtonIndividualAdBidsListener(){
        individual_ad_bids_btn = (Button) findViewById(R.id.individual_ad_bid);
        individual_ad_bids_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent individual_ad_bid_intent = new Intent(getBaseContext(), ManageAdvertIndividualAdBidsStep1.class);
                        startActivity(individual_ad_bid_intent);
                    }
                }
        );
    }
    public void onClickButtonStatsAdListener(){
        ad_stats_btn = (Button) findViewById(R.id.stats_advert);
        ad_stats_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent stats_advert_intent = new Intent(getBaseContext(), ManageAdvertStats.class);
                        startActivity(stats_advert_intent);
                    }
                }
        );
    }
    public void onClickButtonRankingAdListener(){
        ad_ranking = (Button) findViewById(R.id.ranking_advert);
        ad_ranking.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ranking_advert_intent = new Intent(getBaseContext(), ManageAdvertRanking.class);
                        startActivity(ranking_advert_intent);
                    }
                }
        );
    }
    public void onClickButtonFaqAdListener(){
        ad_faq = (Button) findViewById(R.id.faq_advert);
        ad_faq.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent faq_advert_intent = new Intent(getBaseContext(), ManageAdvertFAQ.class);
                        startActivity(faq_advert_intent);
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
        getMenuInflater().inflate(R.menu.manage_advert_dashboard, menu);
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
        } else if (id == R.id.nav_email) {

        } else if (id == R.id.nav_phone) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}