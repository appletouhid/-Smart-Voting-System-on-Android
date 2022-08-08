package com.touhid.electionvotingsystem;

import android.annotation.SuppressLint;

import android.app.Activity;

import android.content.Context;

import android.content.Intent;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout; import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

public class CandidateRecyclerAdapter extends RecyclerView.Adapter<CandidateRecyclerAdapter.ViewHolder> {

public List<CandidatePost> candidate_list;

public Context context;

public CandidateRecyclerAdapter(List<CandidatePost> candidate_list){

this.candidate_list = candidate_list;

}

@NonNull

@Override

public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

 


View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_list_item, parent, false);

context = parent.getContext();

return new ViewHolder(view);

}

@Override

public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


holder.setIsRecyclable(false);

String candId = candidate_list.get(position).CandidateId;



String cand_name_data = candidate_list.get(position).getCandidatename(); holder.setCandnameText(cand_name_data);

String cand_post_data = candidate_list.get(position).getCandidatepost(); holder.setCandPost(cand_post_data);

String cand_party_data = candidate_list.get(position).getPartyname(); holder.setCandParty(cand_party_data);

String cand_speech_data = candidate_list.get(position).getCandidatespeech();



String image_uri = candidate_list.get(position).getImage_uri(); holder.setCandImage(image_uri);

holder.CandidateBtn.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View v) {

Intent votingintent = new Intent(context, VotingActivity.class);

votingintent.putExtra("CandId", candidate_list.get(position).CandidateId);

votingintent.putExtra("CandName", candidate_list.get(position).getCandidatename());

votingintent.putExtra("CandPost", candidate_list.get(position).getCandidatepost());

votingintent.putExtra("PartyName", candidate_list.get(position).getPartyname());

votingintent.putExtra("CandSpeech", candidate_list.get(position).getCandidatespeech());

votingintent.putExtra("CandImage", candidate_list.get(position).getImage_uri());

context.startActivity(votingintent);

Activity activity = (Activity) context;

activity.finish();
 



}

 


});

holder.view_profile.setOnClickListener(new View.OnClickListener() { @Override

public void onClick(View view) {

Intent votingintent = new Intent(context, CandidateProfile.class); votingintent.putExtra("CandId", candidate_list.get(position).CandidateId); votingintent.putExtra("CandName", candidate_list.get(position).getCandidatename()); votingintent.putExtra("CandPost", candidate_list.get(position).getCandidatepost()); votingintent.putExtra("PartyName", candidate_list.get(position).getPartyname()); votingintent.putExtra("CandSpeech", candidate_list.get(position).getCandidatespeech()); votingintent.putExtra("CandImage", candidate_list.get(position).getImage_uri()); context.startActivity(votingintent);

}

});

}

@Override

public int getItemCount() {

return candidate_list.size();

}

public class ViewHolder extends RecyclerView.ViewHolder{

private View mView;

private TextView CandNameView;

private TextView CandPostView;

private TextView CandPartyView;

private ImageView CandidateImageView;

private Button view_profile;

private ConstraintLayout CandidateBtn;

public ViewHolder(View itemView) {

super(itemView);

mView = itemView;

CandidateBtn = mView.findViewById(R.id.candidate_btn); view_profile = mView.findViewById(R.id.view_profile);
}

public void setCandnameText(String candText){

CandNameView = mView.findViewById(R.id.candname_result); CandNameView.setText(candText);
}

 
public void setCandImage(String downloadUri){

CandidateImageView = mView.findViewById(R.id.candidate_image_result); Glide.with(context).load(downloadUri).into(CandidateImageView);

}

public void setCandPost(String candPostText){

CandPostView = mView.findViewById(R.id.candidate_post_result); CandPostView.setText(candPostText);
}

public void setCandParty(String candPartyText){

CandPartyView = mView.findViewById(R.id.party_name_result); CandPartyView.setText(candPartyText);
}



}

}
