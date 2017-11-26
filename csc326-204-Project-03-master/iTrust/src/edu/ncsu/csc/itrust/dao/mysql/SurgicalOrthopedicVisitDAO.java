package edu.ncsu.csc.itrust.dao.mysql;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;
import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;
import edu.ncsu.csc.itrust.beans.loaders.SurgicalOrthopedicVisitLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

public class SurgicalOrthopedicVisitDAO {
	/** Factory for acquiring connection to the database. */
	private DAOFactory factory;
	private SurgicalOrthopedicVisitLoader loader;
	
	public SurgicalOrthopedicVisitDAO( DAOFactory daoFactory ) {
		factory = daoFactory;
		loader = new SurgicalOrthopedicVisitLoader();
	}
	
	public void orderSurgicalOrthopedicVisit( SurgicalOrthopedicVisitBean bean ) throws ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO surgicalorthopedicvisits("
						+ "OrthopedicID,"
						+ "OrthopedicVisitID,"
						+ "PID,"
						+ "SurgicalOrthopedicVisitDate"
						+ ")"
						+ "VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);) {
			
			ps.setLong(1, bean.getOrthopedicID());
			ps.setLong(2, bean.getOrthopedicVisitID());
			ps.setLong(3, bean.getPatientID());
			ps.setDate(4, new Date(bean.getSurgicalOrthopedicVisitDate().getTime()));
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if( rs.next() ) {
				bean.setSurgicalOrthopedicVisitID(rs.getLong(1));
			} else {
				throw new ITrustException("Error retrieving unique visit ID from database" );
			}
		} catch( Exception e ) {
			throw new ITrustException(e.getMessage());
		}
	}
	
	public List<SurgicalOrthopedicVisitBean> getAllSurgicalOrthopedicVisits( long patientID ) throws ITrustException {
		try( Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM surgicalorthopedicvisits WHERE "
						+	"PID=?" );) {
			
			ps.setLong(1, patientID);
			List<SurgicalOrthopedicVisitBean> list = loader.loadList(ps.executeQuery());
			return list;
		} catch( Exception e ) {
			throw new ITrustException("Could not retrieve surgical orthopedic records from database");
		}
	}
	
	public SurgicalOrthopedicVisitBean getSurgicalOrthopedicVisit( long visitID ) throws ITrustException {
		try( Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM surgicalorthopedicvisits WHERE "
						+	"SurgicalOrthopedicVisitID=?");) {
			
			ps.setLong(1, visitID);
			return loader.loadList(ps.executeQuery()).get(0);
		} catch( Exception e ) {
			throw new ITrustException("Could not retrieve surgical orthopedic records from database");
		}
	}
	
	public void editSurgicalOrthopedicVisit( SurgicalOrthopedicVisitBean bean ) throws ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("UPDATE surgicalorthopedicvisits SET "
						+ "OrthopedicID=?,"						//1
						+ "OrthopedicVisitID=?,"				//2
						+ "PID=?,"								//3
						+ "SurgicalOrthopedicVisitDate=?,"		//4
						+ "TotalKneeReplacement=?,"				//5
						+ "TotalJointReplacement=?,"			//6	
						+ "ACLReconstruction=?,"				//7
						+ "AnkleReplacement=?,"					//8
						+ "SpineSurgery=?,"						//9
						+ "ArthroscopicSurgery=?,"				//10	
						+ "RotatorCuffRepair=?,"				//11	
						+ "SurgeryNotes=?,"						//12
						+ "AddedVisit=? "						//13
						+ "WHERE SurgicalOrthopedicVisitID=?");	//14 
				) {
			
			ps.setLong(1, bean.getOrthopedicID());
			ps.setLong(2, bean.getOrthopedicVisitID());
			ps.setLong(3, bean.getPatientID());
			ps.setDate(4, new Date(bean.getSurgicalOrthopedicVisitDate().getTime()));
			ps.setBoolean(5, bean.getSurgery(0));
			ps.setBoolean(6, bean.getSurgery(1));
			ps.setBoolean(7, bean.getSurgery(2));
			ps.setBoolean(8, bean.getSurgery(3));
			ps.setBoolean(9, bean.getSurgery(4));
			ps.setBoolean(10, bean.getSurgery(5));
			ps.setBoolean(11, bean.getSurgery(6));
			ps.setString(12, bean.getSurgicalNotes());
			ps.setBoolean(13, bean.getAddedVisit());
			ps.setLong(14, bean.getSurgicalOrthopedicVisitID());
			ps.executeUpdate();
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public List<SurgicalOrthopedicVisitBean> getSurgicalOrthopedicVisitsByOrthopedicVisitID( long patientID, long orthpedicVisitID ) throws ITrustException {
		try( Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM surgicalorthopedicvisits WHERE "
						+	"PID=? AND OrthopedicVisitID=?" );) {
			
			ps.setLong(1, patientID);
			ps.setLong(2, orthpedicVisitID);
			List<SurgicalOrthopedicVisitBean> list = loader.loadList(ps.executeQuery());
			return list;
		} catch( Exception e ) {
			throw new ITrustException("Could not retrieve surgical orthopedic records from database");
		}
	}
}



















