/*****************************************************************
 * File:  NewProfessorView.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;

import databank.model.ProfessorPojo;

/**
 * This class represents the scope of adding a new professor to the DB.
 * 
 * TODO 09 - This class is a managed bean.  Use the name "newProfessor".<br>
 * TODO 10 - Like previous assignment where ProfessorPojo was view scoped, this class is?<br>
 * TODO 11 - Add the missing variables and their getters and setters.  Have in mind dates and version are internal
 * only.<br>
 * 
 */
@Named("newProfessor")
@ApplicationScoped
public class NewProfessorView implements Serializable {
	/** explicit set serialVersionUID */
	private static final long serialVersionUID = 1L;

	protected String lastName;
	protected String firstName;
	private String email;
    private String phoneNumber;
    private LocalDateTime created;
    private LocalDateTime updated;
    private int version = 1;
    private boolean editable;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Inject
	@ManagedProperty("#{professorController}")
	protected ProfessorController professorController;

	public NewProfessorView() {
	}

	/**
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void addProfessor() {
		if (allNotNullOrEmpty(firstName, lastName, email, phoneNumber, created, updated, version, editable  )) {
			ProfessorPojo theNewProfessor = new ProfessorPojo();
			theNewProfessor.setFirstName(getFirstName());
			theNewProfessor.setLastName(getLastName());
			theNewProfessor.setEmail(getEmail());
			theNewProfessor.setPhoneNumber(getPhoneNumber());
			theNewProfessor.setCreated(getCreated());
			theNewProfessor.setUpdated(getUpdated());
			theNewProfessor.setVersion(getVersion());
			theNewProfessor.setEditable(isEditable());
			
			professorController.addNewProfessor(theNewProfessor);
			//Clean up
			professorController.toggleAdding();
			setFirstName(null);
			setLastName(null);
			setEmail(null);
			setPhoneNumber(null);
			setCreated(null);
			setUpdated(null);
			setVersion(1);
			setEditable(false);
		}
	}

	static boolean allNotNullOrEmpty(final Object... values) {
		if (values == null) {
			return false;
		}
		for (final Object val : values) {
			if (val == null) {
				return false;
			}
			if (val instanceof String) {
				String str = (String) val;
				if (str.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}
}
