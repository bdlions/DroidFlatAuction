package auction.org.droidflatauction;

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

import com.auction.dto.User;
import com.auction.dto.response.SignInResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        etIdentity = (EditText) findViewById(R.id.et_sign_in_email);
        etPassword = (EditText) findViewById(R.id.et_sign_in_password);

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
                                String stringSignInResponse = (String)msg.obj;
                                //Toast.makeText(getApplicationContext(), stringSignInResponse, Toast.LENGTH_LONG).show();
                                System.out.println(stringSignInResponse);
                                Gson gson = new Gson();
                                SignInResponse signInResponse = gson.fromJson(stringSignInResponse, SignInResponse.class);
                                if(signInResponse.isSuccess())
                                {
                                    session.createLoginSession(identity, signInResponse.getSessionId());
                                    //navigate to member dashboard
                                    Intent login_intent = new Intent(getBaseContext(), MemberDashboard.class);
                                    startActivity(login_intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Invalid login. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
        );
    }
}
