/***************************************************************************
 * File:  Student.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the STUDENT database table in the acmecollege schema
 * </br></br>
 * 
 * Note:  This is <b>NOT</b> the same Student entity from Lab 1/Assignment 1/Assignment 2.
 * </br>
 * This entity does <b>NOT</b> have member fields email, phoneNumber, level, or program.
 * </br>
 * 
 */
@Entity
@Table(name = "STUDENT", schema = "acmecollege")
public class Student extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@javax.persistence.Column(name = "FIRSTNAME")
	private String firstName;

	@javax.persistence.Column(name = "LASTNAME")
	private String lastName;

	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
	private Set<MembershipCard> membershipCards = new HashSet<>();

	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
	private Set<CourseRegistration> courseRegistrations = new HashSet<>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "STUDENT_COURSE_REGISTRATION", schema = "acmecollege", joinColumns = {
			@JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "COURSE_ID", referencedColumnName = "ID") })
	private Set<Course> courses = new HashSet<>();

	public Set<MembershipCard> getMembershipCards() {
		return membershipCards;
	}

	public void setMembershipCards(Set<MembershipCard> membershipCards) {
		this.membershipCards = membershipCards;
	}

	public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	// Inherited hashCode/equals is sufficient for this entity class

}
