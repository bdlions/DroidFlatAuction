package auction.org.droidflatauction;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NonMemberHome extends AppCompatActivity {
 private  static Button sign_in_btn, sign_up_btn, non_member_home_search_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_member_home);

        onClickButtonSignInListener();
        onClickButtonSignUpListener();
        onClickButtonSearchListener();

        SessionManager session = new SessionManager(getApplicationContext());
        session.checkLogin();
    }
    public void onClickButtonSignInListener(){
        sign_in_btn = (Button)findViewById(R.id.sign_in_button);
        sign_in_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sign_in_intent = new Intent(getBaseContext(), SignIn.class);
                        startActivity(sign_in_intent);
                    }
                }
        );
    }
    public void onClickButtonSignUpListener(){
        sign_up_btn = (Button)findViewById(R.id.register_button);
        sign_up_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sign_up_intent = new Intent(getBaseContext(), SignUpStep1.class);
                        startActivity(sign_up_intent);
                    }
                }
        );
    }
    public void onClickButtonSearchListener(){
        non_member_home_search_btn = (Button)findViewById(R.id.non_member_home_search_button);
        non_member_home_search_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent non_member_home_search_intent = new Intent(getBaseContext(), NonMemberSearch.class);
                        startActivity(non_member_home_search_intent);
                    }
                }
        );
    }
}
