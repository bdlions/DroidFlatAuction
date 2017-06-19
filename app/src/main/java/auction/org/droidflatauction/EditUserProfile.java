package auction.org.droidflatauction;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.auction.udp.BackgroundUploader;
import org.bdlions.client.reqeust.uploads.UploadService;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

public class EditUserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final int SELECT_PHOTO = 1;
    private static ImageView iv_profile_photo;
    private static Button btn_user_name,btn__user_email,btn__user_password,btn__cell_number;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
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
                        System.out.println("Path---------:" + picturePath);
//                        if (imageUri != null) {
//                            String path = imageUri.toString();
//                            if (path.toLowerCase().startsWith("file://")) {
//                                path = (new File(URI.create(path))).getAbsolutePath();
//                            }
//                            System.out.println("Path---------: " + path);
//                        }
                        new BackgroundUploader().execute(picturePath, new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_LONG).show();
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

        onClickEditUserProfilePhotoEditListener();
        onClickEditUserNameEditListener();
        onClickEditUserEmailEditListener();
        onClickEditUserPasswordEditListener();
        onClickEditUserCellNumberEditListener();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        iv_profile_photo = (ImageView) findViewById(R.id.user_photo);
        Picasso.with(getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").into(iv_profile_photo);
    }
    public void onClickEditUserProfilePhotoEditListener(){
        iv_profile_photo = (ImageView) findViewById(R.id.user_photo);
        iv_profile_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Dialog dialog = new Dialog(EditUserProfile.this);

                dialog.setContentView(R.layout.upload_user_photo);

                dialog.setTitle("Upload Your Photo");


                 ImageView profile_image = (ImageView) dialog.findViewById(R.id.user_profile_image);
                profile_image.setImageResource(R.drawable.user);

                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_photo_upload_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(EditUserProfile.this, "Upload is successfull!",Toast.LENGTH_SHORT).show();
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    }
                });
                Button declineButton = (Button) dialog.findViewById(R.id.user_photo_upload_cancel_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        });
    }
    public void onClickEditUserNameEditListener(){
        btn_user_name = (Button) findViewById(R.id.user_name);
        btn_user_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Dialog dialog = new Dialog(EditUserProfile.this);

                dialog.setContentView(R.layout.edit_user_name_popup);

              dialog.setTitle("Change Your Name");

                EditText first_name = (EditText) dialog.findViewById(R.id.user_first_name);
                first_name.setText("Nazmul");
                EditText last_name = (EditText) dialog.findViewById(R.id.user_last_name);
                last_name.setText("Hasan");
              //  ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
               // image.setImageResource(R.drawable.dashboard);

                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_name_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditUserProfile.this, "Edit is successfull!",Toast.LENGTH_SHORT).show();
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
    public void onClickEditUserEmailEditListener(){
        btn__user_email = (Button) findViewById(R.id.user_email);
        btn__user_email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Dialog dialog = new Dialog(EditUserProfile.this);

                dialog.setContentView(R.layout.edit_user_email_popup);

                dialog.setTitle("Change Your Email");

                EditText user_email = (EditText) dialog.findViewById(R.id.user_email);
                user_email.setText("bdlions@gmail.com");

                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_email_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditUserProfile.this, "Edit is successfull!",Toast.LENGTH_SHORT).show();
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
    public void onClickEditUserPasswordEditListener(){
        btn__user_password = (Button) findViewById(R.id.user_password);
        btn__user_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Dialog dialog = new Dialog(EditUserProfile.this);

                dialog.setContentView(R.layout.edit_user_password_popup);

                dialog.setTitle("Change Your Password");

                EditText user_password = (EditText) dialog.findViewById(R.id.user_password);
                user_password.setText("password");

                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_password_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditUserProfile.this, "Edit is successfull!",Toast.LENGTH_SHORT).show();
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
    public void onClickEditUserCellNumberEditListener(){
        btn__cell_number = (Button) findViewById(R.id.user_cell);
        btn__cell_number.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Dialog dialog = new Dialog(EditUserProfile.this);

                dialog.setContentView(R.layout.edit_user_cell_number_popup);

                dialog.setTitle("Change Your Cell Number");

                EditText user_cell_number = (EditText) dialog.findViewById(R.id.user_cell);
                user_cell_number.setText("+8801234512345");

                dialog.show();
                Button submitButton = (Button) dialog.findViewById(R.id.user_cell_edit_submit_button);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditUserProfile.this, "Edit is successfull!",Toast.LENGTH_SHORT).show();
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
