package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.beans.loaders.ProcedureBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

/**
 * Used for managing CPT codes.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 * The CPT code set accurately describes medical, surgical, and diagnostic services 
 * and is designed to communicate uniform information about medical services and procedures 
 * among physicians, coders, patients, accreditation organizations, and payers for administrative, 
 * financial, and analytical purposes.
 *
 * @see http://www.ama-assn.org/ama/pub/physician-resources/solutions-managing-your-practice/coding-billing-insurance/cpt/about-cpt.shtml
 *  
 * 
 */
public class CPTCodesDAO {
	private DAOFactory factory;
	private ProcedureBeanLoader procedureBeanLoader = new ProcedureBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public CPTCodesDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of all CPT codes.
	 * 
	 * @return A java.util.List of ProcedureBeans for the CPT codes.
	 * @throws DBException
	 */
	public List<ProcedureBean> getAllCPTCodes() throws DBException {
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM cptcodes ORDER BY CODE"); ) {
			ResultSet rs = ps.executeQuery();
			List<ProcedureBean> loadlist = procedureBeanLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			
			throw new DBException(e);
		} 
	}
	
	/**
	 * Returns a list of all immunization CPT codes.
	 * 
	 * @return A java.util.List of all Immunization-related CPT codes.
	 * @throws DBException
	 */
	public List<ProcedureBean> getImmunizationCPTCodes() throws DBException {
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from cptcodes where attribute='immunization' order by code"); ) {
			ResultSet rs = ps.executeQuery();
			List<ProcedureBean> loadlist = procedureBeanLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			
			throw new DBException(e);
		} 
	}
	
	/**
	 * Returns a list of all non-immunization CPT codes.
	 * 
	 * @return A java.util.List of all Immunization-related CPT codes.
	 * @throws DBException
	 */
	public List<ProcedureBean> getProcedureCPTCodes() throws DBException {
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("select * from cptcodes where attribute is NULL order by code"); ) {
			ResultSet rs = ps.executeQuery();
			List<ProcedureBean> loadlist = procedureBeanLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			
			throw new DBException(e);
		} 
	}

	/**
	 * Returns a particular procedure description for a code.
	 * 
	 * @param code The string representation of the code.
	 * @return The textual description of the code.
	 * @throws DBException
	 */
	public ProcedureBean getCPTCode(String code) throws DBException {
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM cptcodes WHERE Code = ?"); ) {
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				ProcedureBean loaded = procedureBeanLoader.loadSingle(rs);
				rs.close();
				return loaded;
			}
			rs.close();
			return null;
		} catch (SQLException e) {
			
			throw new DBException(e);
		} 
	}

	/**
	 * Adds a new CPT code, returns that it was added successfully
	 * 
	 * @param proc A ProcedureBean representing the new code information.
	 * @return A boolean for whether the operation was successful.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean addCPTCode(ProcedureBean proc) throws DBException, ITrustException {
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO cptcodes (Code, Description, Attribute) " + "VALUES (?,?,?)"); ) {
			ps.setString(1, proc.getCPTCode());
			ps.setString(2, proc.getDescription());
			ps.setString(3, proc.getAttribute());
			boolean check = (1==ps.executeUpdate());
			return check;
		} catch (SQLException e) {
			
			if (1062 == e.getErrorCode())
				throw new ITrustException("Error: Code already exists.");
			throw new DBException(e);
		} 
	}

	/**
	 * Change the procedure description for a particular CPT code
	 * 
	 * @param proc A ProcedureBean representing the new code information.
	 * @return A boolean for whether the operation was successful.
	 * @throws DBException
	 */
	public int updateCode(ProcedureBean proc) throws DBException {
		try(Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("UPDATE cptcodes SET Description = ?, Attribute = ? WHERE Code = ?"); ) {
			ps.setString(1, proc.getDescription());
			ps.setString(2, proc.getAttribute());
			ps.setString(3, proc.getCPTCode());
			int check = ps.executeUpdate();
			return check;
		} catch (SQLException e) {
			
			throw new DBException(e);
		}
	}

}
