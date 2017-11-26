package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.SurgicalOrthopedicVisitDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;

public class SurgicalOrthopedicVisitAction {
	private SurgicalOrthopedicVisitDAO dao;
	private long loggedInMID;
	private long pid;
	
	public SurgicalOrthopedicVisitAction( DAOFactory factory,
										  Long loggedInMID,
										  String pidString ) {
		this.dao = factory.getSurgicalOrthopedicVisitDAO();
		this.loggedInMID = loggedInMID;
		this.pid = Long.parseLong(pidString);
	}
	
	public List<SurgicalOrthopedicVisitBean> getAllSurgicalOrthopedicVisits( long patientID ) throws ITrustException {
		return dao.getAllSurgicalOrthopedicVisits(patientID);
	}
	
	public List<SurgicalOrthopedicVisitBean> getSurgicalOrthopedicVisitsByOrthopedicVisitID( long patientID, long orthopedicVisitID ) throws ITrustException {
		return dao.getSurgicalOrthopedicVisitsByOrthopedicVisitID(patientID, orthopedicVisitID);
	}
	
	public SurgicalOrthopedicVisitBean getVisit( long visitID ) throws ITrustException {
		SurgicalOrthopedicVisitBean bean = dao.getSurgicalOrthopedicVisit(visitID);
		return bean;
	}
	
	public void editVisit( SurgicalOrthopedicVisitBean bean ) throws ITrustException {
		dao.editSurgicalOrthopedicVisit(bean);
	}
	
	public void orderVisit( SurgicalOrthopedicVisitBean bean ) throws ITrustException {
		dao.orderSurgicalOrthopedicVisit(bean);
	}
	
	public SurgicalOrthopedicVisitBean orderVisit( long hcpID, String date, long orthopedicVisitID ) throws ITrustException {
		SurgicalOrthopedicVisitBean bean = new SurgicalOrthopedicVisitBean();
		bean.setSurgicalOrthopedicVisitDate(date);
		bean.setPatientID(pid);
		bean.setOrthopedicID(hcpID);
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
