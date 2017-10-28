package auction.org.droidflatauction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Property;
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

import com.bdlions.dto.Product;
import com.bdlions.dto.ProductBid;
import com.bdlions.dto.response.GeneralResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

public class PropertyPlaceBid extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    SessionManager session;
    private static Button btnPostBid;
    private static EditText etBidDirectly;
    public Product product;
    int adIdentity;
    String productString;
    public int addProductBidCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_place_bid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        etBidDirectly = (EditText) findViewById(R.id.et_bid_directly);
        btnPostBid = (Button)findViewById(R.id.btn_submit_bid);

        try
        {
            adIdentity = getIntent().getExtras().getInt("adIdentity");
            productString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, Product.class);
        }
        catch(Exception ex)
        {
            //handle exception
        }

        onClickButtonPostBidListener();
        onClickButtonBackArrowListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onClickButtonPostBidListener(){
        btnPostBid.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            Double bidAmount = Double.parseDouble(etBidDirectly.getText().toString());
                            if(bidAmount <= 0)
                            {
                                Toast.makeText(PropertyPlaceBid.this, "Please add valid bid amount!",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            placeDirectBid();
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(PropertyPlaceBid.this, "Please add valid bid amount!",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }

    public void placeDirectBid()
    {
        ProductBid productBid = new ProductBid();
        Product tempProduct = new Product();
        tempProduct.setId(product.getId());
        productBid.setProduct(tempProduct);
        //set bid directly price
        try
        {
            Double bidAmount = Double.parseDouble(etBidDirectly.getText().toString());
            productBid.setPrice(bidAmount);
        }
        catch(Exception ex)
        {

        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String tempProductBidString = gson.toJson(productBid);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.ADD_PRODUCT_BID);
        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, tempProductBidString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    GeneralResponse response = null;
                    String responseString = null;
                    if(msg != null && msg.obj != null)
                    {
                        responseString = (String) msg.obj;
                    }
                    if(responseString != null)
                    {
                        Gson gson = new Gson();
                        response = gson.fromJson(responseString, GeneralResponse.class);
                    }
                    if(response != null)
                    {
                        if(response.isSuccess())
                        {
                            Toast.makeText(PropertyPlaceBid.this, response.getMessage(),Toast.LENGTH_SHORT).show();
                            //redirect to product details page
                            Intent productDetailsIntent = new Intent(PropertyPlaceBid.this, ShowAdvertProductDetails.class);
                            productDetailsIntent.putExtra("productString", productString);
                            productDetailsIntent.putExtra("adIdentity", Constants.MY_AD_IDENTITY);
                            startActivity(productDetailsIntent);
                        }
                        else
                        {
                            Toast.makeText(PropertyPlaceBid.this, response.getMessage(),Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else
                    {
                        addProductBidCounter++;
                        if (addProductBidCounter <= 5)
                        {
                            placeDirectBid();
                        }
                    }
                }
                catch(Exception ex)
                {
                    addProductBidCounter++;
                    if (addProductBidCounter <= 5)
                    {
                        placeDirectBid();
                    }
                }
            }
        });
    }

    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.property_place_bid_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent property_place_bid_back_arrow_intent = new Intent(getBaseContext(), SavedAdvertStep2.class);
                        startActivity(property_place_bid_back_arrow_intent);
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
        getMenuInflater().inflate(R.menu.property_place_bid, menu);
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
