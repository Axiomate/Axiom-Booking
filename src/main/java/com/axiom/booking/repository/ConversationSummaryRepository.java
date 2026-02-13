package com.axiom.booking.repository;

import com.axiom.booking.model.entity.ConversationSummary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationSummaryRepository extends CrudRepository<ConversationSummary, String> {

    // Optional: Add custom query methods if needed
}
 