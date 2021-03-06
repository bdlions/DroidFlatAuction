package auction.org.droidflatauction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by bdlions on 28/08/2017.
 */

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static EditText etTxtDate;

    public DateDialog(View view){
        etTxtDate = (EditText)view;
    }
    public Dialog onCreateDialog (Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year,month,day);
    }
    public void onDateSet(DatePicker view, int year, int month, int day){
        String monthString = (month + 1)+"";
        if(month < 9)
        {
            monthString = "0"+monthString;
        }
        String dayString = day+"";
        if(day < 10)
        {
            dayString = "0"+dayString;
        }
        String date = dayString + "-" + monthString + "-" + year;
        etTxtDate.setText(date);
    }
}
