/**
 * File:  ClubMembershipResource.java Course materials (23W) CST 8277
 * 
 * Created by:  Group 10
 *   040983871, Andres, Alvarado
 *   040992268, Khaled, Albowaidani
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 * 
 */
package acmecollege.rest.resource;

import static acmecollege.entity.SecurityRole.SECURITY_ROLE_FIND_ID;
import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.CLUB_MEMBERSHIP_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.PARAM1;
import static acmecollege.utility.MyConstants.PU_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmecollege.utility.MyConstants.STUDENT_COURSE_PROFESSOR_RESOURCE_PATH;
import static acmecollege.utility.MyConstants.USER_ROLE;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
import acmecollege.entity.ClubMembership;
import acmecollege.entity.Professor;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;
import acmecollege.entity.StudentClub;

@Path(CLUB_MEMBERSHIP_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClubMembershipResource {

	private static final Logger LOG = LogManager.getLogger();
	
    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;
    
    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getClubMembership() {
        LOG.debug("retrieving all club memberships ...");
        List<ClubMembership> clubMem = service.getAll(ClubMembership.class, ClubMembership.FIND_ALL);
        Response response = Response.ok(clubMem).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getClubMembershipById(int cmId) {
        LOG.debug("Retrieving club membership with id = {}", cmId);
        ClubMembership clubMembership = service.getClubMembershipById(cmId);
        Response response = Response.ok(clubMembership).build();
        return response;
    }



    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addClubMembership(ClubMembership newClubMembership) {
	    LOG.debug("Adding a new club membership = {}", newClubMembership);
    	ClubMembership tempClubMembership = service.persistClubMembership(newClubMembership);
        return Response.ok(tempClubMembership).build();
    }
    
    @RolesAllowed({ADMIN_ROLE})
    public void deleteClubMembershipById(int id) {
        ClubMembership clubMem = service.getById(ClubMembership.class, ClubMembership.FIND_BY_ID, id);
        if (clubMem != null) {
            em.remove(clubMem);
        }
    }



}