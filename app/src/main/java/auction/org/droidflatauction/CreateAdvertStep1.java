package auction.org.droidflatauction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
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

import com.auction.dto.Product;
import com.auction.dto.ProductCategory;
import com.auction.dto.ProductCategoryList;
import com.auction.dto.ProductSize;
import com.auction.dto.ProductSizeList;
import com.auction.dto.ProductType;
import com.auction.dto.ProductTypeList;
import com.auction.dto.User;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.List;

public class CreateAdvertStep1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private static Spinner sp_i_have_for_rent,sp_size_of_property,sp_type_of_property;
    ArrayAdapter<CharSequence> i_have_for_rent_adapter,size_of_property_adapter,type_of_property_adapter;
    Product product;
    SessionManager session;

    ArrayAdapter<ProductType> productTypeAdapter;
    ArrayAdapter<ProductSize> productSizeAdapter;
    ArrayAdapter<ProductCategory> productCategoryAdapter;

    public List<ProductType> productTypeList = new ArrayList<>();
    public List<ProductSize> productSizeList = new ArrayList<>();
    public List<ProductCategory> productCategoryList = new ArrayList<>();

    ProductType selectedProductType;
    ProductSize selectedProductSize;
    ProductCategory selectedProductCategory;

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

        product = new Product();

        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
        //iHaveForRentSpinner();
        //sizeOfPropertySpinner();
        //typeOfPropertySpinner();

        fetchProductCategoryList();
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
                ProductCategoryList pCategoryList = null;
                String productCategoryListString = null;
                if(msg != null)
                {
                    productCategoryListString = (String) msg.obj;
                }
                if(productCategoryListString != null)
                {
                    Gson gson = new Gson();
                    pCategoryList = gson.fromJson(productCategoryListString, ProductCategoryList.class);
                }
                if(pCategoryList != null && pCategoryList.isSuccess() && pCategoryList.getProductCategories() != null )
                {
                    productCategoryList = pCategoryList.getProductCategories();

                    productCategoryAdapter = new ArrayAdapter<ProductCategory>( CreateAdvertStep1.this, android.R.layout.simple_spinner_item, productCategoryList);
                    sp_i_have_for_rent = (Spinner) findViewById(R.id.i_have_for_rent_spinner);
                    sp_i_have_for_rent.setAdapter(productCategoryAdapter);
                    sp_i_have_for_rent.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {

                                    selectedProductCategory = (ProductCategory)sp_i_have_for_rent.getSelectedItem();
                                    System.out.println(selectedProductCategory.getTitle());
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
                    fetchProductCategoryCounter++;
                    if (fetchProductCategoryCounter <= 5)
                    {
                        fetchProductCategoryList();
                    }
                }

            }
        });

    }

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
                ProductSizeList pSizeList = null;
                String productSizeListString = null;
                if(msg != null)
                {
                    productSizeListString = (String) msg.obj;
                }
                if(productSizeListString != null)
                {
                    Gson gson = new Gson();
                    pSizeList = gson.fromJson(productSizeListString, ProductSizeList.class);
                }
                if(pSizeList != null && pSizeList.isSuccess() && pSizeList.getProductSizes() != null )
                {
                    productSizeList = pSizeList.getProductSizes();

                    productSizeAdapter = new ArrayAdapter<ProductSize>( CreateAdvertStep1.this, android.R.layout.simple_spinner_item, productSizeList);
                    sp_size_of_property = (Spinner) findViewById(R.id.size_of_property_spinner);
                    sp_size_of_property.setAdapter(productSizeAdapter);
                    sp_size_of_property.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {

                                    selectedProductSize = (ProductSize)sp_size_of_property.getSelectedItem();
                                    System.out.println(selectedProductSize.getTitle());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub

                                }

                            }
                    );

                    fetchProductTypeList();
                }
                else
                {
                    fetchProductSizeCounter++;
                    if (fetchProductSizeCounter <= 5)
                    {
                        fetchProductSizeList();
                    }
                }

            }
        });

    }

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
                ProductTypeList pTypeList = null;
                String productTypeListString = null;
                if(msg != null)
                {
                    productTypeListString = (String) msg.obj;
                }
                if(productTypeListString != null)
                {
                    Gson gson = new Gson();
                    pTypeList = gson.fromJson(productTypeListString, ProductTypeList.class);
                }
                if(pTypeList != null && pTypeList.isSuccess() && pTypeList.getProductTypes() != null )
                {
                    productTypeList = pTypeList.getProductTypes();

                    productTypeAdapter = new ArrayAdapter<ProductType>( CreateAdvertStep1.this, android.R.layout.simple_spinner_item, productTypeList);
                    sp_type_of_property = (Spinner) findViewById(R.id.type_of_property_spinner);
                    sp_type_of_property.setAdapter(productTypeAdapter);
                    sp_type_of_property.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {

                                    selectedProductType = (ProductType)sp_type_of_property.getSelectedItem();
                                    System.out.println(selectedProductType.getTitle());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub

                                }

                            }
                    );
                }
                else
                {
                    fetchProductTypeCounter++;
                    if (fetchProductTypeCounter <= 5)
                    {
                        fetchProductTypeList();
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
                        Intent create_advert_step1_forward_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep2.class);
                        //setting a default image for the time being
                        product.setImg("a.jpg");
                        create_advert_step1_forward_arrow_intent.putExtra("product",product);
                        startActivity(create_advert_step1_forward_arrow_intent);
                    }
                }
        );
    }

    public void iHaveForRentSpinner(){
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
