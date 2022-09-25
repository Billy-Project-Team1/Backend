package com.sparta.billy.repository;

import com.sparta.billy.model.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEsRepository extends ElasticsearchRepository<PostDocument, Long> {
}
