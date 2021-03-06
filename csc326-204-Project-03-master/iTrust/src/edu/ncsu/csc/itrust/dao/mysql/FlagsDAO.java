package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.FlagsBean;
import edu.ncsu.csc.itrust.beans.loaders.FlagsLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * FlagsDAO is the data accessor object for the obstetrics flags.
 *
 */
public class FlagsDAO {
	private DAOFactory factory;
	private FlagsLoader flagsLoader;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public FlagsDAO(DAOFactory factory) {
		this.factory = factory;
		this.flagsLoader = new FlagsLoader();
	}
 
	/**
	 * Based on input, either inserts a new flag record, deletes an existing flag record,
	 * or does nothing. It will insert the flag if a match is not found for the MID and flagType.
	 * It will delete a flag if a match is found in the database and the flagged value is false.
	 * The idea is that the database only holds flagged flags, so missing = false.
	 * 
	 * @throws DBException
	 */
	public void setFlag(FlagsBean p) throws DBException {
		
		try(Connection conn = factory.getConnection();
				PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM flags WHERE MID = ? AND pregId = ? AND flagType = ?");
				PreparedStatement ps2 = conn.prepareStatement("DELETE FROM flags WHERE MID = ? AND pregId=? AND flagType = ?");
				PreparedStatement ps3 = conn.prepareStatement("INSERT INTO flags VALUES(?, ?, ?, ?)");) {
			ps1.setLong(1, p.getMid());
			ps1.setLong(2, p.getPregId());
			ps1.setString(3, p.getFlagValue().toString());
			ResultSet rs = ps1.executeQuery();
			
			//if the result exists
			if (rs.next()) {
				//if it's set to false, delete from the DB
				if (!p.isFlagged()) {
					rs.close();
					ps2.setLong(1, p.getMid());
					ps2.setLong(2, p.getPregId());
					ps2.setString(3, p.getFlagValue().toString());
					ps2.execute();
				}
				//else, it's true and it exists so nothing needs to change
				
				rs.close();
			}
			//else, it doesn't exist in the DB
			else {
				//if it's true, add it
				if (p.isFlagged()) {
					flagsLoader.loadParameters(ps3, p);
					ps3.execute();
				}
				//else, it's false, so ignore it
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} 
	}
	
	/**
	 * Returns a FlagsBean object for the record indicated in the FlagsBean argument.
	 * @param p
	 * @return
	 * @throws DBException
	 */
	public FlagsBean getFlag(FlagsBean p) throws DBException {
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM flags WHERE MID = ? AND pregId = ? AND flagType = ?"); ) {
			ps.setLong(1, p.getMid());
			ps.setLong(2, p.getPregId());
			ps.setString(3, p.getFlagValue().toString());
			ResultSet rs = ps.executeQuery();
			
			//now set the bean to whether or not the record exists in the database
			p.setFlagged(rs.next());
			
			return p;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}
	}
}
