package auction.org.droidflatauction;

import android.app.Dialog;
import android.app.FragmentTransaction;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bdlions.dto.Amenity;
import com.bdlions.dto.AmenityList;
import com.bdlions.dto.Availability;
import com.bdlions.dto.AvailabilityList;
import com.bdlions.dto.Product;
import com.bdlions.dto.Stay;
import com.bdlions.dto.StayList;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.List;

public class CreateAdvertStep3 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private static Spinner sp_minimum_stay,sp_maximum_stay;
    ArrayAdapter<CharSequence> minimum_stay_adapter,maximum_stay_adapter;
    private static EditText etCreateProductAvailableFrom,etCreateProductAvailableTo;
    private static CheckBox cbManageProductOngoing;


    Product product;
    SessionManager session;
    NavigationManager navigationManager;

    private static Spinner minStaySpinner, maxStaySpinner;

    public List<Stay> stayList = new ArrayList<>();

    ArrayAdapter<Stay> stayAdapter;

    Stay selectedMinStay, selectedMaxStay;

    public int fetchStayCounter = 0, fetchAvailabilityListCounter = 0;

    public Dialog progressBarDialog;
    public ListView listViewAvailablability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        etCreateProductAvailableFrom = (EditText) findViewById(R.id.et_create_product_available_from);
        etCreateProductAvailableTo = (EditText) findViewById(R.id.et_create_product_available_to);
        cbManageProductOngoing = (CheckBox) findViewById(R.id.cb_manage_product_ongoing);


        try {
            String productString = (String) getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, Product.class);

            if(product.getAvailableFrom() != null)
            {
                etCreateProductAvailableFrom.setText(product.getAvailableFrom());
            }
            if(product.getAvailableTo() != null)
            {
                etCreateProductAvailableTo.setText(product.getAvailableTo());
            }
            if(product.isOngoing())
            {
                etCreateProductAvailableTo.setText("");
                cbManageProductOngoing.setChecked(true);
            }
            if(product.getId() == 0 && product.getAvailabilities() == null)
            {
                product.setAvailabilities(new ArrayList<Availability>());
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }


        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
        //minimumStaySpinner();
        //maximumStaySpinner();

        listViewAvailablability = (ListView)findViewById(R.id.availabilities_listView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBarDialog = new Dialog(CreateAdvertStep3.this);
        progressBarDialog.setContentView(R.layout.progressbar);
        progressBarDialog.show();
        fetchAvailabilityList();
    }

    public void fetchAvailabilityList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_AVAILABILITY_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    AvailabilityList availabilityList = null;
                    String availabilityListString = null;
                    if(msg != null  && msg.obj != null)
                    {
                        availabilityListString = (String) msg.obj;
                    }
                    if(availabilityListString != null)
                    {
                        Gson gson = new Gson();
                        availabilityList = gson.fromJson(availabilityListString, AvailabilityList.class);
                    }
                    if(availabilityList != null && availabilityList.isSuccess())
                    {
                        //progressBarDialog.dismiss();
                        ArrayList<Integer> availabilityListId = new ArrayList<Integer>();
                        for(int counter = 0; counter < product.getAvailabilities().size(); counter++)
                        {
                            availabilityListId.add(product.getAvailabilities().get(counter).getId());
                        }
                        final List<DTOAvailablability> availabilities = new ArrayList<>();
                        for(int counter = 0; counter < availabilityList.getAvailabilities().size(); counter++ )
                        {
                            Availability availability = availabilityList.getAvailabilities().get(counter);
                            DTOAvailablability dtoAvailablability = new DTOAvailablability(false,availability.getTitle());
                            if(availabilityListId.contains(availability.getId()))
                            {
                                dtoAvailablability.setSelected(true);
                            }

                            dtoAvailablability.setId(availability.getId());
                            availabilities.add(dtoAvailablability);
                        }

                        final AvailablabilityAdapter availablabilityAdapter = new AvailablabilityAdapter(CreateAdvertStep3.this, availabilities);
                        listViewAvailablability.setAdapter(availablabilityAdapter);
                        listViewAvailablability.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                DTOAvailablability dtoAvailablability = availabilities.get(i);
                                if(dtoAvailablability.isSelected())
                                {
                                    dtoAvailablability.setSelected(false);
                                }
                                else
                                {
                                    dtoAvailablability.setSelected(true);
                                }
                                Availability availability = new Availability();
                                availability.setId(dtoAvailablability.getId());
                                List<Availability> tempAvailabilityList = new ArrayList<Availability>();
                                boolean isExists = false;
                                if(product.getAvailabilities() != null && product.getAvailabilities().size() > 0)
                                {
                                    for (int counter = 0; counter < product.getAvailabilities().size(); counter++)
                                    {
                                        if (product.getAvailabilities().get(counter).getId() == availability.getId())
                                        {
                                            isExists = true;
                                        }
                                        else
                                        {
                                            tempAvailabilityList.add(product.getAvailabilities().get(counter));
                                        }
                                    }
                                }
                                if (!isExists)
                                {
                                    tempAvailabilityList.add(availability);
                                }
                                product.setAvailabilities(tempAvailabilityList);
                                availabilities.set(i,dtoAvailablability);
                                availablabilityAdapter.updateRecords(availabilities);
                            }
                        });
                        fetchStayList();
                    }
                    else
                    {
                        fetchAvailabilityListCounter++;
                        if (fetchAvailabilityListCounter <= 5)
                        {
                            fetchAvailabilityList();
                        }
                        else
                        {
                            //toast error message
                            progressBarDialog.dismiss();
                        }
                    }
                }
                catch(Exception ex)
                {
                    fetchAvailabilityListCounter++;
                    if (fetchAvailabilityListCounter <= 5)
                    {
                        fetchAvailabilityList();
                    }
                    else
                    {
                        //toast error message
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }


    public void onStart(){
        super.onStart();
        etCreateProductAvailableFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft,"DatePicker");
                }
            }
        });

        etCreateProductAvailableFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft,"DatePicker");
            }
        });


        etCreateProductAvailableTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft,"DatePicker");
                }
            }
        });

        etCreateProductAvailableTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft,"DatePicker");
            }
        });
    }
    public void ongoingCheckBoxClicked( View view){
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            etCreateProductAvailableTo.setText("");
            product.setAvailableTo("");
            product.setOngoing(true);
        }
        else
        {
            product.setOngoing(false);
        }
    }

    public void fetchStayList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_STAY_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                StayList pStayList = null;
                String stayListString = null;
                if(msg != null && msg.obj != null)
                {
                    stayListString = (String) msg.obj;
                }
                if(stayListString != null)
                {
                    Gson gson = new Gson();
                    pStayList = gson.fromJson(stayListString, StayList.class);
                }
                if(pStayList != null && pStayList.isSuccess() && pStayList.getStays() != null )
                {
                    stayList = pStayList.getStays();
                    int selectedMinStayPosition = 0;
                    if(product != null && product.getId() == 0 && product.getMinStay() == null && stayList.size() > 0)
                    {
                        product.setMinStay(stayList.get(0));
                    }
                    else
                    {
                        int minStayCounter = stayList.size();
                        for(int counter = 0; counter < minStayCounter; counter++ )
                        {
                            if(stayList.get(counter).getId() == product.getMinStay().getId())
                            {
                                selectedMinStay = stayList.get(counter);
                                selectedMinStayPosition = counter;
                                break;
                            }
                        }
                    }
                    int selectedMaxStayPosition = 0;
                    if(product != null && product.getId() == 0 && product.getMaxStay() == null && stayList.size() > 0)
                    {
                        product.setMaxStay(stayList.get(0));
                    }
                    else
                    {
                        int maxStayCounter = stayList.size();
                        for(int counter = 0; counter < maxStayCounter; counter++ )
                        {
                            if(stayList.get(counter).getId() == product.getMaxStay().getId())
                            {
                                selectedMaxStay = stayList.get(counter);
                                selectedMaxStayPosition = counter;
                                break;
                            }
                        }
                    }

                    stayAdapter = new ArrayAdapter<Stay>( CreateAdvertStep3.this, android.R.layout.simple_spinner_item, stayList);
                    stayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    minStaySpinner = (Spinner) findViewById(R.id.minimum_stay_spinner);
                    minStaySpinner.setAdapter(stayAdapter);
                    if(selectedMinStay != null)
                    {
                        minStaySpinner.setSelection(selectedMinStayPosition);
                    }
                    minStaySpinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                {
                                    selectedMinStay = (Stay)minStaySpinner.getSelectedItem();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub

                                }
                            }
                    );
                    maxStaySpinner = (Spinner) findViewById(R.id.maximum_stay_spinner);
                    maxStaySpinner.setAdapter(stayAdapter);
                    if(selectedMaxStay != null){
                        maxStaySpinner.setSelection(selectedMaxStayPosition);
                    }
                    maxStaySpinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                {
                                    selectedMaxStay = (Stay)maxStaySpinner.getSelectedItem();
                                    System.out.println(selectedMaxStay.getTitle());
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
                    fetchStayCounter++;
                    if (fetchStayCounter <= 5)
                    {
                        fetchStayList();
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
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step3_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(setInputToProduct())
                        {
                            Intent create_advert_step3_back_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep2.class);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String productString = gson.toJson(product);
                            create_advert_step3_back_arrow_intent.putExtra("productString", productString);
                            startActivity(create_advert_step3_back_arrow_intent);
                        }
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton) findViewById(R.id.create_advert_step3_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(setInputToProduct())
                        {
                            Intent create_advert_step3_forward_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep4.class);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String productString = gson.toJson(product);
                            create_advert_step3_forward_arrow_intent.putExtra("productString", productString);
                            startActivity(create_advert_step3_forward_arrow_intent);
                        }
                    }
                }
        );
    }

    public boolean setInputToProduct()
    {
        String availableFrom = etCreateProductAvailableFrom.getText().toString();
        String availableTo = etCreateProductAvailableTo.getText().toString();
        if(availableFrom != null && !availableFrom.equals(""))
        {
            availableFrom = availableFrom.replaceAll("/", "-");
            String[] availableFromArray = availableFrom.split("-");
            if(availableFromArray.length != 3 || availableFromArray[2].length() != 4)
            {
                Toast.makeText(getBaseContext(),"Invalid available from date. Use mm-dd-yyyy format." , Toast.LENGTH_SHORT).show();
                return false;
            }
            product.setAvailableFrom(availableFrom);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Available from date is required" , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!product.isOngoing())
        {
            if(availableTo != null && !availableTo.equals(""))
            {
                availableTo = availableTo.replaceAll("/", "-");
                String[] availableToArray = availableTo.split("-");
                if(availableToArray.length != 3 || availableToArray[2].length() != 4)
                {
                    Toast.makeText(getBaseContext(),"Invalid available to date. Use mm-dd-yyyy format." , Toast.LENGTH_SHORT).show();
                    return false;
                }
                product.setAvailableTo(availableTo);
            }
            else
            {
                Toast.makeText(getBaseContext(),"Available to date is required." , Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(selectedMinStay != null)
        {
            product.setMinStay(selectedMinStay);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Min Stay is required." , Toast.LENGTH_SHORT).show();
            return false;
        }

        if(selectedMaxStay != null)
        {
            product.setMaxStay(selectedMaxStay);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Max Stay is required." , Toast.LENGTH_SHORT).show();
            return false;
        }
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
        getMenuInflater().inflate(R.menu.create_advert_step3, menu);
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
