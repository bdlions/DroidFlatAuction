package auction.org.droidflatauction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.auction.dto.Image;
import com.auction.dto.Location;
import com.auction.dto.Product;
import com.auction.dto.response.SignInResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateAdvertStep8 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private  static Button btn_submit;
    Product product;
    String productString;
    SessionManager session;
    NavigationManager navigationManager;

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
            product = gson.fromJson(prodString, Product.class);
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

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
                        Intent create_advert_step8_back_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep7.class);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String productString = gson.toJson(product);
                        create_advert_step8_back_arrow_intent.putExtra("productString", productString);
                        startActivity(create_advert_step8_back_arrow_intent);
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

                        //for testing purpose some default fields are set here but it should come from user selection
                        //ProductCategory productCategory = new ProductCategory();
                        //productCategory.setId(1);
                        //product.setProductCategory(productCategory);

                        //ProductSize productSize = new ProductSize();
                        //productSize.setId(1);
                        //product.setProductSize(productSize);

                        //ProductType productType = new ProductType();
                        //productType.setId(1);
                        //product.setProductType(productType);

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

                        String bidStartFrom = product.getBidStartDate();
                        String bidStartTo = product.getBidEndDate();
                        if(bidStartFrom != null && !bidStartFrom.equals(""))
                        {
                            //if user uses / instead of - then convert symbol
                            bidStartFrom = bidStartFrom.replaceAll("/", "-");
                            String[] bidStartFromArray = bidStartFrom.split("-");
                            product.setBidStartDate(bidStartFromArray[2]+"-"+bidStartFromArray[1]+"-"+bidStartFromArray[0]);
                        }
                        else
                        {
                            product.setBidStartDate(currentDate);
                        }
                        if(bidStartTo != null && !bidStartTo.equals(""))
                        {
                            //if user uses / instead of - then convert symbol
                            bidStartTo = bidStartTo.replaceAll("/", "-");
                            String[] bidStartToArray = bidStartTo.split("-");
                            product.setBidEndDate(bidStartToArray[2]+"-"+bidStartToArray[1]+"-"+bidStartToArray[0]);
                        }
                        else
                        {
                            product.setBidEndDate(currentDate);
                        }

                        Location location = new Location();
                        location.setId(1);
                        product.setLocation(location);

                        //Stay minStay = new Stay();
                        //minStay.setId(1);
                        //Stay maxStay = new Stay();
                        //maxStay.setId(1);
                        //product.setMinStay(minStay);
                        //product.setMaxStay(maxStay);

                        //Smoking smoking = new Smoking();
                        //smoking.setId(1);
                        //Occupation occupation = new Occupation();
                        //occupation.setId(1);
                        //Pet pet = new Pet();
                        //pet.setId(1);
                        //product.setSmoking(smoking);
                        //product.setOccupation(occupation);
                        //product.setPet(pet);

                        //from apk, we are allowing to upload one image
                        if(product.getId() == 0)
                        {
                            //setting a default image
                            if(product.getImg() == null || product.getImg().equals(""))
                            {
                                product.setImg("a.jpg");
                            }

                            Image image1 = new Image();
                            image1.setId(1);
                            image1.setTitle(product.getImg());

                            Image[] images = new Image[1];
                            images[0] = image1;
                            product.setImages(images);
                        }


                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        productString = gson.toJson(product);

                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        if(product.getId() == 0)
                        {
                            packetHeader.setAction(ACTION.ADD_PRODUCT);
                        }
                        else
                        {
                            packetHeader.setAction(ACTION.UPDATE_PRODUCT_INFO);
                        }

                        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
                        packetHeader.setSessionId(session.getSessionId());
                        new BackgroundWork().execute(packetHeader, productString, new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
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
                                    Toast.makeText(getApplicationContext(), "Ad is created.", Toast.LENGTH_LONG).show();
                                    //show a message that advert is created and go to my ads page
                                    Intent create_advert_submit_button_intent = new Intent(getBaseContext(), ManageAdvertDashboard.class);
                                    startActivity(create_advert_submit_button_intent);
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

        } else if (id == R.id.nav_phone) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
