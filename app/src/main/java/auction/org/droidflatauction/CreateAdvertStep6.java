package auction.org.droidflatauction;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.auction.dto.Product;
import com.auction.dto.response.GeneralResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;


public class CreateAdvertStep6 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static ImageButton ib_back_arrow,ib_forward_arrow;
    private static ImageView ivProductImage;
    Product product;
    SessionManager session;
    NavigationManager navigationManager;
    private static LinearLayout llUploadProductPhoto,llEditProductPhoto;
    public int adCreateIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step6);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        llUploadProductPhoto = (LinearLayout)findViewById(R.id.ll_upload_product_photo);
        llEditProductPhoto = (LinearLayout)findViewById(R.id.ll_edit_product_photo);
        ivProductImage = (ImageView) findViewById(R.id.iv_edit_product_photo);

        //product = (Product)getIntent().getExtras().get("product");
        try
        {
            adCreateIdentity = getIntent().getExtras().getInt("adCreateIdentity");
            //Toast.makeText(CreateAdvertStep6.this, "adCreateIdentity: " + adCreateIdentity,Toast.LENGTH_SHORT).show();

            String productString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, Product.class);
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        if(adCreateIdentity == Constants.MY_AD_CREATE_IDENTITY)
        {
           // Toast.makeText(CreateAdvertStep6.this, "adCreateIdentity: " + adCreateIdentity,Toast.LENGTH_LONG).show();
            llUploadProductPhoto.setVisibility(View.VISIBLE);
            llEditProductPhoto.setVisibility(View.GONE);
        } else
        {
            //Toast.makeText(CreateAdvertStep6.this, "adEditIdentity: " + adCreateIdentity,Toast.LENGTH_LONG).show();
            Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.productImagePath_328_212+product.getImg()).into(ivProductImage);
            llUploadProductPhoto.setVisibility(View.GONE);
            llEditProductPhoto.setVisibility(View.VISIBLE);
        }

        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
       // EditProductImage();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
   /* public void EditProductImage()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String productString = gson.toJson(product);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.UPDATE_USER_LOGO);
        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, productString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    GeneralResponse response = null;
                    String responseString = null;
                    if(msg != null  && msg.obj != null)
                    {
                        responseString = (String) msg.obj;
                    }
                    if(responseString != null)
                    {
                        Gson gson = new Gson();
                        response = gson.fromJson(responseString, GeneralResponse.class);
                    }
                    if(response != null && response.isSuccess())
                    {
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.productImagePath_328_212+product.getImg()).into(ivProductImage);
                        Toast.makeText(CreateAdvertStep6.this, "Agent logo is uploaded successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(CreateAdvertStep6.this, "Unable to upload agent logo. Please try again later.",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(CreateAdvertStep6.this, "Unable to upload agent logo. Please try again later.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step6_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent create_advert_step6_back_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep5.class);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String productString = gson.toJson(product);
                        create_advert_step6_back_arrow_intent.putExtra("productString", productString);
                        create_advert_step6_back_arrow_intent.putExtra("adCreateIdentity", adCreateIdentity);
                        startActivity(create_advert_step6_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton) findViewById(R.id.create_advert_step6_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent create_advert_step6_forward_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep7.class);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String productString = gson.toJson(product);

                        create_advert_step6_forward_arrow_intent.putExtra("productString", productString);
                        create_advert_step6_forward_arrow_intent.putExtra("adCreateIdentity", adCreateIdentity);
                        startActivity(create_advert_step6_forward_arrow_intent);
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
        getMenuInflater().inflate(R.menu.create_advert_step6, menu);
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
        //navigationManager.navigateTo(id);

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
