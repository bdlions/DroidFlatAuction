package auction.org.droidflatauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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
                        Intent login_intent = new Intent(getBaseContext(), MemberDashboard.class);
                        startActivity(login_intent);
                    }
                }
        );
    }
}
