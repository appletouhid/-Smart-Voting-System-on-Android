package com.touhid.electionvotingsystem;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.LinearLayout;

import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener; import com.google.android.gms.tasks.OnSuccessListener; import com.google.firebase.FirebaseException; import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.PhoneAuthCredential;

import com.google.firebase.auth.PhoneAuthOptions;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {



private FirebaseAuth mAuth;

private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks; private String mVerificationId;

private PhoneAuthProvider.ForceResendingToken mResendToken;



private LinearLayout phoneL , codeL;

private ProgressDialog pd;

private Button phoneContinueBtn, codeSubmitBtn,alreadyLoginBtn;

private EditText phontEt , codeEt;

private TextView resendCode,codeDes;

@Override

protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_register);

mAuth = FirebaseAuth.getInstance(); getSupportActionBar().setDisplayHomeAsUpEnabled(true);

phoneL = findViewById(R.id.phonel);

codeL = findViewById(R.id.codel);

phontEt = findViewById(R.id.phoneEt);

phoneContinueBtn = findViewById(R.id.phoneContinueBtn);

codeEt = findViewById(R.id.codeEt);

codeSubmitBtn = findViewById(R.id.codeSubmitBtn); resendCode = findViewById(R.id.resendTV); codeDes = findViewById(R.id.codeDes); alreadyLoginBtn = findViewById(R.id.reglogbtn);

alreadyLoginBtn.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View view) {

Intent register = new Intent(RegisterActivity.this, HaveAccountActivity.class); startActivity(register);

}

});

phoneL.setVisibility(View.VISIBLE);

codeL.setVisibility(View.GONE);



pd = new ProgressDialog(this);

pd.setTitle("Please Wait...");

pd.setCancelable(false);

mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() { @Override

public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)

{

signInWithPhoneAuthCredentials(phoneAuthCredential);

}

@Override



public void onVerificationFailed(@NonNull FirebaseException e) { pd.dismiss();

Toast.makeText(RegisterActivity.this, "Invalid Verification Code", Toast.LENGTH_SHORT).show();

}

@Override

public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

super.onCodeSent(verificationId, forceResendingToken);

mVerificationId = verificationId;

mResendToken = forceResendingToken;

pd.dismiss();

phoneL.setVisibility(View.GONE);

codeL.setVisibility(View.VISIBLE);

codeDes.setText("Please type the verification code we sent \nto "+phontEt.getText().toString().trim());

}

};



phoneContinueBtn.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View view) {

String phone = phontEt.getText().toString().trim();

if(TextUtils.isEmpty(phone)){

Toast.makeText(RegisterActivity.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();

}else{

startPhoneNumberVerification(phone);

}

}

});

resendCode.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View view) {

String phone = phontEt.getText().toString().trim();

if(TextUtils.isEmpty(phone)){

Toast.makeText(RegisterActivity.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();

}else{

resendVerificationCode(phone,mResendToken);


}

}

});

codeSubmitBtn.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View view) {

String code = codeEt.getText().toString().trim();

if(TextUtils.isEmpty(code)){

Toast.makeText(RegisterActivity.this, "Please Enter Verification Code", Toast.LENGTH_SHORT).show();

}else{

verifyPhoneNumber(mVerificationId,code);

}

}

});

}

private void verifyPhoneNumber(String verificationId , String code) { pd.setMessage("Verifying Code");

pd.show();

PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code); signInWithPhoneAuthCredentials(credential);
}

private void signInWithPhoneAuthCredentials(PhoneAuthCredential credential) {

pd.setMessage("Logging In");

mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
@Override

public void onSuccess(AuthResult authResult) {



Intent setupactivity = new Intent(RegisterActivity.this, SetupActivity.class);

startActivity(setupactivity);

finish();

pd.dismiss();

//Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();

}

}).addOnFailureListener(new OnFailureListener() {




@Override

public void onFailure(@NonNull Exception e) {

Toast.makeText(RegisterActivity.this, "There are some error in credential you provided",

Toast.LENGTH_SHORT).show();

}

});

}

private void resendVerificationCode(String phone,PhoneAuthProvider.ForceResendingToken token) {

pd.setMessage("Resending Code");

pd.show();

PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)

.setPhoneNumber(phone)

.setTimeout(60L,TimeUnit.SECONDS)

.setActivity(this)

.setCallbacks(mCallbacks)

.setForceResendingToken(token)

.build();

PhoneAuthProvider.verifyPhoneNumber(options);

}

private void startPhoneNumberVerification(String phone) { pd.setMessage("Verifying Phone Number"); pd.show();

PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)

.setPhoneNumber(phone)

.setTimeout(60L,TimeUnit.SECONDS)

.setActivity(this)

.setCallbacks(mCallbacks)

.build();

PhoneAuthProvider.verifyPhoneNumber(options);

}

}
