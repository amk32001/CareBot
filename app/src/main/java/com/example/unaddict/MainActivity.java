package com.example.unaddict;

import static com.example.unaddict.msgAdapter.orderId;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    EditText type_user;
    ImageButton sendBtn,camera;
    public static TextToSpeech textToSpeech;
    Integer msgId = 12;
    public static msgAdapter adapter;
    public static ArrayList<msgModel> l = new ArrayList<>();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    public String title1="No value found";
    Services services=new Services();
    public static String id="";

    private IntentIntegrator qrScan;
    //int flg=0;

    /**
     * 1) Upload current request to database
     * 2) Retrieve data in list view(Track Previous Request)
     * 3) QR code handling after scanning.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.chat_window_rv);
        type_user = findViewById(R.id.type_msg);
        sendBtn = findViewById(R.id.sendBtn);
        camera=findViewById(R.id.cameraBut);



        //text to speech converter.
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        String blank="";

        l.clear();
        startfun();


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,upload_image.class);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = type_user.getText().toString();
                msgModel user_msg  = new msgModel(s,"","","","","",true);
                l.add(user_msg);
                type_user.setText("");
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new msgAdapter(l);
        //rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private void startfun()
    {
        l.add( new msgModel("Hi! I am CARE BOT. Please select your query from below" , "order related", "General Query","Track previous requests","","",false));
        //adapter.notifyDataSetChanged();
        try{
        MainActivity.textToSpeech.speak("Hi! I am CARE BOT. Please select your query from below",TextToSpeech.QUEUE_FLUSH,null);}
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.logout,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                firebaseAuth.signOut();
                Intent myIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.QR:
                //intializing scan object
                qrScan = new IntentIntegrator(this);

                qrScan.initiateScan();


        }

        return super.onOptionsItemSelected(item);
    }

    //taking voice as input.
    public void voicerecog(View view){
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi speak your query");
        try {
            startActivityForResult(intent,1);
        }
        catch(Exception e)
        {
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<String> result;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 1:
                if(resultCode==RESULT_OK && null!=data) {
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    msgModel user_message= new msgModel(result.get(0),"","","","","",true);
                    l.add(user_message);
                    //ModelMessageClass bot_message= new ModelMessageClass(result.get(0),false);

                    //modelMessageClassList.add(bot_message);
                    //textToSpeech.speak(result.get(0),TextToSpeech.QUEUE_FLUSH,null);



                    type_user.setText("");
                    adapter.notifyDataSetChanged();

                }
                break;
            default:
                try{
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (result != null) {
                        //if qrcode has nothing in it
                        if (result.getContents() == null) {
                            Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_LONG).show();
                        } else {
                            //if qr contains data
                            id=result.getContents();
                           // Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();

                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://fakestoreapi.com/products/"+id,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String reply) {
                                                //add_bt(JsonParser.parseString(reply).getAsJsonArray());
                                                JsonObject jsonObject;
                                                jsonObject=JsonParser.parseString(reply).getAsJsonObject();
                                                title1=jsonObject.get("title").toString();
                                                //Toast.makeText(MainActivity.this, title1, Toast.LENGTH_SHORT).show();
                                                l.add(new msgModel(title1,true));
                                                l.add(new msgModel("Please select your problem from options below","object delivered is defective","return","damaged within warranty period","Request Call from executive","",false));
                                                adapter.notifyDataSetChanged();
                                                MainActivity.textToSpeech.speak("Please select your problem from options below", TextToSpeech.QUEUE_FLUSH, null);

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), R.string.error+ error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                queue.add(stringRequest);

                            /*databaseReference.addListenerValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //title1=snapshot.getValue(String.class);
                                   //Toast.makeText(getApplicationContext(), snapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                                    l.add(new msgModel(snapshot.getValue(String.class),true));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });*/

                        }
                    } else {
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                }

        }
    }






   /* //firebase auth object
    private FirebaseAuth firebaseAuth;


    CardView CardLogout;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colourBlue1));
        }




        CardLogout=findViewById(R.id.cardLogout);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views

        //displaying logged in user name

        //adding listener to button


       /* CardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                Intent myIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(myIntent);

            }
        });*/



