package com.example.unaddict;

//import static com.example.cbotv01.Services.jsonArray;

import static com.example.unaddict.MainActivity.l;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class msgAdapter extends RecyclerView.Adapter<msgAdapter.ViewHolder> {
    private ArrayList<msgModel> list;
    public msgAdapter(ArrayList<msgModel> list){
        this.list = list;
    }
    public String previous="";
    public static int flg=0;
    public static String orderId="";
    String mobile="1800 202 9898";




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.msgView.setText(list.get(position).getMsg());
        btn_shower(holder.btn1, list.get(position).getBtn1_text());
        btn_shower(holder.btn2, list.get(position).getBtn2_text());
        btn_shower(holder.btn3, list.get(position).getBtn3_text());
        btn_shower(holder.btn4, list.get(position).getBtn4_text());
        btn_shower(holder.btn5, list.get(position).getBtn5_text());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class  ViewHolder extends RecyclerView.ViewHolder{
        Button btn1, btn2, btn3,btn4,btn5;
        TextView msgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn1 = itemView.findViewById(R.id.button);
            btn2 = itemView.findViewById(R.id.button2);
            btn3 = itemView.findViewById(R.id.button3);
            btn4 = itemView.findViewById(R.id.button4);
            btn5 = itemView.findViewById(R.id.button5);
            msgView = itemView.findViewById(R.id.msg_chat);
        }
    }

    /**
     * This function is assigning function to btn ; i.e function code written in Services.java and supposed to execute
     * will be assigned to onclick of btn based on text
     * */
    private void fun_selector(String btn_text, Button btn) {
        Services services = new Services(btn.getContext(), btn);
        // v->services its lambda ; it's basically as
        //        new View.OnClickListener()


        //MainActivity.adapter.notifyDataSetChanged();//causes error!!!!!

        if(btn_text == "damaged within warranty period"){

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));
                    previous=btn_text;
                    try {
                        if(Services.ismethodQR==true)
                            services.QRavailWarranty();
                        else
                            services.AvailWarranty();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }
        else if (btn_text=="Yes")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.clear();
                    l.add( new msgModel("Please select your query from below" , "order related", "General Query","Track previous requests","","",false));
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.textToSpeech.speak("Please select your query from below",TextToSpeech.QUEUE_FLUSH,null);

                }
            });

        }
        else if(btn_text=="No")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.add(new msgModel("Thank You",false));
                    MainActivity.textToSpeech.speak("Thank You",TextToSpeech.QUEUE_FLUSH,null);
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });

        }
        else if(btn_text == "Track previous requests"){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));

                    services.trackPrevious();
                    MainActivity.adapter.notifyDataSetChanged();

                    try{
                       // l.clear();
                        //l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
                        //MainActivity.adapter.notifyDataSetChanged();
                        v.getContext().startActivity(new Intent(v.getContext(),TrackPreviousRequest.class));}
                    catch(Exception e)
                    {
                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(btn_text == "Request Call from executive"){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));

                   services.requestCall();
                  MainActivity.adapter.notifyDataSetChanged();

                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+ mobile));

       /* if (ActivityCompat.checkSelfPermission(v.getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/
                    try{
                   v.getContext().startActivity(callIntent);}
                    catch (Exception e){
                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(btn_text=="order related")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));

                    services.getActions();
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }
        else if(btn_text=="General Query")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));

                    services.getgeneralQuery();
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }
        else if(btn_text=="object delivered is defective")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));
                    previous=btn_text;
                    if(Services.ismethodQR)
                        services.QRdefective();
                    else
                        services.defective();
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }

        else if(btn_text=="return")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));
                    previous=btn_text;
                    if(Services.ismethodQR==true) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Return Confirmation")
                                .setMessage("Are you sure you want to return this product?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        services.QRreturnAction();
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                    else
                        services.returnAction();
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }
        else if (btn_text=="get refund")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));
                    flg=1;
                    services.returnAction();
                    MainActivity.adapter.notifyDataSetChanged();
//                    l.clear();
//                    l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
//                    MainActivity.adapter.notifyDataSetChanged();
//                    MainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);
                }
            });
        }
        else if(btn_text=="replace")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));
                    services.replaceAction();
                    MainActivity.adapter.notifyDataSetChanged();
                   // l.clear();
                    //l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
                    //MainActivity.adapter.notifyDataSetChanged();
                    //MainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);
                }
            });
        }
        else if (btn_text=="QR code scanning")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));
                    Services.ismethodQR =true;
                    services.QRgetActions();
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }
        else if (btn_text=="messages")
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));
                    Services.ismethodQR =false;
                    flg=1;
                    services.getActions();
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }



        else if(Services.responseMapping.containsKey(btn_text))
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.l.add(new msgModel(btn_text,true));
                    MainActivity.adapter.notifyDataSetChanged();
                    int x=Integer.parseInt(String.valueOf(Services.responseMapping.get(btn_text)));
                    orderId=String.valueOf(x-1);


                    if(previous=="object delivered is defective")
                    {flg=1;
                        services.defective();}
                    else if(previous=="return")
                    {

                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Return Confirmation")
                                .setMessage("Are you sure you want to return this product?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        flg=1;
                                        services.returnAction();
                                        //l.clear();
                                        //l.add( new msgModel("Thank You for using CARE BOT. If any other query then please select your query from below" , "order related", "General Query","Track previous requests","","",false));
                                        //MainActivity.adapter.notifyDataSetChanged();
                                        //M/ainActivity.textToSpeech.speak("Thank You for using CARE BOT. If any other query then please select your query from below", TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                    else if(previous=="damaged within warranty period")
                    {
                        flg=1;
                        try {
                            services.AvailWarranty();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    // Toast.makeText(btn.getContext(), Services.responseMapping.get(btn_text).toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    /**
     * This function will make btns with no text `View.Visibility.GONE`  else make it visible
     * */
    private void btn_shower(Button btn, String btnText){
        if (btnText.equals("")) {
            btn.setVisibility(View.GONE);
        }
        else{
            btn.setText(btnText);
            fun_selector(btnText,btn);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isUser())
            return R.layout.user_message_view;
        else
            return R.layout.msgview;
    }
}