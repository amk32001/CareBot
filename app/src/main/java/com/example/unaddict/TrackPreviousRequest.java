package com.example.unaddict;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class TrackPreviousRequest extends AppCompatActivity {
    ArrayList<com.example.unaddict.PreviousRequestModel> req_list;
    ListView trackPreviousRequestListView ;

    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_previous_request);
        firebaseDatabase = FirebaseDatabase.getInstance();

        /**
         * get user id and place this information after previousrequest
         * */
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("previousRequest").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        try{
            addDatatoFirebase(2, "resolved", "modi", "return");
        }catch (Exception e){
           // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        req_list = new ArrayList<>();
        trackPreviousRequestListView = findViewById(R.id.prev_req_listview);
//        int orderId, String status, String order, String action) {
        req_list.add(new com.example.unaddict.PreviousRequestModel(1,"resolved","mens cotton shirt","return"));
        req_list.add(new com.example.unaddict.PreviousRequestModel(1,"resolved","mens cotton shirt","return"));
        req_list.add(new com.example.unaddict.PreviousRequestModel(2,"in-progress","modi kurta","defective"));
        com.example.unaddict.CustomAdapter customAdapter = new com.example.unaddict.CustomAdapter(req_list, this);
        trackPreviousRequestListView.setAdapter(customAdapter);

    }
    private void addDatatoFirebase(int orderId, String orderStatus , String orderName , String orderAction) {
        com.example.unaddict.PreviousRequestModel model = new com.example.unaddict.PreviousRequestModel(orderId, orderStatus , "modi" , orderAction);
        DatabaseReference databaseReference1=databaseReference.child(Math.random() + "");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(model);

                // after adding this data we are showing toast message.
                Toast.makeText(getApplicationContext(), "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(getApplicationContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}