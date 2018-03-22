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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bdlions.dto.response.ClientListResponse;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateAdvertStep1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private static Spinner productCategorySpinner, productSizeSpinner, productTypeSpinner;
    //ArrayAdapter<CharSequence> i_have_for_rent_adapter,size_of_property_adapter,type_of_property_adapter;
    EntityProduct product;
    SessionManager session;
    NavigationManager navigationManager;

    ArrayAdapter<EntityProductType> productTypeAdapter;
    ArrayAdapter<EntityProductSize> productSizeAdapter;
    ArrayAdapter<EntityProductCategory> productCategoryAdapter;

    public List<EntityProductType> productTypeList = new ArrayList<>();
    public List<EntityProductSize> productSizeList = new ArrayList<>();
    public List<EntityProductCategory> productCategoryList = new ArrayList<>();

    EntityProductType selectedProductType;
    EntityProductSize selectedProductSize;
    EntityProductCategory selectedProductCategory;

    public Dialog progressBarDialog;

    public int fetchProductInfoCounter = 0;
    public int fetchProductTypeCounter = 0;
    public int fetchProductSizeCounter = 0;
    public int fetchProductCategoryCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getBaseContext());

        product = new EntityProduct();
        //-------------if this activity is called by product id then handle it ----------------------------------
        //-------------if this activity is called from back arrow of step2 then handle it -----------------------
        try
        {
            String productString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, EntityProduct.class);

            progressBarDialog = new Dialog(CreateAdvertStep1.this);
            progressBarDialog.setContentView(R.layout.progressbar);
            progressBarDialog.show();

            if(product.getId() > 0)
            {
                fetchProductInfo();
            }
            else
            {
                product.setImg("a.jpg");
                fetchProductTypeList();
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
        //iHaveForRentSpinner();
        //sizeOfPropertySpinner();
        //typeOfPropertySpinner();


        //fetchProductSizeList();
        //fetchProductTypeList();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /***
     * this method will fetch product info from the server
     * */
    public void fetchProductInfo()
    {
        EntityProduct tempProduct = new EntityProduct();
        tempProduct.setId(product.getId());
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
                    ClientResponse clientResponse = null;
                    String clientResponseString = null;
                    if(msg != null && msg.obj != null)
                    {
                        clientResponseString = (String) msg.obj;
                    }
                    if(clientResponseString != null)
                    {
                        Gson gson = new Gson();
                        clientResponse = gson.fromJson(clientResponseString, ClientResponse.class);
                    }
                    if(clientResponse != null && clientResponse.isSuccess())
                    {
                        product = (EntityProduct) clientResponse.getResult();

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

                        String bidStartFrom = product.getAuctionStartDate();
                        String bidStartTo = product.getAuctionEndDate();
                        if(bidStartFrom != null && !bidStartFrom.equals(""))
                        {
                            String[] bidStartFromArray = bidStartFrom.split("-");
                            product.setAuctionStartDate(bidStartFromArray[2]+"-"+bidStartFromArray[1]+"-"+bidStartFromArray[0]);
                        }
                        if(bidStartTo != null && !bidStartTo.equals(""))
                        {
                            String[] bidStartToArray = bidStartTo.split("-");
                            product.setAuctionEndDate(bidStartToArray[2]+"-"+bidStartToArray[1]+"-"+bidStartToArray[0]);
                        }
                        fetchProductTypeList();
                    }
                    else
                    {
                        fetchProductInfoCounter++;
                        if (fetchProductInfoCounter <= 5)
                        {
                            fetchProductInfo();
                        }
                        else
                        {
                            //display a popup to show error message
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
                        fetchProductInfo();
                    }
                    else
                    {
                        //display a popup to show error message
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    /***
     * this method will fetch product type list from the server
     * */
    public void fetchProductTypeList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_PRODUCT_TYPE_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String clientListResponseString = null;
                ClientListResponse clientListResponse = null;
                Gson gson = new Gson();
                if(msg != null && msg.obj != null)
                {
                    clientListResponseString = (String) msg.obj;
                }
                if(clientListResponseString != null)
                {

                    clientListResponse = gson.fromJson(clientListResponseString, ClientListResponse.class);
                }
                if(clientListResponse != null && clientListResponse.isSuccess() && clientListResponse.getList() != null )
                {
                    try
                    {
                        JSONObject obj = new JSONObject(clientListResponseString);
                        productTypeList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<EntityProductType>>(){}.getType());
                        if(productTypeList == null)
                        {
                            progressBarDialog.dismiss();
                            return;
                        }
                    }
                    catch(Exception ex)
                    {
                        progressBarDialog.dismiss();
                        return;
                    }

                    int selectedTypePosition = 0;
                    if(product != null && product.getId() == 0 && product.getTypeId() == 0 && productTypeList.size() > 0)
                    {
                        product.setTypeId(productTypeList.get(0).getId());
                        product.setTypeTitle(productTypeList.get(0).getTitle());
                    }
                    else
                    {
                        int typeCounter = productTypeList.size();
                        for(int counter = 0; counter < typeCounter; counter++ )
                        {
                            if(productTypeList.get(counter).getId() == product.getTypeId())
                            {
                                selectedProductType = productTypeList.get(counter);
                                selectedTypePosition = counter;
                                break;
                            }
                        }
                    }

                    productTypeAdapter = new ArrayAdapter<EntityProductType>( CreateAdvertStep1.this, android.R.layout.simple_spinner_item, productTypeList);
                    productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    productTypeSpinner = (Spinner) findViewById(R.id.i_have_for_rent_spinner);
                    productTypeSpinner.setAdapter(productTypeAdapter);
                    if(selectedProductType != null)
                    {
                        productTypeSpinner.setSelection(selectedTypePosition);
                    }
                    productTypeSpinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                {
                                    selectedProductType = (EntityProductType) productTypeSpinner.getSelectedItem();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            }
                    );
                    fetchProductSizeList();
                }
                else
                {
                    fetchProductTypeCounter++;
                    if (fetchProductTypeCounter <= 5)
                    {
                        fetchProductTypeList();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    /***
     * this method will fetch product size list from the server
     * */
    public void fetchProductSizeList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_PRODUCT_SIZE_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String clientListResponseString = null;
                ClientListResponse clientListResponse = null;
                Gson gson = new Gson();
                if(msg != null && msg.obj != null)
                {
                    clientListResponseString = (String) msg.obj;
                }
                if(clientListResponseString != null)
                {

                    clientListResponse = gson.fromJson(clientListResponseString, ClientListResponse.class);
                }
                if(clientListResponse != null && clientListResponse.isSuccess() && clientListResponse.getList() != null )
                {
                    try
                    {
                        JSONObject obj = new JSONObject(clientListResponseString);
                        productSizeList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<EntityProductSize>>(){}.getType());
                        if(productSizeList == null)
                        {
                            progressBarDialog.dismiss();
                            return;
                        }
                    }
                    catch(Exception ex)
                    {
                        progressBarDialog.dismiss();
                        return;
                    }

                    int selectedSizePosition = 0;
                    if(product != null && product.getId() == 0 && product.getSizeId() == 0 && productSizeList.size() > 0)
                    {
                        product.setSizeId(productSizeList.get(0).getId());
                        product.setSizeTitle(productSizeList.get(0).getTitle());
                    }
                    else
                    {
                        int sizeCounter = productSizeList.size();
                        for(int counter = 0; counter < sizeCounter; counter++ )
                        {
                            if(productSizeList.get(counter).getId() == product.getSizeId())
                            {
                                selectedProductSize = productSizeList.get(counter);
                                selectedSizePosition = counter;
                                break;
                            }
                        }
                    }

                    productSizeAdapter = new ArrayAdapter<EntityProductSize>( CreateAdvertStep1.this, android.R.layout.simple_spinner_item, productSizeList);
                    productSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    productSizeSpinner = (Spinner) findViewById(R.id.size_of_property_spinner);
                    productSizeSpinner.setAdapter(productSizeAdapter);
                    if(selectedProductSize != null)
                    {
                        productSizeSpinner.setSelection(selectedSizePosition);
                    }
                    productSizeSpinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {

                                    selectedProductSize = (EntityProductSize)productSizeSpinner.getSelectedItem();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            }
                    );
                    fetchProductCategoryList();
                }
                else
                {
                    fetchProductSizeCounter++;
                    if (fetchProductSizeCounter <= 5)
                    {
                        fetchProductSizeList();
                    }
                    else
                    {
                        //display pop up error message
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    /***
     * this method will fetch product category list from the server
     * */
    public void fetchProductCategoryList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_PRODUCT_CATEGORY_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String clientListResponseString = null;
                ClientListResponse clientListResponse = null;
                Gson gson = new Gson();
                if(msg != null && msg.obj != null)
                {
                    clientListResponseString = (String) msg.obj;
                }
                if(clientListResponseString != null)
                {
                    clientListResponse = gson.fromJson(clientListResponseString, ClientListResponse.class);
                }
                if(clientListResponse != null && clientListResponse.isSuccess() && clientListResponse.getList() != null )
                {
                    try
                    {
                        JSONObject obj = new JSONObject(clientListResponseString);
                        productCategoryList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<EntityProductCategory>>(){}.getType());
                        if(productCategoryList == null)
                        {
                            progressBarDialog.dismiss();
                            return;
                        }
                    }
                    catch(Exception ex)
                    {
                        progressBarDialog.dismiss();
                        return;
                    }

                    int selectedCategoryPosition = 0;

                    //setting default product category
                    if(product != null && product.getId() == 0 && product.getCategoryId() == 0 && productCategoryList.size() > 0)
                    {
                        product.setCategoryId(productCategoryList.get(0).getId());
                        product.setCategoryTitle(productCategoryList.get(0).getTitle());
                    }
                    //setting product selected category
                    else
                    {
                        int categoryCounter = productCategoryList.size();
                        for(int counter = 0; counter < categoryCounter; counter++ )
                        {
                            if(productCategoryList.get(counter).getId() == product.getCategoryId())
                            {
                                selectedProductCategory = productCategoryList.get(counter);
                                selectedCategoryPosition = counter;
                                break;
                            }
                        }
                    }

                    productCategoryAdapter = new ArrayAdapter<EntityProductCategory>( CreateAdvertStep1.this, android.R.layout.simple_spinner_item, productCategoryList);
                    productCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    productCategorySpinner = (Spinner) findViewById(R.id.type_of_property_spinner);
                    productCategorySpinner.setAdapter(productCategoryAdapter);
                    if(selectedProductCategory != null)
                    {
                        productCategorySpinner.setSelection(selectedCategoryPosition);
                    }
                    productCategorySpinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                {
                                    selectedProductCategory = (EntityProductCategory)productCategorySpinner.getSelectedItem();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            }
                    );
                    progressBarDialog.dismiss();
                }
                else
                {
                    fetchProductCategoryCounter++;
                    if (fetchProductCategoryCounter <= 5)
                    {
                        fetchProductCategoryList();
                    }
                    else
                    {
                        //display pop up with error message
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }





    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step1_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent create_advert_step1_back_arrow_intent = new Intent(getBaseContext(), ManageAdvertDashboard.class);
                        startActivity(create_advert_step1_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton) findViewById(R.id.create_advert_step1_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(validateInputProduct())
                        {
                            setInputToProduct();
                            Intent manageAdvertStep1ForwardArrowIntent = new Intent(getBaseContext(), CreateAdvertStep2.class);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String productString = gson.toJson(product);
                            manageAdvertStep1ForwardArrowIntent.putExtra("productString",productString);
                            startActivity(manageAdvertStep1ForwardArrowIntent);
                        }
                    }
                }
        );
    }

    //validating input fields
    public boolean validateInputProduct()
    {
        if(selectedProductCategory == null)
        {
            Toast.makeText(getBaseContext(),"Category is required." , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(selectedProductSize == null)
        {
            Toast.makeText(getBaseContext(),"Size is required." , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(selectedProductType == null)
        {
            Toast.makeText(getBaseContext(),"Type is required." , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //setting input fields into product info
    public void setInputToProduct()
    {
        product.setCategoryId(selectedProductCategory.getId());
        product.setCategoryTitle(selectedProductCategory.getTitle());
        product.setSizeId(selectedProductSize.getId());
        product.setSizeTitle(selectedProductSize.getTitle());
        product.setTypeId(selectedProductType.getId());
        product.setTypeTitle(selectedProductType.getTitle());
    }

    /*public void iHaveForRentSpinner(){
        sp_i_have_for_rent = (Spinner) findViewById(R.id.i_have_for_rent_spinner);
        i_have_for_rent_adapter = ArrayAdapter.createFromResource(this,R.array.i_have_for_rent_spinner_options,android.R.layout.simple_spinner_item);
        i_have_for_rent_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_i_have_for_rent.setAdapter(i_have_for_rent_adapter);
        sp_i_have_for_rent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void sizeOfPropertySpinner(){
        sp_size_of_property = (Spinner) findViewById(R.id.size_of_property_spinner);
        size_of_property_adapter = ArrayAdapter.createFromResource(this,R.array.size_of_property_spinner_options,android.R.layout.simple_spinner_item);
        size_of_property_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_size_of_property.setAdapter(size_of_property_adapter);
        sp_size_of_property.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void typeOfPropertySpinner(){
        sp_type_of_property = (Spinner) findViewById(R.id.type_of_property_spinner);
        type_of_property_adapter = ArrayAdapter.createFromResource(this,R.array.type_of_property_spinner_options,android.R.layout.simple_spinner_item);
        type_of_property_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type_of_property.setAdapter(type_of_property_adapter);
        sp_type_of_property.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        getMenuInflater().inflate(R.menu.create_advert_step1, menu);
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
       // }

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
