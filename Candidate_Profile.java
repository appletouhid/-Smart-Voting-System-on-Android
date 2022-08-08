package com.touhid.electionvotingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.Bundle;

import android.widget.Button;

import android.widget.ImageView;

import android.widget.TextView;

import android.widget.Toast;

import com.bumptech.glide.Glide;

public class CandidateProfile extends AppCompatActivity {

private TextView mcandName;

private ImageView mcand_image;

private TextView mcandSpeech;

private TextView mcandPost;

private TextView mcandParty;



@Override

protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_candidate_profile);

mcandName = findViewById(R.id.candName);

mcand_image = findViewById(R.id.cand_image);

mcandSpeech = findViewById(R.id.candSpeech);

mcandPost = findViewById(R.id.candPost);

mcandParty = findViewById(R.id.candParty);

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

Glide.with(this).load(candImage).into(mcand_image); mcandSpeech.setText(candSpeech); mcandPost.setText(candPost); mcandParty.setText(partyName);

}else {
Toast.makeText(CandidateProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();

}

}

}

