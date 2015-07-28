package com.obe.bean;

public class Identification_result {
	private String faceId;
	private Candidate[] candidates;
	public String getFaceId() {
		return faceId;
	}
	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}
	public Candidate[] getCandidates() {
		return candidates;
	}
	public void setCandidates(Candidate[] candidates) {
		this.candidates = candidates;
	}

}
