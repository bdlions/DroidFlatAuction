package auction.org.droidflatauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.auction.dto.User;
import com.auction.dto.response.SignInResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;

import org.auction.udp.BackgroundWork;
import org.bdlions.client.reqeust.threads.IServerCallback;
import org.bdlions.transport.packet.*;

public class SignIn extends AppCompatActivity {
    private  static ImageButton login_ib_back_arrow;
    private  static Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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
                        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                        packetHeader.setAction(ACTION.SIGN_IN);
                        packetHeader.setRequestType(REQUEST_TYPE.AUTH);
                        new BackgroundWork().execute(packetHeader, "{\"userName\": \"" + "bdlions@gmail.com" + "\", \"password\": \"" + "password" + "\"}", new IServerCallback() {
                            @Override
                            public void timeout(String s) {
                                System.out.println(s);
                            }

                            @Override
                            public void resultHandler(IPacketHeader iPacketHeader, String stringSignInResponse) {
                                System.out.println(stringSignInResponse);
                                Gson gson = new Gson();
                                SignInResponse signInResponse = gson.fromJson(stringSignInResponse, SignInResponse.class);
                                if(signInResponse.isSuccess())
                                {
                                    //navigate to member dashboard
                                }
                            }
                        });
                        Intent login_intent = new Intent(getBaseContext(), MemberDashboard.class);
                        startActivity(login_intent);
                    }
                }
        );
    }
}
