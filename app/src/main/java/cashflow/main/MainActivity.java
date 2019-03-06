package cashflow.main;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY");;
    String dataFile = "data.csv";
    String categoriesFile = "Categories.csv";

    TextView textViewDate,
            textViewPrice,
            textViewKategorie,
            textView_displaySum;
    Spinner spinnerAusgabenEinnahmen,
            spinnerKatagorie;
    Button buttonOK;
    ListView myListView;

    //For the Categorie Spinner
    ArrayAdapter<String> arrayAdapterCategorie;
    List<String> currentCategories;

    //For my list View
    MyListViewAdapter adapterListView;
    List<Entry> currentData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        initOV();
        initTextViewDate();
        initAdapter1();
        initAdapter2();
        initAdapter3();
    }

    private void initOV() {
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewKategorie = findViewById(R.id.textViewKategorie);
        buttonOK = findViewById(R.id.buttonOK);
        textView_displaySum = findViewById(R.id.textView_displaySum);
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
        currentCategories = new ArrayList<>();
        spinnerKatagorie = findViewById(R.id.spinnerKatagorie);


        try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(categoriesFile)))) {
            currentCategories.addAll(Arrays.asList(br.readLine().split(";")));
        }catch(Exception e){
            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(openFileOutput(categoriesFile, MODE_PRIVATE)))) {
                out.println("Auto;Essen;Party;Schule;Allgemeines;Motor;");
                out.flush();
            }catch(Exception ee){
                e.printStackTrace();
            }
            initAdapter2();
        }

        arrayAdapterCategorie = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, currentCategories);
        spinnerKatagorie.setAdapter(arrayAdapterCategorie);
    }
    private void initAdapter3(){
        currentData = new ArrayList<>();
        myListView = findViewById(R.id.listView);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(dataFile)))) {
            String line = br.readLine();
            while(line!=null){
                String[] arr = line.split(";");
                currentData.add(new Entry(arr[0],arr[1],arr[2],arr[3]));
                line=br.readLine();
            }
        }catch(Exception e){
            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(openFileOutput(dataFile, MODE_PRIVATE)))) {
                out.flush();
            }catch(Exception ee){
                e.printStackTrace();
            }
            initAdapter3();
        }

        calculateCurrentBudget();

        adapterListView = new MyListViewAdapter(this,R.layout.my_list_view,currentData);
        myListView.setAdapter(adapterListView);
    }
    private void calculateCurrentBudget(){
        int budget = 500;
        if(currentData!=null&&!currentData.isEmpty()) {
            for (Entry d : currentData) {
                String price = d.getPrice();
                String io = d.getIo();
                if (io.equals("Einnahmen")) {
                    budget += Integer.valueOf(price);
                } else {
                    budget -= Integer.valueOf(price);
                }
            }
        }
            textView_displaySum.setText("Ca$h :"+budget+" €");
    }
    private void clearInputFields() {
        textViewDate.setText("");
        textViewPrice.setText("");
        textViewKategorie.setText(" ");
    }
    private void writeToFile(Entry entry,String toFile, String fileName, boolean isCategorie) {
        if(isCategorie){
                String currentEntries ="";
                try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(categoriesFile)))) {
                    currentEntries = br.readLine();
                }catch(Exception e){
                    Log.e("writeToFile","Error at reading File");
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
                out.println(entry.toString());
                out.flush();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onButtonClicked(View view) {
        String date = textViewDate.getText().toString();
        String price = textViewPrice.getText().toString();
        String IO = spinnerAusgabenEinnahmen.getSelectedItem().toString();

        String categorieSpinner = spinnerKatagorie.getSelectedItem().toString();
        String categorieTextView = textViewKategorie.getText().toString();

        if(date.equals("") || price.equals("")){
            toast();
            return;
        }

        Entry entry;
        if(categorieTextView.equals(" ")){ //If categorieTextView is empty, the selection of the spinner is taken
            entry = new Entry(date,price,categorieSpinner,IO);
        }else{
            entry = new Entry(date,price,categorieTextView.trim(),IO);
            writeToFile(null,categorieTextView.trim()+";",categoriesFile,true);
            currentCategories.add(categorieTextView.trim());
            arrayAdapterCategorie.notifyDataSetChanged();
            spinnerKatagorie.setAdapter(arrayAdapterCategorie);
        }
        currentData.add(entry);
        adapterListView.notifyDataSetChanged();
        clearInputFields();
        calculateCurrentBudget();
        writeToFile(entry,"",dataFile,false);
    }

    private void toast() {
        Toast.makeText(getApplicationContext(),"INPUTS",Toast.LENGTH_LONG);
    }
}