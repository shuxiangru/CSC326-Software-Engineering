package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ZipCodeBean;
import edu.ncsu.csc.itrust.beans.loaders.ZipCodeLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * DAO for the Zip Code table
 */
public class ZipCodeDAO 
{
	private DAOFactory factory;
	
	public ZipCodeDAO(DAOFactory factory)
	{
		this.factory = factory;
	}
	
	/**
	 * Returns the zip code bean for a particular zip code.
	 * @param zipCode
	 * @return
	 * @throws DBException 
	 */
	public ZipCodeBean getZipCode(String zipCode) throws DBException
	{
		ZipCodeLoader loader = new ZipCodeLoader();
		ZipCodeBean bean = null;
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM zipcodes WHERE zip=?");){
			
			ps.setString(1, zipCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				bean = loader.loadSingle(rs);
				rs.close();
			
				
			} else{
				rs.close();
				return null;
			}
		} catch (SQLException e) {
			
			throw new DBException(e);
		} 
		
		return bean;
	}
	
}
