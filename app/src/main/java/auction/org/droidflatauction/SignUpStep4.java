package auction.org.droidflatauction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.auction.dto.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

public class SignUpStep4 extends AppCompatActivity {
    private static final String TAG = "SignUpStep4";
    private static ImageButton ib_back_arrow,ib_forward_arrow;
    private static EditText et_sign_up_birthday;
    private DatePickerDialog.OnDateSetListener datePickerSetListener;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step4);

        //String userString = getIntent().getExtras().getString("userString");
        //Gson gson = new Gson();
        //user = gson.fromJson(userString, User.class);

        user = (User)getIntent().getExtras().get("user");

        onClickButtonBackArrowListener();
        onClickButtonForwardArrowListener();
        onClickButtonBirthdayListener();
    }
    public void onClickButtonBackArrowListener(){
        ib_back_arrow = (ImageButton)findViewById(R.id.sing_up_step4_back_arrow);
        ib_back_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sing_up_step4_back_arrow_intent = new Intent(getBaseContext(), SignUpStep3.class);
                        startActivity(sing_up_step4_back_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonForwardArrowListener(){
        ib_forward_arrow = (ImageButton)findViewById(R.id.sing_up_step4_forward_arrow);
        ib_forward_arrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sign_up_step4_forward_arrow_intent = new Intent(getBaseContext(), SignUpStep5.class);

                        //date of birth is not set yet

                        //GsonBuilder gsonBuilder = new GsonBuilder();
                        //Gson gson = gsonBuilder.create();
                        //String userString = gson.toJson(user);

                        sign_up_step4_forward_arrow_intent.putExtra("user", user);

                        startActivity(sign_up_step4_forward_arrow_intent);
                    }
                }
        );
    }
    public void onClickButtonBirthdayListener(){
        et_sign_up_birthday = (EditText) findViewById(R.id.sign_up_birthday);
        et_sign_up_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month =cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SignUpStep4.this,
                        R.style.Theme_AppCompat_Light_Dialog_MinWidth,
                        datePickerSetListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
        datePickerSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy:" + dayOfMonth +"/" + month + "/" + year);
                String date = dayOfMonth +"/" + month + "/" + year;
                et_sign_up_birthday.setText(date);
            }
        };
    }
}
