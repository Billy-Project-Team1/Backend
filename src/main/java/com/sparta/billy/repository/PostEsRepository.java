package com.sparta.billy.repository;

import com.sparta.billy.model.PostDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEsRepository extends ElasticsearchRepository<PostDocument, Long> {
    @Query("{\"multi_match\" : {\"query\": \"?0\", \"type\": \"best_fields\", \"fields\":" +
            " [ \"titleAndDetailLocation.nori\", \"titleAndDetailLocation.ngram\" ],\n" +
            "      \"operator\":   \"and\" \n" +
            "    }\n" +
            "  }")
    List<PostDocument> findBySearchKeyword(String keyword);

    List<PostDocument> findAll();
}
