package com.touhid.electionvotingsystem;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity; import androidx.appcompat.widget.Toolbar;

import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;

import android.util.Log;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener; import com.google.android.gms.tasks.OnFailureListener; import com.google.android.gms.tasks.Task; import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class HaveAccountActivity extends AppCompatActivity {

private TextView nationalId,password;

private Button authenticationBtn;

private FirebaseFirestore firebaseFirestore;

public static String id;



@Override

protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_have_account);

 



authenticationBtn = findViewById(R.id.votebtns);

nationalId = findViewById(R.id.voteauthtxt);

password = findViewById(R.id.password);

firebaseFirestore = FirebaseFirestore.getInstance();

authenticationBtn.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View v) {

if (!TextUtils.isEmpty(nationalId.getText().toString().trim())) {

if(!TextUtils.isEmpty(password.getText().toString().trim())){

firebaseFirestore.collection("Users")

.whereEqualTo("National",nationalId.getText().toString().trim())

.whereEqualTo("Password",password.getText().toString().trim())

.get()

.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { @Override

public void onComplete(@NonNull Task<QuerySnapshot> task) {

if (task.isSuccessful() && !task.getResult().isEmpty()) {

for(QueryDocumentSnapshot	snapshot	:

Objects.requireNonNull(task.getResult())){

id = snapshot.getString("id");

Log.d("iii", "onComplete: "+id);

}

Toast.makeText(HaveAccountActivity.this, "Go to main", Toast.LENGTH_SHORT).show();

Intent main = new Intent(HaveAccountActivity.this, MainActivity.class);

startActivity(main);

finish();

} else {

// String error = task.getException().getMessage();

Toast.makeText(HaveAccountActivity.this, "User not found" , Toast.LENGTH_SHORT).show();

}

}

}).addOnFailureListener(new OnFailureListener() { @Override

public void onFailure(@NonNull Exception e) {

Toast.makeText(HaveAccountActivity.this, "User not found", Toast.LENGTH_SHORT).show();

}

});

}else{




Toast.makeText(HaveAccountActivity.this, "Please enter Password", Toast.LENGTH_SHORT).show();

}

} else {

Toast.makeText(HaveAccountActivity.this, "Please enter National Id", Toast.LENGTH_SHORT).show();

}

}

});

}

}
