
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TableRow.LayoutParams;

public class PropertyBidList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String bidder[] = {
            "Nazmul hasan","Alamgir Kabir",
            "Nazmul hasan","Alamgir Kabir",
            "Nazmul hasan","Alamgir Kabir",
            "Nazmul hasan","Alamgir Kabir",
            "Nazmul hasan","Alamgir Kabir"};
    String bid_amount[]       =  {
            "$10","$20",
            "$30","$40",
            "$50","$60",
            "$70","$80",
            "$90","$100",
    };
    String bid_time[]       =  {
            "21 Apr 2017 9:38:35AM","22 Apr 2017 9:38:35AM",
            "23 Apr 2017 9:38:35AM","24 Apr 2017 9:38:35AM",
            "25 Apr 2017 9:38:35AM","26 Apr 2017 9:38:35AM",
            "27 Apr 2017 9:38:35AM","28 Apr 2017 9:38:35AM",
            "29 Apr 2017 9:38:35AM","30 Apr 2017 9:38:35AM",
    };

    TableLayout tl;
    TableRow tr;
    TextView bidder_name,price_list, time_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_bid_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tl = (TableLayout) findViewById(R.id.bidder_list);
        addHeaders();
        addData();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    /** This function add the headers to the table **/
    public void addHeaders(){

        /** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView bidder = new TextView(this);
        bidder.setText("Bidder");
        bidder.setTextColor(Color.parseColor("#f06226"));
        bidder.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        bidder.setPadding(5, 5, 5, 0);
        tr.addView(bidder);  // Adding textView to tablerow.


        /** Creating another textview **/
        TextView amount = new TextView(this);
        amount.setText("Bid Amount");
        amount.setTextColor(Color.parseColor("#f06226"));
        amount.setPadding(5, 5, 5, 0);
        amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(amount); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView time = new TextView(this);
        time.setText("Bid Time");
        time.setTextColor(Color.parseColor("#f06226"));
        time.setPadding(5, 5, 5, 0);
        time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(time); // Adding textView to tablerow.

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        /** Creating another textview **/
        TextView divider = new TextView(this);
        divider.setText("-----------------");
        divider.setTextColor(Color.parseColor("#5a5a5a"));
        divider.setPadding(5, 0, 0, 0);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.

        TextView divider2 = new TextView(this);
        divider2.setText("---------------");
        divider2.setTextColor(Color.parseColor("#5a5a5a"));
        divider2.setPadding(5, 0, 0, 0);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.

        TextView divider3 = new TextView(this);
        divider3.setText("-----------------------------");
        divider3.setTextColor(Color.parseColor("#5a5a5a"));
        divider3.setPadding(5, 0, 0, 0);
        divider3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider3); // Adding textView to tablerow.
        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    /** This function add the data to the table **/
    public void addData() {

        for (int i = 0; i < bidder.length; i++) {
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            bidder_name = new TextView(this);
            bidder_name.setText(bidder[i]);
            bidder_name.setTextColor(Color.parseColor("#5a5a5a"));
            bidder_name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            bidder_name.setPadding(5, 5, 5, 5);
            tr.addView(bidder_name);  // Adding textView to tablerow.

            /** Creating another textview **/
            price_list = new TextView(this);
            price_list.setText(bid_amount[i]);
            price_list.setTextColor(Color.parseColor("#5a5a5a"));
            price_list.setPadding(5, 5, 5, 5);
            price_list.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(price_list); // Adding textView to tablerow.

            /** Creating another textview **/
            time_list = new TextView(this);
            time_list.setText(bid_time[i]);
            time_list.setTextColor(Color.parseColor("#5a5a5a"));
            time_list.setPadding(5, 5, 5, 5);
            time_list.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(time_list); // Adding textView to tablerow.

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
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
        getMenuInflater().inflate(R.menu.property_bid_list, menu);
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
        } else if (id == R.id.nav_email) {

        } else if (id == R.id.nav_phone) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}