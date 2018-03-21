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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.SignInResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import org.auction.udp.BackgroundWork;
import org.json.JSONObject;

import java.util.List;

public class ShowAdvertProductDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private static TextView tvProductDetailsTotalBids, tvProductDetailsTitle, tvProductDetailsPrice, tvProductDetailsDescription, tvProductDetailsBidTimeLeft, tvProductDetailsAvailableFrom, tvProductDetailsAvailableTo, tvProductDetailsAvailability, tvProductDetailsMinTerm, tvProductDetailsMaxTerm, tvProductDetailsOccupation, tvProductDetailsPets, tvProductDetailsSmoking, tvProductDetailsAmenityParking, tvProductDetailsAmenityBalcony, tvProductDetailsAmenityGarden, tvProductDetailsAmenityDisabledAccess, tvProductDetailsAmenityGarage,tvProductBusinessName, tvProductAddress,tvProductCompanyName;
    private  static Button proppertyContentEditBtn, proppertyPlaceBidBtn, proppertyContactBtn;
    private static ImageView ivProductDetailsImage,ivProductAgentLogo,ivProductSave;
    private static LinearLayout llProductAgent,llProductCompanyName;
    private EntityProduct product;
    DTOProduct dtoProduct;
    private String productString;
    SessionManager session;
    public static RelativeLayout myAdvertBtnRow,savedAdvertBtnRow;
    public int adIdentity;
    public Dialog progressBarDialog;
    public int fetchProfileCounter = 0;

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
        ivProductSave = (ImageView) findViewById(R.id.iv_product_details_save_product);

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
            progressBarDialog = new Dialog(ShowAdvertProductDetails.this);
            progressBarDialog.setContentView(R.layout.progressbar);

            adIdentity = getIntent().getExtras().getInt("adIdentity");
            productString = getIntent().getExtras().getString("productString");
            Gson gson = new Gson();
            dtoProduct = gson.fromJson(productString, DTOProduct.class);
            if(dtoProduct != null && dtoProduct.getEntityProduct() != null)
            {
                product = dtoProduct.getEntityProduct();
            }
            else
            {
                return;
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
            tvProductDetailsPrice.setText("£" + String.format("%.2f",  product.getBasePrice()) + " Guide Price");
            tvProductDetailsDescription.setText(product.getDescription());
            Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.productImagePath_328_212+product.getImg()).into(ivProductDetailsImage);
            tvProductDetailsTotalBids.setText(product.getTotalBids()+"");
            //tvProductDetailsBidTimeLeft.setText("7 Hours 24 Mins 12 Seconds");
            tvProductDetailsAvailableFrom.setText(product.getAvailableFrom());
            if(product.isOnGoing())
            {
                tvProductDetailsAvailableTo.setText("Ongoing");
            }
            else
            {
                tvProductDetailsAvailableTo.setText(product.getAvailableTo());
            }

            tvProductDetailsAvailability.setText(product.getAvailabilityTitles());
            tvProductDetailsMinTerm.setText(product.getMinStayTitle());
            tvProductDetailsMaxTerm.setText(product.getMaxStayTitle());
            tvProductDetailsSmoking.setText(product.getSmokingTitle());

            String amenityIds = product.getAmenityIds();
            String[] amenityIdArray = amenityIds.split(",");
            if(amenityIdArray != null && amenityIdArray.length > 0)
            {
                for(int counter = 0; counter < amenityIdArray.length; counter++)
                {
                    int amenityId = 0;
                    try
                    {
                        amenityId = Integer.parseInt(amenityIdArray[counter]);
                    }
                    catch(Exception ex)
                    {

                    }
                    if(amenityId  == 1)
                    {
                        tvProductDetailsAmenityParking.setText("Yes");
                    }
                    if(amenityId  == 2)
                    {
                        tvProductDetailsAmenityBalcony.setText("Yes");
                    }
                    if(amenityId  == 3)
                    {
                        tvProductDetailsAmenityGarden.setText("Yes");
                    }
                    if(amenityId  == 4)
                    {
                        tvProductDetailsAmenityDisabledAccess.setText("Yes");
                    }
                    if(amenityId  == 5)
                    {
                        tvProductDetailsAmenityGarage.setText("Yes");
                    }
                }
            }

            tvProductDetailsPets.setText(product.getPetTitle());
            tvProductDetailsOccupation.setText(product.getOccupationTitle());
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
            ivProductSave.setVisibility(View.GONE);
            if(dtoProduct.getAuctionEndTimeLeft() == 0){
                tvProductDetailsBidTimeLeft.setVisibility(View.GONE);
            }
        }
        else {
            myAdvertBtnRow.setVisibility(View.GONE);
            savedAdvertBtnRow.setVisibility(View.VISIBLE);
            ivProductSave.setVisibility(View.VISIBLE);
            if(dtoProduct.getAuctionEndTimeLeft() == 0){
                //Toast.makeText(ShowAdvertProductDetails.this, "Time is finished! " + product.getTime(),Toast.LENGTH_SHORT).show();
                proppertyPlaceBidBtn.setVisibility(View.GONE);
                tvProductDetailsBidTimeLeft.setVisibility(View.GONE);
            }

            ivProductSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBarDialog.show();
                    try
                    {
                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        packetHeader.setAction(ACTION.ADD_SAVED_PRODUCT);
                        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
                        packetHeader.setSessionId(session.getSessionId());
                        new BackgroundWork().execute(packetHeader, productString, new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                progressBarDialog.dismiss();
                                SignInResponse signInResponse = null;
                                String stringSignInResponse = null;
                                if(msg != null && msg.obj != null)
                                {
                                    stringSignInResponse = (String)msg.obj;
                                }
                                if(stringSignInResponse != null)
                                {
                                    Gson gson = new Gson();
                                    signInResponse = gson.fromJson(stringSignInResponse, SignInResponse.class);
                                }
                                if(signInResponse != null && signInResponse.isSuccess())
                                {
                                    Toast.makeText(getApplicationContext(), "Ad is saved.", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Error while saving ad. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    catch(Exception ex)
                    {
                        progressBarDialog.dismiss();
                    }
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        executeTimer();
        this.fetchUserProfile(product.getUserId());
    }

    public void fetchUserProfile(final int userId)
    {
        progressBarDialog = new Dialog(ShowAdvertProductDetails.this);
        progressBarDialog.setContentView(R.layout.progressbar);
        progressBarDialog.show();

        EntityUser entityUser = new EntityUser();
        entityUser.setId(userId);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String userString = gson.toJson(entityUser);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_USER_INFO);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, userString, new Handler(){
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
                        if(user == null | user.getEntityUser() == null)
                        {
                            return;
                        }
                        progressBarDialog.dismiss();
                        boolean isAgent = false;
                        List<EntityRole> roleList = user.getRoles();
                        if(roleList != null && roleList.size() > 0)
                        {
                            for(int counter = 0; counter < roleList.size(); counter++)
                            {
                                EntityRole role = roleList.get(counter);
                                if(role.getId() == Constants.USER_TYPE_ID_AGENT)
                                {
                                    isAgent = true;
                                }
                            }
                        }

                        //isAgent = true;
                        if(isAgent)
                        {
                            Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getEntityUser().getAgentLogo()).into(ivProductAgentLogo);
                            tvProductBusinessName.setText(user.getEntityUser().getBusinessName());
                            tvProductAddress.setText(user.getEntityUser().getAddress());
                            llProductAgent.setVisibility(View.VISIBLE);
                            llProductCompanyName.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvProductCompanyName.setText(product.getCompanyName());
                            llProductAgent.setVisibility(View.GONE);
                            llProductCompanyName.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        fetchProfileCounter++;
                        if (fetchProfileCounter <= 5)
                        {
                            fetchUserProfile(userId);
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
                        fetchUserProfile(userId);
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
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
                                long tempTime = dtoProduct.getAuctionEndTimeLeft();
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
                                    dtoProduct.setAuctionEndTimeLeft(dtoProduct.getAuctionEndTimeLeft() - 1);
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
                        property_total_bids_intent.putExtra("productString", productString);
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
                        Intent intentProductDetailsBack = new Intent(getBaseContext(), ManageAdvertDashboard.class);
                        startActivity(intentProductDetailsBack);

                        /*if(adIdentity == Constants.MY_AD_IDENTITY){
                            Intent show_advert_product_details_intent = new Intent(getBaseContext(), MyAdvertStep1.class);
                            startActivity(show_advert_product_details_intent);
                        }
                        else {
                            Intent show_advert_product_details_intent = new Intent(getBaseContext(), SavedAdvertStep1.class);
                            startActivity(show_advert_product_details_intent);
                        }*/

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
