package com.example.triip;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
   private ArrayList<ListAdapter> labels = new ArrayList<ListAdapter>();
    ListView labelsList;
    private static final String URL = "http://triip.yasya.top:80/api/v1/marks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setupActionBar();

        setInitialData();

        labelsList = (ListView) findViewById(R.id.list);
        final Adapter labelAdapter = new Adapter(this, R.layout.list_item, labels);
        labelsList.setAdapter(labelAdapter);


        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(URL,

                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0; i<response.length();i++) {
                                JSONObject label = (JSONObject) response.get(i);

                                String name = label.getString("title");
                                String tags = label.getString("tags");
                                String data = label.getString("mark_time");

                                labels.add(labels.size(),new ListAdapter(name, data, tags));
                                labelsList.setTag(i);
                            }
                            labelAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, "LIST");

        labelsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                labelsList.setTag(position);
                Dialog dialog = Dialogs.getDialog(Main2Activity.this, Dialogs.IDD_MAIN);
                dialog.show();
                return true;
            }
        });

        FloatingActionButton fab_plus =  findViewById(R.id.fab2);
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intObj = new Intent(Main2Activity.this, CreateLabel.class);
                startActivity(intObj);
            }
        });

    }

    //тест без сервера
    private void setInitialData(){
        labels.add(new ListAdapter ("занос на дороге", "2019:10:12 16:11:00", "трасса, Карелия"));
        labels.add(new ListAdapter ("дтп в пробке", "2019:06:10 09:11:18", "Ленина, дтп"));
        labels.add(new ListAdapter ("дтп", "2019:05:31 15:11:20", "удар, дтп"));
        labels.add(new ListAdapter ("парковка на газоне", "2019:05:31 10:31:15", "газон, двор"));

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intObj= new Intent(Main2Activity.this, MainActivity.class);
            intObj.putExtra("count", 1);
            startActivity(intObj);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
