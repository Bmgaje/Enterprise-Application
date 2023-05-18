/**
 * File:  TestACMECollegeSystem.java
 * Course materials (23W) CST 8277
 * Teddy Yap
 * (Original Author) Mike Norman
 *
 * @date 2020 10
 *
 * (Modified) @author Student Name
 */
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.STUDENT_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.PROFESSOR_SUBRESOURCE_NAME;
import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.COURSE_REGISTRATION_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.MEMBERSHIP_CARD_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.*;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acmecollege.entity.Professor;
import acmecollege.entity.Student;
import acmecollege.entity.Course;
import acmecollege.entity.CourseRegistration;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.StudentClub;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestACMECollegeSystem {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // Test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        uri = UriBuilder
            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
            .scheme(HTTP_SCHEMA)
            .host(HOST)
            .port(PORT)
            .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    @Test
    public void test01_all_students_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(500));
        List<Student> students = response.readEntity(new GenericType<List<Student>>(){});
        assertThat(students, is(not(empty())));
        assertThat(students, hasSize(1));
    }
    
    @Test
    public void test02_all_students_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/9999") // use a non-existent ID
                .request()
                .get();
            assertThat(response.getStatus(), is(404));
    }
    @Test
    public void test03_add_student_with_adminrole() throws JsonMappingException, JsonProcessingException {
        // Create a new student object
        Student newStudent = new Student();
        newStudent.setFirstName("Alex");
        newStudent.setLastName("Fane");
        
        // Send a POST request to add the new student
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .post(Entity.json(newStudent));
        assertThat(response.getStatus(), is(201)); // Created
        
        // Check that the new student was added successfully
        List<Student> students = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get(new GenericType<List<Student>>(){});
        assertThat(students, hasSize(2));
        assertThat(students.get(1).getFirstName(), is("Alex"));
    }
    @Test
    public void test04_update_student_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/1") // use an existing ID
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            Student student = response.readEntity(Student.class);
            student.setFirstName("Jane");
            response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/1")
                .request()
                .put(Entity.json(student));
            assertThat(response.getStatus(), is(204)); // 204 No Content
    }
    
    @Test
    public void test05_delete_student_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/1") // use an existing ID
                .request()
                .delete();
            assertThat(response.getStatus(), is(204)); // 204 No Content
            response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME + "/1")
                .request()
                .get();
            assertThat(response.getStatus(), is(404));
    }
    
    
    @Test
    public void test06_create_new_student() throws JsonMappingException, JsonProcessingException {
    	Student newStudent = new Student();
    	newStudent.setFirstName("Micke");
    	newStudent.setLastName("Highman");
 
    	Response response = webTarget
    	    .register(adminAuth)
    	    .path(STUDENT_RESOURCE_NAME)
    	    .request()
    	    .post(Entity.json(newStudent));
        assertThat(response.getStatus(), is(201));
        List<Student> students = response.readEntity(new GenericType<List<Student>>(){});
        assertThat(students, is(not(empty())));
        assertThat(students, hasSize(1));

    }
    
    @Test
    public void test07_get_nonexistent_student_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/9999") // use a non-existent ID
            .request()
            .get();
        assertThat(response.getStatus(), is(404));
    }
    
    @Test
    public void test08_get_student_by_id() throws JsonMappingException, JsonProcessingException {
        // Assume there is a student with ID 1
        int studentId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/" + studentId)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Student student = response.readEntity(Student.class);
        assertThat(student.getId(), is(studentId));
    }
    @Test
    public void test09_update_student() throws JsonMappingException, JsonProcessingException {
        // Assume there is a student with ID 1
        int studentId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/" + studentId)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Student student = response.readEntity(Student.class);

        // Update the student's information
        student.setFirstName("Ran");
        student.setLastName("Lee");

        response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/" + studentId)
            .request()
            .put(Entity.json(student));
        assertThat(response.getStatus(), is(204)); // No Content

        // Verify that the student was updated
        response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/" + studentId)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Student updatedStudent = response.readEntity(Student.class);
        assertThat(updatedStudent.getFirstName(), is("Nick"));
        assertThat(updatedStudent.getLastName(), is("Smith"));
    }
    
    @Test
    public void test10_delete_student() throws JsonMappingException, JsonProcessingException {
        // Assume there is a student with ID 1
        int studentId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/" + studentId)
            .request()
            .delete();
        assertThat(response.getStatus(), is(204)); // No Content
    }
    
    @Test
    public void test11_create_professor_with_adminrole() throws JsonProcessingException {
        Professor professor = new Professor();
        professor.setFirstName("Ann");
        professor.setLastName("Mor");
        professor.setDepartment("Computer Science");

        Response response = webTarget
            .register(adminAuth)
            .path(PROFESSOR_SUBRESOURCE_NAME)
            .request()
            .post(Entity.json(professor));
        assertThat(response.getStatus(), is(201));

        URI professorUri = response.getLocation();
        response = webTarget
            .register(adminAuth)
            .path(professorUri.getPath())
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Professor> createdProfessor = response.readEntity(new GenericType<List<Professor>>(){});
        assertThat(createdProfessor, is(not(empty())));
        assertThat(createdProfessor, hasSize(1));
    }
    
    @Test
    public void test12_get_all_professors() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path("professor")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Professor> professors = response.readEntity(new GenericType<List<Professor>>(){});
        assertThat(professors, is(not(empty())));
        assertThat(professors, hasSize(2));
    }

    @Test
    public void test13_get_all_courses() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Course> professors = response.readEntity(new GenericType<List<Course>>(){});
        assertThat(professors, is(not(empty())));
        assertThat(professors, hasSize(2));
    }
    
    @Test
    public void test14_delete_course() throws JsonMappingException, JsonProcessingException {
        // Assume there is a course with ID 1
        int courseId = 1;
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME + "/" + courseId)
                .request()
                .delete();
            assertThat(response.getStatus(), is(204)); // No Content
    }
    
    @Test
	void test15_create_new_course() {
    	Course newCourse = new Course();
    	newCourse.setCourseCode("CST8277");
    	newCourse.setCourseTitle("Java");
    	newCourse.setYear(2023);
    	newCourse.setSemester("Winter");
    	newCourse.setCreditUnits(5);
    	newCourse.setOnline((byte) 1);
 
    	Response response = webTarget
    	    .register(adminAuth)
    	    .path(COURSE_RESOURCE_NAME)
    	    .request()
    	    .post(Entity.json(newCourse));
        assertThat(response.getStatus(), is(201));
        
        List<Course> courses = response.readEntity(new GenericType<List<Course>>(){});
        assertThat(courses, is(not(empty())));
        assertThat(courses, hasSize(1));
    }
    
    @Test
    public void test16_get_nonexistent_course() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME + "/9999") // use a non-existent ID
            .request()
            .get();
        assertThat(response.getStatus(), is(404));
    }

    @Test
    void test17_update_course_existing_course() {
    // create a new course first
    Course newCourse = new Course();
    newCourse.setCourseCode("CST8277");
    newCourse.setCourseTitle("Java");
    newCourse.setYear(2023);
    newCourse.setSemester("Winter");
    newCourse.setCreditUnits(5);
    newCourse.setOnline((byte) 1);
	Response response = webTarget
		    .register(adminAuth)
		    .path(COURSE_RESOURCE_NAME)
		    .request()
		    .post(Entity.json(newCourse));
	    assertThat(response.getStatus(), is(201));
	    
	    // retrieve the newly created course
	    List<Course> courses = response.readEntity(new GenericType<List<Course>>(){});
	    assertThat(courses, is(not(empty())));
	    assertThat(courses, hasSize(1));
	    Course courseToUpdate = courses.get(0);
	    
	    // update the course's year and credit units
	    courseToUpdate.setYear(2024);
	    courseToUpdate.setCreditUnits(6);
	    
	    // perform the update request
	    response = webTarget
	    	.register(adminAuth)
	    	.path(COURSE_RESOURCE_NAME + "/" + courseToUpdate.getId())
	    	.request()
	    	.put(Entity.json(courseToUpdate));
	    assertThat(response.getStatus(), is(204));
	    
	    // retrieve the updated course and check if it has been updated successfully
	    response = webTarget
	    	.path(COURSE_RESOURCE_NAME + "/" + courseToUpdate.getId())
	    	.request()
	    	.get();
	    assertThat(response.getStatus(), is(200));
	    Course updatedCourse = response.readEntity(Course.class);
	    assertThat(updatedCourse.getYear(), is(2024));
	    assertThat(updatedCourse.getCreditUnits(), is(6));
	}

    @Test
    void test18_get_course_by_id() {
        // First, create a new course and retrieve its ID
        Course newCourse = new Course();
        newCourse.setCourseCode("CST1234");
        newCourse.setCourseTitle("Database Systems");
        newCourse.setYear(2023);
        newCourse.setSemester("Winter");
        newCourse.setCreditUnits(4);
        newCourse.setOnline((byte) 0);
     
        Response createResponse = webTarget
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME)
            .request()
            .post(Entity.json(newCourse));
        assertThat(createResponse.getStatus(), is(201));
        int courseId = createResponse.readEntity(new GenericType<List<Course>>() {}).get(0).getId();

        // Then, retrieve the course by ID
        Response getResponse = webTarget
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME + "/" + courseId)
            .request()
            .get();
        assertThat(getResponse.getStatus(), is(200));

        Course retrievedCourse = getResponse.readEntity(Course.class);
        assertThat(retrievedCourse.getCourseCode(), is(equalTo("CST1234")));
        assertThat(retrievedCourse.getCourseTitle(), is(equalTo("Database Systems")));
        assertThat(retrievedCourse.getYear(), is(equalTo(2023)));
        assertThat(retrievedCourse.getSemester(), is(equalTo("Winter")));
        assertThat(retrievedCourse.getCreditUnits(), is(equalTo(4)));
        assertThat(retrievedCourse.getOnline(), is(equalTo((byte) 0)));
    }

    @Test
    void test19_CourseRegistration() {
    // create a new student
    Student student = new Student();
    student.setFirstName("John");
    student.setLastName("Doe");
    
    // create a new course
    Course course = new Course();
    course.setCourseCode("CST8277");
    course.setCourseTitle("Java Programming");
    course.setYear(2023);
    course.setSemester("Winter");
    course.setCreditUnits(5);
    course.setOnline((byte) 1);

    // create a new course registration
    CourseRegistration registration = new CourseRegistration();
    registration.setStudent(student);
    registration.setCourse(course);

    // send a POST request to create the course registration
    Response response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME)
            .request()
            .post(Entity.json(registration));
    assertThat(response.getStatus(), is(201));

    // send a GET request to retrieve the course registrations for the student
    response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME)
            .request()
            .get();
    assertThat(response.getStatus(), is(200));

    List<CourseRegistration> registrations = response.readEntity(new GenericType<List<CourseRegistration>>(){});
    assertThat(registrations, is(not(empty())));
    assertThat(registrations, hasSize(1));
    assertThat(registrations.get(0).getCourse().getCourseCode(), is("CST8277"));
    assertThat(registrations.get(0).getCourse().getCourseTitle(), is("Java Programming"));
    }
    
    @Test
    void test20_update_course_registration() {
    // create a new student
    Student student = new Student();
    student.setFirstName("John");
    student.setLastName("Doe");
 // create a new course
    Course course = new Course();
    course.setCourseCode("CST8277");
    course.setCourseTitle("Java Programming");
    course.setYear(2023);
    course.setSemester("Winter");
    course.setCreditUnits(5);
    course.setOnline((byte) 1);

    // create a new course registration
    CourseRegistration registration = new CourseRegistration();
    registration.setStudent(student);
    registration.setCourse(course);

    // send a POST request to create the course registration
    Response response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME)
            .request()
            .post(Entity.json(registration));
    assertThat(response.getStatus(), is(201));

    // get the course registration ID from the response
    String location = response.getHeaderString("Location");
    int registrationId = Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));

    // update the course registration
    registration.setCourse(new Course("CST8888", "Web Development", 2023, "Winter", 4, (byte) 1));
    response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME + "/" + registrationId)
            .request()
            .put(Entity.json(registration));
    assertThat(response.getStatus(), is(204));

    // send a GET request to retrieve the updated course registration
    response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME + "/" + registrationId)
            .request()
            .get();
    assertThat(response.getStatus(), is(200));

    CourseRegistration updatedRegistration = response.readEntity(CourseRegistration.class);
    assertThat(updatedRegistration.getCourse().getCourseCode(), is("CST8888"));
    assertThat(updatedRegistration.getCourse().getCourseTitle(), is("Web Development"));
    }
    
    @Test
    void test21_delete_CourseRegistration() {
    // create a new student
    Student student = new Student();
    student.setFirstName("John");
    student.setLastName("Doe");
    
    // create a new course
    Course course = new Course();
    course.setCourseCode("CST8277");
    course.setCourseTitle("Java Programming");
    course.setYear(2023);
    course.setSemester("Winter");
    course.setCreditUnits(5);
    course.setOnline((byte) 1);

    // create a new course registration
    CourseRegistration registration = new CourseRegistration();
    registration.setStudent(student);
    registration.setCourse(course);

    // send a POST request to create the course registration
    Response response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME)
            .request()
            .post(Entity.json(registration));
    assertThat(response.getStatus(), is(201));

    // send a GET request to retrieve the course registrations for the student
    response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME)
            .request()
            .get();
    assertThat(response.getStatus(), is(200));

    List<CourseRegistration> registrations = response.readEntity(new GenericType<List<CourseRegistration>>(){});
    assertThat(registrations, is(not(empty())));
    assertThat(registrations, hasSize(1));
    assertThat(registrations.get(0).getCourse().getCourseCode(), is("CST8277"));
    assertThat(registrations.get(0).getCourse().getCourseTitle(), is("Java Programming"));
    
 // send a DELETE request to delete the course registration
    response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME + "/" + registrations.get(0).getId())
            .request()
            .delete();
    assertThat(response.getStatus(), is(204));

    // send a GET request to retrieve the course registrations for the student
    response = webTarget
            .register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME)
            .request()
            .get();
    assertThat(response.getStatus(), is(200));

    registrations = response.readEntity(new GenericType<List<CourseRegistration>>(){});
    assertThat(registrations, is(empty()));
    }
}