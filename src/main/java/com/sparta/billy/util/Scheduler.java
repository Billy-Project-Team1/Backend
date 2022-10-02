package com.sparta.billy.util;

import com.sparta.billy.model.Post;
import com.sparta.billy.model.PostDocument;
import com.sparta.billy.repository.PostEsRepository;
import com.sparta.billy.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final PostEsRepository postEsRepository;
    private final PostRepository postRepository;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void insertPostDocumentMissingPost() throws InterruptedException {
        List<Post> postList = postRepository.findAll();
        List<PostDocument> postDocumentList = postEsRepository.findAll();

        List<Long> postIdList = new ArrayList<>();
        List<Long> postDocIdList = new ArrayList<>();

        for (Post p : postList) {
            postIdList.add(p.getId());
        }

        for (PostDocument p : postDocumentList) {
            postDocIdList.add(p.getId());
        }
        postIdList.removeAll(postDocIdList);

        for (Long l : postIdList) {
            Post post = postRepository.findById(l).orElseThrow();
            PostDocument postDocument = new PostDocument(post.getId(), post.getTitle(), post.getDetailLocation(),
                    post.getTitle() + " " + post.getDetailLocation());
            postEsRepository.save(postDocument);
        }
    }
}
