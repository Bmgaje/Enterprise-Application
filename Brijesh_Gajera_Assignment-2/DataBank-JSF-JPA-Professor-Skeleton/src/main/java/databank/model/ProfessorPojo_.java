package databank.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-03-04T22:49:41.682-0500")
@StaticMetamodel(ProfessorPojo.class)
public class ProfessorPojo_ {
	public static volatile SingularAttribute<ProfessorPojo, Integer> id;
	public static volatile SingularAttribute<ProfessorPojo, String> lastName;
	public static volatile SingularAttribute<ProfessorPojo, String> firstName;
	public static volatile SingularAttribute<ProfessorPojo, String> email;
	public static volatile SingularAttribute<ProfessorPojo, LocalDateTime> created;
	public static volatile SingularAttribute<ProfessorPojo, LocalDateTime> updated;
	public static volatile SingularAttribute<ProfessorPojo, Integer> version;
	public static volatile SingularAttribute<ProfessorPojo, Boolean> editable;
	public static volatile SingularAttribute<ProfessorPojo, String> phoneNumber;
}
