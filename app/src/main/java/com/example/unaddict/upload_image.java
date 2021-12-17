package com.example.unaddict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class upload_image extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1 ;
    Button upload,choose_image;
    ImageView imageView;
    Uri image,imageUrl;
    ProgressBar progressBar;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
   // DatabaseReference databaseReference;
    private StorageTask uploading;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    String uid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        upload=findViewById(R.id.upload);
        choose_image=findViewById(R.id.ChooseImage);
        imageView=findViewById(R.id.imageView);
        progressBar=findViewById(R.id.progressBar);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("uploads");
        uid= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();



        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploading != null && uploading.isInProgress()) {
                    Toast.makeText(upload_image.this, "Uploading in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                    //onBackPressed();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.l.add(new msgModel("Image successfully uploaded",false));
        MainActivity.l.add(new msgModel("What would you like to do with that defective product?","replace","get refund","","","",false));
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.textToSpeech.speak("Image successfully uploaded. What would you like to do with that defective product?", TextToSpeech.QUEUE_FLUSH,null);

    }


    private void openFileChooser () {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data != null && data.getData() != null) {
            image = data.getData();
            Picasso.get().load(image).into(imageView);
        }
    }

   private String getFileExtension(Uri uri)
    {
        ContentResolver cR= this.getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile()
    {
        if(image!=null)
        {
            StorageReference sR= storageReference.child(uid+"."+getFileExtension(image));
            uploading=sR.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 500);
                    Toast.makeText(upload_image.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    sR.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl=uri;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(upload_image.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress=(100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        }
        else
        {
            Toast.makeText(upload_image.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

}