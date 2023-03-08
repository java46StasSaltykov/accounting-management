package telran.spring.accounting.repo;

import java.time.LocalDateTime;
import java.util.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import telran.spring.accounting.entities.AccountEntity;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class AccountAggregationRepositoryImpl implements AccountAggregationRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public long getMaxRoles() {
		ArrayList<AggregationOperation> operations = new ArrayList<AggregationOperation>();
		operations.add(unwind("roles"));
		operations.add(group("email").count().as("count"));
		operations.add(group().max("count").as("maxCount"));
		Aggregation pipeline = newAggregation(operations);
		var document = mongoTemplate.aggregate(pipeline, AccountEntity.class, Document.class);
		return document.getUniqueMappedResult().getInteger("maxCount");
	}

	@Override
	public int getMaxRolesOccurrenceCount() {
		ArrayList<AggregationOperation> operations = new ArrayList<AggregationOperation>();
		operations.add(unwind("roles"));
		operations.add(group("roles").count().as("count"));
		operations.add(group().max("count").as("maxCount"));
		operations.add(limit(1));
		Aggregation pipeline = newAggregation(operations);
		var document = mongoTemplate.aggregate(pipeline, AccountEntity.class, Document.class);
		return document.getUniqueMappedResult().getInteger("maxCount");
	}

	@Override
	public List<String> getAllRolesWithMaxOccurrrence(int maxOccurrence) {
		ArrayList<AggregationOperation> operations = new ArrayList<AggregationOperation>();
		operations.add(unwind("roles"));
		operations.add(sortByCount("roles"));
		operations.add(match(Criteria.where("count").is(maxOccurrence)));
		Aggregation pipeline = newAggregation(operations);
		var document = mongoTemplate.aggregate(pipeline, AccountEntity.class, Document.class);
		return document.getMappedResults().stream().map(e -> e.toString()).toList();
	}

	@Override
	public int getActiveMinRolesOccurrenceCount(LocalDateTime ldt) {
		ArrayList<AggregationOperation> operations = new ArrayList<AggregationOperation>();
		operations.add(match(Criteria.where("expiration").gt(ldt)));
		operations.add(unwind("roles"));
		operations.add(group("roles").count().as("count"));
		operations.add(group().min("count").as("minCount"));
		operations.add(sort(Sort.Direction.ASC, "minCount"));
		operations.add(limit(1));
		Aggregation pipeline = newAggregation(operations);
		var document = mongoTemplate.aggregate(pipeline, AccountEntity.class, Document.class);
		return document.getUniqueMappedResult().getInteger("minCount");
	}

	
}
