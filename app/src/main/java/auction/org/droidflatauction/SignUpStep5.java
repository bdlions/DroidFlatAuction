package auction.org.droidflatauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SignUpStep5 extends AppCompatActivity {
    private  static ImageButton ib_back_arrow;
    private  static Button b_accept_btn, b_decline_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step5);

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
                        Intent sing_up_step5_back_arrow_intent = new Intent(getBaseContext(), SignUpStep4.class);
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
                        Intent sing_up_step5_accept_intent = new Intent(getBaseContext(), MemberDashboard.class);
                        startActivity(sing_up_step5_accept_intent);
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
