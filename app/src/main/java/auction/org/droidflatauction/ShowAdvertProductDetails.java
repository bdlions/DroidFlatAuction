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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auction.dto.Amenity;
import com.auction.dto.Availability;
import com.auction.dto.Product;
import com.auction.dto.Role;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowAdvertProductDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private static TextView tvProductDetailsTotalBids, tvProductDetailsTitle, tvProductDetailsPrice, tvProductDetailsDescription, tvProductDetailsBidTimeLeft, tvProductDetailsAvailableFrom, tvProductDetailsAvailableTo, tvProductDetailsAvailability, tvProductDetailsMinTerm, tvProductDetailsMaxTerm, tvProductDetailsOccupation, tvProductDetailsPets, tvProductDetailsSmoking, tvProductDetailsAmenityParking, tvProductDetailsAmenityBalcony, tvProductDetailsAmenityGarden, tvProductDetailsAmenityDisabledAccess, tvProductDetailsAmenityGarage,tvProductBusinessName,tvProductAddress,tvProductCompanyName;
    private  static Button proppertyContentEditBtn, proppertyPlaceBidBtn, proppertyContactBtn;
    private static ImageView ivProductDetailsImage,ivProductAgentLogo;
    private static LinearLayout llProductAgent,llProductCompanyName;
    private Product product;
    private String productString;
    SessionManager session;
    public static RelativeLayout myAdvertBtnRow,savedAdvertBtnRow;
    public int adIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_advert_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        tvProductDetailsTitle = (TextView) findViewById(R.id.tv_product_details_title);
        tvProductDetailsPrice = (TextView) findViewById(R.id.tv_product_details_price);
        tvProductDetailsDescription = (TextView) findViewById(R.id.tv_product_details_description);
        ivProductDetailsImage = (ImageView) findViewById(R.id.iv_product_details_image);

        llProductAgent = (LinearLayout)findViewById(R.id.ll_product_agent);
        llProductCompanyName = (LinearLayout)findViewById(R.id.ll_product_company_name);

        ivProductAgentLogo = (ImageView) findViewById(R.id.iv_product_agent_logo);
        tvProductBusinessName = (TextView) findViewById(R.id.tv_product_business_name);
        tvProductAddress = (TextView) findViewById(R.id.tv_product_address);
        tvProductCompanyName = (TextView) findViewById(R.id.tv_product_company_name);

        tvProductDetailsTotalBids = (TextView) findViewById(R.id.tv_product_details_total_bids);
        tvProductDetailsBidTimeLeft = (TextView) findViewById(R.id.tv_product_details_bid_time_left);
        tvProductDetailsAvailableFrom = (TextView) findViewById(R.id.tv_product_details_available_from);
        tvProductDetailsAvailableTo = (TextView) findViewById(R.id.tv_product_details_available_to);
        tvProductDetailsAvailability = (TextView) findViewById(R.id.tv_product_details_availability);
        tvProductDetailsMinTerm = (TextView) findViewById(R.id.tv_product_details_min_term);
        tvProductDetailsMaxTerm = (TextView) findViewById(R.id.tv_product_details_max_term);

        tvProductDetailsAmenityParking = (TextView) findViewById(R.id.tv_product_details_amenity_parking);
        tvProductDetailsAmenityBalcony = (TextView) findViewById(R.id.tv_product_details_amenity_balcony);
        tvProductDetailsAmenityGarden = (TextView) findViewById(R.id.tv_product_details_amenity_garden);
        tvProductDetailsAmenityDisabledAccess = (TextView) findViewById(R.id.tv_product_details_amenity_disabled_access);
        tvProductDetailsAmenityGarage = (TextView) findViewById(R.id.tv_product_details_amenity_garage);

        tvProductDetailsSmoking = (TextView) findViewById(R.id.tv_product_details_smoking);
        tvProductDetailsPets = (TextView) findViewById(R.id.tv_product_details_pets);
        tvProductDetailsOccupation = (TextView) findViewById(R.id.tv_product_details_occupation);

        try
        {
            adIdentity = getIntent().getExtras().getInt("adIdentity");
            productString = getIntent().getExtras().getString("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, Product.class);

            boolean isAgent = false;
            List<Role> roleList = product.getUser().getRoleList();
            if(roleList != null && roleList.size() > 0)
            {
                for(int counter = 0; counter < roleList.size(); counter++)
                {
                    Role role = roleList.get(counter);
                    if(role.getId() == Constants.USER_TYPE_ID_AGENT)
                    {
                        isAgent = true;
                    }
                }
            }

            //isAgent = true;
            if(isAgent)
            {
                //show agent logo, business name and address
                //Toast.makeText(ShowAdvertProductDetails.this, "Agent: " + isAgent,Toast.LENGTH_LONG).show();
                Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+product.getUser().getAgentLogo()).into(ivProductAgentLogo);
                tvProductBusinessName.setText(product.getUser().getBusinessName());
                tvProductAddress.setText(product.getUser().getAddress());
                llProductAgent.setVisibility(View.VISIBLE);
                llProductCompanyName.setVisibility(View.GONE);
            } else
            {
                //show company name
                //Toast.makeText(ShowAdvertProductDetails.this, "NonAgent: " + isAgent,Toast.LENGTH_LONG).show();
                tvProductCompanyName.setText(product.getCompanyName());
                llProductAgent.setVisibility(View.GONE);
                llProductCompanyName.setVisibility(View.VISIBLE);
            }


            //formatting date to user display format
            String availableFrom = product.getAvailableFrom();
            String availableTo = product.getAvailableTo();
            if(availableFrom != null && !availableFrom.equals(""))
            {
                String[] availableFromArray = availableFrom.split("-");
                product.setAvailableFrom(availableFromArray[2]+"-"+availableFromArray[1]+"-"+availableFromArray[0]);
            }
            if(availableTo != null && !availableTo.equals(""))
            {
                String[] availableToArray = availableTo.split("-");
                product.setAvailableTo(availableToArray[2]+"-"+availableToArray[1]+"-"+availableToArray[0]);
            }

            //product = (Product)getIntent().getExtras().get("productInfo");
            tvProductDetailsTitle.setText(product.getTitle());
            tvProductDetailsPrice.setText(product.getBasePrice()+" £");
            tvProductDetailsDescription.setText(product.getDescription());
            Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.productImagePath_328_212+product.getImg()).into(ivProductDetailsImage);
            tvProductDetailsTotalBids.setText(product.getTotalBids()+"");
            //tvProductDetailsBidTimeLeft.setText("7 Hours 24 Mins 12 Seconds");
            tvProductDetailsAvailableFrom.setText(product.getAvailableFrom());
            tvProductDetailsAvailableTo.setText(product.getAvailableTo());
            String availabilityString = "";
            List<Availability> availabilityList = product.getAvailabilities();
            if(availabilityList != null && availabilityList.size() > 0)
            {
                for(int counter = 0; counter < availabilityList.size(); counter++)
                {
                    if(counter == 0)
                    {
                        availabilityString = availabilityList.get(counter).getTitle();
                    }
                    else
                    {
                        availabilityString = availabilityString + ", " +availabilityList.get(counter).getTitle();
                    }
                }
            }
            tvProductDetailsAvailability.setText(availabilityString);
            tvProductDetailsMinTerm.setText(product.getMinStay().getTitle());
            tvProductDetailsMaxTerm.setText(product.getMaxStay().getTitle());
            tvProductDetailsSmoking.setText(product.getSmoking().getTitle());

            List<Amenity> amenityList = product.getAmenities();
            if(amenityList != null && amenityList.size() > 0)
            {
                for(int counter = 0; counter < amenityList.size(); counter++)
                {
                    Amenity amenity = amenityList.get(counter);
                    if(amenity.getId()  == 1)
                    {
                        tvProductDetailsAmenityParking.setText("Yes");
                    }
                    if(amenity.getId()  == 2)
                    {
                        tvProductDetailsAmenityBalcony.setText("Yes");
                    }
                    if(amenity.getId()  == 3)
                    {
                        tvProductDetailsAmenityGarden.setText("Yes");
                    }
                    if(amenity.getId()  == 4)
                    {
                        tvProductDetailsAmenityDisabledAccess.setText("Yes");
                    }
                    if(amenity.getId()  == 5)
                    {
                        tvProductDetailsAmenityGarage.setText("Yes");
                    }
                }
            }

            tvProductDetailsPets.setText(product.getPet().getTitle());
            tvProductDetailsOccupation.setText(product.getOccupation().getTitle());
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

        myAdvertBtnRow = (RelativeLayout)findViewById(R.id.my_advert_button_row);
        savedAdvertBtnRow = (RelativeLayout)findViewById(R.id.saved_advert_button_row);
        if(adIdentity == Constants.MY_AD_IDENTITY){
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
        executeTimer();
    }

    public void executeTimer()
    {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                long tempTime = product.getTime();
                                String timeLeft = "";
                                if (tempTime > 0)
                                {
                                    if (tempTime >= 86400)
                                    {
                                        timeLeft = timeLeft + (int)Math.floor(tempTime/86400) + " days ";
                                        tempTime = tempTime % 86400;
                                    }
                                    if (tempTime >= 3600)
                                    {
                                        timeLeft = timeLeft + (int)(int)Math.floor(tempTime/3600) + " hours ";
                                        tempTime = tempTime % 3600;
                                    }
                                    if (tempTime >= 60)
                                    {
                                        timeLeft = timeLeft + (int)Math.floor(tempTime/60) + " mins ";
                                        tempTime = tempTime % 60;
                                    }
                                    if (tempTime < 60)
                                    {
                                        timeLeft = timeLeft + tempTime + " secs ";
                                    }
                                    product.setTime(product.getTime() - 1);
                                    tvProductDetailsBidTimeLeft.setText(timeLeft);
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        /*try {
            while(true) {
                long tempTime = product.getTime();
                String timeLeft = "";
                if (tempTime > 0)
                {
                    if (tempTime >= 86400)
                    {
                        timeLeft = timeLeft + Math.floor(tempTime/86400) + " days ";
                        tempTime = tempTime % 86400;
                    }
                    if (tempTime >= 3600)
                    {
                        timeLeft = timeLeft + Math.floor(tempTime/3600) + " hours ";
                        tempTime = tempTime % 3600;
                    }
                    if (tempTime >= 60)
                    {
                        timeLeft = timeLeft + Math.floor(tempTime/60) + " mins ";
                        tempTime = tempTime % 60;
                    }
                    if (tempTime < 60)
                    {
                        timeLeft = timeLeft + tempTime + " secs ";
                    }
                    product.setTime(product.getTime() - 1);
                    tvProductDetailsBidTimeLeft.setText(timeLeft);
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

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
        tvProductDetailsTotalBids.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent property_total_bids_intent = new Intent(getBaseContext(), PropertyBidList.class);
                        property_total_bids_intent.putExtra("productId", product.getId());
                        property_total_bids_intent.putExtra("adIdentity", adIdentity);
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
                        my_advert_prpperty_edit_button_intent.putExtra("adIdentity", adIdentity);
                        my_advert_prpperty_edit_button_intent.putExtra("adCreateIdentity", Constants.MY_AD_EDIT_IDENTITY);
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
                Intent intentPlaceProductBid = new Intent(getBaseContext(), PropertyPlaceBid.class);
                intentPlaceProductBid.putExtra("productString", productString);
                intentPlaceProductBid.putExtra("adIdentity", adIdentity);
                startActivity(intentPlaceProductBid);
            }
        });
    }
    public void onClickPropertyContactButtonListener(){
        proppertyContactBtn = (Button)findViewById(R.id.show_advert_product_details_contact_button);
        proppertyContactBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentContactProperty = new Intent(getBaseContext(), ContactThoughMessage.class);
                        intentContactProperty.putExtra("productString", productString);
                        intentContactProperty.putExtra("adIdentity", adIdentity);
                        startActivity(intentContactProperty);
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
                        if(adIdentity == Constants.MY_AD_IDENTITY){
                            Intent show_advert_product_details_intent = new Intent(getBaseContext(), MyAdvertStep1.class);
                            startActivity(show_advert_product_details_intent);
                        }
                        else {
                            Intent show_advert_product_details_intent = new Intent(getBaseContext(), SavedAdvertStep1.class);
                            startActivity(show_advert_product_details_intent);
                        }

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
