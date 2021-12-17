package com.example.unaddict;

import static com.example.unaddict.MainActivity.adapter;
import static com.example.unaddict.MainActivity.id;
import static com.example.unaddict.MainActivity.l;
import static com.example.unaddict.MainActivity.textToSpeech;
import static com.example.unaddict.msgAdapter.flg;
import static com.example.unaddict.msgAdapter.orderId;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Order return
 * Guarrenty
 * Warrenty
 */

public class Services {
    Context CurrentViewContext;
    View view;
    JsonArray responseArray;
    Integer UserID = 1; // so order 1 to 5;
    String BLANK_STRING = "";
    DatabaseReference databaseReference;
    String dateWarranty="";
    public static boolean ismethodQR;
   // Date dateW;

    public Services() {
        responseArray = new JsonArray();
    }
    public Services(Context currentViewContext, View view) {
        CurrentViewContext = currentViewContext;
        this.view = view;
        responseArray = new JsonArray();
    }



    public  void AvailWarranty() throws ParseException {
        if(flg==1)
        {

            Toast.makeText(CurrentViewContext.getApplicationContext(), orderId, Toast.LENGTH_SHORT).show();
            databaseReference= FirebaseDatabase.getInstance().getReference().child(orderId).child("dateW");

            /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProductInfo productInfo=snapshot.getValue(ProductInfo.class);
                    assert productInfo != null;
                    try{
                    String dateWarranty1=productInfo.dateW;}
                    catch(Exception e){
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(view.getContext(), dateWarranty1, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });*/
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dateWarranty= Objects.requireNonNull(snapshot.getValue()).toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateWithoutTime = null;
                    try {
                        dateWithoutTime = sdf.parse(sdf.format(new Date()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(view.getContext(), dateWithoutTime.toString(), Toast.LENGTH_SHORT).show();
                    Date dateW= null;//date of warranty stored in database must be written instead of "11/10/2021"/
                    try {
                        dateW = sdf.parse(dateWarranty);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(view.getContext(), dateW.toString(), Toast.LENGTH_SHORT).show();
                    if(dateWithoutTime.before(dateW) ||dateWithoutTime.equals(dateW))
                    {//Toast.makeText(CurrentViewContext.getApplicationContext(), "ok then correct", Toast.LENGTH_SHORT).show();
                        l.add(new msgModel("Your product is within warranty period, so what you would like to do now?","replace","get refund","","","",false));
                        MainActivity.adapter.notifyDataSetChanged();
                        MainActivity.textToSpeech.speak("Your product is within warranty period, so what you would like to do now?", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else
                    {
                        l.add(new msgModel("Sorry to say, but your product is not under warranty period so no further actions of return or refund can be performed on this.",false));
                      l.add( new msgModel("Thank You for using CARE BOT. Would you like to proceed further with any other query?" , "Yes", "No","","","",false));
                        //l.add( new msgModel("Thank You for using CARE BOT. Do you have any other query?", "Yes I have a query", "No I don't have","","","",false));
                        MainActivity.adapter.notifyDataSetChanged();
                        MainActivity.textToSpeech.speak("Sorry to say, but your product is not under warranty period so no further actions of return or refund can be performed on this. Thank You for using CARE BOT. Would you like to proceed further with any other query?",TextToSpeech.QUEUE_FLUSH,null);
                        //l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
                        MainActivity.adapter.notifyDataSetChanged();
                        //MainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    flg=0;
                    Toast.makeText(view.getContext(), dateWarranty, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            //tring getCurrentDateTime = sdf.format(new Date());

        }
        else
        {
            httpCall("https://fakestoreapi.com/products/");
        }

    }

    public  void trackPrevious(){
        // l.add(new msgModel("Track previous requests","","","","","",true));

    }
    public  void requestCall(){
        //l.add(new msgModel("Request Call from executive","","","","","",true));
        l.add(new msgModel("Call request made successfully",BLANK_STRING,BLANK_STRING,BLANK_STRING,BLANK_STRING,BLANK_STRING,false));
        l.clear();
        l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.textToSpeech.speak("Call request made successfully", TextToSpeech.QUEUE_FLUSH, null);

    }

    public void reply_creator(String bot_msg, String btn1_text, String btn2_text, String btn3_text,String btn4_text,String btn5_text){
        l.add(new msgModel(bot_msg,btn1_text,btn2_text,btn3_text,btn4_text,btn5_text,false));
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.textToSpeech.speak(bot_msg, TextToSpeech.QUEUE_FLUSH, null);
    }
    static JsonArray jsonArray;
    public void httpCall(String url) {
        RequestQueue queue = Volley.newRequestQueue(CurrentViewContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String reply) {
                        add_bt(JsonParser.parseString(reply).getAsJsonArray());
                        jsonArray=JsonParser.parseString(reply).getAsJsonArray();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CurrentViewContext, R.string.error+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public static Map<String,Integer> responseMapping=new HashMap<>();
    private void add_bt(JsonArray responseA){
        reply_creator("Please select the order for performing the action you have selected above",
                responseA.get(0).getAsJsonObject().get("title").toString(),
                responseA.get(1).getAsJsonObject().get("title").toString(),
                responseA.get(2).getAsJsonObject().get("title").toString(),
                responseA.get(3).getAsJsonObject().get("title").toString(),
                responseA.get(4).getAsJsonObject().get("title").toString()
        );

        for(int i=0;i<20;i++){
            responseMapping.put(responseA.get(i).getAsJsonObject().get("title").toString(),Integer.parseInt(responseA.get(i).getAsJsonObject().get("id").toString()));
        }

    }

    public void getActions() {

        //l.add(new msgModel("Order Related",true));
      //  l.add(new msgModel("Please select your problem from options below","object delivered is defective","return","damaged within warranty period","Request Call from executive","",false));
        //l.add(new msgModel("","","","","","",true));

            if(flg==1)
            {
                l.add(new msgModel("Please select your problem from options below","object delivered is defective","return","damaged within warranty period","Request Call from executive","",false));
                adapter.notifyDataSetChanged();
                MainActivity.textToSpeech.speak("Please select your problem from options below", TextToSpeech.QUEUE_FLUSH, null);
                flg=0;
            }
            else {
                l.add(new msgModel("Would you like to proceed with QR code scanning or with the current method of messages?", "QR code scanning", "messages", "", "", "", false));
                MainActivity.adapter.notifyDataSetChanged();
                //
                MainActivity.textToSpeech.speak("Would you like to proceed with QR code scanning or with the current method of messages?", TextToSpeech.QUEUE_FLUSH, null);
            }


    }


    public void getgeneralQuery()
    {
        String url = "https://www.flipkart.com/helpcentre";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        view.getContext().startActivity(i);
        l.clear();
        l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
        MainActivity.adapter.notifyDataSetChanged();
    }

    public void defective() {
        //l.add(new msgModel("defective","","","","","",true));


        if(flg==1)
        {
            l.add(new msgModel("Please upload the photo of your defective object by clicking on camera icon below",false));
            MainActivity.adapter.notifyDataSetChanged();
            MainActivity.textToSpeech.speak("Please upload the photo of your defective object by clicking on camera icon below", TextToSpeech.QUEUE_FLUSH, null);
            flg=0;
        }
        else{
            httpCall("https://fakestoreapi.com/products/");
            }
    }

    public void returnAction() {

        if(flg==1)
        {
            l.add(new msgModel("Your request for returning object is initiated, shortly you will receive a message from flipkart containing the pickup date and name of our agent.","","","","","",false));
            l.add(new msgModel("Our agent will perform all necessary checks, if it seems to be ok then your refund will be processed in 2 to 4 working days from that day","","","","","",false));
           // l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
            //l.add( new msgModel("Thank You for using CARE BOT. Do you have any other query?" , "Yes I have a query", "No I don't have","","","",false));
            l.add( new msgModel("Thank You for using CARE BOT. Would you like to proceed further with any other query?" , "Yes", "No","","","",false));
            MainActivity.adapter.notifyDataSetChanged();
            MainActivity.textToSpeech.speak("Your request for returning object is initiated, shortly you will receive a message from flipkart containing the pickup date and name of our agent.Our agent will perform all necessary checks, if it seems to be ok then your refund will be processed in 2 to 4 working days from that day.Thank You for using CARE BOT. Would you like to proceed further with any other query?", TextToSpeech.QUEUE_FLUSH, null);



            //MainActivity.adapter.notifyDataSetChanged();
           // MainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);
            flg=0;

        }
        else{
            httpCall("https://fakestoreapi.com/products/");
        }
    }



    public void QRgetActions()
    {
        l.add(new msgModel("Please click on the QR code icon on top right corner and scan the QR code on your bill",false));
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.textToSpeech.speak("Please click on the QR code icon on top right corner and scan the QR code on your bill", TextToSpeech.QUEUE_FLUSH, null);
        //ismethodQR=true;

    }
    public void QRdefective()
    {
        l.add(new msgModel("Please upload the photo of your defective object by clicking on camera icon below",false));
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.textToSpeech.speak("Please upload the photo of your defective object by clicking on camera icon below", TextToSpeech.QUEUE_FLUSH, null);
    }
    public void QRreturnAction()
    {
        l.add(new msgModel("Your request for returning object is initiated, shortly you will receive a message from flipkart containing the pickup date and name of our agent.",false));
        l.add(new msgModel("Our agent will perform all necessary checks, if it seems to be ok then your refund will be processed in 2 to 4 working days from that day",false));
        //l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
        //l.add( new msgModel("Thank You for using CARE BOT. Do you have any other query?" , "Yes I have a query", "No I don't have","","","",false));
        l.add( new msgModel("Thank You for using CARE BOT. Would you like to proceed further with any other query?" , "Yes", "No","","","",false));
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.textToSpeech.speak("Your request for returning object is initiated, shortly you will receive a message from flipkart containing the pickup date and name of our agent.Our agent will perform all necessary checks, if it seems to be ok then your refund will be processed in 2 to 4 working days from that day. Thank You for using CARE BOT. Would you like to proceed further with any other query?", TextToSpeech.QUEUE_FLUSH, null);


       // MainActivity.adapter.notifyDataSetChanged();
       // MainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);
    }

    public  void QRrequestCall(){
        //l.add(new msgModel("Request Call from executive","","","","","",true));
        l.add(new msgModel("Call request made successfully",BLANK_STRING,BLANK_STRING,BLANK_STRING,BLANK_STRING,BLANK_STRING,false));
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.textToSpeech.speak("Call request made successfully", TextToSpeech.QUEUE_FLUSH, null);
        l.clear();
        l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
        MainActivity.adapter.notifyDataSetChanged();
        //MainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);

    }
    public  void QRavailWarranty() throws ParseException {

        int id1=Integer.parseInt(id) -1;
        String id2= String.valueOf(id1);
        Toast.makeText(CurrentViewContext.getApplicationContext(), id2, Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(id2).child("dateW");

            /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProductInfo productInfo=snapshot.getValue(ProductInfo.class);
                    assert productInfo != null;
                    try{
                    String dateWarranty1=productInfo.dateW;}
                    catch(Exception e){
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(view.getContext(), dateWarranty1, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });*/
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dateWarranty = Objects.requireNonNull(snapshot.getValue()).toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateWithoutTime = null;
                try {
                    dateWithoutTime = sdf.parse(sdf.format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(view.getContext(), dateWithoutTime.toString(), Toast.LENGTH_SHORT).show();
                Date dateW = null;//date of warranty stored in database must be written instead of "11/10/2021"/
                try {
                    dateW = sdf.parse(dateWarranty);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), dateW.toString(), Toast.LENGTH_SHORT).show();
                if (dateWithoutTime.before(dateW) || dateWithoutTime.equals(dateW)) {//Toast.makeText(CurrentViewContext.getApplicationContext(), "ok then correct", Toast.LENGTH_SHORT).show();
                    l.add(new msgModel("Your product is within warranty period, so what you would like to do now?", "replace", "get refund", "", "", "", false));
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.textToSpeech.speak("Your product is within warranty period, so what you would like to do now?", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    l.add(new msgModel("Sorry to say, but your product is not under warranty period so no further actions of return or refund can be performed on this.", false));
                    //l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
                    //l.add( new msgModel("Thank You for using CARE BOT. Do you have any other query?" , "Yes I have a query", "No I don't have","","","",false));
                    l.add( new msgModel("Thank You for using CARE BOT. Would you like to proceed further with any other query?" , "Yes", "No","","","",false));
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.textToSpeech.speak("Sorry to say, but your product is not under warranty period so no further actions of return or refund can be performed on this. Thank You for using CARE BOT. Would you like to proceed further with any other query?", TextToSpeech.QUEUE_FLUSH, null);
                   // l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
                    //MainActivity.adapter.notifyDataSetChanged();
                    //MainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);
                }
                Toast.makeText(view.getContext(), dateWarranty, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        //tring getCurrentDateTime = sdf.format(new Date());


    }

    public void replaceAction()
    {
        l.add(new msgModel("Your request for replacing object is initiated, shortly you will receive a message from flipkart containing the pickup date and name of our agent.",false));
        MainActivity.adapter.notifyDataSetChanged();
        l.add(new msgModel("Our agent will visit your address and will perform all needful checks and after that we will send you the confirmation message on your registered mail id",false));
        MainActivity.adapter.notifyDataSetChanged();
        //l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
       // l.add( new msgModel("Thank You for using CARE BOT. Do you have any other query?" , "Yes I have a query", "No I don't have","","","",false));
        l.add( new msgModel("Thank You for using CARE BOT. Would you like to proceed further with any other query?" , "Yes", "No","","","",false));
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.textToSpeech.speak("Your request for replacing object is initiated, shortly you will receive a message from flipkart containing the pickup date and name of our agent. " +
                "Our agent will visit your address and will perform all needful checks and after that we will send you the confirmation message on your registered mail ID. Thank You for using CARE BOT. Would you like to proceed further with any other query?",TextToSpeech.QUEUE_FLUSH,null);
       // l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
        //MainActivity.adapter.notifyDataSetChanged();
        //MainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);
    }
}