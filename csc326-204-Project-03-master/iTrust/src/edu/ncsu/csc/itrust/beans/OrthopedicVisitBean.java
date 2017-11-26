package edu.ncsu.csc.itrust.beans;


import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * Bean for an orthopedic office visit. 
 * @author xshu3, yyang21, tralber2
 * @date 3/20/2016
 */
public class OrthopedicVisitBean {
	//Required
	private String orthopedicVisitDate;
	private String injuredLimbJoint;
	private long OrthopedicVisitID;
	private long orthopedicID;
	private long patientID;
	
	//Optional
	private byte[] XRay;
	private byte[] MRI;
	private String MRIreport;
	
	/**
	 * These fields are actually used like booleans. However, there must also
	 * be a state for "uninitialized". Therefore, 0 = false, 1 = true, and -1 = uninitialized.
	 */
	private short ACLinjury;
	private short MeniscusTear;
	private short RAhand;
	private short Chondromalacia;
	private short CPC;
	private short Whiplashinjury;
	
	/**
	 * Initialize all booleans to uninitialized.
	 */
	public OrthopedicVisitBean(){
		ACLinjury = -1;
		MeniscusTear = -1;
		RAhand = -1;
		Chondromalacia = -1;
		CPC = -1;
		Whiplashinjury = -1;
	}
	
	public long getOrthopedicVisitID(){
		return this.OrthopedicVisitID;
	}
	
	public void setOrthopedicVisitID(long OrthopedicVisitID){
		this.OrthopedicVisitID = OrthopedicVisitID;
	}
	
	public long getPatientID(){
		return this.patientID;
	}
	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}
	
	
	public short getWhiplashinjury(){
		return this.Whiplashinjury;
	}
	
	public void setWhiplashinjury(short a){
		this.Whiplashinjury = a;
	}
	
	public short getCPC(){
		return this.CPC;
	}
	
	public void setCPC(short a){
		this.CPC = a;
	}
	
	public short getMeniscusTear(){
		return this.MeniscusTear;
	}
	
	public void setMeniscusTear(short a){
		this.MeniscusTear = a;
	}
	
	public short getRAhand(){
		return this.RAhand;
	}
	
	public void setRAhand(short a){
		this.RAhand = a;
	}
	
	public short getChondromalacia(){
		return this.Chondromalacia;
	}
	
	public void setChondromalacia(short a){
		this.Chondromalacia = a;
	}
	
	public short getACLinjury(){
		return this.ACLinjury;
	}
	
	public void setACLinjury(short a){
		this.ACLinjury = a;
	}
	
	/**
	 * Return a formatted java.util.Date date
	 * @return a formatted java.util.Date date
	 */
	public Date getOrthopedicVisitDate() {
		Date d = null; 
		try {
			//could cause problems. might have to change type of date
			d = (Date) new SimpleDateFormat("MM/dd/yyyy").parse(orthopedicVisitDate);
		} catch (ParseException e) {
			System.out.println(e.toString());
		}
		
		return d;
	}
	
	/**
	 * Return a formatted java.util.Date date String for displaying on a webpage.
	 * @return a formatted java.util.Date date String
	 */
	public String getOrthopedicVisitDateString() {
		return orthopedicVisitDate;
		
	}
	public void setOrthopedicVisitDate(String a){
		this.orthopedicVisitDate = a;
	}
	
	public String getInjuredLimbJoint(){
		return this.injuredLimbJoint;
	}
	
	public void setInjuredLimbJoint(String a) {
		this.injuredLimbJoint = a;
	}
	
	public byte[] getXRay(){
		return this.XRay;
	}
	
	public void setXRay(byte[] a){
		this.XRay = a;
	}
	
	public void setXRay(Blob a) throws SQLException{
		this.XRay = a.getBytes(0, (int)a.length());
	}
	
	public void setMRI(byte[] a){
		this.MRI = a;
	}
	
	public void setMRI(Blob a) throws SQLException{
		this.MRI = a.getBytes(0, (int)a.length());
	}
	
	public byte[] getMRI(){
		
		return this.MRI;
	}
	
	public void setMRIreport(String a){
		this.MRIreport = a;
	}
	
	public String getMRIreport(){
		return this.MRIreport;
	}
	
	public void printXray(){
		System.out.println(XRay);
	}

	public long getOrthopedicID() {
		return orthopedicID;
	}

	public void setOrthopedicID(long orthopedicID) {
		this.orthopedicID = orthopedicID;
	}
	
}
