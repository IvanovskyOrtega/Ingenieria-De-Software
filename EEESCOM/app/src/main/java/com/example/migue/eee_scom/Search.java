package com.example.migue.eee_scom;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.ListView;
        import android.widget.SearchView;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import java.util.ArrayList;

public class Search extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] componentList;
    ArrayList<String> arraylist = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // Generate sample data

        componentList = new String[]{"L7805", "LM555", "Led",
                "Atmega328", "R 10kOhms", "R 220 Ohms", "CPLD", "Arduino Uno",
                "Potenciometro 10KOhms","Cristal 16MHz","Socket para 28 pines"};

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < componentList.length; i++) {
            // Binds all strings into an array
            arraylist.add(componentList[i]);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Search.this,componentDetails.class);
                intent.putExtra("parametro", (String) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }



}
