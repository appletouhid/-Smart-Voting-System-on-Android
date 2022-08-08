package com.touhid.electionvotingsystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentChange; import com.google.firebase.firestore.DocumentSnapshot; import com.google.firebase.firestore.EventListener; import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException; import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.List;

public class ElectionResultActivity extends AppCompatActivity {

//	private ResultFragment resultFragment;

//	private long backPressedTime;

private RecyclerView candidate_result_list_view;

private List<Candidate_Result> result_list;

private FirebaseFirestore firebaseFirestore;

private FirebaseAuth firebaseAuth;

private String user_id;

private Candidate_Result_Adapter candidate_result_adapter; private DocumentSnapshot lastVisible; private Boolean isFirstPageFirstLoad = true;

@Override

protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);

 



setContentView(R.layout.activity_election_result);

//	resultFragment = new ResultFragment();

//	replaceFragment(resultFragment);



result_list = new ArrayList<>();

candidate_result_list_view = findViewById(R.id.candidate_result_list_view);

candidate_result_adapter = new Candidate_Result_Adapter(result_list); // Inflate the layout for this fragment

candidate_result_list_view.setLayoutManager(new LinearLayoutManager(this)); candidate_result_list_view.setAdapter(candidate_result_adapter);

firebaseAuth = FirebaseAuth.getInstance();

if(HaveAccountActivity.id != null){

user_id = HaveAccountActivity.id;

}else {

user_id = firebaseAuth.getCurrentUser().getUid();

}

firebaseFirestore = FirebaseFirestore.getInstance();

if(user_id != null) {

candidate_result_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() { @Override

public void onScrolled(RecyclerView recyclerView, int dx, int dy) { super.onScrolled(recyclerView, dx, dy);

Boolean reachdBottoms = !recyclerView.canScrollVertically(1);

if(reachdBottoms){

String candidatename = lastVisible.getString("candidatename");

// Toast.makeText(container.getContext(), "Reached"+candidatename, Toast.LENGTH_LONG).show();

loadMorePost();

}

}

});

Query firstQuery = firebaseFirestore.collection("Candidates").orderBy("timestamp", Query.Direction.DESCENDING);

firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() { @Override

public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

 


if (isFirstPageFirstLoad){

lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size()-1);

}

for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

if (doc.getType() == DocumentChange.Type.ADDED) {

String candId = doc.getDocument().getId();

Candidate_Result candidate_result = doc.getDocument().toObject(Candidate_Result.class).withId(candId);

//	candidate_list.add(candidatePosts); if (isFirstPageFirstLoad){

result_list.add(candidate_result);

}else {

result_list.add(0, candidate_result);

}



candidate_result_adapter.notifyDataSetChanged();

}

}

isFirstPageFirstLoad = false;

}

});




}

}

public  void loadMorePost() {

if (user_id !=null) {

Query nextQurey = firebaseFirestore.collection("Candidates")

.orderBy("timestamp", Query.Direction.DESCENDING)

.startAfter(lastVisible)

.limit(3);

nextQurey.addSnapshotListener(this, new EventListener<QuerySnapshot>() { @Override

 

public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

if (!documentSnapshots.isEmpty()) {

lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);

for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

if (doc.getType() == DocumentChange.Type.ADDED) {

String blogPostId = doc.getDocument().getId();

Candidate_Result candidate_result = doc.getDocument().toObject(Candidate_Result.class).withId(blogPostId);

result_list.add(candidate_result);

candidate_result_adapter.notifyDataSetChanged();

}




}




}

}

});

}

}

//	private void replaceFragment(Fragment fragment) {

//	

//	FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

//	fragmentTransaction.replace(R.id.result_container, fragment);

//	fragmentTransaction.commit();

//

//	}

@Override

public void onBackPressed() {

//	if (backPressedTime + 2000 > System.currentTimeMillis()) {

//	Intent main = new Intent(ElectionResultActivity.this, AuthActivity.class);

//	startActivity(main);

//	finish();

//	} else {

//	Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();

//	}

//

//	backPressedTime = System.currentTimeMillis(); finish();

}

}
