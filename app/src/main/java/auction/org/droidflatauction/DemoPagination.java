package auction.org.droidflatauction;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DemoPagination extends AppCompatActivity {
    private static ListView listview;
    private static TextView title;
    private static Button btnPrevious;
    private static Button btnNext;

    private static ArrayList<String> data;
    ArrayAdapter<String> sd;

    private int pageCount ;
    private int increment = 0;

    public int totalListItems = 52;
    public int numItemsPage   = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_pagination);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview = (ListView)findViewById(R.id.list);
        btnPrevious  = (Button)findViewById(R.id.prev);
        btnNext = (Button)findViewById(R.id.next);
        title = (TextView)findViewById(R.id.title);

        btnPrevious.setEnabled(false);

        data = new ArrayList<String>();

        int val = totalListItems % numItemsPage;
        val = val == 0?0:1;
        pageCount = totalListItems / numItemsPage + val;

        for(int i = 0; i < totalListItems; i++)
        {
            data.add("This is Item " + ( i + 1));
        }

        loadList(0);
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                increment++;
                loadList(increment);
                CheckEnable();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                increment--;
                loadList(increment);
                CheckEnable();
            }
        });
    }


    private void CheckEnable()
    {
        if(increment + 1 == pageCount)
        {
            btnNext.setEnabled(false);
        }
        else if(increment == 0)
        {
            btnPrevious.setEnabled(false);
        }
        else
        {
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(true);
        }
    }


    private void loadList(int number)
    {
        ArrayList<String> sort = new ArrayList<String>();
        title.setText("Page "+(number + 1) + " of " + pageCount);

        int start = number * numItemsPage;
        for(int i = start; i < (start) + numItemsPage; i++)
        {
            if(i < data.size())
            {
                sort.add(data.get(i));
            }
            else
            {
                break;
            }
        }
        sd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sort);
        listview.setAdapter(sd);
    }
}
