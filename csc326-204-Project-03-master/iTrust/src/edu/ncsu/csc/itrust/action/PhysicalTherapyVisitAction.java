package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PhysicalTherapyVisitDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
/**
 * Action class for transferring information to and from the database.
 * @author tralber2
 * @date 3/30/2016
 */
public class PhysicalTherapyVisitAction {
	private PhysicalTherapyVisitDAO dao;
	private long loggedInMID;
	private long pid;
	/**
	 * Initialization as well as parsing patient id into long.
	 * @param factory
	 * @param loggedInMID
	 * @param pidString
	 * @throws Exception
	 */
	public PhysicalTherapyVisitAction(DAOFactory factory, 
									  Long loggedInMID, 
									  String pidString ) {
		this.dao = factory.getPhysicalTherapyVisitDAO();
		this.loggedInMID = loggedInMID;
		this.pid = Long.parseLong(pidString);
	}
	/**
	 * Return a list of visit beans associated with that patient.
	 * @param patientID the patient to look for
	 * @return the list of beans
	 * @throws DBException
	 */
	public List<PhysicalTherapyVisitBean> getAllPhysicalTherapyVisits( long patientID) throws ITrustException {
		return dao.getAllPhysicalTherapyVisits( patientID );
	}

	public List<PhysicalTherapyVisitBean> getPhysicalTherapyVisitsByOrthopedicVisitID( long patientID, long orthopedicVisitID) throws ITrustException {
		return dao.getPhysicalTherapyVisitsByOrthopedicVisitID( patientID, orthopedicVisitID );
	}

	public List<PhysicalTherapyVisitBean> getPhysicalTherapyVisitsByHCPID( long patientID, long hcpID) throws ITrustException {
		return dao.getPhysicalTherapyVisitsByHCPID( patientID, hcpID );
	}
	/**
	 * Get the bean associated with a unique visit id.
	 * @param visitID the unique visit id.
	 * @return the bean associated with that visit
	 * @throws Exception
	 */
	public PhysicalTherapyVisitBean getVisit( long visitID) throws ITrustException {
		PhysicalTherapyVisitBean bean = dao.getPhysicalTherapyVisit( visitID );
		return bean;
	}
	/**
	 * Edit the information with this bean's visit id with the remaining information
	 * in the bean
	 * @param bean
	 * @throws ITrustException
	 */
	public void editVisit( PhysicalTherapyVisitBean bean ) throws ITrustException {
		dao.editPhysicalTherapyVisit(bean);
	}
	
	/**
	 * Creates a visit in the database with the provided bean.
	 * @param the bean's information to add to the database
	 * @throws ITrustException
	 */
	public void orderVisit(PhysicalTherapyVisitBean bean) throws ITrustException {
		dao.orderPhysicalTherapyVisit(bean);
	}
	
	/**
	 * Creates a visit in the database with the therapists id and current date
	 * @param hcpID
	 * @param date
	 * @throws ITrustException
	 */
	public PhysicalTherapyVisitBean orderVisit(long hcpID, String date, long orthopedicVisitID ) throws ITrustException {
		PhysicalTherapyVisitBean bean = new PhysicalTherapyVisitBean();
		bean.setPhysicalTherapyVisitDate(date);
		bean.setPatientID(pid);
		bean.setPhysicalTherapistID(hcpID);
		bean.setOrthopedicVisitID(orthopedicVisitID);
		orderVisit(bean);
		return bean;
	}
	
	/**
	 * Return the patient id currently being looked for.
	 * @return the patient id currently being looked for.
	 */
	public long getPid() {
		return pid;
	}
}
