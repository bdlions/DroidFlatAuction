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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.bdlions.dto.response.ClientListResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.auction.udp.BackgroundWork;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateAdvertStep2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private static Spinner sp_area;
    private static EditText etManageProductPrice;

    ArrayAdapter<EntityLocation> locationAdapter;
    ArrayAdapter<CharSequence> area_adapter;

    private static Spinner locationSpinner;

    public List<EntityLocation> locationList = new ArrayList<>();

    EntityLocation selectedLocation;

    public ListView listViewAmenity;
    EntityProduct product;
    SessionManager session;
    NavigationManager navigationManager;
    public Dialog progressBarDialog;
    public int fetchLocationCounter = 0;
    public int fetchAmenityListCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        etManageProductPrice = (EditText) findViewById(R.id.et_manage_product_price);

        try
        {
            String productString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, EntityProduct.class);
            etManageProductPrice.setText(product.getBasePrice()+"");
            /*if(product.getId() == 0 && product.getAmenities() == null)
            {
                product.setAmenities(new ArrayList<Amenity>());
            }*/
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }



        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
        //areaSpinner();

        listViewAmenity = (ListView)findViewById(R.id.amenities_listView);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBarDialog = new Dialog(CreateAdvertStep2.this);
        progressBarDialog.setContentView(R.layout.progressbar);
        progressBarDialog.show();

        fetchLocationList();

        //this.fetchAmenityList();
    }

    public void fetchLocationList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_LOCATION_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {

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
                        progressBarDialog.dismiss();
                        try
                        {
                            JSONObject obj = new JSONObject(clientListResponseString);
                            locationList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<EntityLocation>>(){}.getType());
                            if(locationList == null)
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

                        int selectedLocationPosition = 0;
                        //setting default product category
                        if(product != null && product.getId() == 0 && product.getLocationId() == 0 && locationList.size() > 0)
                        {
                            product.setLocationId(locationList.get(0).getId());
                            product.setLocationTitle(locationList.get(0).getSearchString());
                            product.setPostcode(locationList.get(0).getPostcode());
                            product.setLat(locationList.get(0).getLat());
                            product.setLon(locationList.get(0).getLon());
                        }
                        //setting product selected category
                        else
                        {
                            int locationCounter = locationList.size();
                            for(int counter = 0; counter < locationCounter; counter++ )
                            {
                                if(locationList.get(counter).getId() == product.getLocationId())
                                {
                                    selectedLocation = locationList.get(counter);
                                    selectedLocationPosition = counter;
                                    break;
                                }
                            }
                        }

                        locationAdapter = new ArrayAdapter<EntityLocation>( CreateAdvertStep2.this, android.R.layout.simple_spinner_item, locationList);
                        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        locationSpinner = (Spinner) findViewById(R.id.area_spinner);
                        locationSpinner.setAdapter(locationAdapter);
                        if(selectedLocation != null)
                        {
                            locationSpinner.setSelection(selectedLocationPosition);
                        }
                        locationSpinner.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                    {
                                        selectedLocation = (EntityLocation) locationSpinner.getSelectedItem();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub
                                    }
                                }
                        );
                        fetchAmenityList();
                    }
                    else
                    {
                        fetchLocationCounter++;
                        if (fetchLocationCounter <= 5)
                        {
                            fetchLocationList();
                        }
                        else
                        {
                            progressBarDialog.dismiss();
                        }
                    }
                }
                catch(Exception ex)
                {
                    //System.out.println(ex.toString());
                    if (fetchLocationCounter <= 5)
                    {
                        fetchLocationList();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    public void fetchAmenityList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_AMENITY_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    List<EntityAmenity> amenityList = null;
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
                            amenityList = gson.fromJson(obj.get("list").toString(), new TypeToken<List<EntityAmenity>>(){}.getType());
                            if(amenityList == null)
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

                        progressBarDialog.dismiss();
                        final ArrayList<Integer> amenityListId = new ArrayList<Integer>();
                        String productAmenityIds = product.getAmenityIds();
                        if(productAmenityIds != null && !productAmenityIds.equals(""))
                        {
                            String[] productAmenityIdArray = productAmenityIds.split(",");
                            for(int counter = 0; counter < productAmenityIdArray.length; counter++)
                            {
                                try
                                {
                                    amenityListId.add(Integer.parseInt(productAmenityIdArray[counter]));
                                }
                                catch(Exception ex)
                                {

                                }
                            }
                        }

                        final List<DTOAmenity> amenities = new ArrayList<>();
                        for(int counter = 0; counter < amenityList.size(); counter++ )
                        {
                            EntityAmenity amenity = amenityList.get(counter);
                            DTOAmenity dtoAmenity = new DTOAmenity(false,amenity.getTitle());
                            if(amenityListId.contains(amenity.getId()))
                            {
                                dtoAmenity.setSelected(true);
                            }

                            dtoAmenity.setId(amenity.getId());
                            amenities.add(dtoAmenity);
                        }

                        final AmenityAdapter amenityAdapter = new AmenityAdapter(CreateAdvertStep2.this, amenities);
                        listViewAmenity.setAdapter(amenityAdapter);
                        listViewAmenity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                DTOAmenity dtoAmenity = amenities.get(i);
                                if(dtoAmenity.isSelected())
                                {
                                    dtoAmenity.setSelected(false);
                                }
                                else
                                {
                                    dtoAmenity.setSelected(true);
                                }

                                List<String> tempAmenityIdList = new ArrayList<String>();
                                List<String> tempAmenityTitleList = new ArrayList<String>();
                                String amenityIds = product.getAmenityIds();
                                String amenityTitles = product.getAmenityTitles();
                                boolean isExists = false;
                                if(amenityIds != null && !amenityIds.equals("") && amenityTitles != null && !amenityTitles.equals(""))
                                {
                                    String[] amenityIdArray = amenityIds.split(",");
                                    String[] amenityTitleArray = amenityTitles.split(",");
                                    if(amenityIdArray != null && amenityIdArray.length > 0 && amenityTitleArray != null && amenityTitleArray.length > 0)
                                    {
                                        for(int counter = 0; counter < amenityIdArray.length; counter++)
                                        {
                                            int tempId = 0;
                                            try
                                            {
                                                tempId = Integer.parseInt(amenityIdArray[counter]);
                                            }
                                            catch(Exception ex)
                                            {

                                            }
                                            if(tempId == dtoAmenity.getId())
                                            {
                                                isExists = true;
                                            }
                                            else
                                            {
                                                if(counter < amenityTitleArray.length)
                                                {
                                                    tempAmenityIdList.add(amenityIdArray[counter]);
                                                    tempAmenityTitleList.add(amenityTitleArray[counter]);
                                                }

                                            }
                                        }
                                    }
                                }
                                if(!isExists)
                                {
                                    tempAmenityIdList.add(dtoAmenity.getId()+"");
                                    tempAmenityTitleList.add(dtoAmenity.getTitle());
                                }
                                String amenityIdString = "";
                                String amenityTitleString = "";
                                for(int counter = 0; counter < tempAmenityIdList.size(); counter++)
                                {
                                    if(counter > 0)
                                    {
                                        amenityIdString = amenityIdString + "," + tempAmenityIdList.get(counter) ;
                                        amenityTitleString = amenityTitleString + "," + tempAmenityTitleList.get(counter);
                                    }
                                    else
                                    {
                                        amenityIdString = tempAmenityIdList.get(counter);
                                        amenityTitleString = tempAmenityTitleList.get(counter);
                                    }
                                }
                                product.setAmenityIds(amenityIdString);
                                product.setAmenityTitles(amenityTitleString);
                                amenities.set(i,dtoAmenity);
                                amenityAdapter.updateRecords(amenities);
                            }
                        });
                    }
                    else
                    {
                        fetchAmenityListCounter++;
                        if (fetchAmenityListCounter <= 5)
                        {
                            fetchAmenityList();
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
                    fetchAmenityListCounter++;
                    if (fetchAmenityListCounter <= 5)
                    {
                        fetchAmenityList();
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

    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step2_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setInputToProduct();
                        Intent manageAdvertStep2BackArrowIntent = new Intent(getBaseContext(), CreateAdvertStep1.class);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String productString = gson.toJson(product);
                        manageAdvertStep2BackArrowIntent.putExtra("productString", productString);
                        startActivity(manageAdvertStep2BackArrowIntent);
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton) findViewById(R.id.create_advert_step2_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(validateInputProduct())
                        {
                            setInputToProduct();
                            Intent manageAdvertStep2ForwardArrowIntent = new Intent(getBaseContext(), CreateAdvertStep3.class);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String productString = gson.toJson(product);
                            manageAdvertStep2ForwardArrowIntent.putExtra("productString", productString);
                            startActivity(manageAdvertStep2ForwardArrowIntent);
                        }
                    }
                }
        );
    }

    //validating input fields
    public boolean validateInputProduct()
    {
        boolean isValid = true;
        String strAssignedPrice = etManageProductPrice.getText().toString();
        try
        {
            double basePrice = Double.parseDouble(strAssignedPrice);
            if (basePrice <= 0.0) {
                Toast.makeText(getBaseContext(),"Invalid price " +strAssignedPrice, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(getBaseContext(),"Invalid price " + strAssignedPrice, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(selectedLocation == null)
        {
            Toast.makeText(getBaseContext(),"Location is required." , Toast.LENGTH_SHORT).show();
            return false;
        }

        return isValid;
    }

    //setting input fields into product info
    public void setInputToProduct()
    {
        String strAssignedPrice = etManageProductPrice.getText().toString();
        try
        {
            double basePrice = Double.parseDouble(strAssignedPrice);
            product.setBasePrice(basePrice);
        }
        catch(Exception ex)
        {
            Toast.makeText(getBaseContext(),"Invalid price " + strAssignedPrice, Toast.LENGTH_SHORT).show();
        }

        product.setLocationId(selectedLocation.getId());
        product.setLocationTitle(selectedLocation.getSearchString());
        product.setPostcode(selectedLocation.getPostcode());
        product.setLat(selectedLocation.getLat());
        product.setLon(selectedLocation.getLon());
    }

    /*public void areaSpinner(){
        sp_area = (Spinner) findViewById(R.id.area_spinner);
        area_adapter = ArrayAdapter.createFromResource(this,R.array.area_spinner_options,android.R.layout.simple_spinner_item);
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_area.setAdapter(area_adapter);
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        getMenuInflater().inflate(R.menu.create_advert_step2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //  if (id == R.id.action_settings) {
        //      return true;
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
