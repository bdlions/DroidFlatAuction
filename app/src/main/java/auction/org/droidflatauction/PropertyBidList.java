
package auction.org.droidflatauction;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import com.auction.dto.Product;
import com.auction.dto.ProductBid;
import com.auction.dto.ProductBidList;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundWork;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyBidList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static ImageView ivPropertyImage;
    private static TextView tvProductTitle,tvProductPrice, tvProductTotalBids;
    String bidder[] = {
            "Nazmul hasan","Alamgir Kabir",
            "Nazmul hasan","Alamgir Kabir",
            "Nazmul hasan","Alamgir Kabir",
            "Nazmul hasan","Alamgir Kabir",
            "Nazmul hasan","Alamgir Kabir"};
    String bid_amount[]       =  {
            "$10","$20",
            "$30","$40",
            "$50","$60",
            "$70","$80",
            "$90","$100",
    };
    String bid_time[]       =  {
            "21 Apr 2017 9:38:35AM","22 Apr 2017 9:38:35AM",
            "23 Apr 2017 9:38:35AM","24 Apr 2017 9:38:35AM",
            "25 Apr 2017 9:38:35AM","26 Apr 2017 9:38:35AM",
            "27 Apr 2017 9:38:35AM","28 Apr 2017 9:38:35AM",
            "29 Apr 2017 9:38:35AM","30 Apr 2017 9:38:35AM",
    };

    TableLayout tl;
    TableRow tr;
    TextView bidder_name,price_list, time_list;
    SessionManager session;

    public int fetchProductInfoCounter = 0;
    public int fetchBidListCounter = 0;
    Product product;
    HashMap<Integer, String> userIdNameMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_bid_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        ivPropertyImage = (ImageView)findViewById(R.id.property_image);
        tvProductTitle = (TextView) findViewById(R.id.property_title);
        tvProductPrice = (TextView) findViewById(R.id.property_price);
        tvProductTotalBids = (TextView) findViewById(R.id.tv_product_bid_list_total_bids);
        try
        {
            int productId = getIntent().getExtras().getInt("productId");
            product = new Product();
            product.setId(productId);
            fetchProductInfo();
        }
        catch(Exception ex)
        {
            //handle exception
        }

        tl = (TableLayout) findViewById(R.id.bidder_list);
        addHeaders();
        //addData();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void fetchProductInfo()
    {
        Product tempProduct = new Product();
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
                    Product responseProduct = null;
                    String productInfoString = null;
                    if(msg != null && msg.obj != null)
                    {
                        productInfoString = (String) msg.obj;
                    }
                    if(productInfoString != null)
                    {
                        Gson gson = new Gson();
                        responseProduct = gson.fromJson(productInfoString, Product.class);
                    }
                    if(responseProduct != null && responseProduct.isSuccess() && responseProduct.getId() > 0 )
                    {
                        product = responseProduct;
                        //set product info into interface
                        tvProductTitle.setText(product.getTitle());
                        tvProductPrice.setText(product.getBasePrice()+" £");
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.productImagePath_328_212+product.getImg()).into(ivPropertyImage);
                        tvProductTotalBids.setText(product.getTotalBids()+"");

                        //call server to get bid list
                        fetchBidList();
                    }
                    else
                    {
                        fetchProductInfoCounter++;
                        if (fetchProductInfoCounter <= 5)
                        {
                            fetchProductInfo();
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
                }
            }
        });
    }

    public void fetchBidList()
    {
        Product tempProduct = new Product();
        tempProduct.setId(product.getId());
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String tempProductString = gson.toJson(tempProduct);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_BID_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, tempProductString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    ProductBidList productBidList = null;
                    String bidListString = null;
                    if(msg != null && msg.obj != null)
                    {
                        bidListString = (String) msg.obj;
                    }
                    if(bidListString != null)
                    {
                        Gson gson = new Gson();
                        productBidList = gson.fromJson(bidListString, ProductBidList.class);
                    }
                    if(productBidList != null && productBidList.isSuccess())
                    {
                        //set bid list
                        ArrayList<ProductBid> bidList = productBidList.getProductBidList();
                        showBidList(bidList);
                    }
                    else
                    {
                        fetchBidListCounter++;
                        if (fetchBidListCounter <= 5)
                        {
                            fetchBidList();
                        }
                    }
                }
                catch(Exception ex)
                {
                    System.out.println(ex.toString());
                    fetchBidListCounter++;
                    if (fetchBidListCounter <= 5)
                    {
                        fetchBidList();
                    }
                }
            }
        });
    }

    /** This function add the headers to the table **/
    public void addHeaders(){

        /** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView bidder = new TextView(this);
        bidder.setText("Bidder");
        bidder.setTextColor(Color.parseColor("#f06226"));
        bidder.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        bidder.setPadding(5, 5, 5, 0);
        tr.addView(bidder);  // Adding textView to tablerow.


        /** Creating another textview **/
        TextView amount = new TextView(this);
        amount.setText("Bid Amount");
        amount.setTextColor(Color.parseColor("#f06226"));
        amount.setPadding(5, 5, 5, 0);
        amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(amount); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView time = new TextView(this);
        time.setText("Bid Time");
        time.setTextColor(Color.parseColor("#f06226"));
        time.setPadding(5, 5, 5, 0);
        time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(time); // Adding textView to tablerow.

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        /** Creating another textview **/
        TextView divider = new TextView(this);
        divider.setText("-----------------");
        divider.setTextColor(Color.parseColor("#5a5a5a"));
        divider.setPadding(5, 0, 0, 0);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.

        TextView divider2 = new TextView(this);
        divider2.setText("---------------");
        divider2.setTextColor(Color.parseColor("#5a5a5a"));
        divider2.setPadding(5, 0, 0, 0);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.

        TextView divider3 = new TextView(this);
        divider3.setText("-----------------------------");
        divider3.setTextColor(Color.parseColor("#5a5a5a"));
        divider3.setPadding(5, 0, 0, 0);
        divider3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider3); // Adding textView to tablerow.
        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    public void showBidList(ArrayList<ProductBid> bidList) {
        int totalBids = bidList.size();
        for (int i = 0; i < totalBids; i++)
        {
            ProductBid bid = bidList.get(i);
            if(bid.getUser().getFirstName() == null || bid.getUser().getLastName() == null)
            {
                continue;
            }
            if(bid.getUser().getId() > 0 && !userIdNameMap.containsKey(bid.getUser().getId()))
            {
                userIdNameMap.put(bid.getUser().getId(), bid.getUser().getFirstName()+" " + bid.getUser().getLastName());
            }
        }

        for (int i = 0; i < totalBids; i++) {
            ProductBid bid = bidList.get(i);
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            bidder_name = new TextView(this);
            if(bid.getUser().getFirstName() != null && bid.getUser().getLastName() != null)
            {
                bidder_name.setText(bid.getUser().getFirstName() + " " + bid.getUser().getLastName());
            }
            else if(userIdNameMap.containsKey(bid.getUser().getId()))
            {
                bidder_name.setText(userIdNameMap.get(bid.getUser().getId()));
            }

            bidder_name.setTextColor(Color.parseColor("#5a5a5a"));
            bidder_name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            bidder_name.setPadding(5, 5, 5, 5);
            tr.addView(bidder_name);  // Adding textView to tablerow.

            /** Creating another textview **/
            price_list = new TextView(this);
            price_list.setText(bid.getPrice() + " £");
            price_list.setTextColor(Color.parseColor("#5a5a5a"));
            price_list.setPadding(5, 5, 5, 5);
            price_list.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(price_list); // Adding textView to tablerow.

            String bidTime = bid.getCreatedTime();
            String[] bidTimeArray = bidTime.split(" ");
            if(bidTimeArray != null && bidTimeArray.length == 3)
            {
                String tempDate = bidTimeArray[0];
                String[] tempDateArray = tempDate.split("-");
                if(tempDateArray != null && tempDateArray.length == 3 && tempDateArray[0].length() == 4)
                {
                    bidTime = tempDateArray[2]+"-"+tempDateArray[1]+"-"+tempDateArray[0]+" "+bidTimeArray[1] + " "+bidTimeArray[2];
                }
            }
            /** Creating another textview **/
            time_list = new TextView(this);
            time_list.setText(bidTime);
            time_list.setTextColor(Color.parseColor("#5a5a5a"));
            time_list.setPadding(5, 5, 5, 5);
            time_list.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(time_list); // Adding textView to tablerow.

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }

    /** This function add the data to the table **/
    /*public void addData() {

        for (int i = 0; i < bidder.length; i++) {
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));

            bidder_name = new TextView(this);
            bidder_name.setText(bidder[i]);
            bidder_name.setTextColor(Color.parseColor("#5a5a5a"));
            bidder_name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            bidder_name.setPadding(5, 5, 5, 5);
            tr.addView(bidder_name);  // Adding textView to tablerow.

            price_list = new TextView(this);
            price_list.setText(bid_amount[i]);
            price_list.setTextColor(Color.parseColor("#5a5a5a"));
            price_list.setPadding(5, 5, 5, 5);
            price_list.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(price_list); // Adding textView to tablerow.

            time_list = new TextView(this);
            time_list.setText(bid_time[i]);
            time_list.setTextColor(Color.parseColor("#5a5a5a"));
            time_list.setPadding(5, 5, 5, 5);
            time_list.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(time_list); // Adding textView to tablerow.

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
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
        getMenuInflater().inflate(R.menu.property_bid_list, menu);
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