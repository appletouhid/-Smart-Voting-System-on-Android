
package com.touhid.electionvotingsystem;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;

import android.os.Bundle;

import android.view.View;

import android.widget.TextView;

import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions; import com.google.android.gms.tasks.OnCompleteListener; import com.google.android.gms.tasks.Task; import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot; import com.google.firebase.firestore.FirebaseFirestore; import com.google.firebase.storage.FirebaseStorage; import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {



private TextView fullname;

private TextView address;

private TextView gender;

private CircleImageView mprofile_image;

private String currentUser_id;

private FirebaseAuth mAuth;

private FirebaseFirestore firebaseFirestore;

private StorageReference storageReference;

@Override

protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_profile);

mAuth = FirebaseAuth.getInstance();

firebaseFirestore = FirebaseFirestore.getInstance();



firebaseFirestore = FirebaseFirestore.getInstance();

mAuth = FirebaseAuth.getInstance();

storageReference = FirebaseStorage.getInstance().getReference();

if(HaveAccountActivity.id != null){

currentUser_id = HaveAccountActivity.id;

}else {

currentUser_id = mAuth.getCurrentUser().getUid();

}

fullname = findViewById(R.id.ufullname);

address = findViewById(R.id.uaddress);

gender = findViewById(R.id.ugender);

mprofile_image = findViewById(R.id.profile_image);

firebaseFirestore.collection("Users").document(currentUser_id)

.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { @Override

public void onComplete(@NonNull Task<DocumentSnapshot> task) {

if (task.isSuccessful()) {

if (task.getResult().exists()) {

String fullnames = task.getResult().getString("Fullname");

String addresss = task.getResult().getString("Address");

String genders = task.getResult().getString("Gender");

String national = task.getResult().getString("National");

String profile_images = task.getResult().getString("Images");

address.setText(addresss);

fullname.setText(fullnames);

gender.setText(genders);

RequestOptions placeholderimg = new RequestOptions(); placeholderimg.placeholder(R.drawable.profile);

Glide.with(ProfileActivity.this).setDefaultRequestOptions(placeholderimg).load(profile_images).into( mprofile_image);



} else {

//	Toast.makeText(MainActivity.this, "Data does not exit", Toast.LENGTH_SHORT).show();

}

} else {
String error = task.getException().getMessage();

Toast.makeText(ProfileActivity.this, "Retriving Error" + error, Toast.LENGTH_SHORT).show();

}

}

});

}

}
