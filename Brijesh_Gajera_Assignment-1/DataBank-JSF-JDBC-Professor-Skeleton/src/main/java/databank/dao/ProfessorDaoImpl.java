/******************************************************
 * File:  ProfessorDaoImpl.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.dao;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import databank.model.ProfessorPojo;

@SuppressWarnings("unused")
/**
 * Description:  Implements the C-R-U-D API for the database
 */
//TODO don't forget this object is a managed bean with a application scope
public class ProfessorDaoImpl implements ProfessorDao, Serializable {
	/** Explicitly set serialVersionUID */
	private static final long serialVersionUID = 1L;

	private static final String DATABANK_DS_JNDI = "java:app/jdbc/databank";
	private static final String READ_ALL = "SELECT * FROM professor";
	private static final String READ_PROFESSOR_BY_ID = "SELECT * FROM professor where id = ?";
	private static final String INSERT_PROFESSOR = "INSERT INTO professor(last_name, first_name, email, phone, degree, major, created) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_PROFESSOR_ALL_FIELDS = "UPDATE professor set last_name = ?, first_name = ?, email = ?, phone = ?, degree = ?, major = ? where id = ?";
	private static final String DELETE_PROFESSOR_BY_ID = "DELETE FROM professor where id = ?";

	@Inject
	protected ExternalContext externalContext;

	private void logMsg(String msg) {
		((ServletContext) externalContext.getContext()).log(msg);
	}

	@Resource(lookup = DATABANK_DS_JNDI)
	protected DataSource databankDS;

	protected Connection conn;
	protected PreparedStatement readAllPstmt;
	protected PreparedStatement readByIdPstmt;
	protected PreparedStatement createPstmt;
	protected PreparedStatement updatePstmt;
	protected PreparedStatement deleteByIdPstmt;

	@PostConstruct
	protected void buildConnectionAndStatements() {
		try {
			logMsg("building connection and stmts");
			conn = databankDS.getConnection();
			readAllPstmt = conn.prepareStatement(READ_ALL);
			createPstmt = conn.prepareStatement(INSERT_PROFESSOR, RETURN_GENERATED_KEYS);
			readByIdPstmt= conn.prepareStatement(READ_PROFESSOR_BY_ID);
			updatePstmt = conn.prepareStatement(UPDATE_PROFESSOR_ALL_FIELDS);
			deleteByIdPstmt = conn.prepareStatement(DELETE_PROFESSOR_BY_ID);
		} catch (Exception e) {
			logMsg("something went wrong getting connection from database:  " + e.getLocalizedMessage());
		}
	}

	@PreDestroy
	protected void closeConnectionAndStatements() {
		try {
			logMsg("closing stmts and connection");
			readAllPstmt.close();
			createPstmt.close();
			readByIdPstmt.close();
			updatePstmt.close();
			deleteByIdPstmt.close();
			conn.close();
		} catch (Exception e) {
			logMsg("something went wrong closing stmts or connection:  " + e.getLocalizedMessage());
		}
	}

	@Override
	public List<ProfessorPojo> readAllProfessors() {
		logMsg("reading all professors");
		List<ProfessorPojo> professors = new ArrayList<>();
		try (ResultSet rs = readAllPstmt.executeQuery();) {

			while (rs.next()) {
				ProfessorPojo newProfessor = new ProfessorPojo();
				newProfessor.setId(rs.getInt("id"));
				newProfessor.setLastName(rs.getString("last_name"));
			    newProfessor.setFirstName(rs.getString("first_name"));
				newProfessor.setEmail(rs.getString("email"));
				newProfessor.setPhoneNumber (rs.getString("phoneNumber"));
				newProfessor.setDegree(rs.getString("degree"));
				newProfessor.setMajor(rs.getString("major"));
				newProfessor.setCreated( (LocalDateTime)rs.getObject("created"));
				professors.add(newProfessor);
			}
			
		} catch (SQLException e) {
			logMsg("something went wrong accessing database:  " + e.getLocalizedMessage());
		}
		
		return professors;

	}

	@Override
	public ProfessorPojo createProfessor(ProfessorPojo professor) {
		logMsg("creating a professor");
			try {
				createPstmt.setString(1, professor.getLastName());
				createPstmt.setString(2, professor.getFirstName());
				createPstmt.setString(3, professor.getEmail());
				createPstmt.setString(4, professor.getPhoneNumber());
				createPstmt.setString(5, professor.getDegree());
				createPstmt.setString(6, professor.getMajor());
				createPstmt.setObject(7, professor.getCreated());
				createPstmt.executeUpdate();
				ResultSet rs = createPstmt.getGeneratedKeys();
				if (rs.next()) {
					professor.setId(rs.getInt(1));
				}
				}
			    catch (SQLException e) {
					logMsg("something went wrong accessing database:  " + e.getLocalizedMessage());
				}
				
				return professor;
				}


		@Override
		public ProfessorPojo readProfessorById(int professorId) {
			logMsg("read a specific professor");
			ProfessorPojo proid = null;
			try {   readByIdPstmt.setLong(1, professorId);
					ResultSet rs = readByIdPstmt.executeQuery(); {

				while (rs.next()) {
				    proid = new ProfessorPojo();
				    proid.setId(rs.getInt("id"));
				    proid.setLastName(rs.getString("last_name"));
				    proid.setFirstName(rs.getString("first_name"));
					proid.setEmail(rs.getString("email"));
					proid.setPhoneNumber(rs.getString("phoneNumber"));
					proid.setDegree(rs.getString("degree"));
					proid.setMajor(rs.getString("major"));
					proid.setCreated((LocalDateTime)rs.getObject("created"));
					
					}
				
					}} catch (SQLException e) {
				logMsg("something went wrong accessing database:  " + e.getLocalizedMessage());
			}
			return proid;
		}
	

	@Override
	public void updateProfessor(ProfessorPojo professor) {
		logMsg("updating a specific professor");
		try {
			  updatePstmt.setString(1, professor.getLastName());
				updatePstmt.setString(2,professor.getFirstName());
				updatePstmt.setString(3,professor.getEmail());
				updatePstmt.setString(4,professor.getPhoneNumber());
				updatePstmt.setString(5,professor.getDegree());
				updatePstmt.setString(6,professor.getMajor());
				updatePstmt.setObject(7, professor.getCreated());
		        updatePstmt.executeUpdate();
		}
		catch(SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
    	}
	}

	@Override
	public void deleteProfessorById(int professorId) {
		logMsg("deleting a specific professor");
		try {
			deleteByIdPstmt.setLong(1, professorId);
			deleteByIdPstmt.execute();
    	}
    	catch(SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
    	}	}

}