/**
 * File:  CourseResource.java Course materials (23W) CST 8277
 * 
 * Created by:  Group 10
 *   040983871, Andres, Alvarado
 *   040992268, Khaled, Albowaidani
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 * 
 */
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.CLUB_MEMBERSHIP_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.STUDENT_COURSE_PROFESSOR_RESOURCE_PATH;
import static acmecollege.utility.MyConstants.COURSE_PROFESSOR_RESOURCE_PATH;
import static acmecollege.utility.MyConstants.STUDENT_COURSE_PROFESSOR_RESOURCE_PATH;
import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.PU_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmecollege.utility.MyConstants.USER_ROLE;

import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.Professor;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;
import acmecollege.entity.ClubMembership;
import acmecollege.entity.Course;
import acmecollege.entity.CourseRegistration;

@Path(COURSE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

	private static final Logger LOG = LogManager.getLogger();
	
    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getAllCourse() {
        LOG.debug("retrieving all courses ...");
        List<Course> courses = service.getAll(Course.class, Course.ALL_COURSES_QUERY);
        Response response = Response.ok(courses).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCourseById(int id) {
        LOG.debug("Retrieving courses with id = {}", id);
        Course courses = service.getById(Course.class, Course.FIND_COURSES_BY_ID,id);
        Response response = Response.ok(courses).build();
        return response;
    }



    @POST
    @RolesAllowed({ADMIN_ROLE})
    public void addCourse(Course newCourse) {
	    LOG.debug("Adding a new course = {}", newCourse);
	    Course existingCourse = service.getById(Course.class, Course.FIND_COURSES_BY_ID, newCourse.getId());
	    if (existingCourse != null) {
	        throw new EntityExistsException("Course already exists");
	    }
	    em.persist(newCourse);
    }
    
    @RolesAllowed({ADMIN_ROLE})
    public void deleteCourseById(int id) {
    	Course course = service.getById(Course.class, Course.FIND_COURSES_BY_ID, id);
        if (course != null) {
            em.remove(course);
        }
    }
    
    @RolesAllowed({ADMIN_ROLE})
    public Professor setProfessorForStudentCourse(int studentId, int courseId, Professor newProfessor) {
        Student studentToBeUpdated = em.find(Student.class, studentId);
        if (studentToBeUpdated != null) { // Student exists
            Set<CourseRegistration> courseRegistrations = studentToBeUpdated.getCourseRegistrations();
            courseRegistrations.forEach(c -> {
                if (c.getCourse().getId() == courseId) {
                    if (c.getProfessor() != null) { // Professor exists
                        Professor prof = em.find(Professor.class, c.getProfessor().getId());
                        prof.setProfessor(newProfessor.getFirstName(),
                                          newProfessor.getLastName(),
                                          newProfessor.getDepartment());
                        em.merge(prof);
                    } else { // Professor does not exist
                    	CourseRegistration newCourseRegistration = new CourseRegistration();
                    	newCourseRegistration.setCourse(c.getCourse());
                    	newCourseRegistration.setProfessor(newProfessor);

                    	Set<CourseRegistration> courseRegis = studentToBeUpdated.getCourseRegistrations();
                    	courseRegis.add(newCourseRegistration);

                    	studentToBeUpdated.setCourseRegistrations(courseRegis);
                    	em.merge(studentToBeUpdated);
                    }
                }
            });
            return newProfessor;
        } else {
            return null;  // Student doesn't exist
        }
    }
}
