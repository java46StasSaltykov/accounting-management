package telran.spring.accounting.repo;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.data.mongodb.repository.*;
import telran.spring.accounting.entities.AccountEntity;

public interface AccountRepository extends 
	MongoRepository<AccountEntity, String>, AccountAggregationRepository {

	List<AccountEntity> findByExpirationGreaterThanAndRevokedIsFalse(LocalDateTime ldt);
	
	@Query(value = "{roles: {$elemMatch: {$eq: ?0}}}", fields = "{email: 1}")
	List<AccountEntity> findByRole(String role);
	
	@Query(value = "{roles: {$size: ?0}}", fields = "{email: 1}")
	List<String> getAllAccountsWithMaxRoles(long maxRoles);
	
}
