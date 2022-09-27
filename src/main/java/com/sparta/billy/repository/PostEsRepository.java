package com.sparta.billy.repository;

import com.sparta.billy.model.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEsRepository extends ElasticsearchRepository<PostDocument, Long> {
    @Query("{\"match\": {\"titleAndDetailLocation\": {\"query\": \"?0\", \"operator\": \"and\"}}}")
    List<PostDocument> findBySearchKeyword(String keyword);
}
