/***************************************************************************
 * File:  NonAcademicStudentClub.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 */
package acmecollege.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("0") // Value 1 is academic and value 0 is non-academic.
public class NonAcademicStudentClub extends StudentClub implements Serializable {
private static final long serialVersionUID = 1L;

public NonAcademicStudentClub() {

}
} 