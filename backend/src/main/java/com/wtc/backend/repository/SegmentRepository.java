package com.wtc.backend.repository;

import com.wtc.backend.model.Segment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentRepository extends MongoRepository<Segment, String> {

    List<Segment> findByOperatorIdOrderByCreatedAtDesc(String operatorId);
}
