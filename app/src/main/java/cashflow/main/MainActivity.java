package cashflow.main;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText textViewDate;
    Spinner spinnerAusgabenEinnahmen;
    EditText textViewPrice;
    Spinner spinnerKatagorie;
    TextView textViewKategorie;
    Button buttonOK;
    TextView textView_displaySum;
    Model model;
    ArrayAdapter<String> catAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        initTextViewS();
        initTextViewDate();
        initAdapter1();
        refreshAdapterCategories(catAdapter,true);
    }

    private void initTextViewDate() {
        textViewDate = findViewById(R.id.textViewDate);
        final Calendar buyDate = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                buyDate.set(Calendar.YEAR,year);
                buyDate.set(Calendar.MONTH,month);
                buyDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                textViewDate.setText(simpleDateFormat.format(buyDate.getTime()));
            }
        };

        textViewDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this,date,buyDate.get(Calendar.YEAR),buyDate.get(Calendar.MONTH),buyDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initTextViewS() {
        textView_displaySum = findViewById(R.id.textView_displaySum);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewKategorie = findViewById(R.id.textViewKategorie);
        buttonOK = findViewById(R.id.buttonOK);
    }
    private void initAdapter1() {
        ArrayAdapter<String> adapterAusgabenEinnahmen = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,new ArrayList<String>());
        adapterAusgabenEinnahmen.add("Einnahmen");
        adapterAusgabenEinnahmen.add("Ausgaben");
        spinnerAusgabenEinnahmen = findViewById(R.id.spinnerAusgabenEinnahmen);
        spinnerAusgabenEinnahmen.setAdapter(adapterAusgabenEinnahmen);
    }

    private void refreshAdapterCategories(ArrayAdapter<String> catAdapter, boolean init) {
        List<String> categories = new ArrayList<>();
        spinnerKatagorie = findViewById(R.id.spinnerKatagorie);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("cat.csv")))) {
            categories = Arrays.asList(br.readLine().split(";"));
        }catch(Exception e){
            Log.e("initUI","Error at reading File");
        }

        if(init){
        catAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,categories);
        spinnerKatagorie.setAdapter(catAdapter);
        }else{
            catAdapter.clear();
            catAdapter.addAll(categories);
        }
    }





    /*
        private void bindAdapterToListView(ListView lv) {
        mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,customers);
        lv.setAdapter(mAdapter);
    }

    public void addItemToList(View view) {
        //customers.add("New Element");
        //mAdapter.notifyDataSetChanged();
    }
     */

}
