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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.auction.dto.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CreateAdvertStep5 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private static EditText etManageProductTitle, etManageProductDescription, etManageProductUserFirstName,etManageProductUserLastName, etManageProductCompany, etManageProductPhone;
    Product product;
    SessionManager session;
    NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert_step5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        etManageProductTitle = (EditText) findViewById(R.id.et_create_product_title);
        etManageProductDescription = (EditText) findViewById(R.id.et_create_product_description);
        //etManageProductName = (EditText) findViewById(R.id.et_manage_product_name);
        etManageProductUserFirstName = (EditText) findViewById(R.id.et_manage_product_user_first_name);
        etManageProductUserLastName = (EditText) findViewById(R.id.et_manage_product_user_last_name);
        etManageProductCompany = (EditText) findViewById(R.id.et_manage_product_company);
        etManageProductPhone = (EditText) findViewById(R.id.et_manage_product_phone);

        //product = (Product)getIntent().getExtras().get("product");
        try
        {
            String productString = (String)getIntent().getExtras().get("productString");
            Gson gson = new Gson();
            product = gson.fromJson(productString, Product.class);

            if(product.getTitle() != null)
            {
                etManageProductTitle.setText(product.getTitle());
            }
            if(product.getDescription() != null)
            {
                etManageProductDescription.setText(product.getDescription());
            }
            /*if(product.getFirstName() != null && product.getLastName() != null)
            {
                etManageProductName.setText(product.getFirstName() + product.getLastName());
            }*/
            if(product.getFirstName() != null)
            {
                etManageProductUserFirstName.setText(product.getFirstName());
            }
            if(product.getLastName() != null)
            {
                etManageProductUserLastName.setText(product.getLastName());
            }
            if(product.getCompanyName() != null)
            {
                etManageProductCompany.setText(product.getCompanyName());
            }
            if(product.getPhone() != null)
            {
                etManageProductPhone.setText(product.getPhone());
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }



        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton) findViewById(R.id.create_advert_step5_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent create_advert_step5_back_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep4.class);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String productString = gson.toJson(product);
                        create_advert_step5_back_arrow_intent.putExtra("productString", productString);
                        startActivity(create_advert_step5_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton) findViewById(R.id.create_advert_step5_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        product.setTitle(etManageProductTitle.getText().toString());
                        if(product.getTitle() == null || product.getTitle().equals(""))
                        {
                            Toast.makeText(getBaseContext(),"Product Title can't be Empty" , Toast.LENGTH_SHORT).show();
                        }

                        product.setDescription(etManageProductDescription.getText().toString());
                        if(product.getTitle() == null || product.getTitle().equals(""))
                        {
                            Toast.makeText(getBaseContext(),"Product Description can't be Empty" , Toast.LENGTH_SHORT).show();
                        }

                        product.setFirstName(etManageProductUserFirstName.getText().toString());
                        if(product.getFirstName() == null || product.getFirstName().equals(""))
                        {
                            Toast.makeText(getBaseContext(),"First Name can't be Empty" , Toast.LENGTH_SHORT).show();
                        }

                        product.setLastName(etManageProductUserLastName.getText().toString());
                        if(product.getLastName() == null || product.getLastName().equals("") )
                        {
                            Toast.makeText(getBaseContext(),"Last Name can't be Empty" , Toast.LENGTH_SHORT).show();
                        }

                        product.setCompanyName(etManageProductCompany.getText().toString());
                        product.setPhone(etManageProductPhone.getText().toString());

                        Intent create_advert_step5_forward_arrow_intent = new Intent(getBaseContext(), CreateAdvertStep6.class);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String productString = gson.toJson(product);

                        create_advert_step5_forward_arrow_intent.putExtra("productString", productString);
                        startActivity(create_advert_step5_forward_arrow_intent);
                    }
                }
        );
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
        getMenuInflater().inflate(R.menu.create_advert_step5, menu);
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
