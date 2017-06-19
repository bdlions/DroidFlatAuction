package auction.org.droidflatauction;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.auction.dto.Product;

public class CreateAdvertStep2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private static Spinner sp_area;
    ArrayAdapter<CharSequence> area_adapter;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        product = (Product)getIntent().getExtras().get("product");

        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
        areaSpinner();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void selectItem( View view){
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.amenities_parking:
                String st_parking = getString(R.string.parking);
                if(checked){
                    //Toast.makeText(getBaseContext(), st_parking + " is selected", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), st_parking + " is deselected", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.amenities_balcony_patio:
                String st_balcony_patio = getString(R.string.balcony_patio);
                if(checked){
                    //Toast.makeText(getBaseContext(), st_balcony_patio + " is selected", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), st_balcony_patio + " is deselected", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.amenities_garden_rootTerrace:
                String st_garden_rootTerrace = getString(R.string.garden_rootTerrace);
                if(checked){
                    //Toast.makeText(getBaseContext(), st_garden_rootTerrace + " is selected", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), st_garden_rootTerrace + " is deselected", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.amenities_disabled_access:
                String st_disabled_access = getString(R.string.disabled_access);
                if(checked){
                    //Toast.makeText(getBaseContext(), st_disabled_access + " is selected", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), st_disabled_access + " is deselected", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.amenities_garage:
                String st_garage = getString(R.string.garage);
                if(checked){
                    //Toast.makeText(getBaseContext(), st_garage + " is selected", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), st_garage + " is deselected", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step2_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent create_advert_step2_back_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep1.class);
                        create_advert_step2_back_arrow_intent.putExtra("product", product);
                        startActivity(create_advert_step2_back_arrow_intent);
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
                        Intent create_advert_step2_forward_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep3.class);
                        create_advert_step2_forward_arrow_intent.putExtra("product", product);
                        startActivity(create_advert_step2_forward_arrow_intent);
                    }
                }
        );
    }
    public void areaSpinner(){
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
            Intent member_logout_intent = new Intent(getBaseContext(), SignIn.class);
            startActivity(member_logout_intent);
        } else if (id == R.id.nav_email) {

        } else if (id == R.id.nav_phone) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
