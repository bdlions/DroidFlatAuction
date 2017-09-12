package auction.org.droidflatauction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.auction.dto.Product;
import com.auction.dto.ProductList;
import com.auction.dto.SearchParams;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MemberPropertySearch extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String[] items;
    ArrayList<String> listitems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;
    SessionManager session;
    SearchParams searchParams = new SearchParams();
    public int fetchProductListCounter = 0;

    public Dialog progressBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_property_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Session Manager
        session = new SessionManager(getApplicationContext());

        try
        {
            items = (String[])getIntent().getExtras().get("items");
        }
        catch(Exception ex)
        {

        }

        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.txtsearch);
        initList();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int i2) {
                if(s.toString().equals("")){
                    initList();
                }
                else {
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
    public void searchItem(String textToSearch){
        for(String item : items){
            if(!item.contains(textToSearch)){
            listitems.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

public void initList(){
    //items = new String[]{"London 123","London 124","London 125","London 126","London 127", "NewWork 123","NewWork 124","NewWork 125","NewWork 126","NewWork 127"};
    listitems = new ArrayList<>(Arrays.asList(items));
    adapter = new ArrayAdapter<String>(this, R.layout.search_property_place_row, R.id.texitem, listitems);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            progressBarDialog = new Dialog(MemberPropertySearch.this);
            progressBarDialog.setContentView(R.layout.progressbar);
            progressBarDialog.show();

            fetchProductList();
        }
    });
}

    public void fetchProductList()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String searchParamsString = gson.toJson(searchParams);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_PRODUCT_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, searchParamsString, new Handler(){
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
                            imgList.add(product.getImg());
                            imageList.add(R.drawable.property_image_01);
                            titleList.add(product.getTitle());
                            bedroomList.add("");
                            bathroomList.add("");
                            priceList.add("");
                        }
                    }
                    Intent adverts_intent = new Intent(getBaseContext(), MemberPropertySearchProduct.class);
                    //Intent adverts_intent = new Intent(getBaseContext(), SavedAdvertStep1.class);
                    adverts_intent.putExtra("imageList", imageList);
                    adverts_intent.putExtra("imgList", imgList);
                    adverts_intent.putExtra("productIdList", productIdList);
                    adverts_intent.putExtra("titleList", titleList);
                    adverts_intent.putExtra("bedroomList", bedroomList);
                    adverts_intent.putExtra("bathroomList", bathroomList);
                    adverts_intent.putExtra("priceList", priceList);
                    startActivity(adverts_intent);
                }
                catch(Exception ex)
                {
                    fetchProductListCounter++;
                    if (fetchProductListCounter <= Constants.MAX_REPEAT_SERVER_REQUEST)
                    {
                        fetchProductList();
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
        getMenuInflater().inflate(R.menu.member_property_search, menu);

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
