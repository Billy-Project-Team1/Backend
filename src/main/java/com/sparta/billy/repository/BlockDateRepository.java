package com.sparta.billy.repository;

import com.sparta.billy.model.BlockDate;
import com.sparta.billy.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockDateRepository extends JpaRepository<BlockDate, Long> {
    List<BlockDate> findAllByPost(Post post);
    void deleteByPost(Post post);
}
