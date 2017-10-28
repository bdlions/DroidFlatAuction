package auction.org.droidflatauction;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import org.bdlions.transport.packet.IPacketHeader;

public class SignUpStep5 extends AppCompatActivity {
    private  static ImageButton ib_back_arrow;
    private  static Button b_accept_btn, b_decline_btn;
    private User user;
    private String userString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step5);

        //userString = getIntent().getExtras().getString("userString");
        //Gson gson = new Gson();
        //user = gson.fromJson(userString, User.class);

        user = (User)getIntent().getExtras().get("user");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        userString = gson.toJson(user);

        onClickButtonBackArrowListener();
        onClickButtonAcceptListener();
        onClickButtonDeclineListener();
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.sing_up_step5_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sing_up_step5_back_arrow_intent = new Intent(getBaseContext(), SignUpStep3.class);
                        sing_up_step5_back_arrow_intent.putExtra("user",user);
                        startActivity(sing_up_step5_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonAcceptListener(){
        b_accept_btn = (Button)findViewById(R.id.sign_up_accept_button);
        b_accept_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        packetHeader.setAction(ACTION.SIGN_UP);
                        packetHeader.setRequestType(REQUEST_TYPE.AUTH);
                        /*new BackgroundWork().execute(packetHeader, userString, new IServerCallback() {
                            @Override
                            public void timeout(String s) {
                                System.out.println(s);
                            }

                            @Override
                            public void resultHandler(IPacketHeader iPacketHeader, String s) {
                                System.out.println(s);
                                //check success response and then redirect to dashboard
                                Intent sing_up_step5_accept_intent = new Intent(getBaseContext(), MemberDashboard.class);
                                startActivity(sing_up_step5_accept_intent);
                            }
                        });*/
                        new BackgroundWork().execute(packetHeader, userString, new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                String stringSignInResponse = (String)msg.obj;
                                //Toast.makeText(getApplicationContext(), stringSignInResponse, Toast.LENGTH_LONG).show();
                                //System.out.println(stringSignInResponse);
                                Gson gson = new Gson();
                                SignInResponse signInResponse = gson.fromJson(stringSignInResponse, SignInResponse.class);
                                if(signInResponse.isSuccess())
                                {
                                    Toast.makeText(getApplicationContext(), "Registration successful. Please login.", Toast.LENGTH_LONG).show();
                                    Intent sing_up_step5_accept_intent = new Intent(getBaseContext(), SignIn.class);
                                    startActivity(sing_up_step5_accept_intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Error while registration. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
        );
    }
    public void onClickButtonDeclineListener(){
        b_decline_btn = (Button)findViewById(R.id.sign_up_decline_button);
        b_decline_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sing_up_step5_decline_intent = new Intent(getBaseContext(), NonMemberHome.class);
                        startActivity(sing_up_step5_decline_intent);
                    }
                }
        );
    }
}
