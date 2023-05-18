/******************************************************
 * File:  ListDataDaoImpl.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 */
package databank.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

@SuppressWarnings("unused")
/**
 * Description:  API for reading list data from the database
 */
@Named
@ApplicationScoped
public class ListDataDaoImpl implements ListDataDao, Serializable {
	/** Explicitly set serialVersionUID */
	private static final long serialVersionUID = 1L;

	private static final String READ_ALL_DEGREES = "SELECT * FROM degree";
	private static final String READ_ALL_MAJORS = "SELECT * FROM major";

	@PersistenceContext(name = "PU_DataBank")
	protected EntityManager em;

	@Inject
	protected ExternalContext externalContext;

	private void logMsg(String msg) {
		((ServletContext) externalContext.getContext()).log(msg);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> readAllDegrees() {
		logMsg("reading all degrees");
		List<String> degrees = new ArrayList<>();
		try {
			degrees = (List<String>) em.createNativeQuery(READ_ALL_DEGREES).getResultList();
		}
		catch (Exception e) {
			logMsg("something went wrong:  " + e.getLocalizedMessage());
		}
		return degrees;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> readAllMajors() {
		logMsg("reading all majors");
		List<String> majors = new ArrayList<>();
		try {
			majors = (List<String>) em.createNativeQuery(READ_ALL_MAJORS).getResultList();
		}
		catch (Exception e) {
			logMsg("something went wrong:  " + e.getLocalizedMessage());
		}
		return majors;
	}
	
}
