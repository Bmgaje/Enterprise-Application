/***************************************************************************
 * File:  AcademicStudentClub.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
//TODO ASC01 - Add missing annotations, please see Week 9 slides page 15.  Value 1 is academic and value 0 is non-academic.
public class AcademicStudentClub extends StudentClub implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "is_academic")
    private int isAcademic;

    public AcademicStudentClub() {
    }

    public int getIsAcademic() {
        return isAcademic;
    }

    public void setIsAcademic(int isAcademic) {
        this.isAcademic = isAcademic;
    }
}
