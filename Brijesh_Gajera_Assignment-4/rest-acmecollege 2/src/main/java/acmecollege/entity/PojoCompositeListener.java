/***************************************************************************
 * File:  PojoListener.java Course materials (23W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 *
 * Updated by:  Group 10
 *   040983871, Andres, Alvarado
 *   040992268, Khaled, Albowaidani
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 * 
 */
package acmecollege.entity;

import static java.time.LocalDateTime.now;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PojoCompositeListener {

	@PrePersist
	public void setCreatedOnDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		pojoBaseComposite.setCreated(now());
		pojoBaseComposite.setUpdated(now());
	}

	@PreUpdate
	public void setUpdatedDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		pojoBaseComposite.setUpdated(now());
	}

}
