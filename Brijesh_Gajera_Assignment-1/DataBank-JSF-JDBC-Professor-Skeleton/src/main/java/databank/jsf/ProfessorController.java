/************************************************************
 * File:  ProfessorController.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.SessionMap;
import javax.inject.Inject;
import javax.inject.Named;

import databank.dao.ListDataDao;
import databank.dao.ProfessorDao;
import databank.model.ProfessorPojo;

/**
 * Description:  Responsible for collection of Professor Pojo's in XHTML (list) <h:dataTable> </br>
 * Delegates all C-R-U-D behavior to DAO
 */

//TODO Don't forget this object is a managed bean with a session scope
@Named
@SessionScoped


public class ProfessorController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	@SessionMap
	private Map<String, Object> sessionMap;

	@Inject
	private ProfessorDao professorDao;

	@Inject
	private ListDataDao listDataDao;

	private List<ProfessorPojo> professors;

	//Necessary methods to make controller work

	public void loadProfessors() {
		setProfessors(professorDao.readAllProfessors());
	}

	public List<ProfessorPojo> getProfessors() {
		return professors;
	}

	public void setProfessors(List<ProfessorPojo> professors) {
		this.professors = professors;
	}

	public List<String> getDegrees() {
		return this.listDataDao.readAllDegrees();
	}
	
	public List<String> getMajors() {
		return this.listDataDao.readAllMajors();
	}

	public String navigateToAddForm() {
		//Pay attention to the name here, it will be used as the object name in add-professor.xhtml
		//ex. <h:inputText value="#{newProfessor.firstName}" id="firstName" />
		sessionMap.put("newProfessor", new ProfessorPojo());
		return "add-professor.xhtml?faces-redirect=true";
	}

	public String submitProfessor(ProfessorPojo professor) {
		professor.setCreated(LocalDateTime.now());
		professorDao.createProfessor(professor);
		return "list-professors.xhtml?faces-redirect=true";
	}

	public String navigateToUpdateForm(int professorId) {
		ProfessorPojo professor = professorDao.readProfessorById(professorId);
		sessionMap.put("editedProfessor", professor);
		return "edit-professor.xhtml?faces-redirect=true";
	}

	public String submitUpdatedProfessor(ProfessorPojo professor) {
		professorDao.updateProfessor(professor);
		return "list-professors.xhtml?faces-redirect=true";
	}

	public String deleteProfessor(int professorId) {
		professorDao.deleteProfessorById(professorId);
		return "list-professors.xhtml?faces-redirect=true";
	}

}
