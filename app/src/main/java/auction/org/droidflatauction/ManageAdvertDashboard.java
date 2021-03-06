package auction.org.droidflatauction;

import android.app.Dialog;
import android.content.Entity;
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
import android.widget.Toast;

//import com.bdlions.dto.AccountSettingFA;
import com.bdlions.dto.response.ClientListResponse;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.auction.udp.BackgroundWork;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageAdvertDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static Button create_ad_btn, my_ad_btn, saved_ad_btn, ad_account_settings_btn,
                            individual_ad_bids_btn,ad_stats_btn,ad_ranking,ad_faq;
    SessionManager session;
    public int fetchMyAdsCounter = 0;
    public int fetchSavedAdsCounter = 0;
    public int fetchIndividualAdBidsCounter = 0;
    public int fetchStatsCounter = 0;

    public Dialog progressBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_advert_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

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
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        DTOProduct dtoProduct = new DTOProduct();
                        dtoProduct.setEntityProduct(new EntityProduct());
                        String productString = gson.toJson(dtoProduct);
                        create_advert_intent.putExtra("productString", productString);
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
                        progressBarDialog = new Dialog(ManageAdvertDashboard.this);
                        progressBarDialog.setContentView(R.layout.progressbar);
                        progressBarDialog.show();
                        fetchMyAds();
                    }
                }
        );
    }

    public void fetchMyAds()
    {
        DTOProduct dtoProduct = new DTOProduct();
        dtoProduct.setOffset(0);
        dtoProduct.setLimit(10);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String dtoProductString = gson.toJson(dtoProduct);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_USER_PRODUCT_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, dtoProductString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    List<EntityProduct> productList = new ArrayList<>();
                    ClientListResponse clientListResponse = null;
                    String clientListResponseString = null;
                    Gson gson = new Gson();
                    if(msg != null  && msg.obj != null)
                    {
                        clientListResponseString = (String) msg.obj;
                    }
                    if(clientListResponseString != null)
                    {
                        clientListResponse = gson.fromJson(clientListResponseString, ClientListResponse.class);
                    }
                    if(clientListResponse != null && clientListResponse.isSuccess())
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(clientListResponseString);
                            productList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<EntityProduct>>(){}.getType());
                            if(productList == null)
                            {
                                return;
                            }
                        }
                        catch(Exception ex)
                        {
                            return;
                        }
                    }

                    ArrayList<Integer> imageList = new ArrayList<Integer>();
                    ArrayList<String> imgList = new ArrayList<String>();
                    ArrayList<Integer> productIdList = new ArrayList<Integer>();
                    ArrayList<String> titleList = new ArrayList<String>();
                    ArrayList<String> bedroomList = new ArrayList<String>();
                    ArrayList<String> bathroomList = new ArrayList<String>();
                    ArrayList<String> priceList = new ArrayList<String>();
                    if(productList != null)
                    {
                        int totalProducts = productList.size();
                        for(int productCounter = 0; productCounter < totalProducts; productCounter++)
                        {
                            EntityProduct product = productList.get(productCounter);
                            productIdList.add(product.getId());
                            imageList.add(R.drawable.property_image_01);
                            imgList.add(product.getImg());
                            titleList.add(product.getTitle());
                            bedroomList.add(product.getCategoryTitle() + ", " + product.getSizeTitle());
                            bathroomList.add(product.getTypeTitle());
                            priceList.add("£" + String.format("%.2f",  product.getBasePrice()) + " Guide Price");
                        }
                    }
                    progressBarDialog.dismiss();
                    Intent my_advert_intent = new Intent(getBaseContext(), MyAdvertStep1.class);
                    my_advert_intent.putExtra("imageList", imageList);
                    my_advert_intent.putExtra("imgList", imgList);
                    my_advert_intent.putExtra("productIdList", productIdList);
                    my_advert_intent.putExtra("titleList", titleList);
                    my_advert_intent.putExtra("bedroomList", bedroomList);
                    my_advert_intent.putExtra("bathroomList", bathroomList);
                    my_advert_intent.putExtra("priceList", priceList);
                    startActivity(my_advert_intent);
                }
                catch(Exception ex)
                {
                    System.out.println(ex.toString());
                    fetchMyAdsCounter++;
                    if (fetchMyAdsCounter <= 5)
                    {
                        fetchMyAds();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    public void onClickButtonSavedAdListener(){
        saved_ad_btn = (Button) findViewById(R.id.saved_advert);
        saved_ad_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBarDialog = new Dialog(ManageAdvertDashboard.this);
                        progressBarDialog.setContentView(R.layout.progressbar);
                        progressBarDialog.show();
                        fetchSavedAds();
                    }
                }
        );
    }

    public void fetchSavedAds()
    {
        DTOProduct dtoProduct = new DTOProduct();
        dtoProduct.setOffset(0);
        dtoProduct.setLimit(10);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String dtoProductString = gson.toJson(dtoProduct);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_SAVED_PRODUCT_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, dtoProductString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    progressBarDialog.dismiss();
                    List<EntityProduct> productList = new ArrayList<>();
                    ClientListResponse clientListResponse = null;
                    String clientListResponseString = null;
                    Gson gson = new Gson();
                    if(msg != null  && msg.obj != null)
                    {
                        clientListResponseString = (String) msg.obj;
                    }
                    if(clientListResponseString != null)
                    {
                        clientListResponse = gson.fromJson(clientListResponseString, ClientListResponse.class);
                    }
                    if(clientListResponse != null && clientListResponse.isSuccess())
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(clientListResponseString);
                            productList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<EntityProduct>>(){}.getType());
                            if(productList == null)
                            {
                                return;
                            }
                        }
                        catch(Exception ex)
                        {
                            return;
                        }
                    }

                    ArrayList<Integer> imageList = new ArrayList<Integer>();
                    ArrayList<String> imgList = new ArrayList<String>();
                    ArrayList<Integer> productIdList = new ArrayList<Integer>();
                    ArrayList<String> titleList = new ArrayList<String>();
                    ArrayList<String> bedroomList = new ArrayList<String>();
                    ArrayList<String> bathroomList = new ArrayList<String>();
                    ArrayList<String> priceList = new ArrayList<String>();
                    if(productList != null)
                    {
                        int totalProducts = productList.size();
                        for(int productCounter = 0; productCounter < totalProducts; productCounter++)
                        {
                            EntityProduct product = productList.get(productCounter);
                            productIdList.add(product.getId());
                            imageList.add(R.drawable.property_image_01);
                            imgList.add(product.getImg());
                            titleList.add(product.getTitle());
                            bedroomList.add("");
                            bathroomList.add("");
                            priceList.add("£" + String.format("%.2f",  product.getBasePrice()) + " Guide Price");
                        }
                    }

                    Intent saved_advert_intent = new Intent(getBaseContext(), SavedAdvertStep1.class);
                    saved_advert_intent.putExtra("imageList", imageList);
                    saved_advert_intent.putExtra("imgList", imgList);
                    saved_advert_intent.putExtra("productIdList", productIdList);
                    saved_advert_intent.putExtra("titleList", titleList);
                    saved_advert_intent.putExtra("bedroomList", bedroomList);
                    saved_advert_intent.putExtra("bathroomList", bathroomList);
                    saved_advert_intent.putExtra("priceList", priceList);
                    startActivity(saved_advert_intent);
                }
                catch(Exception ex)
                {
                    System.out.println(ex.toString());
                    fetchSavedAdsCounter++;
                    if (fetchSavedAdsCounter <= 5)
                    {
                        fetchSavedAds();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    public void onClickButtonAccountSettingsAdListener(){
        ad_account_settings_btn = (Button) findViewById(R.id.account_settings_advert);
        ad_account_settings_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent account_settings_advert_intent = new Intent(getBaseContext(), ManageAdvertAccountSettingsStep.class);
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
                        progressBarDialog = new Dialog(ManageAdvertDashboard.this);
                        progressBarDialog.setContentView(R.layout.progressbar);
                        //progressBarDialog.show();
                        //fetchIndividualAdBids();
                    }
                }
        );
    }

    public void fetchIndividualAdBids()
    {
        /*String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_MY_PRODUCT_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    String resultString = (String)msg.obj;
                    Gson gson = new Gson();
                    ProductList response = gson.fromJson(resultString, ProductList.class);
                    System.out.println(response);
                    ArrayList<Product> productList = response.getProducts();
                    ArrayList<Integer> imageList = new ArrayList<Integer>();
                    ArrayList<String> imgList = new ArrayList<String>();
                    ArrayList<Integer> productIdList = new ArrayList<Integer>();
                    ArrayList<String> titleList = new ArrayList<String>();
                    ArrayList<String> bedroomList = new ArrayList<String>();
                    ArrayList<String> bathroomList = new ArrayList<String>();
                    ArrayList<String> priceList = new ArrayList<String>();
                    ArrayList<String> postCodeList = new ArrayList<String>();
                    if(productList != null)
                    {
                        int totalProducts = productList.size();
                        for(int productCounter = 0; productCounter < totalProducts; productCounter++)
                        {
                            Product product = productList.get(productCounter);
                            productIdList.add(product.getId());
                            imageList.add(R.drawable.property_image_01);
                            imgList.add(product.getImg());
                            titleList.add(product.getTitle());
                            bedroomList.add("");
                            bathroomList.add("");
                            priceList.add("");
                            postCodeList.add("c1");
                        }
                    }
                    progressBarDialog.dismiss();

                    Intent intentIndividualAdBids = new Intent(getBaseContext(), ManageAdvertIndividualAdBidsStep1.class);
                    intentIndividualAdBids.putExtra("productIdList", productIdList);
                    intentIndividualAdBids.putExtra("titleList", titleList);
                    intentIndividualAdBids.putExtra("postCodeList", postCodeList);
                    startActivity(intentIndividualAdBids);
                }
                catch(Exception ex)
                {
                    System.out.println(ex.toString());
                    fetchIndividualAdBidsCounter++;
                    if (fetchIndividualAdBidsCounter <= 5)
                    {
                        fetchIndividualAdBids();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });*/
    }

    public void onClickButtonStatsAdListener(){
        ad_stats_btn = (Button) findViewById(R.id.stats_advert);
        ad_stats_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBarDialog = new Dialog(ManageAdvertDashboard.this);
                        progressBarDialog.setContentView(R.layout.progressbar);
                        //progressBarDialog.show();
                        //fetchStats();
                    }
                }
        );
    }

    public void fetchStats()
    {
        /*String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_MY_PRODUCT_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    String resultString = (String)msg.obj;
                    Gson gson = new Gson();
                    ProductList response = gson.fromJson(resultString, ProductList.class);
                    System.out.println(response);
                    ArrayList<Product> productList = response.getProducts();
                    ArrayList<Integer> imageList = new ArrayList<Integer>();
                    ArrayList<String> imgList = new ArrayList<String>();
                    ArrayList<Integer> productIdList = new ArrayList<Integer>();
                    ArrayList<String> titleList = new ArrayList<String>();
                    ArrayList<String> bedroomList = new ArrayList<String>();
                    ArrayList<String> bathroomList = new ArrayList<String>();
                    ArrayList<String> priceList = new ArrayList<String>();
                    if(productList != null)
                    {
                        int totalProducts = productList.size();
                        for(int productCounter = 0; productCounter < totalProducts; productCounter++)
                        {
                            Product product = productList.get(productCounter);
                            productIdList.add(product.getId());
                            imageList.add(R.drawable.property_image_01);
                            imgList.add(product.getImg());
                            titleList.add(product.getTitle());
                            bedroomList.add("");
                            bathroomList.add("");
                            priceList.add("");
                        }
                    }
                    progressBarDialog.dismiss();

                    Intent intentStats = new Intent(getBaseContext(), ManageAdvertStatsStep1.class);
                    intentStats.putExtra("productIdList", productIdList);
                    intentStats.putExtra("titleList", titleList);
                    startActivity(intentStats);
                }
                catch(Exception ex)
                {
                    System.out.println(ex.toString());
                    fetchStatsCounter++;
                    if (fetchStatsCounter <= 5)
                    {
                        fetchStats();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });*/
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
