/***************************************************************************
/ * File:  Course.java Course materials (23W) CST 8277
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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("unused")
@Entity(name = "Course")
@Table(name = "course")
/**
 * The persistent class for the course database table.
 */
//TODO CO01 - Add the missing annotations.
public class Course extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "course_code")
	// TODO CO03 - Add missing annotations.
	@Basic(optional = false)
	private String courseCode;

	@Column(name = "course_title")
	// TODO CO04 - Add missing annotations.
	@Basic(optional = false)
	private String courseTitle;

	@Column(name = "year")
	// TODO CO05 - Add missing annotations.
	@Basic(optional = false)
	private int year;

	@Column(name = "semester")
	// TODO CO06 - Add missing annotations.
	@Basic(optional = false)
	private String semester;

	@Column(name = "credit_units")
	// TODO CO07 - Add missing annotations.
	@Basic(optional = false)
	private int creditUnits;

	@Column(name = "online")
	// TODO CO08 - Add missing annotations.
	@Basic(optional = false)
	private byte online;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
	// TODO CO09 - Add annotations for 1:M relation. Changes to this class should not cascade.
	private Set<CourseRegistration> courseRegistrations = new HashSet<>();

	public Course() {
		super();
	}

	public Course(String courseCode, String courseTitle, int year, String semester, int creditUnits, byte online) {
		this();
		this.courseCode = courseCode;
		this.courseTitle = courseTitle;
		this.year = year;
		this.semester = semester;
		this.creditUnits = creditUnits;
		this.online = online;
	}

	public Course setCourse(String courseCode, String courseTitle, int year, String semester, int creditUnits, byte online) {
		setCourseCode(courseCode);
		setCourseTitle(courseTitle);
		setYear(year);
		setSemester(semester);
		setCreditUnits(creditUnits);
		setOnline(online);
		return this;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public int getCreditUnits() {
		return creditUnits;
	}

	public void setCreditUnits(int creditUnits) {
		this.creditUnits = creditUnits;
	}

	public byte getOnline() {
		return online;
	}

	public void setOnline(byte online) {
		this.online = online;
	}
	
	public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

	//Inherited hashCode/equals is sufficient for this Entity class

}
