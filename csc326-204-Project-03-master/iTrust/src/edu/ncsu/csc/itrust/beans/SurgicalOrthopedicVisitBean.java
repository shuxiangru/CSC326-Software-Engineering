package edu.ncsu.csc.itrust.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ncsu.csc.itrust.exception.ITrustException;

public class SurgicalOrthopedicVisitBean {
	/** Required fields */
	private long surgicalOrthopedicVisitID;
	private long orthopedicVisitID;
	private long orthopedicID;
	private long patientID;
	private String surgicalOrthopedicVisitDate;
	/**
	 * 0 Total knee replacement
	 * 1 Total joint replacement
	 * 2 ACL reconstruction
	 * 3 Ankle replacement
	 * 4 Spine Surgery
	 * 5 Arthroscopic surgery
	 * 6 Rotator cuff repair
	 */
	private boolean[] surgeries = new boolean[7];
	
	/**
	 * Because visits are not "added" until a physical therapist adds them,
	 * this is true if the visit has been formally added.
	 */
	private boolean addedVisit = false;
	
	/** Optional field */
	private String surgicalNotes;

	/**
	 * @return the surgicalNotes
	 */
	public String getSurgicalNotes() {
		return surgicalNotes;
	}

	/**
	 * @param surgicalNotes the surgicalNotes to set
	 */
	public void setSurgicalNotes(String surgicalNotes) {
		this.surgicalNotes = surgicalNotes;
	}

	/**
	 * @return the surgicalOrthopedicVisitDate
	 * @throws ITrustException 
	 */
	public Date getSurgicalOrthopedicVisitDate() throws ITrustException {
		Date d = null; 
		try {
			//could cause problems. might have to change type of date
			d = (Date) new SimpleDateFormat("MM/dd/yyyy").parse(surgicalOrthopedicVisitDate);
		} catch (ParseException e) {
			throw new ITrustException("Improperly formatted date. Please enter as MM/dd/yyyy");
		}
		return d;
	}

	/**
	 * @param surgicalOrthopedicVisitDate the surgicalOrthopedicVisitDate to set
	 */
	public void setSurgicalOrthopedicVisitDate(String surgicalOrthopedicVisitDate) {
		this.surgicalOrthopedicVisitDate = surgicalOrthopedicVisitDate;
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
	 * @return the surgicalOrthopedicVisitID
	 */
	public long getSurgicalOrthopedicVisitID() {
		return surgicalOrthopedicVisitID;
	}

	/**
	 * @param surgicalOrthopedicVisitID the surgicalOrthopedicVisitID to set
	 */
	public void setSurgicalOrthopedicVisitID(long surgicalOrthopedicVisitID) {
		this.surgicalOrthopedicVisitID = surgicalOrthopedicVisitID;
	}
	/**
	 * Sets this surgery at the designated index to the given value
	 * 
	 * 0 Total knee replacement
	 * 1 Total joint replacement
	 * 2 ACL reconstruction
	 * 3 Ankle replacement
	 * 4 Spine Surgery
	 * 5 Arthroscopic surgery
	 * 6 Rotator cuff repair
	 * 
	 * @param index
	 */
	public void setSurgery( int index, boolean b ) {
		surgeries[index] = b;
	}
	
	public void setSurgeries( boolean[] b ) {
		surgeries = b;
	}
	
	/**
	 * Returns the boolean value for the surgery at this index.
	 * 
	 * 0 Total knee replacement
	 * 1 Total joint replacement
	 * 2 ACL reconstruction
	 * 3 Ankle replacement
	 * 4 Spine Surgery
	 * 5 Arthroscopic surgery
	 * 6 Rotator cuff repair
	 * 
	 * @param index
	 * @return
	 */
	public boolean getSurgery( int index ) {
		return surgeries[index];
	}

	public boolean hasSurgeries(){

		for(boolean b : surgeries)
			if(b)
				return true;
		return false;
	}

	/**
	 * @return the orthopedicID
	 */
	public long getOrthopedicID() {
		return orthopedicID;
	}

	/**
	 * @param orthopedicID the orthopedicID to set
	 */
	public void setOrthopedicID(long orthopedicID) {
		this.orthopedicID = orthopedicID;
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
	
	public String getSurgicalOrthopedicVisitDateString() {
		return surgicalOrthopedicVisitDate;
	}

	public long getOrthopedicVisitID() {
		return orthopedicVisitID;
	}

	public void setOrthopedicVisitID(long orthopedicVisitID) {
		this.orthopedicVisitID = orthopedicVisitID;
	}
	
}
