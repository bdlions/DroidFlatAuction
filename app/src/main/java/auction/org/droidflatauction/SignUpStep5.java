package auction.org.droidflatauction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.bdlions.dto.response.SignInResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.auction.udp.BackgroundWork;
import org.bdlions.auction.entity.EntityUser;

public class SignUpStep5 extends AppCompatActivity {
    private  static ImageButton ib_back_arrow;
    private  static Button b_accept_btn, b_decline_btn;
    private EntityUser entityUser;
    private String userString;
    public Dialog progressBarDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step5);
        entityUser = (EntityUser) getIntent().getExtras().get("entityUser");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        userString = gson.toJson(entityUser);

        progressBarDialog = new Dialog(SignUpStep5.this);
        progressBarDialog.setContentView(R.layout.progressbar);

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
                        sing_up_step5_back_arrow_intent.putExtra("entityUser",entityUser);
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
                        progressBarDialog.show();

                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        packetHeader.setAction(ACTION.SIGN_UP);
                        packetHeader.setRequestType(REQUEST_TYPE.AUTH);
                        new BackgroundWork().execute(packetHeader, userString, new Handler(){
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
                                    Toast.makeText(getApplicationContext(), "Registration successful. Please login.", Toast.LENGTH_LONG).show();
                                    Intent sing_up_step5_accept_intent = new Intent(getBaseContext(), SignIn.class);
                                    startActivity(sing_up_step5_accept_intent);
                                }
                                else if(signInResponse != null && !signInResponse.isSuccess())
                                {
                                    Toast.makeText(getBaseContext(), signInResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
