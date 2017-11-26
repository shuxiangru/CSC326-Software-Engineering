package edu.ncsu.csc.itrust.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Bean for a physical therapy office visit. 
 * @author rbscott, tralber2
 * @date 3/30/2016
 */
public class PhysicalTherapyVisitBean {
	// Required fields
	private long physicalTherapyVisitID;
	private long orthopedicVisitID;
	private long patientID;
	private long physicalTherapistID;
	private String physicalTherapyVisitDate;
	
	private static String[] results = {"Unable","Very difficult","Moderate","Little bit","No difficulty"};
	
	// Survey scores from 0-10, incremented by 2
	// Probably should name these better or make an array
	// 0 - HouseWork
	// 1 - BathScore
	// 2 - WalkingRoom
	// 3 - SquatScore
	// 4 - LiftScore
	// 5 - WalkingBlockScore
	// 6 - StairsScore
	// 7 - StandingScore
	// 8 - JumpingScore
	// 9 - RunningScore
	private short[] scores = new short[10];
	
	// Exercises
	// 0 - QuadSet
	// 1 - HeelSlide
	// 2 - CalfTowel
	// 3 - StraightLeg
	// 4 - TerminalKnee
	// 5 - GastrocStretch
	// 6 - WallSlide
	// 7 - Proprioception
	// 8 - HipAbduction
	// 9 - SingleLeg
	private boolean[] exercises = new boolean[10];
	
	/**
	 * Because visits are not "added" until a physical therapist adds them,
	 * this is true if the visit has been formally added.
	 */
	private boolean addedVisit = false;

	public long getPhysicalTherapyVisitID() {
		return physicalTherapyVisitID;
	}

	public void setPhysicalTherapyVisitID(long physicalTherapyVisitID) {
		this.physicalTherapyVisitID = physicalTherapyVisitID;
	}

	/**
	 * @return the patientID
	 */
	public long getPatientID() {
		return patientID;
	}
	/**
	 * @param patientID the patientID to set
	 */
	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}
	/**
	 * @return the houseWorkScore
	 */
	public short getHouseWorkScore() {
		return scores[0];
	}
	public String getHouseWorkScoreString(){
		return results[scores[0]];
	}
	/**
	 * @param houseWorkScore the houseWorkScore to set
	 */
	public void setHouseWorkScore(short houseWorkScore) {
		this.scores[0] = houseWorkScore;
	}
	/**
	 * @return the bathScore
	 */
	public short getBathScore() {
		return scores[1];
	}
	public String getBathScoreString(){
		return results[scores[1]];
	}
	/**
	 * @param bathScore the bathScore to set
	 */
	public void setBathScore(short bathScore) {
		this.scores[1] = bathScore;
	}
	/**
	 * @return the walkingRoomScore
	 */
	public short getWalkingRoomScore() {
		return scores[2];
	}
	public String getWalkingRoomScoreString(){
		return results[scores[2]];
	}
	/**
	 * @param walkingRoomScore the walkingRoomScore to set
	 */
	public void setWalkingRoomScore(short walkingRoomScore) {
		this.scores[2] = walkingRoomScore;
	}
	/**
	 * @return the squatScore
	 */
	public short getSquatScore() {
		return scores[3];
	}
	public String getSquatScoreString(){
		return results[scores[3]];
	}
	/**
	 * @param squatScore the squatScore to set
	 */
	public void setSquatScore(short squatScore) {
		this.scores[3] = squatScore;
	}
	/**
	 * @return the liftScore
	 */
	public short getLiftScore() {
		return scores[4];
	}
	public String getLiftScoreString(){
		return results[scores[4]];
	}
	/**
	 * @param liftScore the liftScore to set
	 */
	public void setLiftScore(short liftScore) {
		this.scores[4] = liftScore;
	}
	/**
	 * @return the walkingBlockScore
	 */
	public short getWalkingBlockScore() {
		return scores[5];
	}
	public String getWalkingBlockScoreString(){
		return results[scores[5]];
	}
	/**
	 * @param walkingBlockScore the walkingBlockScore to set
	 */
	public void setWalkingBlockScore(short walkingBlockScore) {
		this.scores[5] = walkingBlockScore;
	}
	/**
	 * @return the stairsScore
	 */
	public short getStairsScore() {
		return scores[6];
	}
	public String getStairsScoreString(){
		return results[scores[6]];
	}
	/**
	 * @param stairsScore the stairsScore to set
	 */
	public void setStairsScore(short stairsScore) {
		this.scores[6] = stairsScore;
	}
	/**
	 * @return the standingScore
	 */
	public short getStandingScore() {
		return scores[7];
	}
	public String getStandingScoreString(){
		return results[scores[7]];
	}
	/**
	 * @param standingScore the standingScore to set
	 */
	public void setStandingScore(short standingScore) {
		this.scores[7] = standingScore;
	}
	/**
	 * @return the jumpingScore
	 */
	public short getJumpingScore() {
		return scores[8];
	}
	public String getJumpingScoreString(){
		return results[scores[8]];
	}
	/**
	 * @param jumpingScore the jumpingScore to set
	 */
	public void setJumpingScore(short jumpingScore) {
		this.scores[8] = jumpingScore;
	}
	/**
	 * @return the runningScore
	 */
	public short getRunningScore() {
		return scores[9];
	}
	public String getRunningScoreString(){
		return results[scores[9]];
	}
	/**
	 * @param runningScore the runningScore to set
	 */
	public void setRunningScore(short runningScore) {
		this.scores[9] = runningScore;
	}
	/**
	 * @return the quadSetExercise
	 */
	public boolean getQuadSetExercise() {
		return exercises[0];
	}
	/**
	 * @param quadSetExercise the quadSetExercise to set
	 */
	public void setQuadSetExercise(boolean quadSetExercise) {
		this.exercises[0] = quadSetExercise;
	}
	/**
	 * @return the heelSlideExercise
	 */
	public boolean getHeelSlideExercise() {
		return exercises[1];
	}
	/**
	 * @param heelSlideExercise the heelSlideExercise to set
	 */
	public void setHeelSlideExercise(boolean heelSlideExercise) {
		this.exercises[1] = heelSlideExercise;
	}
	/**
	 * @return the calfTowelExercise
	 */
	public boolean getCalfTowelExercise() {
		return exercises[2];
	}
	/**
	 * @param calfTowelExercise the calfTowelExercise to set
	 */
	public void setCalfTowelExercise(boolean calfTowelExercise) {
		this.exercises[2] = calfTowelExercise;
	}
	/**
	 * @return the straightlegExercise
	 */
	public boolean getStraightLegExercise() {
		return exercises[3];
	}
	/**
	 * @param straightLegExercise the straightLegExercise to set
	 */
	public void setStraightLegExercise(boolean straightLegExercise) {
		this.exercises[3] = straightLegExercise;
	}
	/**
	 * @return the terminalKneeExercise
	 */
	public boolean getTerminalKneeExercise() {
		return exercises[4];
	}
	/**
	 * @param terminalKneeExercise the terminalKneeExercise to set
	 */
	public void setTerminalKneeExercise(boolean terminalKneeExercise) {
		this.exercises[4] = terminalKneeExercise;
	}
	/**
	 * @return the gastrocStretchExcercise
	 */
	public boolean getGastrocStretchExcercise() {
		return exercises[5];
	}
	/**
	 * @param gastrocStretchExcercise the gastrocStretchExcercise to set
	 */
	public void setGastrocStretchExcercise(boolean gastrocStretchExcercise) {
		this.exercises[5] = gastrocStretchExcercise;
	}
	/**
	 * @return the wallSlideExercise
	 */
	public boolean getWallSlideExercise() {
		return exercises[6];
	}
	/**
	 * @param wallSlideExercise the wallSlideExercise to set
	 */
	public void setWallSlideExercise(boolean wallSlideExercise) {
		this.exercises[6] = wallSlideExercise;
	}
	/**
	 * @return the proprioceptionExercise
	 */
	public boolean getProprioceptionExercise() {
		return exercises[7];
	}
	/**
	 * @param proprioceptionExercise the proprioceptionExercise to set
	 */
	public void setProprioceptionExercise(boolean proprioceptionExercise) {
		this.exercises[7] = proprioceptionExercise;
	}
	/**
	 * @return the hipAbductionExercise
	 */
	public boolean getHipAbductionExercise() {
		return exercises[8];
	}
	/**
	 * @param hipAbductionExercise the hipAbductionExercise to set
	 */
	public void setHipAbductionExercise(boolean hipAbductionExercise) {
		this.exercises[8] = hipAbductionExercise;
	}
	/**
	 * @return the singleLegExercise
	 */
	public boolean getSingleLegExercise() {
		return exercises[9];
	}
	/**
	 * @param singleLegExercise the singleLegExercise to set
	 */
	public void setSingleLegExercise(boolean singleLegExercise) {
		this.exercises[9] = singleLegExercise;
	}
	/**
	 * @return the physicalTherapistID
	 */
	public long getPhysicalTherapistID() {
		return physicalTherapistID;
	}
	/**
	 * @param physicalTherapistID the physicalTherapistID to set
	 */
	public void setPhysicalTherapistID(long physicalTherapistID) {
		this.physicalTherapistID = physicalTherapistID;
	}
	/**
	 * Return a formatted java.util.Date date
	 * @return a formatted java.util.Date date
	 */
	public Date getPhysicalTherapyVisitDate() {
		Date d = null; 
		try {
			//could cause problems. might have to change type of date
			d = (Date) new SimpleDateFormat("MM/dd/yyyy").parse(physicalTherapyVisitDate);
		} catch (ParseException e) {
		}
		
		return d;
	}
	/**
	 * @param orthopedicVisitDate the orthopedicVisitDate to set
	 */
	public void setPhysicalTherapyVisitDate(String physicalTherapyVisitDate) {
		this.physicalTherapyVisitDate = physicalTherapyVisitDate;
	}
	
	public String getPhysicalTherapyVisitDateString() {
		return physicalTherapyVisitDate;
	}
	
	public float getWellnessScore(){
		float result = 0;
		for(short s : scores)
			result += s * 2.5f;
		return result;
	}
	
	public boolean[] getExercises(){
		return exercises;
	}
	
	public void setExercises(boolean[] exercises){
		this.exercises = exercises;
	}
	
	/**
	 * Return true if a physical therapist has assigned exercises to the patient
	 * Otherwise return false.
	 * @return
	 */
	public boolean hasExercises(){

		for(boolean b : exercises)
			if(b)
				return true;
		return false;
	}
	
	/**
	 * Return the number of exercises prescribed by the physical therapist
	 * @return
	 */
	public short getExerciseCount(){
		short count = 0;
		for(boolean b : exercises)
			if(b)
				count++;
		return count;
	}

	/**
	 * @return the addedVisit
	 */
	public boolean getAddedVisit() {
		return addedVisit;
	}

	/**
	 * @param addedVisit the addedVisit to set
	 */
	public void setAddedVisit(boolean addedVisit) {
		this.addedVisit = addedVisit;
	}

	public long getOrthopedicVisitID() {
		return orthopedicVisitID;
	}

	public void setOrthopedicVisitID(long orthopedicVisitID) {
		this.orthopedicVisitID = orthopedicVisitID;
	}
}
