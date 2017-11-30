package auction.org.droidflatauction;

import android.app.Dialog;
import android.app.FragmentTransaction;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bdlions.dto.BidTime;
import com.bdlions.dto.Image;
import com.bdlions.dto.ListBidTime;
import com.bdlions.dto.Location;
import com.bdlions.dto.Occupation;
import com.bdlions.dto.Pet;
import com.bdlions.dto.Product;
import com.bdlions.dto.ProductCategory;
import com.bdlions.dto.ProductCategoryList;
import com.bdlions.dto.ProductSize;
import com.bdlions.dto.ProductType;
import com.bdlions.dto.Smoking;
import com.bdlions.dto.SmokingList;
import com.bdlions.dto.Stay;
import com.bdlions.dto.response.SignInResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.List;

public class CreateAdvertStep7 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private static EditText etCreateProductBidStartCalendar,etCreateProductBidEndCalendar;
    private static Spinner bidStartTimer,bidEndTimer;
    ArrayAdapter<CharSequence> bidStartTimerAdapter,bidEndTimerAdapter;
    ArrayAdapter<BidTime> bidStartTimeAdapter,bidEndTimeAdapter;
    Product product;
    String productString;
    SessionManager session;
    NavigationManager navigationManager;

    public Dialog progressBarDialog;

    public List<BidTime> bidTimes = new ArrayList<>();
    public BidTime selectedBidStartTime = new BidTime();
    public BidTime selectedBidEndTime = new BidTime();
    public int fetchProductBidTimeCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step7);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        etCreateProductBidStartCalendar = (EditText) findViewById(R.id.et_create_product_bid_start_calendar);
        etCreateProductBidEndCalendar= (EditText) findViewById(R.id.et_create_product_bid_end_calendar);

        //product = (Product)getIntent().getExtras().get("product");
        try
        {
            String prodString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(prodString, Product.class);

            if(product.getBidStartDate() != null)
            {
                etCreateProductBidStartCalendar.setText(product.getBidStartDate());
            }
            if(product.getBidEndDate() != null)
            {
                etCreateProductBidEndCalendar.setText(product.getBidEndDate());
            }
            progressBarDialog = new Dialog(CreateAdvertStep7.this);
            progressBarDialog.setContentView(R.layout.progressbar);
            progressBarDialog.show();
            fetchBidTimeList();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();

        //fetchBidStartTimerList();
        //fetchBidEndTimerList();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void fetchBidTimeList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_BID_TIME_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ListBidTime listBidTime = null;
                String listBidTimeString = null;
                if(msg != null && msg.obj != null)
                {
                    listBidTimeString = (String) msg.obj;
                }
                if(listBidTimeString != null)
                {
                    Gson gson = new Gson();
                    listBidTime = gson.fromJson(listBidTimeString, ListBidTime.class);
                }
                if(listBidTime != null && listBidTime.isSuccess() && listBidTime.getBidTimes() != null )
                {
                    progressBarDialog.dismiss();
                    bidTimes = listBidTime.getBidTimes();
                    if(bidTimes.size() > 0)
                    {
                        int selectedBidStartTimePosition = 0;
                        int selectedBidEndTimePosition = 0;
                        selectedBidStartTime = bidTimes.get(0);
                        selectedBidEndTime = bidTimes.get(0);
                        if(product.getBidStartTime() == null)
                        {
                            product.setBidStartTime("");
                        }
                        if(product.getBidEndTime() == null)
                        {
                            product.setBidEndTime("");
                        }
                        for(int counter = 0; counter < bidTimes.size(); counter++)
                        {
                            if(product.getBidStartTime().equals(bidTimes.get(counter).getTitle()))
                            {
                                selectedBidStartTime = bidTimes.get(counter);
                                selectedBidStartTimePosition = counter;
                            }
                            if(product.getBidEndTime().equals(bidTimes.get(counter).getTitle()))
                            {
                                selectedBidEndTime = bidTimes.get(counter);
                                selectedBidEndTimePosition = counter;
                            }
                        }

                        bidStartTimer = (Spinner) findViewById(R.id.bid_start_timer_spinner);
                        bidStartTimeAdapter = new ArrayAdapter<BidTime>( CreateAdvertStep7.this, android.R.layout.simple_spinner_item, bidTimes);
                        bidStartTimer.setAdapter(bidStartTimeAdapter);
                        bidStartTimer.setSelection(selectedBidStartTimePosition);
                        bidStartTimer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedBidStartTime = (BidTime) bidStartTimer.getSelectedItem();
                                product.setBidStartTime(selectedBidStartTime.getTitle());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        bidEndTimer = (Spinner) findViewById(R.id.bid_end_timer_spinner);
                        bidEndTimeAdapter = new ArrayAdapter<BidTime>( CreateAdvertStep7.this, android.R.layout.simple_spinner_item, bidTimes);
                        bidEndTimer.setAdapter(bidEndTimeAdapter);
                        bidEndTimer.setSelection(selectedBidEndTimePosition);
                        bidEndTimer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedBidEndTime = (BidTime) bidStartTimer.getSelectedItem();
                                product.setBidEndTime(selectedBidEndTime.getTitle());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    else
                    {
                        //show an error message
                    }
                }
                else
                {
                    fetchProductBidTimeCounter++;
                    if (fetchProductBidTimeCounter <= 5)
                    {
                        fetchBidTimeList();
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


    public void fetchBidStartTimerList(){

    }
    public void fetchBidEndTimerList(){

    }
    public void onStart(){
        super.onStart();

        etCreateProductBidStartCalendar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft,"DatePicker");
                }
            }
        });
        etCreateProductBidStartCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft,"DatePicker");
            }
        });


        etCreateProductBidEndCalendar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft,"DatePicker");
                }
            }
        });
        etCreateProductBidEndCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft,"DatePicker");
            }
        });

    }

    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step7_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(setInputToProduct())
                        {
                            Intent create_advert_step7_back_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep6.class);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String productString = gson.toJson(product);
                            create_advert_step7_back_arrow_intent.putExtra("productString", productString);
                            startActivity(create_advert_step7_back_arrow_intent);
                        }
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton) findViewById(R.id.create_advert_step7_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(setInputToProduct())
                        {
                            Intent create_advert_step7_forward_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep8.class);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String productString = gson.toJson(product);
                            create_advert_step7_forward_arrow_intent.putExtra("productString", productString);
                            startActivity(create_advert_step7_forward_arrow_intent);
                        }
                    }
                }
        );
    }

    public boolean setInputToProduct()
    {
        String bidStartFrom = etCreateProductBidStartCalendar.getText().toString();
        String bidStartTo = etCreateProductBidEndCalendar.getText().toString();

        if(bidStartFrom != null && !bidStartFrom.equals(""))
        {
            bidStartFrom = bidStartFrom.replaceAll("/", "-");
            String[] bidStartFromArray = bidStartFrom.split("-");
            if(bidStartFromArray.length != 3 || bidStartFromArray[2].length() != 4)
            {
                Toast.makeText(getBaseContext(),"Invalid bid Start date. Use mm-dd-yyyy format." , Toast.LENGTH_SHORT).show();
                return false;
            }
            product.setBidStartDate(bidStartFrom);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Bid Start date is required." , Toast.LENGTH_SHORT).show();
            return false;
        }

        if(bidStartTo != null && !bidStartTo.equals(""))
        {
            bidStartTo = bidStartTo.replaceAll("/", "-");
            String[] bidStartToArray = bidStartTo.split("-");
            if(bidStartToArray.length != 3 || bidStartToArray[2].length() != 4)
            {
                Toast.makeText(getBaseContext(),"Invalid bid end date. Use mm-dd-yyyy format." , Toast.LENGTH_SHORT).show();
                return false;
            }
            product.setBidEndDate(bidStartTo);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Bid End date is required." , Toast.LENGTH_SHORT).show();
            return false;
        }
        product.setBidStartTime(bidStartTimer.getSelectedItem().toString());
        product.setBidEndTime(bidEndTimer.getSelectedItem().toString());
        return true;
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
        getMenuInflater().inflate(R.menu.create_advert_step7, menu);
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

        } else if (id == R.id.nav_phone) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
