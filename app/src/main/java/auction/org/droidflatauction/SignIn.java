package auction.org.droidflatauction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bdlions.dto.User;
import com.bdlions.dto.response.SignInResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;
import org.bdlions.client.reqeust.threads.IServerCallback;
import org.bdlions.transport.packet.*;

public class SignIn extends AppCompatActivity {
    private  static ImageButton login_ib_back_arrow;
    private  static Button login_btn;
    SessionManager session;
    private static EditText etIdentity, etPassword;
    String identity, password;
    public Dialog progressBarDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        etIdentity = (EditText) findViewById(R.id.et_sign_in_email);
        etPassword = (EditText) findViewById(R.id.et_sign_in_password);

        progressBarDialog = new Dialog(SignIn.this);
        progressBarDialog.setContentView(R.layout.progressbar);

        onClickButtonBackArrowListener();
        onClickButtonLoginListener();
    }


    public void onClickButtonBackArrowListener(){
        login_ib_back_arrow = (ImageButton)findViewById(R.id.login_back_arrow);
        login_ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent login_back_arrow_intent = new Intent(getBaseContext(), NonMemberHome.class);
                        startActivity(login_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonLoginListener(){
        login_btn = (Button)findViewById(R.id.login_button);
        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBarDialog.show();
                        try
                        {
                            identity = etIdentity.getText().toString();
                            password = etPassword.getText().toString();
                            User user = new User();
                            user.setUserName(identity);
                            user.setPassword(password);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String userString = gson.toJson(user);
                            org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                            packetHeader.setAction(ACTION.SIGN_IN);
                            packetHeader.setRequestType(REQUEST_TYPE.AUTH);
                            //new BackgroundWork().execute(packetHeader, "{\"userName\": \"" + "bdlions@gmail.com" + "\", \"password\": \"" + "password" + "\"}", new IServerCallback() {
                            new BackgroundWork().execute(packetHeader, userString, new Handler(){
                                @Override
                                public void handleMessage(Message msg) {
                                    progressBarDialog.dismiss();
                                    SignInResponse signInResponse = null;
                                    String stringSignInResponse = null;
                                    if(msg != null && msg.obj != null)
                                    {
                                        stringSignInResponse = (String)msg.obj;
                                    }
                                    if(stringSignInResponse != null)
                                    {
                                        Gson gson = new Gson();
                                        signInResponse = gson.fromJson(stringSignInResponse, SignInResponse.class);
                                    }

                                    if(signInResponse != null && signInResponse.isSuccess())
                                    {
                                        session.createLoginSession(identity, signInResponse.getSessionId());
                                        session.setEmail(etIdentity.getText().toString());
                                        session.setPassword(etPassword.getText().toString());
                                        //navigate to member dashboard
                                        Intent login_intent = new Intent(getBaseContext(), MemberDashboard.class);
                                        login_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(login_intent);
                                    }
                                    else if(signInResponse != null && !signInResponse.isSuccess())
                                    {
                                        AlertDialog.Builder  sign_in_builder = new AlertDialog.Builder(SignIn.this);
                                        sign_in_builder.setMessage(signInResponse.getMessage())
                                                .setCancelable(false)
                                                .setPositiveButton("", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                })
                                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                });

                                        AlertDialog sign_in_alert = sign_in_builder.create();
                                        sign_in_alert.setTitle("Alert!!!");
                                        sign_in_alert.show();
                                    }
                                    else
                                    {
                                        AlertDialog.Builder  sign_in_builder = new AlertDialog.Builder(SignIn.this);
                                        sign_in_builder.setMessage("Unable to process request. Please try again later.")
                                                .setCancelable(false)
                                                .setPositiveButton("", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                })
                                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                        AlertDialog sign_in_alert = sign_in_builder.create();
                                        sign_in_alert.setTitle("Alert!!!");
                                        sign_in_alert.show();

                                    }
                                }
                            });
                        }
                        catch(Exception ex)
                        {
                            progressBarDialog.dismiss();
                            AlertDialog.Builder  sign_in_builder = new AlertDialog.Builder(SignIn.this);
                            sign_in_builder.setMessage("Unable to process request. Please try again.")
                                    .setCancelable(false)
                                    .setPositiveButton("", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog sign_in_alert = sign_in_builder.create();
                            sign_in_alert.setTitle("Alert!!!");
                            sign_in_alert.show();
                        }
                    }
                }
        );
    }
}
