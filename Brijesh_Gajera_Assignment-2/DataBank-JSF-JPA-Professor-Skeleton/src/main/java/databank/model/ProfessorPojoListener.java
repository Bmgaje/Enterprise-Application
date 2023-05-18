/*****************************************************************
 * File:  ProfessorPojoListener.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.model;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * TODO 21 - What annotations should be used on these methods?
 * https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/entity-listeners.html<br>
 */
public class ProfessorPojoListener {

	@PrePersist
	public void setCreatedOnDate(ProfessorPojo professor) {
		LocalDateTime now = LocalDateTime.now();
		professor.setCreated(now);
		//Might as well call setUpdated as well
		professor.setUpdated(now);
	}

	@PreUpdate
	public void setUpdatedDate(ProfessorPojo professor) {
		professor.setUpdated(LocalDateTime.now());
	}

}
