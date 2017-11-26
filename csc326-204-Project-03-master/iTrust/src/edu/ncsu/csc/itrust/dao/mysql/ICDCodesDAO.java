package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.loaders.DiagnosisBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

/**
 * Used for managing all ICD codes.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 * The International Statistical Classification of Diseases and Related Health Problems 
 * (most commonly known by the abbreviation ICD) provides codes to classify diseases and a 
 * wide variety of signs, symptoms, abnormal findings, complaints, social circumstances and 
 * external causes of injury or disease. 
 * 
 * @see http://www.cdc.gov/nchs/icd9.htm
 *  
 * 
 */
public class ICDCodesDAO {
	private DAOFactory factory;
	private DiagnosisBeanLoader diagnosisLoader = new DiagnosisBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public ICDCodesDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns all non-Ophthalmology ICD9CM codes sorted by code
	 * 
	 * @return java.util.List of DiagnosisBeans
	 * @throws DBException
	 */
	public List<DiagnosisBean> getAllICDCodes() throws DBException {

		
		try (Connection conn = factory.getConnection();

				PreparedStatement ps = conn.prepareStatement("SELECT * FROM icdcodes where Ophthalmology='no' ORDER BY CODE");
			)
		{
			ResultSet rs = ps.executeQuery();
			List<DiagnosisBean> loadlist = diagnosisLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			
			throw new DBException(e);
		} 
	}
	

	
	/**
	 * Returns all ICD9CM codes sorted by code
	 * 
	 * @return java.util.List of DiagnosisBeans
	 * @throws DBException
	 */
	public List<DiagnosisBean> getAllCodes() throws DBException {

		
		try (Connection conn = factory.getConnection();

				PreparedStatement ps = conn.prepareStatement("SELECT * FROM icdcodes ORDER BY CODE");
			)
		{
			ResultSet rs = ps.executeQuery();
			List<DiagnosisBean> loadlist = diagnosisLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			
			throw new DBException(e);
		}
	}

	/**
	 * Returns a particular description for a given code
	 * 
	 * @param code The String representation of the code.
	 * @return A DiagnosisBean of the code.
	 * @throws DBException
	 */
	public DiagnosisBean getICDCode(String code) throws DBException {
	
		
		try(Connection conn = factory.getConnection();

				PreparedStatement ps = conn.prepareStatement("SELECT * FROM icdcodes WHERE Code = ?");
			)
		{
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				DiagnosisBean result = diagnosisLoader.loadSingle(rs);
				rs.close();
				return result;
			}
			rs.close();
			return null;
		} catch (SQLException e) {
			
			throw new DBException(e);
		}
	}

	/**
	 * Adds an ICD9CM code. Returns whether or not the change was made.
	 * 
	 * @param diag The DiagnosisBean representing the changes.
	 * @return A boolean indicating success.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean addICDCode(DiagnosisBean diag) throws DBException, ITrustException {

		try (Connection conn = factory.getConnection();

				PreparedStatement ps = conn.prepareStatement("INSERT INTO icdcodes (Code, Description, Chronic, Ophthalmology) " + "VALUES (?,?,?,?)");
			)
		{
			ps.setString(1, diag.getICDCode());
			ps.setString(2, diag.getDescription());
			ps.setString(3, diag.getClassification());
			ps.setString(4, diag.getOphthalmology());
			return (1 == ps.executeUpdate());
		} catch (SQLException e) {
			
			if (1062 == e.getErrorCode())
				throw new ITrustException("Error: Code already exists.");
			throw new DBException(e);
		}
	}

	/**
	 * Changes a the description of a particular code.
	 * 
	 * @param diag A DiagnosisBean representing the changes.
	 * @return A boolean indicating the number of updated rows.
	 * @throws DBException
	 */
	public int updateCode(DiagnosisBean diag) throws DBException {
		int rows;

		
		try (Connection conn = factory.getConnection();

				PreparedStatement ps = conn.prepareStatement("UPDATE icdcodes SET Description = ?, Chronic = ?, URL = ?, Ophthalmology = ? WHERE Code = ?");
			)
		{
			ps.setString(1, diag.getDescription());
			ps.setString(2, diag.getClassification());
			ps.setString(3, diag.getURL());
			ps.setString(4, diag.getOphthalmology());
			ps.setString(5, diag.getICDCode());
			rows = ps.executeUpdate();
		} catch (SQLException e) {
			
			throw new DBException(e);
		}
		return rows;
	}

}
