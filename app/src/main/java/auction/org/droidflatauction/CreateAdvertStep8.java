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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;
import org.bdlions.auction.entity.*;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateAdvertStep8 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private  static Button btn_submit;
    EntityProduct product;
    String productString;
    SessionManager session;
    NavigationManager navigationManager;

    public int fetchProductInfoCounter = 0;
    public Dialog progressBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step8);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        //product = (Product)getIntent().getExtras().get("product");
        try
        {
            String prodString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(prodString, EntityProduct.class);
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        progressBarDialog = new Dialog(CreateAdvertStep8.this);
        progressBarDialog.setContentView(R.layout.progressbar);

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
    public void selectItem( View view){
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.daily_email_alerts:
                if (checked) {
                    //Toast.makeText(getBaseContext(), "Daily Email Alerts is selected", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), "Daily Email Alerts is deselected", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.instant_email_alerts:
                if (checked) {
                    //Toast.makeText(getBaseContext(),   "Instant Email Alerts is selected", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(),   "Instant Email Alerts is deselected", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step8_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent manageAdvertStep8BackArrowIntent = new Intent(getBaseContext(), CreateAdvertStep7.class);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String productString = gson.toJson(product);
                        manageAdvertStep8BackArrowIntent.putExtra("productString", productString);
                        startActivity(manageAdvertStep8BackArrowIntent);
                    }
                }
        );
    }
    public void onClickButtonSubmitListener(){
        btn_submit = (Button) findViewById(R.id.create_advert_submit_button);
        btn_submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //convert date related fields here
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
                        String currentDate = df.format(c.getTime());

                        String availableFrom = product.getAvailableFrom();
                        String availableTo = product.getAvailableTo();
                        if(availableFrom != null && !availableFrom.equals(""))
                        {
                            //if user uses / instead of - then convert symbol
                            availableFrom = availableFrom.replaceAll("/", "-");
                            String[] availableFromArray = availableFrom.split("-");
                            product.setAvailableFrom(availableFromArray[2]+"-"+availableFromArray[1]+"-"+availableFromArray[0]);
                        }
                        else
                        {
                            product.setAvailableFrom(currentDate);
                        }
                        if(availableTo != null && !availableTo.equals(""))
                        {
                            //if user uses / instead of - then convert symbol
                            availableTo = availableTo.replaceAll("/", "-");
                            String[] availableToArray = availableTo.split("-");
                            product.setAvailableTo(availableToArray[2]+"-"+availableToArray[1]+"-"+availableToArray[0]);
                        }
                        else
                        {
                            product.setAvailableTo(currentDate);
                        }

                        String bidStartFrom = product.getAuctionStartDate();
                        String bidStartTo = product.getAuctionEndDate();
                        if(bidStartFrom != null && !bidStartFrom.equals(""))
                        {
                            //if user uses / instead of - then convert symbol
                            bidStartFrom = bidStartFrom.replaceAll("/", "-");
                            String[] bidStartFromArray = bidStartFrom.split("-");
                            product.setAuctionStartDate(bidStartFromArray[2]+"-"+bidStartFromArray[1]+"-"+bidStartFromArray[0]);
                        }
                        else
                        {
                            product.setAuctionStartDate(currentDate);
                        }
                        if(bidStartTo != null && !bidStartTo.equals(""))
                        {
                            //if user uses / instead of - then convert symbol
                            bidStartTo = bidStartTo.replaceAll("/", "-");
                            String[] bidStartToArray = bidStartTo.split("-");
                            product.setAuctionEndDate(bidStartToArray[2]+"-"+bidStartToArray[1]+"-"+bidStartToArray[0]);
                        }
                        else
                        {
                            product.setAuctionEndDate(currentDate);
                        }

                        //from apk, we are allowing to upload one image
                        if(product.getId() == 0)
                        {
                            //setting a default image
                            if(product.getImg() == null || product.getImg().equals(""))
                            {
                                product.setImg("a.jpg");
                                product.setImages("a.jpg");
                            }
                            else
                            {
                                product.setImages(product.getImg());
                            }
                        }

                        DTOProduct dtoProduct = new DTOProduct();
                        dtoProduct.setEntityProduct(product);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String dtoProductString = gson.toJson(dtoProduct);
                        System.out.println(dtoProductString);

                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        if(product.getId() == 0)
                        {
                            packetHeader.setAction(ACTION.ADD_PRODUCT_INFO);
                            packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
                        }
                        else
                        {
                            packetHeader.setAction(ACTION.UPDATE_PRODUCT_INFO);
                            packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
                        }

                        progressBarDialog.show();


                        packetHeader.setSessionId(session.getSessionId());
                        new BackgroundWork().execute(packetHeader, dtoProductString, new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                progressBarDialog.dismiss();
                                EntityProduct responseProduct = null;
                                ClientResponse clientResponse = new ClientResponse();
                                String stringClientResponse = null;
                                Gson gson = new Gson();
                                if(msg != null && msg.obj != null)
                                {
                                    stringClientResponse = (String)msg.obj;
                                }
                                if(stringClientResponse != null)
                                {

                                    clientResponse = gson.fromJson(stringClientResponse, ClientResponse.class);
                                }
                                if(clientResponse != null && clientResponse.isSuccess())
                                {
                                    Toast.makeText(getApplicationContext(), clientResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    //go to ad details page instead of manageAdvertDashboard page
                                    if(product.getId() == 0)
                                    {
                                        try
                                        {
                                            JSONObject obj = new JSONObject(stringClientResponse);
                                            responseProduct = gson.fromJson(obj.get("result").toString(), EntityProduct.class);
                                            if(responseProduct == null || responseProduct.getId() == 0)
                                            {
                                                Toast.makeText(getApplicationContext(),"Unable to process your request.", Toast.LENGTH_LONG).show();
                                                return;

                                            }
                                            product.setId(responseProduct.getId());
                                        }
                                        catch(Exception ex)
                                        {
                                            Toast.makeText(getApplicationContext(),"Unable to process your request.", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                    progressBarDialog.show();
                                    fetchProductInfo(product.getId());
                                }
                                else if(clientResponse != null && !clientResponse.isSuccess())
                                {
                                    Toast.makeText(getApplicationContext(), clientResponse.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Error while creating a new ad. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
        );
    }

    public void fetchProductInfo(final int productId)
    {
        EntityProduct tempProduct = new EntityProduct();
        tempProduct.setId(productId);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String tempProductString = gson.toJson(tempProduct);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_PRODUCT_INFO);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, tempProductString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    DTOProduct responseProduct = null;
                    ClientResponse clientResponse = new ClientResponse();
                    String stringClientResponse = null;
                    Gson gson = new Gson();
                    if(msg != null && msg.obj != null)
                    {
                        stringClientResponse = (String)msg.obj;
                    }
                    if(stringClientResponse != null)
                    {

                        clientResponse = gson.fromJson(stringClientResponse, ClientResponse.class);
                    }
                    if(clientResponse != null && clientResponse.isSuccess())
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(stringClientResponse);
                            responseProduct = gson.fromJson(obj.get("result").toString(), DTOProduct.class);
                            if(responseProduct == null || responseProduct.getEntityProduct() == null || responseProduct.getEntityProduct().getId() == 0)
                            {
                                return;
                            }
                            Intent my_advert_property_show_details_intent = new Intent(CreateAdvertStep8.this, ShowAdvertProductDetails.class);
                            my_advert_property_show_details_intent.putExtra("productString", obj.get("result").toString());
                            my_advert_property_show_details_intent.putExtra("adIdentity", Constants.MY_AD_IDENTITY);
                            startActivity(my_advert_property_show_details_intent);
                        }
                        catch(Exception ex)
                        {
                            return;
                        }
                    }
                    else
                    {
                        fetchProductInfoCounter++;
                        if (fetchProductInfoCounter <= 5)
                        {
                            fetchProductInfo(productId);
                        }
                        else
                        {
                            progressBarDialog.dismiss();
                        }
                    }
                }
                catch(Exception ex)
                {
                    System.out.println(ex.toString());
                    fetchProductInfoCounter++;
                    if (fetchProductInfoCounter <= 5)
                    {
                        fetchProductInfo(productId);
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
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
        getMenuInflater().inflate(R.menu.create_advert_step8, menu);
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
