package com.ssafy.realrealfinal.rankms.db.repository;

import com.ssafy.realrealfinal.rankms.db.document.Flourish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlourishRepository extends MongoRepository<Flourish, String> {
    Flourish findByProviderId(Integer providerId);
}