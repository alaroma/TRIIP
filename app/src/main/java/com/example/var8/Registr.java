package com.example.var8;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

//import com.google.android.gms.common.api.Response;

public class Registr extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "http://triip.yasya.top:80/api/v1/users";
    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword, signupInputAge;
    private Button btnSignUp;
    private Button btnLinkLogin;
    private RadioGroup genderRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.registr);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        signupInputAge = (EditText) findViewById(R.id.signup_input_age);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
//        btnLinkLogin = (Button) findViewById(R.id.btn_link_login);

        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

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
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void submitForm() {

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        String gender;
        if (selectedId == R.id.female_radio_btn)
            gender = "male";
        else
            gender = "female";

        if (signupInputName.getText().toString().length() == 0) {
            signupInputName.setError("Это поле обязательно для заполнения!");
        }
        if (signupInputEmail.getText().toString().length() == 0) {
            signupInputEmail.setError("Это поле обязательно для заполнения!");
        }
        if (signupInputPassword.getText().toString().length() == 0) {
            signupInputPassword.setError("Это поле обязательно для заполнения!");
        }
        if (signupInputAge.getText().toString().length() == 0) {
            signupInputAge.setError("Это поле обязательно для заполнения!");
        }

        if (signupInputName.getText().toString().length() > 0 && signupInputEmail.getText().toString().length() > 0 && signupInputPassword.getText().toString().length() > 0 &&
                signupInputAge.getText().toString().length() > 0) {


            try {
                createUser(signupInputName.getText().toString(),
                        signupInputEmail.getText().toString(),
                        signupInputPassword.getText().toString(),
                        signupInputAge.getText().toString(),gender);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        registerUser(signupInputName.getText().toString(),
//                signupInputEmail.getText().toString(),
//                signupInputPassword.getText().toString(),
//                gender,
//                signupInputAge.getText().toString());
        }
    }

    //
    private void createUser ( final String name, final String number, final String tags,
                              final String mp, final String gender) throws JSONException {


        JSONObject postparams = new JSONObject();
        postparams.put("name", name);
        postparams.put("email", number);
        postparams.put("password", tags);
        postparams.put("nick", mp);
        postparams.put("sex", gender);
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, "GOOD");
    }

//    private void showDialog() {
//        if (!progressDialog.isShowing())
//            progressDialog.show();
//    }
//
//    private void hideDialog() {
//        if (progressDialog.isShowing())
//            progressDialog.dismiss();
//    }


}