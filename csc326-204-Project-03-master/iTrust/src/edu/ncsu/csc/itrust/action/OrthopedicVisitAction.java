package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.OrthopedicVisitDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
/**
 * Action class for transferring information to and from the database.
 * @author tralber2
 * @date 3/20/2016
 */
public class OrthopedicVisitAction {
	private OrthopedicVisitDAO dao;
	private long loggedInMID;
	private long pid;
	/**
	 * Initialization as well as parsing patient id into long.
	 * @param factory
	 * @param loggedInMID
	 * @param pidString
	 * @throws Exception
	 */
	public OrthopedicVisitAction( DAOFactory factory, Long loggedInMID, String pidString) throws Exception {
		this.dao = factory.getOrthopedicVisitDAO();
		this.loggedInMID = loggedInMID;
		this.pid = Long.parseLong(pidString);
	}
	
	/**
	 * Get the bean associated with a unique visit id.
	 * @param orthopedicVisitID the unique visit id.
	 * @return the bean associated with that visit
	 * @throws Exception
	 */
	public OrthopedicVisitBean viewBean(long orthopedicVisitID) throws Exception {
		OrthopedicVisitBean bean = dao.getOrthopedicVisitRecord(orthopedicVisitID);
		return bean;
	}
	
	/**
	 * Get the bean associated with a unique visit id.
	 * @param orthopedicVisitID the unique visit id.
	 * @return the bean associated with that visit
	 * @throws Exception
	 */
	public void editBean(OrthopedicVisitBean ov) throws Exception {
		dao.editOrthopedicVisitRecord(ov);
	}
	
	public void addBean( OrthopedicVisitBean ov ) throws ITrustException {
		dao.addOrthopedicVisitRecord(ov);
	}
	
	/**
	 * Return a list of visit beans associated with that patient.
	 * @param pid the patient to look for
	 * @return the list of beans
	 * @throws DBException
	 */
	public List<OrthopedicVisitBean> getAllOrthopedicVisits( long pid ) throws DBException {
		List<OrthopedicVisitBean> list = dao.getAllOrthopedicVisits(pid);
		return list;
	}
	
	/**
	 * Return the patient id currently being looked for.
	 * @return the patient id currently being looked for.
	 */
	public long getPid() {
		return pid;
	}
}
