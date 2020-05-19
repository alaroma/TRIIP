package com.example.var8;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.openalpr.jni.Alpr;
import com.openalpr.jni.AlprException;
import com.openalpr.jni.AlprPlateResult;
import com.openalpr.jni.AlprResults;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CreateLabel extends AppCompatActivity {
   // private static final int CAMERA = 1;
    private String ANDROID_DATA_DIR;
    private ImageView imageView;
    private TextView textView;
    private EditText num_rasp;
    private EditText editText;
    private static File destination;
   // private Fragment mp;
    // ProgressDialog prdi;
    private ImageView mapButton;
    private int count = 0;
//    String namelab;
    private final Handler handler = new Handler();
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.activity_create_label);
        imageView = findViewById(R.id.imageView2);
        num_rasp = findViewById(R.id.num_rasp);
        textView = findViewById(R.id.textView3);
        mapButton = findViewById(R.id.imageView5);
        editText = findViewById(R.id.editText);
       // mp = findViewById(R.id.mp);
        //textView = findViewById(R.id.textView);
        //Button button = findViewById(R.id.button);
        //Button camera = findViewById(R.id.camera);
        ImageButton camera = findViewById(R.id.imageButton);
        ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;

        mapButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent intObj = new Intent(CreateLabel.this, MapCreateLabel.class);
                startActivity(intObj);
            }
        });

        final ImageButton be = findViewById(R.id.be);
        be.setVisibility(View.INVISIBLE);

        be.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                    imageView.animate().rotationBy(90);
                be.setEnabled(false);
               handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //чтоб она была снова доступна
                        be.setEnabled(true);
                    }
                }, 800);
                    
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                checkPermission(Manifest.permission.CAMERA, 0);

            }
        });

        ImageButton edit_num = findViewById(R.id.buttonEdit);
        num_rasp.setEnabled(false);

        edit_num.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // num_rasp.setFocusableInTouchMode(true);
                num_rasp.setEnabled(true);
            }
        });

        ImageButton galery = findViewById(R.id.imageButton3);
        galery.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);
            }
        });
     //   button.setOnClickListener(new View.OnClickListener() {

         //   public void onClick(View view) {
                //  takePicture();
       //         checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
              //  Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
               // photoPickerIntent.setType("image/*");
                //startActivityForResult(photoPickerIntent, 1);
           // }
        //});

        final MultiAutoCompleteTextView mp = findViewById(R.id.mp);
        final int co = getIntent().getIntExtra("count",0);
        final double txt = getIntent().getDoubleExtra("lat", 0.0);
        final double txt2 = getIntent().getDoubleExtra("lng", 0.0);
        if (co!=0) {

            Geocoder geocoder = new Geocoder(CreateLabel.this);
            List<Address> adr = new ArrayList<>();
            try {
                adr = geocoder.getFromLocation(txt, txt2, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (0 != adr.size()) {
                Address address = adr.get(0);
                //  String add = address.getAddressLine(0) + ", " + address.getLocality();
                String add = address.getAddressLine(0);

                mp.setText(add);

            }
            else Toast.makeText(getApplicationContext(), "Что-то пошло не так. Попробуйте еще раз!", Toast.LENGTH_LONG).show();

        }
//
        final TextInputEditText name = findViewById(R.id.name_of_label);
        final TextInputEditText tags_of_lab = findViewById(R.id.tag);
        Button addlabel = findViewById(R.id.addlabel);
        // final String strin = name.getText().toString();
        addlabel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                 String namelab = name.getText().toString();
                String data = textView.getText().toString();
                String tags = tags_of_lab.getText().toString();



                Intent intObj = new Intent(CreateLabel.this, Main2Activity.class);

                if(tags_of_lab.getText().toString().length()==0) {
                    tags_of_lab.setError("Это поле обязательно для заполнения!");
                }
                if(name.getText().toString().length()==0) {
                    name.setError("Это поле обязательно для заполнения!");
                }
                if (num_rasp.getText().toString().length()==0) {
                    num_rasp.setError("Это поле обязательно для заполнения!");
                }
                if (mp.getText().toString().length()==0){
                    mp.setError("Это поле обязательно для заполнения!");
                }
                if (imageView.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), "Прикрепите изображение!", Toast.LENGTH_LONG).show();
                }
                if(mp.getText().toString().length()>0&&num_rasp.getText().toString().length()>0&&name.getText().toString().length()>0&&
                        tags_of_lab.getText().toString().length()>0&&imageView.getDrawable()!=null) {


                   try {
                        createLabel(name.getText().toString(),
                                num_rasp.getText().toString(),
                                tags_of_lab.getText().toString(), data,
                                Double.toString(txt), Double.toString(txt2)); //txt txt2 double!!!
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    intObj.putExtra("name", namelab);
                    intObj.putExtra("data", data);
                    intObj.putExtra("tags", tags);
                    //  intObj.putExtra("code",code);
                    // intObj.putExtra("img", bitmap);
                    intObj.putExtra("img", imageView.toString());
//
                    startActivity(intObj);
                    // Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//                    imageLoader(bitmap);




                }
            }
        });

//        sPref = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.clear().commit();
    }

//    @Override
  //  protected void onSaveInstanceState(Bundle outState) {

    //    final TextInputEditText name = findViewById(R.id.name_of_label);
      //  String t = name.getText().toString();
        //outState.putString("name_lab", t);
        //super.onSaveInstanceState(outState);
    //}


    //public void onRestoreInstanceState(Bundle savedInstanceState) {
      //  super.onRestoreInstanceState(savedInstanceState);

       //String mCurrentScore = savedInstanceState.getString("name_lab");

      //  mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
        //final EditText name = findViewById(R.id.num_rasp);
        //name.setText(mCurrentScore);
        // Always call the superclass so it can restore the view hierarchy

       // name.setText(savedInstanceState.getString("name_lab", ""));

        // Restore state members from saved instance
      //  mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
        //mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
    //}
//        @Override
//        public void onPause() {
//            super.onPause();
//           // if(destination!=null) {
//                saveText();
//            //}
//        }
//    void saveText() {
//        sPref = getPreferences(MODE_PRIVATE);
//        final TextInputEditText name = findViewById(R.id.name_of_label);
//        final TextInputEditText tags_of_lab = findViewById(R.id.tag);
//        SharedPreferences.Editor ed = sPref.edit();
//     //   ed.putString("img", String.valueOf(imageView.getDrawable()))/;
//        //if(destination!= null) {
//
//            ed.putString("num_rasp", num_rasp.getText().toString());
//            ed.putString("name_lab", name.getText().toString());
//            ed.putString("tags_lab", tags_of_lab.getText().toString());
//            ed.putString("data_lab", textView.getText().toString());
//            ed.putString("comments", editText.getText().toString());
//         //   ed.putString("img", destination.getAbsolutePath());
//        //}
//    ed.commit();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        final TextInputEditText name = findViewById(R.id.name_of_label);
//        final TextInputEditText tags_of_lab = findViewById(R.id.tag);
//        //if(destination!= null) {
//            loadText(name, textView, tags_of_lab, num_rasp, editText, imageView);
//        //}
//    }
//
//    void loadText(TextInputEditText name, TextView data, TextInputEditText tags_of_lab, EditText num_rasp, EditText comments, ImageView photo) {
//        sPref = getPreferences(MODE_PRIVATE);
//        String savedText = sPref.getString("num_rasp", "");
//        String savedText2 = sPref.getString("name_lab", "");
//        String savedText3 = sPref.getString("data_lab", "");
//        String savedText4 = sPref.getString("tags_lab", "");
//        String savedText5= sPref.getString("comments", "");
////        String saveImg = sPref.getString("img","");
//        num_rasp.setText(savedText);
//        name.setText(savedText2);
//        tags_of_lab.setText(savedText4);
//        data.setText(savedText3);
//        comments.setText(savedText5);
  //      Picasso.with(CreateLabel.this).load(saveImg).into(photo);
       // photo.setImageURI(saveImg);
        //photo.setImageDrawable();
  //      ed.clear().

 //   }

    //@Override
   // protected void onStop() {

      //  super.onStop();
      //  final TextInputEditText name = findViewById(R.id.name_of_label);
       // final TextInputEditText tags_of_lab = findViewById(R.id.tag);
        //num_rasp.setText("");
        //name.setText("");
        //tags_of_lab.setText("");
        //textView.setText("");
        //editText.setText("");
        //imageView.setImageResource(android.R.color.transparent);

    //}

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

        return df.format(date);
    }

    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            switch (requestCode) {
                case 1: {
                    choosePhoto();
                    break;
                }
                case 0: {
                    takePicture();
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(CreateLabel.this, "Разрешите доступ к камере!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    Toast.makeText(CreateLabel.this, "Разрешите доступ к памяти!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default: break;
        }
    }

public void choosePhoto() {
    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
     photoPickerIntent.setType("image/*");
    startActivityForResult(photoPickerIntent, 1);
}
    public void takePicture() {
        // Use a folder to store all results
        File folder = new File(Environment.getExternalStorageDirectory() + "/OpenALPR/");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Generate the path for the next photo
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(folder, name + ".jpg");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(cameraIntent, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 0:

                    Raspoz(0, null, null);
                    showExifcam();

                    break;
                case 1:
                    Uri selectedImage2 = imageReturnedIntent.getData();
                    String Path = getRealPathFromURI(selectedImage2);
                    //  str.replaceAll(":"," ");
                    String[] img = Path.split("\\.");

                    if (!img[1].equals("png")) {

                        Raspoz(1, selectedImage2, Path);
                        showExif(selectedImage2);

                    } else {
                        Toast.makeText(CreateLabel.this, "Неверный формат фото!", Toast.LENGTH_LONG).show();

                    }
                    break;
            }
        }
    }
    public String getRealPathFromURI(Uri uri) {
        if (uri == null) return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;

        }

        return uri.getPath();
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    String str, str2;
    SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy, HH:mm", Locale.getDefault());
    @RequiresApi(api = Build.VERSION_CODES.N)
    void showExif(Uri photoUri){
        if(photoUri != null){
            try {
                InputStream inputStream = getContentResolver().openInputStream(photoUri);
                assert inputStream != null;
                ExifInterface exifInterface = new ExifInterface(inputStream);
                //  datePh = ft.parse(str);
                //  ft.format(dateNow);
                // str2 = ft.format(dateNow);

                str = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
                //assert str != null;
                if(str!= null) {
                    dateformat_(str);
                }
                else textView.setText("Нет данных");

                //     Date dateNow = ft.parse(str);

            //    str2 = ft.format(date);

                inputStream.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
        else {
            textView.setText("Нет данных");
        }
    }
void dateformat_(String str) {
    //List<String> month = new ArrayList<>();
  //  str.replaceAll(":"," ");
    String[] dateArray = str.replaceAll(":"," ").split(" ");

    str2=dateArray[2] + "." + dateArray[1] + "." + dateArray[0] + ", " + dateArray[3]+ ":" + dateArray[4];
    textView.setText(str2);
}

    @RequiresApi(api = Build.VERSION_CODES.N)
    void showExifcam(){
        Date dateNow = new Date();

        textView.setText(ft.format(dateNow));
    }

    ProgressDialog progress;
    @SuppressLint("StaticFieldLeak")
    void Raspoz(final int i, final Uri selectedImage2, final String path) {
        textView.setText("");
        //  final String path_dir = path.replace("JPG", "jpg");
        //   final String path_dir = path;
        progress = ProgressDialog.show(this, "Подождите", "Идет распознавание...", true);
        final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "config"+ File.separatorChar + "openalpr.conf";
        String str = ANDROID_DATA_DIR + File.separatorChar + "runtime_data";
        final Alpr alpr = new Alpr(CreateLabel.this, this.getApplicationInfo().dataDir, "eu", openAlprConfFile, str);
        boolean t3 = alpr.isLoaded();
       // alpr.setTopN(10);
        // alpr.setDefaultRegion("");
        alpr.setDetectRegion(false);
        // progress.show();
        //  final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";


        final ImageButton be = findViewById(R.id.be);

      //  final ProgressDialog progress = null;
        new AsyncTask<Void, Void, AlprResults>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //      progress = new ProgressDialog(CreateLabel.this);
                  //    progress.setTitle("Подождите");
                    //  progress.setMessage("Идет распознавание...");
                     // progress.setIndeterminate(true);
                //if (!progress.isShowing() && progress!=null) {

                    progress.show();
            //   }
            }
            @Override
            protected AlprResults doInBackground(Void... params) {
                AlprResults result = null;

              //  String path = null;
                if(i==0) {
                    if (destination != null) {

                        String path = destination.getAbsolutePath();

                        try {
                            result = alpr.recognize(path);
                        } catch (AlprException e) {
                            e.printStackTrace();
                            // Toast.makeText(CreateLabel.this, "Некорректное распознавание!", Toast.LENGTH_LONG).show();

                        }
                    }
                }
              //  else {
                //    runOnUiThread(new Runnable(){
                  //      public void run() {
                           // Toast.makeText(getApplicationContext(), "Status = " + message.getBody() , Toast.LENGTH_LONG).show();
                    //        Toast.makeText(CreateLabel.this, "Некорректное распознавание!", Toast.LENGTH_LONG).show();

                      //  }
                    //});

                   // Toast.makeText(CreateLabel.this, "Некорректное распознавание!", Toast.LENGTH_LONG).show();

//                }

                if(i==1) {
                    try {
                        result = alpr.recognize(path);
                    } catch (AlprException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(AlprResults result) {
              //  if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                //}
               // if(destination==null){ textView.setText("");}
                //progress.dismiss();
              // progress.cancel();
                if (result == null || result.getPlates() == null || result.getPlates().size() == 0) {
                    textView.setText("");
                    num_rasp.setText("");
                    imageView.setImageResource(android.R.color.transparent);
                    be.setVisibility(View.INVISIBLE);
                    Toast.makeText(CreateLabel.this, "Некорректное распознавание!", Toast.LENGTH_LONG).show();
                } else {
                    if(i == 0) Picasso.with(CreateLabel.this).load(destination).into(imageView);
                    if (i==1 && selectedImage2!=null)  {
                        imageView.setImageURI(selectedImage2);

                    }
                    be.setVisibility(View.VISIBLE);
                  //  num_rasp.setText(result.getPlates().get(0).getBestPlate().getCharacters());

                    //выбор номера по самой большой точности
                    double[] conf = new double[10];
                  //  String[] res = new String[10];
                    int indexOfMax = 0;
                    for(int i=0;i<result.getPlates().size();i++){
                         conf[i] = result.getPlates().get(i).getBestPlate().getOverallConfidence();
                        if (conf[i] > conf[indexOfMax])
                        {
                            indexOfMax = i;
                        }
                    }

                    num_rasp.setText(result.getPlates().get(indexOfMax).getBestPlate().getCharacters());
                   // result.getRegionsOfInterest().get(0).getX();
                }
            }


        }.execute();

    }

    private static final String URL_FOR_REGISTRATION = "http://triip.yasya.top:80/api/v1/marks";

    private void createLabel(final String name,  final String number, final String tags, final String data,
                             final String lat, final String lng) throws JSONException {
//
//
        JSONObject postparams = new JSONObject();
        postparams.put("title", name);
        postparams.put("car_number", number);
        postparams.put("tags", tags);
        postparams.put("mark_time", data);
        postparams.put("lat", lat);
        postparams.put("lng", lng);
        //lat lng add!!
        //data add!!!


        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
               URL_FOR_REGISTRATION, postparams, new Response.Listener<JSONObject>() {
            //DownloadManager.?
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


}
//           AsyncTask.execute(new Runnable() {
//                                 //  String result;
//                                 AlprResults result = null;
//
//                                 @Override
//                                 public void run() {
//                                     switch (i) {
//                                         case 0:
//
//                                             //alpr.setDefaultRegion("wa");
//                                             // AlprResults results = alpr.recognize(imagedata);
//                                             // result =
//                                             //   result = Factory.create(MainActivity.this, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("us", "",  destination.getAbsolutePath(), openAlprConfFile, 10);
//                                             try {
//                                                 String t = destination.getAbsolutePath();
//                                                 //
//                                                 result = alpr.recognize(t);
//                                                 // textView.setText(("OpenALPR Version: " + alpr.getVersion()));
//                                             } catch (AlprException e) {
//                                                 e.printStackTrace();
//                                             }
//                                             break;
//                                         case 1:
//                                             //  result = OpenALPR.Factory.create(CreateLabel.this, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("us", "",  path_dir, openAlprConfFile, 10);
//                                             break;
//                                         default:
//                                     }
//
//
//                                     //try {
//                                     //   final AlprResults results = new JSON().fromJSON(result);
//                                     //  textView.setText("ttt");
//                                     // List<AlprResults> resultItems = result.getResultItems();
//                                     //textView.setText("OpenALPR Version: " + alpr.getVersion());
//                                     // AlprResults resultItem = resultItems.get(0);
//                                     runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             if (result == null) {
//                                                 textView.setText("Found " + result.getPlates().size() + " results");
//                                             } else
//                                                 textView.setText("Found " + result.getPlates().size() + " results");
//                                         }
//                                     });
//  Toast.makeText(MainActivity.this, "Некорректное распознавание!", Toast.LENGTH_LONG).show();
// resultTextView.setText("It was not possible to detect the licence plate.");
//} else {
//  textView.setText("yyy");
// textView.setText(result.getResults().get(0).getPlate());
//}
//        progress.dismiss();
//  }
//});

//catch (JsonSyntaxException exception) {
// final ResultsError resultsError = new Gson().fromJson(result, ResultsError.class);


//    });}}
//   });
// }
//}