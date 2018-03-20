package auction.org.droidflatauction;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.SignInResponse;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;
import org.bdlions.auction.entity.EntityUser;

public class Test extends AppCompatActivity {
    private  static TextView tvTest;
    private  static EditText editTest;
    SessionManager session;
    public int fetchProfileCounter = 0;
    public int fetchProfileMaxCounter = 5;
    public int reAttemptAutoLogin = 0;
    public int reAttemptAutoLoginMaxCounter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        tvTest = (TextView)findViewById(R.id.tv_test);
        //Toast.makeText(Test.this, "Test Text: " + tvTest, Toast.LENGTH_LONG).show();
        editTest = (EditText)findViewById(R.id.ev_test);

        this.initUserProfile();
    }

    public void initUserProfile()
    {
        try
        {
            String sessionId = session.getSessionId();
            org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
            packetHeader.setAction(ACTION.FETCH_USER_PROFILE_INFO);
            packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
            packetHeader.setSessionId(sessionId);
            new BackgroundWork().execute(packetHeader, "{}", new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    EntityUser entityUser = null;
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
                        entityUser = (EntityUser) clientResponse.getResult();
                        if(session.getUserId() == 0)
                        {
                            session.setUserId(entityUser.getId());
                        }
                        editTest.setText(entityUser.getEmail());
                        return;
                    }
                    else if(clientResponse != null && !clientResponse.isSuccess())
                    {
                        reAttemptAutoLogin++;
                        if(reAttemptAutoLogin <= reAttemptAutoLoginMaxCounter)
                        {
                            autoLogin();
                        }
                        else
                        {
                            session.logoutUser();
                        }
                    }
                    else
                    {
                        fetchProfileCounter++;
                        if (fetchProfileCounter <= fetchProfileMaxCounter)
                        {
                            initUserProfile();
                        }
                        else
                        {
                            //auto login again.
                            reAttemptAutoLogin++;
                            if(reAttemptAutoLogin <= reAttemptAutoLoginMaxCounter)
                            {
                                autoLogin();
                            }
                            else
                            {
                                session.logoutUser();
                            }
                        }
                    }
                }
            });
        }
        catch(Exception ex)
        {
            fetchProfileCounter++;
            if (fetchProfileCounter <= fetchProfileMaxCounter)
            {
                initUserProfile();
            }
            else
            {
                //auto login again.
                reAttemptAutoLogin++;
                if(reAttemptAutoLogin <= reAttemptAutoLoginMaxCounter)
                {
                    autoLogin();
                }
                else
                {
                    session.logoutUser();
                }
            }
        }


    }

    public void autoLogin()
    {
        try
        {
            String email = session.getEmail();
            String password = session.getPassword();
            if(email == null || password == null)
            {
                session.logoutUser();
            }

            EntityUser user = new EntityUser();
            user.setUserName(email);
            user.setPassword(password);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String userString = gson.toJson(user);
            org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
            packetHeader.setAction(ACTION.SIGN_IN);
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
                        session.setSessionId(signInResponse.getSessionId());
                        fetchProfileCounter = 0;
                        initUserProfile();
                    }
                    else
                    {
                        session.logoutUser();
                    }
                }
            });
        }
        catch(Exception ex)
        {
            session.logoutUser();
        }
    }
}
