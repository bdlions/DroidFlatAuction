package auction.org.droidflatauction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auction.dto.User;
import com.auction.dto.response.GeneralResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundUploader;
import org.auction.udp.BackgroundWork;
import org.bdlions.client.reqeust.uploads.UploadService;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class EditUserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private final int SELECT_PHOTO = 1;
    private static ImageView ivEditProfilePhoto, ivEditProfileAgentLogo;
    private static Button btnEditProfileName,btnEditProfileEmail,btnEditProfilePassword,btnEditProfileCell,btnEditProfileBusinessName,btnEditProfileAddress;
    SessionManager session;
    NavigationManager navigationManager;

    public User user;

    public int fetchProfileCounter = 0;
    public int imgUploadType;

    public Dialog imageUploadDialog, progressBarDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        Uri uri = imageReturnedIntent.getData();
                        String[] projection = { MediaStore.Images.Media.DATA };

                        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                        cursor.moveToFirst();


                        int columnIndex = cursor.getColumnIndex(projection[0]);
                        String picturePath = cursor.getString(columnIndex); // returns null
                        cursor.close();
                        new BackgroundUploader().execute(picturePath, new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                imageUploadDialog.dismiss();
                                try
                                {
                                    String img = (String)msg.obj;
                                    if(imgUploadType == Constants.IMG_UPLOAD_TYPE_PROFILE_PICTURE)
                                    {
                                        user.setImg(img);
                                        updateUserProfilePicture();
                                    }
                                    else if(imgUploadType == Constants.IMG_UPLOAD_TYPE_AGENT_LOGO)
                                    {
                                        user.setAgentLogo(img);
                                        updateUserAgentLogo();
                                    }
                                }
                                catch(Exception ex)
                                {
                                    Toast.makeText(getApplicationContext(), "Unable to upload image. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        navigationManager = new NavigationManager(getApplicationContext());

        btnEditProfileName = (Button) findViewById(R.id.btn_edit_profile_name);
        btnEditProfileEmail = (Button) findViewById(R.id.btn_edit_profile_email);
        btnEditProfilePassword = (Button) findViewById(R.id.btn_edit_profile_password);
        btnEditProfileCell = (Button) findViewById(R.id.btn_edit_profile_cell);
        btnEditProfileBusinessName = (Button)findViewById(R.id.btn_edit_profile_business_name);
        btnEditProfileAddress = (Button)findViewById(R.id.btn_edit_profile_address);


        ivEditProfilePhoto = (ImageView) findViewById(R.id.iv_edit_profile_photo);
        ivEditProfileAgentLogo = (ImageView) findViewById(R.id.iv_edit_profile_agent_logo);

        onClickButtonBackArrowListener();
        onClickEditUserProfilePhotoEditListener();
        onClickEditUserAgentLogoEditListener();
        onClickEditUserNameEditListener();
        onClickEditUserEmailEditListener();
        onClickEditUserPasswordEditListener();
        onClickEditUserCellNumberEditListener();
        onClickEditUserBusinessNameEditListener();
        onClickEditUserAddressEditListener();

        ListView listViewRole = (ListView)findViewById(R.id.roles_listView);
        final List<RoleModel> roles = new ArrayList<>();
        roles.add(new RoleModel(false,"Landlord"));
        roles.add(new RoleModel(false,"Tanent"));
        roles.add(new RoleModel(false,"Agent"));

        final RoleAdapter roleAdapter = new RoleAdapter(this,roles);
        listViewRole.setAdapter(roleAdapter);
        listViewRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RoleModel roleModel = roles.get(i);
                if(roleModel.isSelected())
                    roleModel.setSelected(false);

                else
                    roleModel.setSelected(true);

                roles.set(i,roleModel);
                roleAdapter.updateRecords(roles);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Picasso.with(getApplicationContext()).load("http://roomauction.co.uk/resources/images/logo.png").into(iv_profile_photo);
        this.fetchUserProfile();
    }

    public void fetchUserProfile()
    {
        progressBarDialog = new Dialog(EditUserProfile.this);
        progressBarDialog.setContentView(R.layout.progressbar);
        progressBarDialog.show();

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_USER_INFO);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    String userString = null;
                    if(msg != null  && msg.obj != null)
                    {
                        userString = (String) msg.obj;
                    }
                    if(userString != null)
                    {
                        Gson gson = new Gson();
                        user = gson.fromJson(userString, User.class);
                    }
                    if(user != null && user.isSuccess())
                    {
                        btnEditProfileName.setText(user.getFirstName()+" "+user.getLastName());
                        btnEditProfileEmail.setText(user.getEmail());
                        btnEditProfilePassword.setText(user.getPassword());
                        btnEditProfileCell.setText(user.getCellNo());
                        btnEditProfileBusinessName.setText(user.getBusinessName());
                        btnEditProfileAddress.setText(user.getAddress());
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getImg()).into(ivEditProfilePhoto);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getAgentLogo()).into(ivEditProfileAgentLogo);
                        progressBarDialog.dismiss();
                    }
                    else
                    {
                        fetchProfileCounter++;
                        if (fetchProfileCounter <= 5)
                        {
                            fetchUserProfile();
                        }
                        else
                        {
                            progressBarDialog.dismiss();
                        }
                    }
                }
                catch(Exception ex)
                {
                    fetchProfileCounter++;
                    if (fetchProfileCounter <= 5)
                    {
                        fetchUserProfile();
                    }
                    else
                    {
                        progressBarDialog.dismiss();
                    }
                }
            }
        });

    }

    public void updateUserProfile()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String userString = gson.toJson(user);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.UPDATE_USER_INFO);
        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, userString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    GeneralResponse response = null;
                    String responseString = null;
                    if(msg != null  && msg.obj != null)
                    {
                        responseString = (String) msg.obj;
                    }
                    if(responseString != null)
                    {
                        Gson gson = new Gson();
                        response = gson.fromJson(responseString, GeneralResponse.class);
                    }
                    if(response != null && response.isSuccess())
                    {
                        btnEditProfileName.setText(user.getFirstName()+" "+user.getLastName());
                        btnEditProfileEmail.setText(user.getEmail());
                        btnEditProfilePassword.setText(user.getPassword());
                        btnEditProfileCell.setText(user.getCellNo());
                        btnEditProfileBusinessName.setText(user.getBusinessName());
                        btnEditProfileAddress.setText(user.getAddress());
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getImg()).into(ivEditProfilePhoto);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getAgentLogo()).into(ivEditProfileAgentLogo);
                        Toast.makeText(EditUserProfile.this, "Profile is updated successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(EditUserProfile.this, "Unable to update profile. Please try again later.",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(EditUserProfile.this, "Unable to update profile. Please try again later.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateUserProfilePicture()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String userString = gson.toJson(user);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.UPDATE_USER_PROFILE_PICTURE);
        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, userString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    GeneralResponse response = null;
                    String responseString = null;
                    if(msg != null  && msg.obj != null)
                    {
                        responseString = (String) msg.obj;
                    }
                    if(responseString != null)
                    {
                        Gson gson = new Gson();
                        response = gson.fromJson(responseString, GeneralResponse.class);
                    }
                    if(response != null && response.isSuccess())
                    {
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getImg()).into(ivEditProfilePhoto);
                        Toast.makeText(EditUserProfile.this, "Profile picture is uploaded successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(EditUserProfile.this, "Unable to upload profile picture. Please try again later.",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(EditUserProfile.this, "Unable to upload profile picture. Please try again later.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateUserAgentLogo()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String userString = gson.toJson(user);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.UPDATE_USER_LOGO);
        packetHeader.setRequestType(REQUEST_TYPE.UPDATE);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, userString, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    GeneralResponse response = null;
                    String responseString = null;
                    if(msg != null  && msg.obj != null)
                    {
                        responseString = (String) msg.obj;
                    }
                    if(responseString != null)
                    {
                        Gson gson = new Gson();
                        response = gson.fromJson(responseString, GeneralResponse.class);
                    }
                    if(response != null && response.isSuccess())
                    {
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getAgentLogo()).into(ivEditProfileAgentLogo);
                        Toast.makeText(EditUserProfile.this, "Agent logo is uploaded successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(EditUserProfile.this, "Unable to upload agent logo. Please try again later.",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(EditUserProfile.this, "Unable to upload agent logo. Please try again later.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickEditUserNameEditListener(){
        btnEditProfileName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(EditUserProfile.this);
                dialog.setContentView(R.layout.edit_user_name_popup);
                dialog.setTitle("Change Your Name");

                final EditText etFirstName = (EditText) dialog.findViewById(R.id.user_first_name);
                final EditText etLastName = (EditText) dialog.findViewById(R.id.user_last_name);
                if(user != null && user.getFirstName() != null)
                {
                    etFirstName.setText(user.getFirstName());
                }
                if(user != null && user.getLastName() != null)
                {
                    etLastName.setText(user.getLastName());
                }

                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_name_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstName = "", lastName = "";
                        firstName = etFirstName.getText().toString();
                        lastName = etLastName.getText().toString();
                        if(firstName.equals("") && lastName.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid name.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            updateUserProfile();
                            dialog.dismiss();
                        }

                    }
                });
                Button declineButton = (Button) dialog.findViewById(R.id.user_name_edit_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        });
    }

    public void onClickEditUserCellNumberEditListener()
    {
        btnEditProfileCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Dialog dialog = new Dialog(EditUserProfile.this);
                dialog.setContentView(R.layout.edit_user_cell_number_popup);
                dialog.setTitle("Change Your Cell Number");
                final EditText etCellNo = (EditText) dialog.findViewById(R.id.user_cell);
                if(user != null && user.getCellNo()!= null)
                {
                    etCellNo.setText(user.getCellNo());
                }
                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_cell_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cellNo = etCellNo.getText().toString();
                        if(cellNo.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid Cell Number.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.setCellNo(cellNo);
                            updateUserProfile();
                            dialog.dismiss();
                        }
                    }
                });
                Button declineButton = (Button) dialog.findViewById(R.id.user_cell_edit_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void onClickEditUserPasswordEditListener(){
        btnEditProfilePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(EditUserProfile.this);
                dialog.setContentView(R.layout.edit_user_password_popup);
                dialog.setTitle("Change Your Password");
                final EditText etPassword = (EditText) dialog.findViewById(R.id.user_password);

                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_password_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = etPassword.getText().toString();
                        if(password.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid password.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.setPassword(etPassword.getText().toString());
                            updateUserProfile();
                            dialog.dismiss();
                        }
                    }
                });
                Button declineButton = (Button) dialog.findViewById(R.id.user_password_edit_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        });
    }

    public void onClickEditUserEmailEditListener(){
        btnEditProfileEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(EditUserProfile.this);
                dialog.setContentView(R.layout.edit_user_email_popup);
                dialog.setTitle("Change Your Email");
                final EditText etEmail = (EditText) dialog.findViewById(R.id.user_email);
                if(user != null && user.getEmail()!= null)
                {
                    etEmail.setText(user.getEmail());
                }
                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_email_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = etEmail.getText().toString();
                        if(email.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid email.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.setEmail(etEmail.getText().toString());
                            updateUserProfile();
                            dialog.dismiss();
                        }
                    }
                });
                Button declineButton = (Button) dialog.findViewById(R.id.user_email_edit_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        });
    }

    public void onClickEditUserBusinessNameEditListener(){
        btnEditProfileBusinessName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(EditUserProfile.this);
                dialog.setContentView(R.layout.edit_user_business_name_popup);
                dialog.setTitle("Change Your Business Name");
                final EditText etBusinessName = (EditText) dialog.findViewById(R.id.user_business_name);
                if(user != null && user.getEmail()!= null)
                {
                    etBusinessName.setText(user.getBusinessName());
                }
                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_business_name_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String businessName = etBusinessName.getText().toString();
                        if(businessName.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign business name.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.setBusinessName(etBusinessName.getText().toString());
                            updateUserProfile();
                            dialog.dismiss();
                        }
                    }
                });
                Button declineButton = (Button) dialog.findViewById(R.id.user_business_name_edit_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void onClickEditUserAddressEditListener(){
        btnEditProfileAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(EditUserProfile.this);
                dialog.setContentView(R.layout.edit_user_address_popup);
                dialog.setTitle("Change Your Address");
                final EditText etAddress = (EditText) dialog.findViewById(R.id.user_address);
                if(user != null && user.getAddress()!= null)
                {
                    etAddress.setText(user.getAddress());
                }
                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_address_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = etAddress.getText().toString();
                        if(address.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign address.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.setAddress(etAddress.getText().toString());
                            updateUserProfile();
                            dialog.dismiss();
                        }
                    }
                });
                Button declineButton = (Button) dialog.findViewById(R.id.user_address_edit_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void onClickEditUserProfilePhotoEditListener(){
        ivEditProfilePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                imageUploadDialog = new Dialog(EditUserProfile.this);
                imageUploadDialog.setContentView(R.layout.upload_user_photo);
                imageUploadDialog.setTitle("Upload Your Photo");
                ImageView profile_image = (ImageView) imageUploadDialog.findViewById(R.id.user_profile_image);
                //profile_image.setImageResource(R.drawable.user);
                imageUploadDialog.show();
                Button submitButton = (Button) imageUploadDialog.findViewById(R.id.user_photo_upload_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(EditUserProfile.this, "Upload is successfull!",Toast.LENGTH_SHORT).show();
                        imgUploadType = Constants.IMG_UPLOAD_TYPE_PROFILE_PICTURE;
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    }
                });
                Button declineButton = (Button) imageUploadDialog.findViewById(R.id.user_photo_upload_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageUploadDialog.dismiss();
                    }
                });
            }

        });
    }
    public void onClickEditUserAgentLogoEditListener(){
        ivEditProfileAgentLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                imageUploadDialog = new Dialog(EditUserProfile.this);
                imageUploadDialog.setContentView(R.layout.upload_agent_logo);
                imageUploadDialog.setTitle("Upload Agent Logo");
                ImageView agent_logo = (ImageView) imageUploadDialog.findViewById(R.id.agent_logo);
                //profile_image.setImageResource(R.drawable.user);
                imageUploadDialog.show();
                Button submitButton = (Button) imageUploadDialog.findViewById(R.id.agent_logo_upload_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(EditUserProfile.this, "Upload is successfull!",Toast.LENGTH_SHORT).show();
                        imgUploadType = Constants.IMG_UPLOAD_TYPE_AGENT_LOGO;
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    }
                });
                Button declineButton = (Button) imageUploadDialog.findViewById(R.id.agent_logo_upload_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageUploadDialog.dismiss();
                    }
                });
            }

        });
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.user_edit_profile_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent user_edit_profile_back_arrow_intent = new Intent(getBaseContext(), ProfileDashboard.class);
                        startActivity(user_edit_profile_back_arrow_intent);
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
        getMenuInflater().inflate(R.menu.edit_user_profile, menu);
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
         //   return true;
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
