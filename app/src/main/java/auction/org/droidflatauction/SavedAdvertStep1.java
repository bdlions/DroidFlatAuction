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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.auction.udp.BackgroundWork;
import org.json.JSONObject;

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

    public Dialog progressBarDialog;

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
                progressBarDialog = new Dialog(SavedAdvertStep1.this);
                progressBarDialog.setContentView(R.layout.progressbar);
                progressBarDialog.show();
                fetchProductInfo(productId);
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
                    progressBarDialog.dismiss();
                    DTOProduct responseProduct = null;
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
                            responseProduct = gson.fromJson(obj.get("result").toString(), DTOProduct.class);
                            if(responseProduct == null || responseProduct.getEntityProduct() == null || responseProduct.getEntityProduct().getId() == 0)
                            {
                                return;
                            }
                            Intent saved_advert_property_show_details_intent = new Intent(SavedAdvertStep1.this, ShowAdvertProductDetails.class);
                            saved_advert_property_show_details_intent.putExtra("productString", obj.get("result").toString());
                            saved_advert_property_show_details_intent.putExtra("adIdentity", Constants.OTHER_AD_IDENTITY);
                            startActivity(saved_advert_property_show_details_intent);
                            return;
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
