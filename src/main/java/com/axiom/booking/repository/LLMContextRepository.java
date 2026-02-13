package com.axiom.booking.repository;

import com.axiom.booking.model.entity.UserContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LLMContextRepository extends CrudRepository<UserContext, String> {

    /**
     * Finds a UserContext by userId
     * Spring Data Redis OM automatically implements this
     */
    UserContext findByUserId(String userId);

    /**
     * Optional: add more custom queries in future
     * For example, semantic search using embeddings
     */
}
