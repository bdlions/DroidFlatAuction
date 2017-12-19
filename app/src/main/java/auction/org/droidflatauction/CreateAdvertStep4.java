package auction.org.droidflatauction;

import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bdlions.dto.Occupation;
import com.bdlions.dto.OccupationList;
import com.bdlions.dto.Pet;
import com.bdlions.dto.PetList;
import com.bdlions.dto.Product;
import com.bdlions.dto.ProductCategory;
import com.bdlions.dto.ProductCategoryList;
import com.bdlions.dto.ProductSize;
import com.bdlions.dto.ProductType;
import com.bdlions.dto.Smoking;
import com.bdlions.dto.SmokingList;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.List;

public class CreateAdvertStep4 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;

    private static Spinner sp_smoking,sp_occupation,sp_pets;
    private static Spinner smokingSpinner,petSpinner, occupationSpinner;

    ArrayAdapter<CharSequence> smoking_adapter,occupation_adapter,pets_adapter;
    Product product;
    SessionManager session;
    NavigationManager navigationManager;

    ArrayAdapter<Smoking> smokingAdapter;
    ArrayAdapter<Pet> petAdapter;
    ArrayAdapter<Occupation> occupationAdapter;

    public List<Smoking> smokingList = new ArrayList<>();
    public List<Pet> petList = new ArrayList<>();
    public List<Occupation> occupationList = new ArrayList<>();

    Smoking selectedSmoking;
    Pet selectedPet;
    Occupation selectedOccupation;

    public int fetchSmokingCounter = 0;
    public int fetchPetCounter = 0;
    public int fetchOccupationCounter = 0;

    public Dialog progressBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        //product = (Product)getIntent().getExtras().get("product");
        try
        {
            String productString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, Product.class);
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
        //smokingSpinner();
        //occupationSpinner();
        //petsSpinner();
        progressBarDialog = new Dialog(CreateAdvertStep4.this);
        progressBarDialog.setContentView(R.layout.progressbar);
        progressBarDialog.show();
        fetchSmokingList();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void fetchSmokingList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_SMOKING_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                SmokingList pSmokingList = null;
                String smokingListString = null;
                if(msg != null && msg.obj != null)
                {
                    smokingListString = (String) msg.obj;
                }
                if(smokingListString != null)
                {
                    Gson gson = new Gson();
                    pSmokingList = gson.fromJson(smokingListString, SmokingList.class);
                }
                if(pSmokingList != null && pSmokingList.isSuccess() && pSmokingList.getSmokings() != null )
                {
                    smokingList = pSmokingList.getSmokings();
                    int selectedSmokingPosition = 0;

                    if(product != null && product.getId() == 0 && product.getSmoking() == null && smokingList.size() > 0)
                    {
                        product.setSmoking(smokingList.get(0));
                    }
                    else
                    {
                        int smokingCounter = smokingList.size();
                        for(int counter = 0; counter < smokingCounter; counter++ )
                        {
                            if(smokingList.get(counter).getId() == product.getSmoking().getId())
                            {
                                selectedSmoking = smokingList.get(counter);
                                selectedSmokingPosition = counter;
                                break;
                            }
                        }
                    }

                    smokingAdapter = new ArrayAdapter<Smoking>( CreateAdvertStep4.this, android.R.layout.simple_spinner_item, smokingList);
                    smokingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    smokingSpinner = (Spinner) findViewById(R.id.smoking_spinner);
                    smokingSpinner.setAdapter(smokingAdapter);
                    if(selectedSmoking != null)
                    {
                        smokingSpinner.setSelection(selectedSmokingPosition);
                    }
                    smokingSpinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                {
                                    selectedSmoking = (Smoking)smokingSpinner.getSelectedItem();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            }
                    );
                    fetchOccupationList();
                }
                else
                {
                    fetchSmokingCounter++;
                    if (fetchSmokingCounter <= 5)
                    {
                        fetchSmokingList();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    public void fetchOccupationList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_OCCUPATION_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                OccupationList pOccupationList = null;
                String occupationListString = null;
                if(msg != null && msg.obj != null)
                {
                    occupationListString = (String) msg.obj;
                }
                if(occupationListString != null)
                {
                    Gson gson = new Gson();
                    pOccupationList = gson.fromJson(occupationListString, OccupationList.class);
                }
                if(pOccupationList != null && pOccupationList.isSuccess() && pOccupationList.getOccupations() != null )
                {
                    occupationList = pOccupationList.getOccupations();
                    int selectedOccupationPosition = 0;
                    if(product != null && product.getId() == 0 && product.getOccupation() == null && occupationList.size() > 0)
                    {
                        product.setOccupation(occupationList.get(0));
                    }
                    else
                    {
                        int occupationCounter = occupationList.size();
                        for(int counter = 0; counter < occupationCounter; counter++ )
                        {
                            if(occupationList.get(counter).getId() == product.getOccupation().getId())
                            {
                                selectedOccupation = occupationList.get(counter);
                                selectedOccupationPosition = counter;
                                break;
                            }
                        }
                    }

                    occupationAdapter = new ArrayAdapter<Occupation>( CreateAdvertStep4.this, android.R.layout.simple_spinner_item, occupationList);
                    occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    occupationSpinner = (Spinner) findViewById(R.id.occupation_spinner);
                    occupationSpinner.setAdapter(occupationAdapter);
                    if(selectedOccupation != null)
                    {
                        occupationSpinner.setSelection(selectedOccupationPosition);
                    }
                    occupationSpinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                {
                                    selectedOccupation = (Occupation)occupationSpinner.getSelectedItem();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            }
                    );
                    fetchPetList();
                }
                else
                {
                    fetchOccupationCounter++;
                    if (fetchOccupationCounter <= 5)
                    {
                        fetchOccupationList();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });
    }

    public void fetchPetList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_PET_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                PetList pPetList = null;
                String petListString = null;
                if(msg != null && msg.obj != null)
                {
                    petListString = (String) msg.obj;
                }
                if(petListString != null)
                {
                    Gson gson = new Gson();
                    pPetList = gson.fromJson(petListString, PetList.class);
                }
                if(pPetList != null && pPetList.isSuccess() && pPetList.getPets() != null )
                {
                    petList = pPetList.getPets();
                    int selectedPetPosition = 0;
                    if(product != null && product.getId() == 0 && product.getPet() == null&& petList.size() > 0)
                    {
                        product.setPet(petList.get(0));
                    }
                    else
                    {
                        int petCounter = petList.size();
                        for(int counter = 0; counter < petCounter; counter++ )
                        {
                            if(petList.get(counter).getId() == product.getPet().getId())
                            {
                                selectedPet = petList.get(counter);
                                selectedPetPosition = counter;
                                break;
                            }
                        }
                    }

                    petAdapter = new ArrayAdapter<Pet>( CreateAdvertStep4.this, android.R.layout.simple_spinner_item, petList);
                    petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    petSpinner = (Spinner) findViewById(R.id.pets_spinner);
                    petSpinner.setAdapter(petAdapter);
                    if(selectedPet != null)
                    {
                        petSpinner.setSelection(selectedPetPosition);
                    }
                    petSpinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                {
                                    selectedPet = (Pet)petSpinner.getSelectedItem();
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
                    fetchPetCounter++;
                    if (fetchPetCounter <= 5)
                    {
                        fetchPetList();
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
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step4_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setInputToProduct();
                        Intent manageAdvertStep4BackArrowIntent = new Intent(getBaseContext(), CreateAdvertStep3.class);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String productString = gson.toJson(product);
                        manageAdvertStep4BackArrowIntent.putExtra("productString", productString);
                        startActivity(manageAdvertStep4BackArrowIntent);
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton) findViewById(R.id.create_advert_step4_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(validateInputProduct())
                        {
                            setInputToProduct();
                            Intent manageAdvertStep4ForwardArrowIntent = new Intent(getBaseContext(), CreateAdvertStep5.class);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String productString = gson.toJson(product);
                            manageAdvertStep4ForwardArrowIntent.putExtra("productString", productString);
                            startActivity(manageAdvertStep4ForwardArrowIntent);
                        }
                    }
                }
        );
    }

    //validating input fields
    public boolean validateInputProduct()
    {
        if(selectedSmoking == null)
        {
            Toast.makeText(getBaseContext(),"Smoking is required." , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(selectedOccupation == null)
        {
            Toast.makeText(getBaseContext(),"Occupation is required." , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(selectedPet == null)
        {
            Toast.makeText(getBaseContext(),"Pet is required." , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //setting input fields into product info
    public void setInputToProduct()
    {
        product.setSmoking(selectedSmoking);
        product.setOccupation(selectedOccupation);
        product.setPet(selectedPet);
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
        getMenuInflater().inflate(R.menu.create_advert_step4, menu);
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
