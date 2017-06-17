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

public class MessageShow extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    ListView messageListView;
    ArrayList<Integer> user_iamges;
    ArrayList<String> user_list,message_time_list,message_text_list;
    MessageShowAdapter messageInboxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onClickButtonBackArrowListener();

        messageListView = (ListView) findViewById(R.id.message_show_listview);
        user_iamges = new ArrayList<>();
        user_iamges = getUserIamges();
        user_list = getMessageSenderList();
        message_time_list = getMessageTimeList();
        message_text_list = getMessageTextList();
        messageInboxAdapter = new MessageShowAdapter(MessageShow.this,user_iamges,user_list,message_time_list,message_text_list);

        messageListView.setAdapter(messageInboxAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.message_show_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent message_show_back_arrow_intent = new Intent(getBaseContext(), MessageInbox.class);
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
        } else if (id == R.id.nav_email) {

        } else if (id == R.id.nav_phone) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}