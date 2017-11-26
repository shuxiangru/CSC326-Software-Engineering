package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.CDCStatsBean;
import edu.ncsu.csc.itrust.beans.loaders.CDCStatsBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used for storing and getting data from the cdcbmistats table.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 */
public class CDCBmiStatsDAO extends CDCStatsDAO {

	private DAOFactory factory;
	private CDCStatsBeanLoader loader = new CDCStatsBeanLoader();
	
	/**
	 * Constructor for CDCBmiStatsDAO. Initializes the DAOFactory to have database interactions with.
	 * @param factory the DAOFactory to interact with
	 */
	public CDCBmiStatsDAO(DAOFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * Stores a CDCStatsBean into the cdcbmistats table. Inserts and creates a new entry if the sex and age of the bean
	 * are not currently in the table. Otherwise, the matching row in the table will be updated with the data from the 
	 * passed in CDCStatsBean. 
	 * @param statsBean The CDCStatsBean to store in the database
	 */
	@Override
	public void storeStats(CDCStatsBean statsBean) throws DBException {
		
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO cdcbmistats (sex, age, L, M, S) VALUES (?,?,?,?,?) "
				+ "ON DUPLICATE KEY UPDATE  sex=VALUES(sex), age=VALUES(age), L=VALUES(L), M=VALUES(M), S=VALUES(S)");) {
			
			loader.loadParameters(ps, statsBean);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Gets a CDCStatsBean with the specified sex and age from the cdcbmistats table.
	 * If there are no associated CDC stats with the specified parameters, then null is returned.
	 * @param sex integer for the sex of the patient. 1 for male and 2 for female.
	 * @param age float for the age of the patient.
	 * @return the CDCStatsBean with the specified sex and age.
	 */
	@Override
	public CDCStatsBean getCDCStats(int sex, float age) throws DBException {
		CDCStatsBean statsBean = null;
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM cdcbmistats WHERE sex=? AND age=?");) {
			ps.setInt(1, sex);
			ps.setFloat(2, age);
			ResultSet rs = ps.executeQuery();
			//Get the first and only stats bean
			List<CDCStatsBean> stats = loader.loadList(rs);
			rs.close();
			ps.close();
			if (stats.size() != 0)
				statsBean = stats.get(0);
		} catch (SQLException e) {
			throw new DBException(e);
		} 
		
		return statsBean;
	}

}
