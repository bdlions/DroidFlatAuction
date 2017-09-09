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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auction.dto.Product;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ShowAdvertProductDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private static TextView tv_view_product_title, tv_view_product_price, tv_view_product_description, tv_property_total_bids;
    private  static Button proppertyContentEditBtn, proppertyPlaceBidBtn, proppertyContactBtn;
    private static ImageView ivProductDetails;
    private Product product;
    private String productString;
    SessionManager session;
    public static RelativeLayout myAdvertBtnRow,savedAdvertBtnRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_advert_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        tv_view_product_title = (TextView) findViewById(R.id.view_product_title);
        tv_view_product_price = (TextView) findViewById(R.id.view_product_price);
        tv_view_product_description = (TextView) findViewById(R.id.view_product_description);
        ivProductDetails = (ImageView) findViewById(R.id.img_product_details);

        try
        {
            productString = getIntent().getExtras().getString("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, Product.class);
            //product = (Product)getIntent().getExtras().get("productInfo");
            tv_view_product_title.setText(product.getTitle());
            tv_view_product_price.setText(product.getBasePrice()+" £");
            tv_view_product_description.setText(product.getDescription());
            Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.productImagePath_328_212+product.getImg()).into(ivProductDetails);
        }
        catch(Exception ex)
        {
            //handle exception
        }

        onClickButtonBackArrowListener();
        onClickShowTotalBiddersListener();
        onClickButtonEditListener();
        onClickPropertyPlaceBidButtonListener();
        onClickPropertyContactButtonListener();

        Intent myAdvertButtonDisplayIntent = getIntent();
        int myAdvertButtonDisplayIntegerValue = myAdvertButtonDisplayIntent.getIntExtra("myAdvertButtonDisplayInteger", 0);
        myAdvertBtnRow = (RelativeLayout)findViewById(R.id.my_advert_button_row);
        savedAdvertBtnRow = (RelativeLayout)findViewById(R.id.saved_advert_button_row);
        if(myAdvertButtonDisplayIntegerValue == 1){
            myAdvertBtnRow.setVisibility(View.VISIBLE);
            savedAdvertBtnRow.setVisibility(View.GONE);
        }
        else {
            myAdvertBtnRow.setVisibility(View.GONE);
            savedAdvertBtnRow.setVisibility(View.VISIBLE);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng bangladesh = new LatLng(23.6850, 90.3563);
        LatLng dhaka = new LatLng(23.8103, 90.4125);
        LatLng chittagong = new LatLng(22.3475, 91.8123);
        googleMap.addMarker(new MarkerOptions().position(dhaka)
                .title("Marker in dhaka"));
        googleMap.addMarker(new MarkerOptions().position(chittagong)
                .title("Marker in chittagong"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bangladesh));
    }

    public void onClickShowTotalBiddersListener(){
        tv_property_total_bids = (TextView) findViewById(R.id.property_total_bids);
        tv_property_total_bids.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent property_total_bids_intent = new Intent(getBaseContext(), PropertyBidList.class);
                        property_total_bids_intent.putExtra("productId", product.getId());
                        startActivity(property_total_bids_intent);
                    }
                }
        );
    }
    public void onClickButtonEditListener(){
        proppertyContentEditBtn = (Button)findViewById(R.id.show_advert_product_details_edit_button);
        proppertyContentEditBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent my_advert_prpperty_edit_button_intent = new Intent(getBaseContext(), CreateAdvertStep1.class);
                        my_advert_prpperty_edit_button_intent.putExtra("productString", productString);
                        startActivity(my_advert_prpperty_edit_button_intent);
                    }
                }
        );
    }
    public void onClickPropertyPlaceBidButtonListener(){
        proppertyPlaceBidBtn = (Button)findViewById(R.id.show_advert_product_details_place_bid_button);
        proppertyPlaceBidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent propperty_place_bid_button_intent = new Intent(getBaseContext(), PropertyPlaceBid.class);
                propperty_place_bid_button_intent.putExtra("productId", product.getId());
                startActivity(propperty_place_bid_button_intent);
            }
        });
    }
    public void onClickPropertyContactButtonListener(){
        proppertyContactBtn = (Button)findViewById(R.id.show_advert_product_details_contact_button);
        proppertyContactBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent propperty_contact_button_intent = new Intent(getBaseContext(), ContactThoughMessage.class);
                        startActivity(propperty_contact_button_intent);
                    }
                }
        );
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.show_advert_product_details_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent my_advert_step2_back_arrow_intent = new Intent(getBaseContext(), MyAdvertStep1.class);
                        startActivity(my_advert_step2_back_arrow_intent);
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
        getMenuInflater().inflate(R.menu.show_advert_product_details, menu);
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
