package com.example.migue.eee_scom;

        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.ListView;
        import android.widget.SearchView;

        import java.util.ArrayList;

public class Search extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] componetList;
    ArrayList<Components> arraylist = new ArrayList<Components>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // Generate sample data

        componetList = new String[]{"L7805", "LM555", "Led",
                "Atmega328", "R 10kOhms", "R 220 Ohms", "CPLD", "Arduino Uno",
                "Potenciometro 10KOhms","Cristal 16MHz","Socket para 28 pines"};

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < componetList.length; i++) {
            Components Components = new Components(componetList[i]);
            // Binds all strings into an array
            arraylist.add(Components);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
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
