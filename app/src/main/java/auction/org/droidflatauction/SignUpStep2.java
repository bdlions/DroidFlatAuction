package auction.org.droidflatauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import org.bdlions.auction.entity.EntityUser;

public class SignUpStep2 extends AppCompatActivity {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private EntityUser entityUser;
    private static EditText etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);

        etEmail = (EditText) findViewById(R.id.et_sign_up_email);
        entityUser = (EntityUser)getIntent().getExtras().get("entityUser");
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
                        sing_up_step2_back_arrow_intent.putExtra("entityUser",entityUser);
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
                        String email = etEmail.getText().toString();
                        entityUser.setEmail(email);
                        if(email == null || email.equals(""))
                        {
                            Toast.makeText(SignUpStep2.this, "Please assign valid email.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent sign_up_step2_forward_arrow_intent = new Intent(getBaseContext(), SignUpStep3.class);
                        sign_up_step2_forward_arrow_intent.putExtra("entityUser", entityUser);
                        startActivity(sign_up_step2_forward_arrow_intent);
                    }
                }
        );
    }
}
