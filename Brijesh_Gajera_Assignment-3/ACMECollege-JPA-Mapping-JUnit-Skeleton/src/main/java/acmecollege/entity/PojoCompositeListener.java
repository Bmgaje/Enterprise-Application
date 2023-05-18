
/***************************************************************************

 *File: PojoListener.java Course materials (23W) CST 8277
 *@author Teddy Yap
 *@author Shariar (Shawn) Emami
 *NOTE: This file has been modified from the original version
 */
package acmecollege.entity;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@SuppressWarnings("unused")
public class PojoCompositeListener {


/**
 * This method sets the created date and time for a pojoBaseComposite object
 * just before it is persisted into the database.
 * 
 * @param pojoBaseComposite - the pojoBaseComposite object to be persisted
 */
@PrePersist
public void setCreatedOnDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
	LocalDateTime now = LocalDateTime.now();
	pojoBaseComposite.setCreated(now);
}

/**
 * This method sets the updated date and time for a pojoBaseComposite object
 * just before it is updated in the database.
 * 
 * @param pojoBaseComposite - the pojoBaseComposite object to be updated
 */
@PreUpdate
public void setUpdatedDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
	LocalDateTime now = LocalDateTime.now();
	pojoBaseComposite.setUpdated(now);
}
}