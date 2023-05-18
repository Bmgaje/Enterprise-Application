/**
 * File:  MembershipCardResource.java Course materials (23W) CST 8277
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
import static acmecollege.utility.MyConstants.USER_ROLE;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.ClubMembership;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.StudentClub;

@Path("membershipcard")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MembershipCardResource {
    
    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    @Inject
    protected SecurityContext sc;
    
    @GET
    public Response getMembershipCards() {
        LOG.debug("Retrieving all membership cards...");
        List<MembershipCard> membershipCards = service.getAllMembershipCards();
        LOG.debug("Membership cards found = {}", membershipCards);
        Response response = Response.ok(membershipCards).build();
        return response;
    }
    
    @GET
    @RolesAllowed({USER_ROLE})
    @Path("/{membershipCardId}")
    public Response getMembershipCardById(@PathParam("membershipCardId") int membershipCardId) {
        LOG.debug("Retrieving student club with id = {}", membershipCardId);
        MembershipCard membershipCard = service.getMembershipCardById(membershipCardId);
        Response response = Response.ok(membershipCard).build();
        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{membershipCardId}")
    public Response deleteMembershipCard(@PathParam("membershipCardId") int mcId) {
        LOG.debug("Deleting membership card with id = {}", mcId);
        MembershipCard mc = service.deleteMembershipCard(mcId);
        Response response = Response.ok(mc).build();
        return response;
    }    
 
    @RolesAllowed({ADMIN_ROLE})
    @POST
    public Response addMembershipCard(MembershipCard newMembershipCard) {
        LOG.debug("Adding a new student club = {}", newMembershipCard);
        if (service.isDuplicated(newMembershipCard)) {
            HttpErrorResponse err = new HttpErrorResponse(Status.CONFLICT.getStatusCode(), "Entity already exists");
            return Response.status(Status.CONFLICT).entity(err).build();
        }
        else {
            MembershipCard tempMembershipCard = service.persistMembershipCard(newMembershipCard);
            return Response.ok(tempMembershipCard).build();
        }
    } 

    @RolesAllowed({ADMIN_ROLE})
    @POST
    @Path("/{membershipCardId}/clubmembership")
    public Response addClubMembershipToMembershipCard(@PathParam("membershipCardId") int mcId, ClubMembership newClubMembership) {
        LOG.debug( "Adding a new ClubMembership to membership card with id = {}", mcId);
        
        MembershipCard mc = service.getMembershipCardById(mcId);
        newClubMembership.setCard(mc); 
        mc.setClubMembership(newClubMembership);  
        service.updateMembershipCard(mcId, mc);   
        
        return Response.ok(mc).build();
    }
    
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @PUT
    @Path("/{membershipCardId}")
    public Response updateMembershipCard(@PathParam("MembershipCardId") int mcId, MembershipCard updatingMembershipCard) {
        LOG.debug("Updating a specific membership card with id = {}", mcId);
        Response response = null;
        MembershipCard updatedMembershipCard = service.updateMembershipCard(mcId, updatingMembershipCard);
        response = Response.ok(updatedMembershipCard).build();
        return response;
    }

}

