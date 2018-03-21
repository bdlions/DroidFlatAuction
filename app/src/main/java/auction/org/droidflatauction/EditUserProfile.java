package auction.org.droidflatauction;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.Toast;
import com.bdlions.dto.response.ClientListResponse;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import org.auction.udp.BackgroundUploader;
import org.auction.udp.BackgroundWork;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditUserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  static ImageButton ib_back_arrow;
    private final int SELECT_PHOTO = 1;
    private static ImageView ivEditProfilePhoto, ivEditProfileAgentLogo,ivEditProfileDocument;
    private static Button btnEditProfileName,btnEditProfileEmail,btnEditProfilePassword,btnEditProfileCell,btnEditProfileBusinessName,btnEditProfileAddress;
    SessionManager session;
    NavigationManager navigationManager;

    ListView listViewRole;

    public DTOUser user;
    public List<EntityRole> userRoleList = new ArrayList<>();

    public int fetchProfileCounter = 0;
    public int fetchRoleListCounter = 0;

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
                                        user.getEntityUser().setImg(img);
                                        updateUserProfilePicture();
                                    }
                                    else if(imgUploadType == Constants.IMG_UPLOAD_TYPE_AGENT_LOGO)
                                    {
                                        user.getEntityUser().setAgentLogo(img);
                                        updateUserAgentLogo();
                                    }
                                    else if(imgUploadType == Constants.IMG_UPLOAD_TYPE_PROFILE_DOCUMENT)
                                    {
                                        user.getEntityUser().setDocument(img);
                                        updateUserProfileDocument();
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
        ivEditProfileDocument = (ImageView) findViewById(R.id.iv_edit_profile_profile_document);

        onClickButtonBackArrowListener();
        onClickEditUserProfilePhotoEditListener();
        onClickEditUserAgentLogoEditListener();
        onClickEditUserProfileDocumentEditListener();
        onClickEditUserNameEditListener();
        onClickEditUserEmailEditListener();
        onClickEditUserPasswordEditListener();
        onClickEditUserCellNumberEditListener();
        onClickEditUserBusinessNameEditListener();
        onClickEditUserAddressEditListener();

        listViewRole = (ListView)findViewById(R.id.roles_listView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Picasso.with(getApplicationContext()).load("http://roomauction.co.uk/resources/images/logo.png").into(iv_profile_photo);
        progressBarDialog = new Dialog(EditUserProfile.this);
        progressBarDialog.setContentView(R.layout.progressbar);
        progressBarDialog.show();

        this.fetchUserProfile();
    }

    public void fetchUserProfile()
    {
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
                    ClientResponse clientResponse = null;
                    String clientResponseString = null;
                    Gson gson = new Gson();
                    if(msg != null  && msg.obj != null)
                    {
                        clientResponseString = (String) msg.obj;
                    }
                    if(clientResponseString != null)
                    {
                        clientResponse = gson.fromJson(clientResponseString, ClientResponse.class);
                    }
                    if(clientResponse != null && clientResponse.isSuccess())
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(clientResponseString);
                            user = gson.fromJson(obj.get("result").toString(), DTOUser.class);
                            if(user == null || user.getEntityUser() == null || user.getEntityUser().getId() == 0)
                            {
                                return;
                            }
                        }
                        catch(Exception ex)
                        {
                            return;
                        }
                        userRoleList = user.getRoles();

                        btnEditProfileName.setText(user.getEntityUser().getFirstName()+" "+user.getEntityUser().getLastName());
                        btnEditProfileEmail.setText(user.getEntityUser().getEmail());
                        btnEditProfilePassword.setText(user.getEntityUser().getPassword());
                        btnEditProfileCell.setText(user.getEntityUser().getCell());
                        btnEditProfileBusinessName.setText(user.getEntityUser().getBusinessName());
                        btnEditProfileAddress.setText(user.getEntityUser().getAddress());
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getEntityUser().getImg()).into(ivEditProfilePhoto);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getEntityUser().getAgentLogo()).into(ivEditProfileAgentLogo);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profileDocument+user.getEntityUser().getDocument()).into(ivEditProfileDocument);
                        fetchRoleList();
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
                            //toast error message
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
                        //toast error message
                        progressBarDialog.dismiss();
                    }
                }
            }
        });

    }

    public void fetchRoleList()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_MEMBER_ROLES);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try
                {
                    List<EntityRole> roleList = null;
                    String clientListResponseString = null;
                    ClientListResponse clientListResponse = null;
                    if(msg != null && msg.obj != null)
                    {
                        clientListResponseString = (String) msg.obj;
                    }
                    if(clientListResponseString != null)
                    {
                        Gson gson = new Gson();
                        clientListResponse = gson.fromJson(clientListResponseString, ClientListResponse.class);
                    }
                    if(clientListResponse != null && clientListResponse.isSuccess() && clientListResponse.getList() != null )
                    {
                        roleList = (List<EntityRole>)clientListResponse.getList();
                        progressBarDialog.dismiss();
                        ArrayList<Integer> userRoleListId = new ArrayList<Integer>();
                        for(int counter = 0; counter < userRoleList.size(); counter++)
                        {
                            userRoleListId.add(userRoleList.get(counter).getId());
                        }
                        final List<RoleDTO> roles = new ArrayList<>();
                        for(int counter = 0; counter < roleList.size(); counter++ )
                        {
                            EntityRole role = roleList.get(counter);
                            RoleDTO roleDTO = new RoleDTO(false,role.getDescription());
                            if(userRoleListId.contains(role.getId()))
                            {
                                roleDTO.setSelected(true);
                            }

                            roleDTO.setId(role.getId());
                            roles.add(roleDTO);
                        }
                        //roles.add(new RoleDTO(false,"Landlord"));
                        //roles.add(new RoleDTO(false,"Tanent"));
                        //roles.add(new RoleDTO(false,"Agent"));

                        final RoleAdapter roleAdapter = new RoleAdapter(EditUserProfile.this,roles);
                        listViewRole.setAdapter(roleAdapter);
                        listViewRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                RoleDTO roleModel = roles.get(i);

                                EntityRole role = new EntityRole();
                                role.setId(roleModel.getId());

                                if(roleModel.isSelected())
                                {
                                    roleModel.setSelected(false);
                                }
                                else
                                {
                                    roleModel.setSelected(true);
                                }

                                List<EntityRole> tempRoleList = new ArrayList<EntityRole>();
                                boolean isExists = false;
                                if(userRoleList != null && userRoleList.size() > 0)
                                {
                                    for (int counter = 0; counter < userRoleList.size(); counter++)
                                    {
                                        if (userRoleList.get(counter).getId() == role.getId())
                                        {
                                            isExists = true;
                                        }
                                        else
                                        {
                                            tempRoleList.add(userRoleList.get(counter));
                                        }
                                    }
                                }
                                if (!isExists)
                                {
                                    tempRoleList.add(role);
                                }
                                userRoleList = tempRoleList;
                                user.setRoles(userRoleList);

                                roles.set(i,roleModel);
                                roleAdapter.updateRecords(roles);

                                progressBarDialog = new Dialog(EditUserProfile.this);
                                progressBarDialog.setContentView(R.layout.progressbar);
                                progressBarDialog.show();
                                updateUserProfile();
                            }
                        });
                    }
                    else
                    {
                        fetchRoleListCounter++;
                        if (fetchRoleListCounter <= 5)
                        {
                            fetchRoleList();
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
                    fetchRoleListCounter++;
                    if (fetchRoleListCounter <= 5)
                    {
                        fetchRoleList();
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
                progressBarDialog.dismiss();
                try
                {
                    ClientResponse clientResponse = null;
                    String clientResponseString = null;
                    if(msg != null  && msg.obj != null)
                    {
                        clientResponseString = (String) msg.obj;
                    }
                    if(clientResponseString != null)
                    {
                        Gson gson = new Gson();
                        clientResponse = gson.fromJson(clientResponseString, ClientResponse.class);
                    }
                    if(clientResponse != null && clientResponse.isSuccess())
                    {
                        btnEditProfileName.setText(user.getEntityUser().getFirstName()+" "+user.getEntityUser().getLastName());
                        btnEditProfileEmail.setText(user.getEntityUser().getEmail());
                        btnEditProfilePassword.setText(user.getEntityUser().getPassword());
                        btnEditProfileCell.setText(user.getEntityUser().getCell());
                        btnEditProfileBusinessName.setText(user.getEntityUser().getBusinessName());
                        btnEditProfileAddress.setText(user.getEntityUser().getAddress());
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getEntityUser().getImg()).into(ivEditProfilePhoto);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getEntityUser().getAgentLogo()).into(ivEditProfileAgentLogo);
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profileDocument+user.getEntityUser().getDocument()).into(ivEditProfileDocument);
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
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profilePicturePath+user.getEntityUser().getImg()).into(ivEditProfilePhoto);
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
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.agentLogoPath_100_100+user.getEntityUser().getAgentLogo()).into(ivEditProfileAgentLogo);
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

    public void updateUserProfileDocument()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String userString = gson.toJson(user);

        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.UPDATE_USER_DOCUMENT);
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
                        Picasso.with(getApplicationContext()).load(Constants.baseUrl+Constants.profileDocument+user.getEntityUser().getDocument()).into(ivEditProfileDocument);
                        Toast.makeText(EditUserProfile.this, "Profile document is uploaded successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(EditUserProfile.this, "Unable to upload profile document. Please try again later.",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(EditUserProfile.this, "Unable to upload profile document. Please try again later.",Toast.LENGTH_SHORT).show();
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
                if(user != null && user.getEntityUser().getFirstName() != null)
                {
                    etFirstName.setText(user.getEntityUser().getFirstName());
                }
                if(user != null && user.getEntityUser().getLastName() != null)
                {
                    etLastName.setText(user.getEntityUser().getLastName());
                }

                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_name_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstName = "", lastName = "";
                        firstName = etFirstName.getText().toString();
                        lastName = etLastName.getText().toString();
                        if(firstName == null || firstName.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid first name.",Toast.LENGTH_SHORT).show();
                        }
                        else if(lastName == null || lastName.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid last name.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.getEntityUser().setFirstName(firstName);
                            user.getEntityUser().setLastName(lastName);
                            dialog.dismiss();

                            progressBarDialog = new Dialog(EditUserProfile.this);
                            progressBarDialog.setContentView(R.layout.progressbar);
                            progressBarDialog.show();
                            updateUserProfile();
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
                if(user != null && user.getEntityUser().getCell()!= null)
                {
                    etCellNo.setText(user.getEntityUser().getCell());
                }
                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_cell_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cellNo = etCellNo.getText().toString();
                        if(cellNo == null || cellNo.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid Cell Number.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.getEntityUser().setCell(cellNo);
                            dialog.dismiss();

                            progressBarDialog = new Dialog(EditUserProfile.this);
                            progressBarDialog.setContentView(R.layout.progressbar);
                            progressBarDialog.show();
                            updateUserProfile();
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
                        if(password == null || password.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid password.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.getEntityUser().setPassword(etPassword.getText().toString());
                            dialog.dismiss();

                            progressBarDialog = new Dialog(EditUserProfile.this);
                            progressBarDialog.setContentView(R.layout.progressbar);
                            progressBarDialog.show();
                            updateUserProfile();
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
                if(user != null && user.getEntityUser().getEmail()!= null)
                {
                    etEmail.setText(user.getEntityUser().getEmail());
                }
                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_email_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = etEmail.getText().toString();
                        if(email == null || email.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign valid email.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.getEntityUser().setEmail(etEmail.getText().toString());
                            dialog.dismiss();

                            progressBarDialog = new Dialog(EditUserProfile.this);
                            progressBarDialog.setContentView(R.layout.progressbar);
                            progressBarDialog.show();
                            updateUserProfile();
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
                if(user != null && user.getEntityUser().getBusinessName()!= null)
                {
                    etBusinessName.setText(user.getEntityUser().getBusinessName());
                }
                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_business_name_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String businessName = etBusinessName.getText().toString();
                        if(businessName == null || businessName.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign business name.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.getEntityUser().setBusinessName(etBusinessName.getText().toString());
                            dialog.dismiss();

                            progressBarDialog = new Dialog(EditUserProfile.this);
                            progressBarDialog.setContentView(R.layout.progressbar);
                            progressBarDialog.show();
                            updateUserProfile();
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
                if(user != null && user.getEntityUser().getAddress()!= null)
                {
                    etAddress.setText(user.getEntityUser().getAddress());
                }
                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_address_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = etAddress.getText().toString();
                        if(address == null || address.equals(""))
                        {
                            Toast.makeText(EditUserProfile.this, "Please assign address.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user.getEntityUser().setAddress(etAddress.getText().toString());
                            dialog.dismiss();

                            progressBarDialog = new Dialog(EditUserProfile.this);
                            progressBarDialog.setContentView(R.layout.progressbar);
                            progressBarDialog.show();
                            updateUserProfile();
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
    public void onClickEditUserProfileDocumentEditListener(){
        ivEditProfileDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                imageUploadDialog = new Dialog(EditUserProfile.this);
                imageUploadDialog.setContentView(R.layout.upload_profile_document);
                imageUploadDialog.setTitle("Upload Profile Document");
                ImageView profile_document = (ImageView) imageUploadDialog.findViewById(R.id.profile_document);
                //profile_image.setImageResource(R.drawable.user);
                imageUploadDialog.show();
                Button submitButton = (Button) imageUploadDialog.findViewById(R.id.profile_document_upload_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(EditUserProfile.this, "Upload is successfull!",Toast.LENGTH_SHORT).show();
                        imgUploadType = Constants.IMG_UPLOAD_TYPE_PROFILE_DOCUMENT;
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    }
                });
                Button declineButton = (Button) imageUploadDialog.findViewById(R.id.profile_document_upload_cancel_button);
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
