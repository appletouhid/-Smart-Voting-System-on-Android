package com.touhid.electionvotingsystem;
import android.app.ProgressDialog; import android.content.Intent; import android.os.Bundle;
import android.view.View;
import android.widget.Button; import android.widget.Toast;
import androidx.annotation.NonNull;
4
   import androidx.appcompat.app.AppCompatActivity; import androidx.constraintlayout.widget.ConstraintLayout; import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener; import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange; import com.google.firebase.firestore.DocumentSnapshot; import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException; import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList; import java.util.HashMap; import java.util.List; import java.util.Map;
public class Candidate_List_Activity extends AppCompatActivity { private Button mVotefinish;
// private FirebaseAuth mAuth;
private FirebaseFirestore firebaseFirestore; private String currentUser_id;
private ProgressDialog mProgress;
private RecyclerView mCandidate_List_View;
private FirebaseAuth firebaseAuth;
private List<CandidatePost> candidate_list;
//private FirebaseFirestore firebaseFirestore;
private CandidateRecyclerAdapter candidateRecyclerAdapter; private ConstraintLayout mCandidate_btn;
private DocumentSnapshot lastVisible; private Boolean isFirstPageFirstLoad = true;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState); setContentView(R.layout.activity_candidate__list_); getSupportActionBar().setDisplayHomeAsUpEnabled(true);
firebaseFirestore = FirebaseFirestore.getInstance(); firebaseAuth = FirebaseAuth.getInstance(); if(HaveAccountActivity.id != null){
currentUser_id = HaveAccountActivity.id;
5
 
   }else {
currentUser_id = firebaseAuth.getCurrentUser().getUid();
}
candidate_list = new ArrayList<>();
mCandidate_List_View = findViewById(R.id.candidate_list_view); mCandidate_btn = findViewById(R.id.candidate_btn); candidateRecyclerAdapter = new CandidateRecyclerAdapter(candidate_list); mCandidate_List_View.setLayoutManager(new LinearLayoutManager(this)); mCandidate_List_View.setAdapter(candidateRecyclerAdapter);
// if(firebaseAuth.getCurrentUser() != null) if(currentUser_id != null)
{
firebaseFirestore = FirebaseFirestore.getInstance();
mCandidate_List_View.addOnScrollListener(new RecyclerView.OnScrollListener() { @Override
public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
super.onScrolled(recyclerView, dx, dy);
Boolean reachdBottoms = !recyclerView.canScrollVertically(1);
if(reachdBottoms){
String candidatename = lastVisible.getString("candidatename");
// Toast.makeText(container.getContext(), Toast.LENGTH_LONG).show();
loadMorePost(); }
} });
Query firstQuery = firebaseFirestore.collection("Candidates") .orderBy("timestamp", Query.Direction.DESCENDING);
"Reached"+candidatename,
firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
@Override
public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
if (!documentSnapshots.isEmpty()) {
if (isFirstPageFirstLoad) {
lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
}
for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
6
 
   if (doc.getType() == DocumentChange.Type.ADDED) {
String candId = doc.getDocument().getId();
CandidatePost candidatePosts doc.getDocument().toObject(CandidatePost.class).withId(candId);
// candidate_list.add(candidatePosts);
if (isFirstPageFirstLoad) { candidate_list.add(candidatePosts);
} else {
candidate_list.add(0, candidatePosts);
}
candidateRecyclerAdapter.notifyDataSetChanged(); }
}
isFirstPageFirstLoad = false; }else{
Toast.makeText(Candidate_List_Activity.this, "No Toast.LENGTH_SHORT).show();
} }
}); }
mVotefinish = findViewById(R.id.votefinish); firebaseFirestore.collection("Users").document(currentUser_id)
=
// // // // // // // // // // // // // // // // // // // //
.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { @Override
public void onComplete(@NonNull Task<DocumentSnapshot> task) {
if (task.isSuccessful()){
if (task.getResult().exists()) {
String finish = task.getResult().getString("Finish");
if (finish == null) { //voteFinish();
}else{ mVotefinish.setVisibility(View.INVISIBLE);
} }
}
} });
}
Candidate available",
7
 
   public void voteFinish(){ mVotefinish.setVisibility(View.VISIBLE);
mVotefinish.setOnClickListener(new View.OnClickListener() { @Override
public void onClick(View v) {
firebaseFirestore.collection("Users")
.document(currentUser_id)
.get()
.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
result");
@Override
public void onComplete(@NonNull Task<DocumentSnapshot> task) {
if (task.isSuccessful()) {
if (task.getResult().exists()) {
String finish = "Voted";
Map<String, Object> voteMap = new HashMap<>(); voteMap.put("Finish", finish);
firebaseFirestore.collection("Users")
.document(currentUser_id)
.update(voteMap)
.addOnCompleteListener(new OnCompleteListener<Void>() {
@Override
public void onComplete(@NonNull Task<Void> task) {
mV otefinish.setEnabled(false);
mProgress = new ProgressDialog(Candidate_List_Activity.this); mProgress.setTitle("Vote Processing");
mProgress.setMessage("Please wait while system is processing election
mProgress.show(); mProgress.setCancelable(false);
Intent HomeInt = new ElectionResultActivity.class);
startActivity(HomeInt);
finish(); }
});
Intent(Candidate_List_Activity.this,
8
 
   } }
} });
} });
}
public void loadMorePost() {
if (firebaseAuth.getCurrentUser() !=null){
Query nextQurey = firebaseFirestore.collection("Candidates")
.orderBy("timestamp", Query.Direction.DESCENDING) .startAfter(lastVisible)
.limit(3);
nextQurey.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
@Override
public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
if (!documentSnapshots.isEmpty()) {
lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
for (DocumentChange doc : documentSnapshots.getDocumentChanges()) { if (doc.getType() == DocumentChange.Type.ADDED) {
String blogPostId = doc.getDocument().getId();
CandidatePost blogPost = doc.getDocument().toObject(CandidatePost.class).withId(blogPostId);
candidate_list.add(blogPost);
candidateRecyclerAdapter.notifyDataSetChanged(); }
} }
} });
} }
9
 
}