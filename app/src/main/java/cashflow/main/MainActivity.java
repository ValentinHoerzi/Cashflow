package cashflow.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textViewDate;
    Spinner spinnerAusgabenEinnahmen;
    TextView textViewPrice;
    Spinner spinnerKatagorie;
    TextView textViewKategorie;
    Button buttonOK;
    TextView textView_displaySum;
    ListView listView;
    Model model;

    ArrayAdapter<String> catAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        initTextViewS();
        initAdapter1();
        refreshAdapterCategories(catAdapter,true);
    }
    private void initTextViewS() {
        textViewDate = findViewById(R.id.textViewDate);
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
