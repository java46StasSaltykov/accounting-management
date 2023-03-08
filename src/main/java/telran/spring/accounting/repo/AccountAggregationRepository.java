package telran.spring.accounting.repo;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountAggregationRepository {
	
	long getMaxRoles();
	
	int getMaxRolesOccurrenceCount();
	
	List<String> getAllRolesWithMaxOccurrrence(int maxOccurrence);
	
	int getActiveMinRolesOccurrenceCount(LocalDateTime ldt);

}
