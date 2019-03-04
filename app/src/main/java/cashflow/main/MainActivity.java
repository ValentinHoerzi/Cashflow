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
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textViewDate,
            textViewPrice,
            textViewKategorie,
            textView_displaySum;

    Spinner spinnerAusgabenEinnahmen,
            spinnerKatagorie;

    
    ArrayAdapter<String> arrayAdapterCategorie;

    Button buttonOK;

    SimpleDateFormat simpleDateFormat;

    Model model;

    String data = "data.csv";
    String categories = "Categories.csv";


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


        initAdapter2();

        simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY");

        model = new Model();



    }
    private void initTextViewS() {
        textView_displaySum = findViewById(R.id.textView_displaySum);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewKategorie = findViewById(R.id.textViewKategorie);
        buttonOK = findViewById(R.id.buttonOK);
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
    private void initAdapter1() {
        ArrayAdapter<String> adapterAusgabenEinnahmen = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,new ArrayList<String>());
        adapterAusgabenEinnahmen.add("Einnahmen");
        adapterAusgabenEinnahmen.add("Ausgaben");
        spinnerAusgabenEinnahmen = findViewById(R.id.spinnerAusgabenEinnahmen);
        spinnerAusgabenEinnahmen.setAdapter(adapterAusgabenEinnahmen);
    }

    private void initAdapter2() {
        List<String> c = new ArrayList<>();
        spinnerKatagorie = findViewById(R.id.spinnerKatagorie);


        try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(categories)))) {
            c = Arrays.asList(br.readLine().split(";"));
        }catch(Exception e){
            Log.e("initUI","Error at reading File");
        }

        arrayAdapterCategorie = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, c);
        spinnerKatagorie.setAdapter(arrayAdapterCategorie);
    }

    public void onButtonClicked(View view) {
        String date = textViewDate.getText().toString();
        String price = textViewPrice.getText().toString();

        String categorieSpinner = spinnerKatagorie.getSelectedItem().toString();
        String categorieTextView = textViewKategorie.getText().toString();

        String IO = spinnerAusgabenEinnahmen.getSelectedItem().toString();

        String eingabe ="";
        if(categorieTextView.equals(" ")){ //If categorieTextView is empty, the selection of the spinner is taken
            eingabe = date +";"+price+";"+categorieSpinner+";"+IO;
        }else{
            eingabe = date +";"+price+";"+categorieTextView.trim()+";"+IO;
            writeToDataFile(categorieTextView.trim()+";",categories,true);

            arrayAdapterCategorie.add(categorieTextView.trim());
            arrayAdapterCategorie.notifyDataSetChanged();
        }

        writeToDataFile(eingabe,data,false);
    }

    private void writeToDataFile(String toFile,String fileName,boolean isCategorie) {
        if(isCategorie){
                String currentEntries ="";
                try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(categories)))) {
                    currentEntries = br.readLine();
                }catch(Exception e){
                    Log.e("writeToDataFile","Error at reading File");
                }
                String newLine = currentEntries += toFile;
            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(openFileOutput(fileName, MODE_PRIVATE)))) {
                out.println(newLine);
                out.flush();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(openFileOutput(fileName, MODE_APPEND)))) {
                out.println(toFile);
                out.flush();
            }catch(Exception e){
                e.printStackTrace();
            }
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