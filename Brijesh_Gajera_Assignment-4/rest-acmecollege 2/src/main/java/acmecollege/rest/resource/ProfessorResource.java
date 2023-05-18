/**
 * File:  ProfessorResource.java Course materials (23W) CST 8277
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
import static acmecollege.utility.MyConstants.PROFESSOR_SUBRESOURCE_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmecollege.utility.MyConstants.STUDENT_COURSE_PROFESSOR_RESOURCE_PATH;
import static acmecollege.utility.MyConstants.USER_ROLE;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
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


@Path(PROFESSOR_SUBRESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfessorResource {
	
    private static final Logger LOG = LogManager.getLogger();
    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getProfessor() {
        LOG.debug("retrieving all professors ...");
        List<Professor> professors = service.getAllProfessors();
        Response response = Response.ok(professors).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getProfessorById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("try to retrieve specific professsor " + id);
        Response response = null;
        Professor professor = null;

        if (sc.isCallerInRole(ADMIN_ROLE)) {
        	professor = service.getProfessorById(id);
            response = Response.status(professor == null ? Status.NOT_FOUND : Status.OK).entity(professor).build();
        } else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            professor = sUser.getProfessor();
            if (professor != null && professor.getId() == id) {
                response = Response.status(Status.OK).entity(professor).build();
            } else {
                throw new ForbiddenException("User trying to access resource it does not own (wrong userid)");
            }
        } else {
            response = Response.status(Status.BAD_REQUEST).build();
        }
        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addPerson(Professor newProfessor) {
        Response response = null;
        Professor newProfessorWithIdTimestamps = service.persistProfessor(newProfessor);
        // Build a SecurityUser linked to the new professor
        service.buildUserForNewProfessor(newProfessorWithIdTimestamps);
        response = Response.ok(newProfessorWithIdTimestamps).build();
        return response;
    }

    //not sure if we need to keep it or delete it
    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(STUDENT_COURSE_PROFESSOR_RESOURCE_PATH)
    public Response updateProfessorForStudentCourse(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId, Professor newProfessor) {
        Response response = null;
        Professor professor = service.setProfessorForStudentCourse(studentId, courseId, newProfessor);
        response = Response.ok(professor).build();
        return response;
    }

}
