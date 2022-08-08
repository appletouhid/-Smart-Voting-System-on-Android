package com.touhid.electionvotingsystem;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.ImageView;

import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener; import com.google.android.gms.tasks.Task; import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot; import com.google.firebase.firestore.FieldValue;

import com.google.firebase.firestore.FirebaseFirestore; import com.google.firebase.storage.StorageReference;

import java.net.InetAddress;

import java.net.NetworkInterface;

import java.util.Enumeration;

import java.util.HashMap;

import java.util.Map;

public class VotingActivity extends AppCompatActivity {

private FirebaseFirestore firebaseFirestore;

private FirebaseAuth mAuth;

private StorageReference storageReference;



private TextView mcandName;

private ImageView mcand_image;

private TextView mcandSpeech;

private TextView mcandPost;

private TextView mcandParty;

private Button mcastvotebtn;

private ProgressDialog mProgress;

private String user_id;

@Override

protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_voting);

firebaseFirestore = FirebaseFirestore.getInstance(); mAuth = FirebaseAuth.getInstance(); getSupportActionBar().setDisplayHomeAsUpEnabled(true);


mcandName = findViewById(R.id.candName);

mcand_image = findViewById(R.id.cand_image);

mcandSpeech = findViewById(R.id.candSpeech);

mcandPost = findViewById(R.id.candPost);

mcandParty = findViewById(R.id.candParty);

mcastvotebtn = findViewById(R.id.castvotebtn);

if(HaveAccountActivity.id != null){

user_id = HaveAccountActivity.id;

}else {

user_id = mAuth.getCurrentUser().getUid();

}




if (getIntent().hasExtra("CandId") &&

getIntent().hasExtra("CandName") &&

getIntent().hasExtra("CandPost") &&

getIntent().hasExtra("PartyName") &&

getIntent().hasExtra("CandSpeech") &&

getIntent().hasExtra("CandImage")) {

String CandId = getIntent().getStringExtra("CandId");

String candName = getIntent().getStringExtra("CandName");

String candPost = getIntent().getStringExtra("CandPost");

String partyName = getIntent().getStringExtra("PartyName");

String candSpeech = getIntent().getStringExtra("CandSpeech");

String candImage = getIntent().getStringExtra("CandImage");



mcandName.setText(candName);

Glide.with(this).load(candImage).into(mcand_image); mcandSpeech.setText(candSpeech); mcandPost.setText(candPost); mcandParty.setText(partyName); String CandidateIds = CandId;




firebaseFirestore.collection("Users")

.document(user_id)

.get()

.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { @Override

public void onComplete(@NonNull Task<DocumentSnapshot> task) {

if (task.isSuccessful()) {

if (task.getResult().exists()) {

String usercandvt = (String) task.getResult().getString("" + getIntent().getStringExtra("CandPost"));

if (usercandvt == null) {

vote();

}else if (usercandvt.equals(getIntent().getStringExtra("CandPost") )){

mcastvotebtn.setVisibility(View.INVISIBLE);

}

 


}

}

}

});



}else {

Toast.makeText(VotingActivity.this, "You don't have right to be here", Toast.LENGTH_SHORT).show();

Intent HomeInt = new Intent(VotingActivity.this, HomeActivity.class);

startActivity(HomeInt);

finish();

}



//Toast.makeText(VotingActivity.this, ""+getDeviceIP(), Toast.LENGTH_SHORT).show();

}

private void vote(){

mcastvotebtn.setVisibility(View.VISIBLE);

mcastvotebtn.setOnClickListener(new View.OnClickListener() {

@Override

public void onClick(View v) {

mProgress = new ProgressDialog(VotingActivity.this);

mProgress.setTitle("Vote Processing");

mProgress.setMessage("Please wait while your vote is processing");

mProgress.show();

mProgress.setCancelable(false);

String finish = "Voted";

Map<String, Object> voteuserMap = new HashMap<>(); voteuserMap.put("" + mcandPost.getText(), mcandPost.getText()); voteuserMap.put("deviceIp", getDeviceIP()); voteuserMap.put(""+mcandPost.getText(),getIntent().getStringExtra("CandId")); voteuserMap.put("Finish", finish);


Map<String, Object> voteMap = new HashMap<>(); voteMap.put("candidatePost", mcandPost.getText()); voteMap.put("deviceIp", getDeviceIP());

 


voteMap.put("timestamp", FieldValue.serverTimestamp());



//

firebaseFirestore.collection("Users").document(user_id).collection("Activities").document("Votes").u pdate(voteuserMap);

firebaseFirestore.collection("Users").document(user_id).update(voteuserMap);



firebaseFirestore.collection("Candidates/"

+	getIntent().getStringExtra("CandId") + "/Votes")

.document(user_id)

.set(voteMap)

.addOnCompleteListener(new OnCompleteListener<Void>() {

@Override

public void onComplete(@NonNull Task<Void> task) {

if (task.isSuccessful()) {



mcastvotebtn.setVisibility(View.INVISIBLE);

Toast.makeText(VotingActivity.this, "Your Vote is Successfully Cast ", Toast.LENGTH_LONG).show();

Intent HomeInt = new Intent(VotingActivity.this, ElectionResultActivity.class);

startActivity(HomeInt);

finish();

} else {

mcastvotebtn.setVisibility(View.INVISIBLE);

}



}

});



}

});

}

private String getDeviceIP(){

try{




for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){

NetworkInterface inf = en.nextElement();

for (Enumeration<InetAddress> enumIpAddr = inf.getInetAddresses(); enumIpAddr.hasMoreElements();){

InetAddress inetAddress = enumIpAddr.nextElement();

if (!inetAddress.isLoopbackAddress()){

return inetAddress.getHostAddress().toString();

}

}

}

}catch (Exception e){

Toast.makeText(VotingActivity.this, ""+e, Toast.LENGTH_SHORT).show();

}

return null;

}

}
