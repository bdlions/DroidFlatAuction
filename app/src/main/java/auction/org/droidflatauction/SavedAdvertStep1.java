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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.auction.dto.Product;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;

public class SavedAdvertStep1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    ListView savedAdvertPropertyListView;
    ArrayList<Integer> property_iamges, productIdList;
    ArrayList<String> property_title_list,property_bedroom_list,property_bathroom_list,property_price_list, imgList;
    SavedAdvertPropertyAdapter savedAdvertPropertyAdapter;

    SessionManager session;

    public int fetchProductInfoCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_advert_step1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onClickButtonBackArrowListener();

        // Session Manager
        session = new SessionManager(getApplicationContext());

        savedAdvertPropertyListView = (ListView) findViewById(R.id.saved_advert_property_listview);
        property_iamges = new ArrayList<>();
        property_title_list = new ArrayList<>();

        //property_iamges = getPropertyIamges();
        //property_title_list = getPropertyTitileList();
        //property_bedroom_list = getPropertyBedroomList();
        //property_bathroom_list = getPropertyBathroomList();
        //property_bathroom_list = getPropertyBathroomList();
        //property_price_list = getPropertyPriceList();

        productIdList = (ArrayList<Integer>)getIntent().getExtras().get("productIdList");
        property_iamges = (ArrayList<Integer>)getIntent().getExtras().get("imageList");
        imgList = (ArrayList<String>)getIntent().getExtras().get("imgList");
        property_title_list = (ArrayList<String>)getIntent().getExtras().get("titleList");
        property_bedroom_list = (ArrayList<String>)getIntent().getExtras().get("bedroomList");
        property_bathroom_list = (ArrayList<String>)getIntent().getExtras().get("bathroomList");
        property_price_list = (ArrayList<String>)getIntent().getExtras().get("priceList");

        savedAdvertPropertyAdapter = new SavedAdvertPropertyAdapter(SavedAdvertStep1.this,session.getSessionId(), productIdList, property_iamges,imgList, property_title_list,property_bedroom_list,property_bathroom_list,property_price_list);
        savedAdvertPropertyListView.setAdapter(savedAdvertPropertyAdapter);
        savedAdvertPropertyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int productId = productIdList.get(position);
                fetchProductInfo(productId);

                /*Product product = new Product();
                product.setId(productId);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                String productString = gson.toJson(product);

                //String sessionId = session.getSessionId();
                org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                packetHeader.setAction(ACTION.FETCH_PRODUCT_INFO);
                packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
                packetHeader.setSessionId(session.getSessionId());
                new BackgroundWork().execute(packetHeader, productString, new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        try
                        {
                            String resultString = (String)msg.obj;
                            Gson gson = new Gson();
                            Product responseProduct = gson.fromJson(resultString, Product.class);
                            System.out.println(responseProduct.getTitle());

                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson2 = gsonBuilder.create();
                            String productString = gson2.toJson(responseProduct);

                            Intent saved_advert_property_show_details_intent = new Intent(SavedAdvertStep1.this, ShowAdvertProductDetails.class);
                            saved_advert_property_show_details_intent.putExtra("productString", productString);
                            startActivity(saved_advert_property_show_details_intent);
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex.toString());
                        }
                    }
                });*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void fetchProductInfo(final int productId)
    {
        Product tempProduct = new Product();
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
                    Product responseProduct = null;
                    String productInfoString = null;
                    if(msg != null && msg.obj != null)
                    {
                        productInfoString = (String) msg.obj;
                    }
                    if(productInfoString != null)
                    {
                        Gson gson = new Gson();
                        responseProduct = gson.fromJson(productInfoString, Product.class);
                    }
                    if(responseProduct != null && responseProduct.isSuccess() && responseProduct.getId() > 0 )
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson2 = gsonBuilder.create();
                        String productString = gson2.toJson(responseProduct);

                        Intent saved_advert_property_show_details_intent = new Intent(SavedAdvertStep1.this, ShowAdvertProductDetails.class);
                        saved_advert_property_show_details_intent.putExtra("productString", productString);
                        startActivity(saved_advert_property_show_details_intent);
                    }
                    else
                    {
                        fetchProductInfoCounter++;
                        if (fetchProductInfoCounter <= 5)
                        {
                            fetchProductInfo(productId);
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
                }
            }
        });
    }

    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.saved_advert_step1_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent saved_advert_step1_back_arrow_intent = new Intent(getBaseContext(), ManageAdvertDashboard.class);
                        startActivity(saved_advert_step1_back_arrow_intent);
                    }
                }
        );
    }

    /*public ArrayList<Integer> getPropertyIamges(){
        property_iamges = new ArrayList<>();
        property_iamges.add(R.drawable.property_image_01);
        property_iamges.add(R.drawable.property_image_02);
        property_iamges.add(R.drawable.property_image_03);
        property_iamges.add(R.drawable.property_image_04);
        property_iamges.add(R.drawable.property_image_05);
        return property_iamges;
    }
    public ArrayList<String> getPropertyTitileList(){
        property_title_list = new ArrayList<>();
        property_title_list.add("23 pLANET ST, london EC4N 6AJ, UK");
        property_title_list.add("10 Trinity Square, OXFORD EC3N 4AJ, UK");
        property_title_list.add("23 pLANET ST, london EC4N 6AJ, UK");
        property_title_list.add("100 Trinity Square, OXFORD EC3N 4AJ, UK");
        property_title_list.add("223 pLANET ST, london EC4N 6AJ, UK");
        return property_title_list;
    }
    public ArrayList<String> getPropertyBedroomList(){
        property_bedroom_list = new ArrayList<>();
        property_bedroom_list.add("1 Bed Room");
        property_bedroom_list.add("2 Bed Rooms");
        property_bedroom_list.add("3 Bed Rooms");
        property_bedroom_list.add("4 Bed Rooms");
        property_bedroom_list.add("5 Bed Rooms");
        return property_bedroom_list;
    }
    public ArrayList<String> getPropertyBathroomList(){
        property_bathroom_list = new ArrayList<>();
        property_bathroom_list.add("1 Bathroom");
        property_bathroom_list.add("2 Bathrooms");
        property_bathroom_list.add("3 Bathrooms");
        property_bathroom_list.add("4 Bathrooms");
        property_bathroom_list.add("5 Bathrooms");
        return property_bathroom_list;
    }
    public ArrayList<String> getPropertyPriceList(){
        property_price_list = new ArrayList<>();
        property_price_list.add("$152.40 PW");
        property_price_list.add("$162.40 PW");
        property_price_list.add("$172.40 PW");
        property_price_list.add("$182.40 PW");
        property_price_list.add("$192.40 PW");
        return property_price_list;
    }*/

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
        getMenuInflater().inflate(R.menu.saved_advert_step1, menu);
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
