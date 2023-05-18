package databank.ejb;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import databank.model.ProfessorPojo;


@Singleton
public class ProfessorService implements Serializable {
	private static final long serialVersionUID = 1L;
	  
	private static final Logger LOG = LogManager.getLogger();
	
	@PersistenceContext( name = "PU_DataBank")
	protected EntityManager em;

	public ProfessorService() {
		
	}
	

	public List<ProfessorPojo> readAllProfessors() {
		LOG.debug("reading all professors");
		//Use the named JPQL query from the ProfessorPojo class to grab all the professors
		TypedQuery<ProfessorPojo> allProfessorsQuery = em.createNamedQuery(ProfessorPojo.PROFESSOR_FIND_ALL, ProfessorPojo.class);
		//Execute the query and return the result/s.
		return allProfessorsQuery.getResultList();
	}

	@Transactional
	public ProfessorPojo createProfessor(ProfessorPojo professor) {
		LOG.debug("creating a professor = {}", professor);
		em.persist(professor);
		return professor;
	}

	
	public ProfessorPojo readProfessorById(int professorId) {
		LOG.debug("read a specific professor = {}", professorId);
		return em.find(ProfessorPojo.class, professorId);
	}

	@Transactional
	public ProfessorPojo updateProfessor(ProfessorPojo professorWithUpdates) {
		LOG.debug("updating a specific professor = {}", professorWithUpdates);
		ProfessorPojo mergedProfessorPojo = em.merge(professorWithUpdates);
		return mergedProfessorPojo;
	}

	@Transactional
	public void deleteProfessorById(int professorId) {
		LOG.debug("deleting a specific professorID = {}", professorId);
		ProfessorPojo professor = readProfessorById(professorId);
		LOG.debug("deleting a specific professor = {}", professor);
		if (professor != null) {
			em.refresh(professor);
			em.remove(professor);
		}
	}

	

}
