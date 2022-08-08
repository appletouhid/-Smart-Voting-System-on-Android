package com.touhid.electionvotingsystem;

import android.annotation.SuppressLint;

import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth; import com.google.firebase.firestore.EventListener; import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException; import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;

import java.util.Comparator;

import java.util.List;

public class Candidate_Result_Adapter extends RecyclerView.Adapter<Candidate_Result_Adapter.ViewHolder> {

private FirebaseFirestore firebaseFirestore;

// private FirebaseAuth firebaseAuth;



public List<Candidate_Result> result_list;

public Context context;

public Candidate_Result_Adapter(List<Candidate_Result> result_list) {

this.result_list = result_list;

}



@NonNull

@Override

 



public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);

context = parent.getContext();

firebaseFirestore = FirebaseFirestore.getInstance();

//	firebaseAuth = FirebaseAuth.getInstance(); return new ViewHolder(mview);
}

@Override

public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

Collections.sort(result_list, new Comparator<Candidate_Result>() { public int compare(Candidate_Result obj1, Candidate_Result obj2) {

// ## Ascending order

// return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values

return Integer.valueOf(obj2.getVoteCount()).compareTo(obj1.getVoteCount()); // To compare integer values;

//	## Descending order

//	return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values

//	return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values

}

});

holder.setIsRecyclable(false);

String candId = result_list.get(position).CandidateId;

String cand_name_data = result_list.get(position).getCandidatename(); holder.setCandnameText(cand_name_data);

String cand_post_data = result_list.get(position).getCandidatepost(); holder.setCandPost(cand_post_data);

String cand_party_data = result_list.get(position).getPartyname(); holder.setCandParty(cand_party_data);

String cand_speech_data = result_list.get(position).getCandidatespeech();

String image_uri = result_list.get(position).getImage_uri(); holder.setCandImage(image_uri);

 



firebaseFirestore.collection("Candidates/" + candId + "/Votes").addSnapshotListener(new EventListener<QuerySnapshot>() {

@Override

public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

if (!documentSnapshots.isEmpty()) {

int count = documentSnapshots.size();

//	holder.updatecount(count);

Candidate_Result updatedResult = result_list.get(position); updatedResult.setVoteCount(count); result_list.set(position, updatedResult);

notifyDataSetChanged();

} else {

//	holder.updatecount(0);

}

}

});

holder.updatecount(result_list.get(position).getVoteCount());

}



@Override

public int getItemCount() {

return result_list.size();

}

@Override

public int getItemViewType(int position) {

return super.getItemViewType(position);

}

public class ViewHolder extends RecyclerView.ViewHolder {

private View mView_Result;

private TextView CandNameView_Result;

private TextView CandPostView_Result;

private TextView CandPartyView_Result;

private ImageView CandidateImageView_Result;

private TextView CandidateResultCount;

 






public ViewHolder(View itemView) {

super(itemView);

mView_Result = itemView;

}



public void setCandnameText(String candText) {

CandNameView_Result = mView_Result.findViewById(R.id.candname_result); CandNameView_Result.setText(candText);
}

public void setCandImage(String downloadUri) {

CandidateImageView_Result = mView_Result.findViewById(R.id.candidate_image_result); Glide.with(context).load(downloadUri).into(CandidateImageView_Result);

}

public void setCandPost(String candPostText) {

CandPostView_Result = mView_Result.findViewById(R.id.candidate_post_result); CandPostView_Result.setText(candPostText);
}

public void setCandParty(String candPartyText) {

CandPartyView_Result = mView_Result.findViewById(R.id.party_name_result); CandPartyView_Result.setText(candPartyText);

}

public void updatecount(int count) {

CandidateResultCount = mView_Result.findViewById(R.id.candidate_results); CandidateResultCount.setText(count + "");

}

}

}
