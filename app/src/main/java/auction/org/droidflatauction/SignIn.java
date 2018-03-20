package auction.org.droidflatauction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bdlions.dto.response.SignInResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;
import org.bdlions.auction.entity.EntityUser;

public class SignIn extends AppCompatActivity {
    private  static ImageButton login_ib_back_arrow;
    private  static Button login_btn;
    SessionManager session;
    private static EditText etIdentity, etPassword;
    String identity, password;
    public Dialog progressBarDialog;
    private static String message = "";
    public int reAttemptLogin = 0;
    public int reAttemptLoginMaxCounter = 5;
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
                        login();
                    }
                }
        );
    }

    public void login()
    {
        try
        {
            identity = etIdentity.getText().toString();
            password = etPassword.getText().toString();
            EntityUser entityUser = new EntityUser();
            entityUser.setUserName(identity);
            entityUser.setPassword(password);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String entityUserString = gson.toJson(entityUser);
            org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
            packetHeader.setAction(ACTION.SIGN_IN);
            packetHeader.setRequestType(REQUEST_TYPE.AUTH);
            new BackgroundWork().execute(packetHeader, entityUserString, new Handler(){
                @Override
                public void handleMessage(Message msg) {

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
                        progressBarDialog.dismiss();
                        session.createLoginSession(identity, signInResponse.getSessionId());
                        session.setEmail(etIdentity.getText().toString());
                        session.setPassword(etPassword.getText().toString());
                        //navigate to member dashboard
                        Intent login_intent = new Intent(getBaseContext(), MemberDashboard.class);
                        login_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(login_intent);
                    }
                    else
                    {
                        reAttemptLogin++;
                        if(reAttemptLogin <= reAttemptLoginMaxCounter)
                        {
                            login();
                        }
                        else
                        {

                            progressBarDialog.dismiss();
                            if(signInResponse != null && !signInResponse.isSuccess())
                            {
                                Toast.makeText(getBaseContext(), signInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "Unable to process request. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    /*else if(signInResponse != null && !signInResponse.isSuccess())
                    {
                        Toast.makeText(getBaseContext(), signInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Unable to process request. Please try again later.", Toast.LENGTH_SHORT).show();
                    }*/
                }
            });
        }
        catch(Exception ex)
        {
            progressBarDialog.dismiss();
            Toast.makeText(getBaseContext(), "Unable to process request. Please try again later..", Toast.LENGTH_SHORT).show();
        }
    }
}
