package com.touhid.electionvotingsystem;

public class Candidate_Result extends CandidateId {

public String candidatename;

public String candidatepost;

public int getVoteCount() {

return voteCount;

}

public void setVoteCount(int voteCount) {

this.voteCount = voteCount;

}

public int voteCount = 0;

public Candidate_Result() {

}

public Candidate_Result(String candidatename, String candidatepost, String candidatespeech, String partyname, String image_thumb, String image_uri) {

this.candidatename = candidatename;

this.candidatepost = candidatepost;

this.candidatespeech = candidatespeech;

this.partyname = partyname;

this.image_thumb = image_thumb;

this.image_uri = image_uri;

}

public String candidatespeech;

public String partyname;

public String image_thumb;

public String image_uri;

public String getCandidatename() {

return candidatename;

}

public void setCandidatename(String candidatename) { this.candidatename = candidatename;

}

public String getCandidatepost() {

return candidatepost;

}

 



public void setCandidatepost(String candidatepost) { this.candidatepost = candidatepost;

}

public String getCandidatespeech() {

return candidatespeech;

}

public void setCandidatespeech(String candidatespeech) { this.candidatespeech = candidatespeech;

}

public String getPartyname() {

return partyname;

}

public void setPartyname(String partyname) {

this.partyname = partyname;

}

public String getImage_thumb() {

return image_thumb;

}

public void setImage_thumb(String image_thumb) { this.image_thumb = image_thumb;

}

public String getImage_uri() {

return image_uri;

}

public void setImage_uri(String image_uri) {

this.image_uri = image_uri;

}

}
