package com.touhid.electionvotingsystem;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class CandidateId {

 


@Exclude

public  String CandidateId;

public <T extends CandidateId> T withId(@NonNull final String id){ this.CandidateId = id;

return (T) this;

}

}
