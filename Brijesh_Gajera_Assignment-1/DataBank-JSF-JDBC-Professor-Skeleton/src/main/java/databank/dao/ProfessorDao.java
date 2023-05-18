/******************************************************
 * File:  ProfessorDao.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.dao;

import java.util.List;

import databank.model.ProfessorPojo;

/**
 * Description:  API for the database C-R-U-D operations
 */
public interface ProfessorDao {

	// C
	public ProfessorPojo createProfessor(ProfessorPojo professor);

	// R
	public ProfessorPojo readProfessorById(int professorId);

	public List<ProfessorPojo> readAllProfessors();

	// U
	public void updateProfessor(ProfessorPojo professor);

	// D
	public void deleteProfessorById(int professorId);

}