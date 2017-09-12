package auction.org.droidflatauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.auction.dto.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SignUpStep3 extends AppCompatActivity {
    private  static ImageButton ib_back_arrow,ib_forward_arrow;
    private User user;
    private static EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step3);

        etPassword = (EditText) findViewById(R.id.et_sign_up_password);

        //String userString = getIntent().getExtras().getString("userString");
        //Gson gson = new Gson();
        //user = gson.fromJson(userString, User.class);

        user = (User)getIntent().getExtras().get("user");

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
                        Intent sign_up_step3_forward_arrow_intent = new Intent(getBaseContext(), SignUpStep5.class);
                        user.setPassword(etPassword.getText().toString());

                        //GsonBuilder gsonBuilder = new GsonBuilder();
                        //Gson gson = gsonBuilder.create();
                        //String userString = gson.toJson(user);

                        sign_up_step3_forward_arrow_intent.putExtra("user", user);

                        startActivity(sign_up_step3_forward_arrow_intent);
                    }
                }
        );
    }
}
