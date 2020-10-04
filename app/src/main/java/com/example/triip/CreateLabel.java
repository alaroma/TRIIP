package com.example.triip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.openalpr.jni.Alpr;
import com.openalpr.jni.AlprException;
import com.openalpr.jni.AlprResults;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateLabel extends AppCompatActivity {
    private String ANDROID_DATA_DIR;
    private ImageView imageView;
    private TextView data_add_label;
    private EditText num_rasp;
    private EditText comment_field;
    private File destination;
    private ImageView btnMap;
    private final Handler handler = new Handler();
    private String mCurrentPhotoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.activity_create_label);

        imageView = findViewById(R.id.imageView);
        num_rasp = findViewById(R.id.num_rasp);
        data_add_label = findViewById(R.id.data_add_label);
        btnMap = findViewById(R.id.btnMap);
        comment_field = findViewById(R.id.comment_field);
        ImageButton btnCam = findViewById(R.id.btnCam);

        ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;

        btnMap.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intObj = new Intent(CreateLabel.this, MapCreateLabel.class);
                startActivity(intObj);
            }
        });

        final ImageButton btnRotate = findViewById(R.id.btnRotate);
        btnRotate.setVisibility(View.INVISIBLE);

        btnRotate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                imageView.animate().rotationBy(90);
                btnRotate.setEnabled(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //чтоб она была снова доступна
                        btnRotate.setEnabled(true);
                    }
                }, 800);

            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 0);
            }
        });

        ImageButton btnEdit = findViewById(R.id.btnEdit);
        num_rasp.setEnabled(false);

        btnEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                num_rasp.setEnabled(true);
            }
        });

        ImageButton btnGal = findViewById(R.id.btnGal);
        btnGal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);
            }
        });

        final MultiAutoCompleteTextView mp = findViewById(R.id.mp);
        final int count = getIntent().getIntExtra("count",0);
        final double lat = getIntent().getDoubleExtra("lat", 0.0);
        final double lng = getIntent().getDoubleExtra("lng", 0.0);
        if (count!=0) {

            Geocoder geocoder = new Geocoder(CreateLabel.this);
            List<Address> adr = new ArrayList<>();
            try {
                adr = geocoder.getFromLocation(lat, lng, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (0 != adr.size()) {
                Address address = adr.get(0);
                String add = address.getAddressLine(0);

                mp.setText(add);
            }
            else Toast.makeText(getApplicationContext(), "Что-то пошло не так. Попробуйте еще раз!", Toast.LENGTH_LONG).show();
        }

        final TextInputEditText name = findViewById(R.id.name_of_label);
        final TextInputEditText tags_of_lab = findViewById(R.id.tag);
        Button btnAddLabel = findViewById(R.id.btnAddLabel);

        btnAddLabel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String namelab = name.getText().toString();
                String data = data_add_label.getText().toString();
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
                                Double.toString(lat), Double.toString(lng));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    intObj.putExtra("name", namelab);
                    intObj.putExtra("data", data);
                    intObj.putExtra("tags", tags);
                    //  intObj.putExtra("code",code);
                    // intObj.putExtra("img", bitmap);
                    //intObj.putExtra("img", imageView.toString());
                    // Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    // imageLoader(bitmap);
                    startActivity(intObj);
                }
            }
        });
    }

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
                    try {
                        takePicture();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    try {
                        takePicture();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    public void takePicture() throws IOException {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            //File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "OpenALPR");

            if (!folder.exists()) {
                folder.mkdir();
            }
            String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
            destination = new File(folder, name + ".jpg");
            //File.createTempFile(name,".jpg", folder);

            mCurrentPhotoPath = destination.getAbsolutePath();

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(CreateLabel.this, BuildConfig.APPLICATION_ID + ".provider", destination));
            startActivityForResult(cameraIntent, 0);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        if(mCurrentPhotoPath!=null) {
            File file = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }
        else {
            Toast.makeText(CreateLabel.this, "Изображение не удалось сохранить :(", Toast.LENGTH_SHORT).show();
        }
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
                    galleryAddPic();
                    break;

                case 1:
                    Uri selectedImage = imageReturnedIntent.getData();
                    String Path = getRealPathFromURI(selectedImage);
                    String[] img = Path.split("\\.");

                    if (!img[1].equals("png")) {
                        Raspoz(1, selectedImage, Path);
                        showExif(selectedImage);
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

    String str, str_data;
    SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy, HH:mm", Locale.getDefault());
    @RequiresApi(api = Build.VERSION_CODES.N)
    void showExif(Uri photoUri){
        if(photoUri != null){
            try {
                InputStream inputStream = getContentResolver().openInputStream(photoUri);
                assert inputStream != null;
                ExifInterface exifInterface = new ExifInterface(inputStream);
                str = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
                if(str!= null) {
                    dateformat_(str);
                }
                else data_add_label.setText("Нет данных");
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            data_add_label.setText("Нет данных");
        }
    }
    void dateformat_(String str) {
        String[] dateArray = str.replaceAll(":"," ").split(" ");
        str_data =dateArray[2] + "." + dateArray[1] + "." + dateArray[0] + ", " + dateArray[3]+ ":" + dateArray[4];
        data_add_label.setText(str_data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void showExifcam(){
        Date dateNow = new Date();
        data_add_label.setText(ft.format(dateNow));
    }

    ProgressDialog progress;
    @SuppressLint("StaticFieldLeak")
    void Raspoz(final int i, final Uri selectedImage, final String path) {
        data_add_label.setText("");
        progress = ProgressDialog.show(CreateLabel.this, "Подождите", "Идет распознавание...", true);
        final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "config"+ File.separatorChar + "openalpr.conf";
        String str = ANDROID_DATA_DIR + File.separatorChar + "runtime_data";
        final Alpr alpr = new Alpr(CreateLabel.this, this.getApplicationInfo().dataDir, "eu", openAlprConfFile, str);
        // boolean t3 = alpr.isLoaded();
        alpr.setDetectRegion(false);
        final ImageButton btnRotate = findViewById(R.id.btnRotate);

        new AsyncTask<Void, Void, AlprResults>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.show();
            }

            @Override
            protected AlprResults doInBackground(Void... params) {
                AlprResults result = null;

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
                progress.dismiss();

                if (result == null || result.getPlates() == null || result.getPlates().size() == 0) {
                    data_add_label.setText("");
                    num_rasp.setText("");
                    imageView.setImageResource(android.R.color.transparent);
                    btnRotate.setVisibility(View.INVISIBLE);
                    Toast.makeText(CreateLabel.this, "Некорректное распознавание!", Toast.LENGTH_LONG).show();
                } else {
                    if(i == 0) Picasso.with(CreateLabel.this).load(destination).into(imageView);
                    if (i==1 && selectedImage!=null)  {
                        imageView.setImageURI(selectedImage);
                    }
                    btnRotate.setVisibility(View.VISIBLE);
                    //  num_rasp.setText(result.getPlates().get(0).getBestPlate().getCharacters());
                    int h = result.getPlates().size();
                    //выбор номера по самой большой точности
                    double[] conf = new double[h];
                    int indexOfMax = 0;
                    for(int i=0;i<h;i++){
                        conf[i] = result.getPlates().get(i).getBestPlate().getOverallConfidence();
                        if (conf[i] > conf[indexOfMax])
                        {
                            indexOfMax = i;
                        }
                    }
                    num_rasp.setText(result.getPlates().get(indexOfMax).getBestPlate().getCharacters());
                    //comment_field.setText(conf[indexOfMax]+"");
                    // result.getRegionsOfInterest().get(0).getX();
                }
            }

        }.execute();
    }

    private static final String URL_FOR_REGISTRATION = "http://triip.yasya.top:80/api/v1/marks";

    private void createLabel(final String name,  final String number, final String tags, final String data,
                             final String lat, final String lng) throws JSONException {

        JSONObject postparams = new JSONObject();
        postparams.put("title", name);
        postparams.put("car_number", number);
        postparams.put("tags", tags);
        postparams.put("mark_time", data);
        postparams.put("lat", lat);
        postparams.put("lng", lng);

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
}
