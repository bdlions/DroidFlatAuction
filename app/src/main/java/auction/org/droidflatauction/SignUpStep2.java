package auction.org.droidflatauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SignUpStep2 extends AppCompatActivity {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);

        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.sing_up_step2_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sing_up_step2_back_arrow_intent = new Intent(getBaseContext(), SignUpStep1.class);
                        startActivity(sing_up_step2_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton)findViewById(R.id.sing_up_step2_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sing_up_step2_forward_arrow_intent = new Intent(getBaseContext(), SignUpStep3.class);
                        startActivity(sing_up_step2_forward_arrow_intent);
                    }
                }
        );
    }
}
