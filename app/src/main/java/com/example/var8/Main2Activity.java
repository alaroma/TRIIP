package com.example.var8;


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

//import android.support.v7.widget.Toolbar;

public class Main2Activity extends AppCompatActivity {
   private ArrayList<ListAdapter> labels = new ArrayList<ListAdapter>();
   //private JSONArray<ListAdapter> labels = new JSONArray<ListAdapter>();
    ListView labelsList;
    private static final String URL = "http://triip.yasya.top:80/api/v1/marks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setupActionBar();

       // setInitialData();

        labelsList = (ListView) findViewById(R.id.list);
        final Adapter labelAdapter = new Adapter(this, R.layout.list_item, labels);

        labelsList.setAdapter(labelAdapter);


        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(URL,

                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
//                            labelsList = (ListView) findViewById(R.id.list);
//                            Adapter labelAdapter = new Adapter(this, R.layout.list_item, labels);
//
//                            labelsList.setAdapter(labelAdapter);
                           // int i;
                            for(int i=0; i<response.length();i++) {
                          //  for(int i=response.length()-1; i>=0;i--) {
                                JSONObject label = (JSONObject) response.get(i);

                                String name = label.getString("title");
                                String tags = label.getString("tags");
                                String data = label.getString("mark_time");

                                labels.add(labels.size(),new ListAdapter(name, data, tags));
                                labelsList.setTag(i);
                            }
                            labelAdapter.notifyDataSetChanged();
                            //  txtResponse.setText(jsonResponse);

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

        Bundle arguments = getIntent().getExtras();

        //createList(labels);
//        if(arguments!=null) {
//
//            String name = arguments.getString("name");
//            String data = arguments.getString("data");
//            String tags = arguments.getString("tags");
//
//           // int price = arguments.getInt("img");
//       //     Bitmap mBitmap = (Bitmap) getIntent().getParcelableExtra("img");
//                Uri uri = Uri.parse(arguments.getString("img"));
//                labels.add(labels.size(),new ListAdapter(name, data, tags));
//                labelAdapter.notifyDataSetChanged();
//
//
//        }


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

        // слушатель выбора в списке
////        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
////
////                // получаем выбранный пункт
////                ListAdapter selectedState = (ListAdapter) parent.getItemAtPosition(position);
////                Toast.makeText(getApplicationContext(), "Был выбран пункт " + selectedState.getName(),
////                        Toast.LENGTH_SHORT).show();
////            }
////        };
////        labelsList.setOnItemClickListener(itemListener);


        FloatingActionButton fab_plus =  findViewById(R.id.fab2);
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intObj = new Intent(Main2Activity.this, CreateLabel.class);
                startActivity(intObj);
            }
              //  return false;

        });


    }
//    private static final String URL = "http://triip.yasya.top:80/api/v1/marks";
//    public void createList(final ArrayList<ListAdapter> labels) {
//
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
////                            labelsList = (ListView) findViewById(R.id.list);
////                            Adapter labelAdapter = new Adapter(this, R.layout.list_item, labels);
////
////                            labelsList.setAdapter(labelAdapter);
//                            for(int i=0; i<response.length();i++) {
//                                // Parsing json object response
//                                // response will be a json object
//                                String name = response.getString("title");
//                                String car_number = response.getString("car_number");
//                                String tags = response.getString("tags");
//                               // JSONObject phone = response.getJSONObject("tags");
//                               // String home = phone.getString("tags");
//
//                                labels.add(0,new ListAdapter(name, "", tags));
//                                labelAdapter.notifyDataSetChanged();
//                            }
//                          //  txtResponse.setText(jsonResponse);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, "LIST");
//    }
      //  @Override
        //public Dialog onCreateDialog(Bundle savedInstanceState) {




   /* private void setInitialData(){

        labels.add(new ListAdapter ("Бразилия", "Бразилиа", "ааа", R.drawable.hhh));
        labels.add(new ListAdapter ("Аргентина", "Буэнос-Айрес", "fff",  R.drawable.blockf));

    }*/

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            final int co = getIntent().getIntExtra("count",0);
            Intent intObj= new Intent(Main2Activity.this, MainActivity.class);
            intObj.putExtra("count", 1);
            startActivity(intObj);
           // this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
