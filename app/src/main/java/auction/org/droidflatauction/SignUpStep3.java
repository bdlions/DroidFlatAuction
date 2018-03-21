package auction.org.droidflatauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



public class SignUpStep3 extends AppCompatActivity {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private EntityUser entityUser;
    private static EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step3);
        etPassword = (EditText) findViewById(R.id.et_sign_up_password);
        entityUser = (EntityUser)getIntent().getExtras().get("entityUser");
        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
    }

    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.sing_up_step3_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sing_up_step3_back_arrow_intent = new Intent(getBaseContext(), SignUpStep2.class);
                        sing_up_step3_back_arrow_intent.putExtra("entityUser",entityUser);
                        startActivity(sing_up_step3_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton)findViewById(R.id.sing_up_step3_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = etPassword.getText().toString();
                        entityUser.setPassword(etPassword.getText().toString());
                        if(password == null || password.equals(""))
                        {
                            Toast.makeText(SignUpStep3.this, "Please assign password.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent sign_up_step3_forward_arrow_intent = new Intent(getBaseContext(), SignUpStep5.class);
                        sign_up_step3_forward_arrow_intent.putExtra("entityUser", entityUser);
                        startActivity(sign_up_step3_forward_arrow_intent);
                    }
                }
        );
    }
}
