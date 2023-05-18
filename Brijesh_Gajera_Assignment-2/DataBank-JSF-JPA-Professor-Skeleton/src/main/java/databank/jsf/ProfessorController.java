/*****************************************************************
z  * File:  ProfessorController.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import databank.dao.ListDataDao;
import databank.dao.ProfessorDao;
import databank.model.ProfessorPojo;

/**
 * <p>
 * Description:  Responsible for collection of ProfessorPojo's in XHTML (list) <h:dataTable> </br>
 * Delegates all C-R-U-D behavior to DAO
 * </p>
 * 
 * <p>
 * This class is complete.
 * </p>
 */
@Named("professorController")
@SessionScoped
public class ProfessorController implements Serializable {
	private static final long serialVersionUID = 1L;

	//Get the log4j2 logger for this class
	private static final Logger LOG = LogManager.getLogger();

	public static final String UICONSTS_BUNDLE_EXPR = "#{uiconsts}";
	public static final String PROFESSOR_MISSING_REFRESH_BUNDLE_MSG = "refresh";
	public static final String PROFESSOR_OUTOFDATE_REFRESH_BUNDLE_MSG = "outOfDate";

	@Inject
	protected FacesContext facesContext;

	@Inject
	protected ProfessorDao professorDao;

	@Inject
	protected ListDataDao listDataDao;

	@Inject
	@ManagedProperty(UICONSTS_BUNDLE_EXPR)
	protected ResourceBundle uiconsts;

	protected List<ProfessorPojo> professors;
	//Boolean used for toggling the rendering of add professor in index.xhtml
	protected boolean adding;

	public void loadProfessors() {
		LOG.debug("loadProfessors");
		professors = professorDao.readAllProfessors();
	}

	public List<ProfessorPojo> getProfessors() {
		return this.professors;
	}

	public void setProfessors(List<ProfessorPojo> professors) {
		this.professors = professors;
	}

	public boolean isAdding() {
		return adding;
	}

	public void setAdding(boolean adding) {
		this.adding = adding;
	}

	/**
	 * Toggles the add professor mode which determines whether the addProfessor form is rendered
	 */
	public void toggleAdding() {
		this.adding = !this.adding;
	}

	public String editProfessor(ProfessorPojo professor) {
		LOG.debug("editProfessor = {}", professor);
		professor.setEditable(true);
		return null; //Stay on current page
	}

	public String updateProfessor(ProfessorPojo professorWithEdits) {
		LOG.debug("updateProfessor = {}", professorWithEdits);
		ProfessorPojo professorToBeUpdated = professorDao.readProfessorById(professorWithEdits.getId());
		if (professorToBeUpdated == null) {
			LOG.debug("FAILED update professor, does not exists = {}", professorToBeUpdated);
			//If the professor being updated does not exists, send an error message to index.xhtml
			//<h:messages globalOnly="true" layout="table" styleClass="alert alert-danger"/>
			//null clientId means it is global not for a specific h:message
			//Someone else deleted it
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					uiconsts.getString(PROFESSOR_MISSING_REFRESH_BUNDLE_MSG), null));
		} else {
			professorToBeUpdated = professorDao.updateProfessor(professorWithEdits);
			if (professorToBeUpdated == null) {
				LOG.debug("FAILED update professor = {}", professorToBeUpdated);
				//It could be the transaction is already been committed
				//OptimisticLockException 
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						uiconsts.getString(PROFESSOR_OUTOFDATE_REFRESH_BUNDLE_MSG), null));
			} else {
				professorToBeUpdated.setEditable(false);
				int idx = professors.indexOf(professorWithEdits);
				professors.remove(idx);
				professors.add(idx, professorToBeUpdated);
			}
		}
		return null; //Stay on current page
	}

	public String cancelUpdate(ProfessorPojo professor) {
		LOG.debug("cancelUpdate = {}", professor);
		professor.setEditable(false);
		return null; //Stay on current page
	}

	public void deleteProfessor(int professorId) {
		LOG.debug("deleteProfessor = {}", professorId);
		ProfessorPojo professorToBeRemoved = professorDao.readProfessorById(professorId);
		if (professorToBeRemoved == null) {
			LOG.debug("failed deleteProfessor does not exists = {}", professorId);
			return;
		}
		professorDao.deleteProfessorById(professorId);
		professors.remove(professorToBeRemoved);
	}

	public void addNewProfessor(ProfessorPojo professor) {
		LOG.debug("adding new professor = {}", professor);
		ProfessorPojo newProfessor = professorDao.createProfessor(professor);
		professors.add(newProfessor);
	}

	public String refreshProfessorForm() {
		LOG.debug("refreshProfessorForm");
		//Clear all messaged in facesContext first
		Iterator<FacesMessage> facesMessageIterator = facesContext.getMessages();
		while (facesMessageIterator.hasNext()) {
			facesMessageIterator.remove();
		}
		return "index.xhtml?faces-redirect=true";
	}

	public List<String> getDegrees() {
		return listDataDao.readAllDegrees();
	}
	
	public List<String> getMajors() {
		return listDataDao.readAllMajors();
	}

}
