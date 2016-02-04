package com.example.relationchecker;

public class Relation {

	private String firstTerm;
	private String secondTerm;
	private String thirdTerm;
	private int weight;
	private int confidence;
	
	public Relation(String firstTerm , String secondTerm , String thirdTerm , int weight , int confidence){
		this.firstTerm=firstTerm;
		this.secondTerm=secondTerm;
		this.thirdTerm=thirdTerm;
		this.weight=weight;
		this.confidence=confidence;
	}
	
	public String getFirstTerm() {
		return firstTerm;
	}
	public void setFirstTerm(String firstTerm) {
		this.firstTerm = firstTerm;
	}
	public String getSecondTerm() {
		return secondTerm;
	}
	public void setSecondTerm(String secondTerm) {
		this.secondTerm = secondTerm;
	}
	public String getThirdTerm() {
		return thirdTerm;
	}
	public void setThirdTerm(String thirdTerm) {
		this.thirdTerm = thirdTerm;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getConfidence() {
		return confidence;
	}
	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}
	

	
}

