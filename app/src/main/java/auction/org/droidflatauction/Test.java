package auction.org.droidflatauction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Test extends AppCompatActivity {
    private  static TextView tvTest;
    private  static EditText editTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvTest = (TextView)findViewById(R.id.tv_test);
        Toast.makeText(Test.this, "Test Text: " + tvTest, Toast.LENGTH_LONG).show();
        editTest = (EditText)findViewById(R.id.ev_test);
    }
}
