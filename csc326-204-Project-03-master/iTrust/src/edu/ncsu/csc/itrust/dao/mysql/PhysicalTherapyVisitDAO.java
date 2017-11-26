package edu.ncsu.csc.itrust.dao.mysql;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;
import edu.ncsu.csc.itrust.beans.loaders.PhysicalTherapyVisitLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
/**
 * Facilitates interaction for going from java to SQL for Physical Therapy visits.
 * 
 * @author tralber2, xshu3, yyang21
 * @date 3/30/2016
 */
public class PhysicalTherapyVisitDAO {
	/** Factory for acquiring connection to the database. */
	private DAOFactory factory;
	/** Used to handle SELECT queries from the database. */
	private PhysicalTherapyVisitLoader loader;
	/**
	 * Creates the DAO with the DAOFactory as well as the loader
	 * 
	 * @param factory
	 *            the factory used for getting db connection
	 */
	public PhysicalTherapyVisitDAO(DAOFactory daoFactory) {
		factory = daoFactory;
		loader = new PhysicalTherapyVisitLoader();
	}
	/**
	 * This is basically an add method, except Physical therapy visits are first
	 * ordered and then officially added. When a visit is first ordered, the
	 * provided bean has only the id of the physical therapist, the patient,
	 * and the physical therapy visit date
	 * @param bean
	 */
	public void orderPhysicalTherapyVisit( PhysicalTherapyVisitBean bean) {
		try( Connection conn = factory.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("INSERT INTO physicaltherapyvisits("
							+	"PhysicalTherapistID,"
							+	"PID,"
							+	"PhysicalTherapyVisitDate,"
							+ 	"HouseWorkScore,"
							+ 	"BathScore,"
							+	"WalkingRoomScore,"
							+ 	"SquatScore,"
							+ 	"LiftScore,"
							+ 	"WalkingBlockScore,"
							+ 	"StairsScore,"
							+	"StandingScore,"
							+ 	"JumpingScore,"
							+ 	"RunningScore,"
							+ 	"QuadSetExercise,"
							+ 	"HeelSlideExercise,"
							+ 	"CalfTowelExercise,"
							+ 	"StraightLegExercise,"
							+ 	"TerminalKneeExercise,"
							+ 	"GastrocStretchExercise,"
							+ 	"WallSlideExercise,"
							+ 	"ProprioceptionExercise,"
							+ 	"HipAbductionExercise,"
							+	"SingleLegExercise,"
							+	"AddedVisit,"
							+	"OrthopedicVisitID"
							+ ") "
							+	"VALUES( ?,?,?,?,?,"
							+ 			"?,?,?,?,?,"
							+ 			"?,?,?,?,?,"
							+ 			"?,?,?,?,?,"
							+ 			"?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);)
			{
			ps.setLong(1, bean.getPhysicalTherapistID());
			ps.setLong(2, bean.getPatientID());
			ps.setDate(3, new Date(bean.getPhysicalTherapyVisitDate().getTime()));
			ps.setShort(4, bean.getHouseWorkScore());
			ps.setShort(5, bean.getBathScore());
			ps.setShort(6, bean.getWalkingRoomScore());
			ps.setShort(7, bean.getSquatScore());
			ps.setShort(8, bean.getLiftScore());
			ps.setShort(9, bean.getWalkingBlockScore());
			ps.setShort(10, bean.getStairsScore());
			ps.setShort(11, bean.getStandingScore());
			ps.setShort(12, bean.getJumpingScore());
			ps.setShort(13, bean.getRunningScore());
			ps.setBoolean(14, bean.getQuadSetExercise());
			ps.setBoolean(15, bean.getHeelSlideExercise());
			ps.setBoolean(16, bean.getCalfTowelExercise());
			ps.setBoolean(17, bean.getStraightLegExercise());
			ps.setBoolean(18, bean.getTerminalKneeExercise());
			ps.setBoolean(19, bean.getGastrocStretchExcercise());
			ps.setBoolean(20, bean.getWallSlideExercise());
			ps.setBoolean(21, bean.getProprioceptionExercise());
			ps.setBoolean(22, bean.getHipAbductionExercise());
			ps.setBoolean(23, bean.getSingleLegExercise());
			ps.setBoolean(24, bean.getAddedVisit());
			ps.setLong(25, bean.getOrthopedicVisitID());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if( rs.next() ) {
				bean.setPhysicalTherapyVisitID(rs.getLong(1));
			} else {
				// Somehow no id from the table.
			}
		} catch( Exception e ) {
			e.printStackTrace();
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
	public List<PhysicalTherapyVisitBean> getAllPhysicalTherapyVisits( long patientID) throws ITrustException {
		try( Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM physicaltherapyvisits WHERE "
							+	"PID=?" );)
		{
			ps.setLong(1, patientID);
			List<PhysicalTherapyVisitBean> list = loader.loadList(ps.executeQuery());
			return list;
		} catch( Exception e ) {
			throw new ITrustException("Could not retrieve physical therapy records from database");
		}
	}
	/**
	 * Retrieves a PhysicalTherapyBean based off of the unique visit id.
	 * 
	 * @param PhysicalTherapyVisitID
	 *            the id to search for in the db.
	 * @return the bean with that id.
	 * @throws ITrustException
	 */
	public PhysicalTherapyVisitBean getPhysicalTherapyVisit( long visitID ) throws ITrustException {
		try( Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM physicaltherapyvisits WHERE "
							+	"PhysicalTherapyVisitID=?");)
		{
			ps.setLong(1, visitID);
			return loader.loadList(ps.executeQuery()).get(0);
		} catch( Exception e ) {
			throw new ITrustException("Could not retrieve physical therapy records from database");
		}
	}
	
	public PhysicalTherapyVisitBean getPhysicalTherapyVisitByP( long visitID, long pid) throws ITrustException {
		try( Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM physicaltherapyvisits WHERE "
							+	"PhysicalTherapyVisitID=? AND PID=?");
		   )
		{
			ps.setLong(1, visitID);
			ps.setLong(2, pid);
			return loader.loadList(ps.executeQuery()).get(0);
		} catch( Exception e ) {
			throw new ITrustException("Could not retrieve physical therapy records from database");
		}
	}
	/**
	 * Updates the visit in the database with the associated unique visit ID.
	 * Updates every field in the table, based off of whether or not the user
	 * left the box blank. If the box was erased, fill the table with NULL.
	 * 
	 * @param bean
	 *            the bean from which the data will be retrieved to overwrite
	 *            the data in the database.
	 * @throws ITrustException
	 */
	public void editPhysicalTherapyVisit(PhysicalTherapyVisitBean bean) {
		try( Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE physicaltherapyvisits SET "
							+	"PhysicalTherapistID=?,"
							+	"PID=?,"
							+	"PhysicalTherapyVisitDate=?,"
							+ 	"HouseWorkScore=?,"
							+ 	"BathScore=?,"
							+   "WalkingRoomScore=?,"
							+ 	"SquatScore=?,"
							+ 	"LiftScore=?,"
							+ 	"WalkingBlockScore=?,"
							+ 	"StairsScore=?,"
							+	"StandingScore=?,"
							+ 	"JumpingScore=?,"
							+ 	"RunningScore=?,"
							+ 	"QuadSetExercise=?,"
							+ 	"HeelSlideExercise=?,"
							+ 	"CalfTowelExercise=?,"
							+ 	"StraightLegExercise=?,"
							+ 	"TerminalKneeExercise=?,"
							+ 	"GastrocStretchExercise=?,"
							+ 	"WallSlideExercise=?,"
							+ 	"ProprioceptionExercise=?,"
							+ 	"HipAbductionExercise=?,"
							+	"SingleLegExercise=?,"
							+	"AddedVisit=?,"
							+	"OrthopedicVisitID=? "
							+   "WHERE PhysicalTherapyVisitID=?");)
		{
			ps.setLong(1, bean.getPhysicalTherapistID());
			ps.setLong(2, bean.getPatientID());
			ps.setDate(3, new Date(bean.getPhysicalTherapyVisitDate().getTime()));
			ps.setShort(4, bean.getHouseWorkScore());
			ps.setShort(5, bean.getBathScore());
			ps.setShort(6, bean.getWalkingRoomScore());
			ps.setShort(7, bean.getSquatScore());
			ps.setShort(8, bean.getLiftScore());
			ps.setShort(9, bean.getWalkingBlockScore());
			ps.setShort(10, bean.getStairsScore());
			ps.setShort(11, bean.getStandingScore());
			ps.setShort(12, bean.getJumpingScore());
			ps.setShort(13, bean.getRunningScore());
			ps.setBoolean(14, bean.getQuadSetExercise());
			ps.setBoolean(15, bean.getHeelSlideExercise());
			ps.setBoolean(16, bean.getCalfTowelExercise());
			ps.setBoolean(17, bean.getStraightLegExercise());
			ps.setBoolean(18, bean.getTerminalKneeExercise());
			ps.setBoolean(19, bean.getGastrocStretchExcercise());
			ps.setBoolean(20, bean.getWallSlideExercise());
			ps.setBoolean(21, bean.getProprioceptionExercise());
			ps.setBoolean(22, bean.getHipAbductionExercise());
			ps.setBoolean(23, bean.getSingleLegExercise());
			ps.setBoolean(24, bean.getAddedVisit());
			ps.setLong(25, bean.getOrthopedicVisitID());
			ps.setLong(26, bean.getPhysicalTherapyVisitID());
			ps.executeUpdate();
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public List<PhysicalTherapyVisitBean> getPhysicalTherapyVisitsByOrthopedicVisitID( long patientID, long orthopedicVisitID) throws ITrustException {
		try( Connection conn = factory.getConnection() ;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM physicaltherapyvisits WHERE "
							+	"PID=? AND OrthopedicVisitID=?" );)
		{
			ps.setLong(1, patientID);
			ps.setLong(2, orthopedicVisitID);
			List<PhysicalTherapyVisitBean> list = loader.loadList(ps.executeQuery());
			return list;
		} catch( Exception e ) {
			throw new ITrustException("Could not retrieve physical therapy records from database");
		}
	}
	
	public List<PhysicalTherapyVisitBean> getPhysicalTherapyVisitsByHCPID( long patientID, long hcpID) throws ITrustException {
		try( Connection conn = factory.getConnection() ) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM physicaltherapyvisits WHERE "
							+	"PID=? AND PhysicalTherapistID=?" );
			ps.setLong(1, patientID);
			ps.setLong(2, hcpID);
			List<PhysicalTherapyVisitBean> list = loader.loadList(ps.executeQuery());
			return list;
		} catch( Exception e ) {
			throw new ITrustException("Could not retrieve physical therapy records from database");
		}
	}
}
