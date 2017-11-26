package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.beans.loaders.OrthopedicVisitLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

/**
 * Facilitates interaction for going from java to SQL for Orthopedic visits.
 * 
 * @author tralber2, xshu3, yyang21
 * @date 3/20/2016
 */
public class OrthopedicVisitDAO {
	/** Factory for acquiring connection to the database. */
	private DAOFactory factory;
	/** Used to handle SELECT queries from the database. */
	private OrthopedicVisitLoader OrthopedicVisitLoader = new OrthopedicVisitLoader();

	/**
	 * Creates the DAO with the DAOFactory.
	 * 
	 * @param factory
	 *            the factory used for getting db connection
	 */
	public OrthopedicVisitDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Insert the visit bean into the database. The table has an AUTO_INCREMENT
	 * variable for creating the unique orthopedicVisitID used to distinguish
	 * between visits.
	 * 
	 * @param ov
	 *            the bean to be inserted.
	 * @throws ITrustException
	 */
	public void addOrthopedicVisitRecord(OrthopedicVisitBean ov) {
		try (Connection conn = factory.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("INSERT INTO orthopedicvisits("
					+ "OrthopedicID," // 1
					+ "PID," // 2
					+ "OrthopedicVisitDate," // 3
					+ "InjuredPart," // 4
					+ "XRay," // 5
					+ "MRI," // 6
					+ "MRIReport," // 7
					+ "ACLInjury," // 8
					+ "MeniscusTear," // 9
					+ "RAHand," // 10
					+ "Chondromalacia," // 11
					+ "CPC," // 12
					+ "WhiplashInjury) "// 13
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				)
		{
			ps.setLong(1, ov.getOrthopedicID());
			ps.setLong(2, ov.getPatientID());
			ps.setDate(3, new Date(ov.getOrthopedicVisitDate().getTime()));
			ps.setString(4, ov.getInjuredLimbJoint());
			
			if (ov.getXRay() != null) {
				ps.setBytes(5, ov.getXRay());
			} else {
				ps.setNull(5, Types.BLOB);
			}

			if (ov.getMRI() != null) {
				ps.setBytes(6, ov.getMRI());
			} else {
				ps.setNull(6, Types.BLOB);
			}

			if (ov.getMRIreport() != null) {
				ps.setString(7, ov.getMRIreport());
			} else {
				ps.setNull(7, Types.VARCHAR);
			}
			
			ps.setShort(8, ov.getACLinjury());
			ps.setShort(9, ov.getMeniscusTear());
			ps.setShort(10, ov.getRAhand());
			ps.setShort(11, ov.getChondromalacia());
			ps.setShort(12, ov.getCPC());
			ps.setShort(13, ov.getWhiplashinjury());
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if( rs.next() ) {
				ov.setOrthopedicVisitID(rs.getLong(1));
			} else {
				//shit
			}
		} catch (Exception e) {
			//throw new ITrustException("Information was not filled out properly.");
			e.printStackTrace();
		}
	}

	/**
	 * Updates the visit in the database with the associated unique visit ID.
	 * Updates every field in the table, based off of whether or not the user
	 * left the box blank. If the box was erased, fill the table with NULL.
	 * 
	 * @param ov
	 *            the bean from which the data will be retrieved to overwrite
	 *            the data in the database.
	 * @throws ITrustException
	 */
	public void editOrthopedicVisitRecord(OrthopedicVisitBean ov) throws ITrustException {
		try (Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE orthopedicvisits SET "
					+ "OrthopedicID=?," // 1
					+ "PID=?," // 2
					+ "OrthopedicVisitDate=?," // 3
					+ "InjuredPart=?," // 4
					+ "XRay=?," // 5
					+ "MRI=?," // 6
					+ "MRIReport=?," // 7
					+ "ACLInjury=?," // 8
					+ "MeniscusTear=?," // 9
					+ "RAHand=?," // 10
					+ "Chondromalacia=?," // 11
					+ "CPC=?," // 12
					+ "WhiplashInjury=? " // 13
					+ "WHERE OrthopedicVisitID=?"); // 14
			)
		{
			ps.setLong(14, ov.getOrthopedicVisitID());
			ps.setLong(1, ov.getOrthopedicID());
			ps.setLong(2, ov.getPatientID());
			ps.setDate(3, new Date(ov.getOrthopedicVisitDate().getTime()));
			ps.setString(4, ov.getInjuredLimbJoint());

			if (ov.getXRay() != null) {
				ps.setBytes(5, ov.getXRay());
			} else {
				ps.setNull(5, Types.BLOB);
			}

			if (ov.getMRI() != null) {
				ps.setBytes(6, ov.getMRI());
			} else {
				ps.setNull(6, Types.BLOB);
			}

			if (ov.getMRIreport() != null) {
				ps.setString(7, ov.getMRIreport());
			} else {
				ps.setNull(7, Types.VARCHAR);
			}
			
			ps.setShort(8, ov.getACLinjury());
			ps.setShort(9, ov.getMeniscusTear());
			ps.setShort(10, ov.getRAhand());
			ps.setShort(11, ov.getChondromalacia());
			ps.setShort(12, ov.getCPC());
			ps.setShort(13, ov.getWhiplashinjury());
			ps.executeUpdate();
		} catch (Exception e) {
			throw new ITrustException("Information was not filled out properly.");
		}
	}

	/**
	 * Retrieves an OrthopedicVisitBean based off of the unique visit id.
	 * 
	 * @param OrthopedicVisitID
	 *            the id to search for in the db.
	 * @return the bean with that id.
	 * @throws ITrustException
	 */
	public OrthopedicVisitBean getOrthopedicVisitRecord(long OrthopedicVisitID) throws ITrustException {
		try (Connection conn = factory.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orthopedicvisits WHERE OrthopedicVisitID=?");
			)
		{
			pstmt.setLong(1, OrthopedicVisitID);
			final ResultSet results = pstmt.executeQuery();
			// A list that should only have one bean.
			final List<OrthopedicVisitBean> loadedBeans = OrthopedicVisitLoader.loadList(results);
			results.close();
			// Because the id is unique, should only be one result.
			return loadedBeans.get(0);
		} catch (Exception e) {
			throw new ITrustException("Could not retrieve visit from database.");
		}
	}
	
	public OrthopedicVisitBean getOrthopedicVisitRecordByP(long OrthopedicVisitID, long pid) throws ITrustException {
		try (Connection conn = factory.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orthopedicvisits WHERE OrthopedicVisitID=? AND PID=?");
			)
		{
			pstmt.setLong(1, OrthopedicVisitID);
			pstmt.setLong(2, pid);
			final ResultSet results = pstmt.executeQuery();
			// A list that should only have one bean.
			final List<OrthopedicVisitBean> loadedBeans = OrthopedicVisitLoader.loadList(results);
			results.close();
			// Because the id is unique, should only be one result.
			return loadedBeans.get(0);
		} catch (Exception e) {
			throw new ITrustException("Could not retrieve visit from database.");
		}
	}

	/**
	 * Returns the list of beans associated with a patient.
	 * 
	 * @param pid
	 *            the id of the patient to look for.
	 * @return the list of visits associated with this patient.
	 * @throws DBException
	 */
	public List<OrthopedicVisitBean> getAllOrthopedicVisits(long pid) throws DBException {

		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM orthopedicvisits WHERE PID=? ORDER BY OrthopedicVisitDate DESC");
			)
		{
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			List<OrthopedicVisitBean> loadlist = OrthopedicVisitLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {

			throw new DBException(e);
		}
	}
}
