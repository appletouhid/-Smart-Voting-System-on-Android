package com.touhid.electionvotingsystem;

import android.content.Intent;

import android.content.pm.PackageManager;

import android.net.Uri;

import android.os.Build;


import android.os.Bundle;

import android.text.TextUtils;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ProgressBar;

import android.widget.RadioButton;

import android.widget.RadioGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity; import androidx.core.app.ActivityCompat; import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener; import com.google.android.gms.tasks.OnSuccessListener; import com.google.android.gms.tasks.Task; import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot; import com.google.firebase.firestore.FirebaseFirestore; import com.google.firebase.storage.FirebaseStorage; import com.google.firebase.storage.StorageReference; import com.google.firebase.storage.UploadTask; import com.theartofdev.edmodo.cropper.CropImage; import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

private CircleImageView mSelect_image;

private EditText mFullname;

private EditText mNationId;

private EditText mAddress;

private EditText mPassword;

private RadioGroup mGender;

private RadioButton radioButton;

private ProgressBar mSetprogress;

private Button mSetup_Save;
 



private Uri mainImageUrl = null;

 


private StorageReference storageReference;

private FirebaseAuth firebaseAuth;

private FirebaseFirestore firebaseFirestore;

private String currentUser_id;

@Override

protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_setup);

firebaseFirestore = FirebaseFirestore.getInstance();

firebaseAuth = FirebaseAuth.getInstance();

storageReference = FirebaseStorage.getInstance().getReference();

mSetup_Save = (Button) findViewById(R.id.save_changes); mFullname = (EditText) findViewById(R.id.fullname); mNationId = (EditText) findViewById(R.id.nationalid); mPassword = (EditText) findViewById(R.id.password); mAddress = (EditText) findViewById(R.id.address); mGender = (RadioGroup) findViewById(R.id.gender);


mSetprogress = (ProgressBar) findViewById(R.id.setup_progress); mSelect_image = (CircleImageView) findViewById(R.id.select_image);



mSetup_Save.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View v) {

final String fullname = mFullname.getText().toString(); final String national = mNationId.getText().toString(); final String password = mPassword.getText().toString(); final String address = mAddress.getText().toString(); int radioId = mGender.getCheckedRadioButtonId(); radioButton = findViewById(radioId);


if (!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(national)

&& !TextUtils.isEmpty(password) && !TextUtils.isEmpty(address) && mainImageUrl

!= null) {

mSetprogress.setVisibility(View.VISIBLE);

final String user_id = firebaseAuth.getCurrentUser().getUid();




StorageReference image_path = storageReference.child("profile_image").child(user_id +

".jpg");

image_path.putFile(mainImageUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

@Override

public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

if(task.isSuccessful())

image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
@Override

public void onSuccess(Uri uri) {



Uri download_uri = uri;

Map<String, String> userMap = new HashMap<>(); userMap.put("Fullname", fullname); userMap.put("Gender", radioButton.getText().toString()); userMap.put("National", national); userMap.put("Password", password); userMap.put("Address", address); userMap.put("Images", uri.toString()); userMap.put("id",currentUser_id);




firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {

@Override

public void onComplete(@NonNull Task<Void> task) {

if (task.isSuccessful()) {

Toast.makeText(SetupActivity.this,	"User	Successully	Created",

Toast.LENGTH_LONG).show();

Intent main = new Intent(SetupActivity.this, MainActivity.class);

startActivity(main);

} else {

String errorimage = task.getException().getMessage(); Toast.makeText(SetupActivity.this, "FireStore Error" + errorimage,

Toast.LENGTH_LONG).show();

}

mSetprogress.setVisibility(View.INVISIBLE);

}




});

Toast.makeText(SetupActivity.this, "Image is successfully uploaded", Toast.LENGTH_SHORT).show();


}

});

else

mSetprogress.setVisibility(View.INVISIBLE);

}

});

} else {

Toast.makeText(SetupActivity.this, "Select image and all inputs are required", Toast.LENGTH_SHORT).show();

}

}

});

//Select image

mSelect_image.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View v) {

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

if	(ContextCompat.checkSelfPermission(SetupActivity.this,

android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

ActivityCompat.requestPermissions(SetupActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

} else {

cropImage();

}

} else {

cropImage();

}

}

});

}



@Override

protected void onStart() {

super.onStart();

if(HaveAccountActivity.id != null){

currentUser_id = HaveAccountActivity.id;

}else {

currentUser_id = firebaseAuth.getCurrentUser().getUid();

}

firebaseFirestore.collection("Users").document(currentUser_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
@Override

public void onComplete(@NonNull Task<DocumentSnapshot> task) {

if (task.isSuccessful()) {

if (task.getResult().exists()) {

Intent AccountSeeting = new Intent(SetupActivity.this, MainActivity.class);

startActivity(AccountSeeting);

finish();

}

} else {



String errormsg = task.getException().getMessage(); Toast.makeText(SetupActivity.this, "" + errormsg, Toast.LENGTH_SHORT).show();

}

}

});

}



private void cropImage() {

CropImage.activity()

.setGuidelines(CropImageView.Guidelines.ON)

.setAspectRatio(1, 1)

.start(SetupActivity.this);

}



@Override

protected void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);

if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {



CropImage.ActivityResult result = CropImage.getActivityResult(data); if (resultCode == RESULT_OK) {

mainImageUrl = result.getUri();

mSelect_image.setImageURI(mainImageUrl);

} else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

Exception error = result.getError();

}

}

}

public void checkButton(View v) {

int radioId = mGender.getCheckedRadioButtonId();

radioButton = findViewById(radioId);



}

}
