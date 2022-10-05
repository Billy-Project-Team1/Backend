package com.sparta.billy.service;

import com.sparta.billy.dto.LikeRequestDto;
import com.sparta.billy.dto.PostDto.PostResponseDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.model.Like;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.repository.LikeRepository;
import com.sparta.billy.repository.PostQueryRepository;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostQueryRepository postQueryRepository;
    private final Check check;

    @Transactional
    public ResponseDto<String> upDownLike(Long postId, HttpServletRequest request) {
        Member member = check.validateMember(request);
        check.tokenCheck(request, member);
        Post post = check.getCurrentPost(postId);
        check.checkPost(post);

        Like findLike = likeRepository.findByMemberAndPost(member, post).orElse(null);

        if(findLike == null){
            LikeRequestDto likerequestDto = new LikeRequestDto(member, post);
            Like like = new Like(likerequestDto);
            likeRepository.save(like);
            return ResponseDto.success("찜하기 성공!");
        } else {
            likeRepository.deleteById(findLike.getId());
            return ResponseDto.success("찜하기 취소!");
        }

    }

    @Transactional
    public ResponseDto<List<PostResponseDto>> getLikePostListByMember(HttpServletRequest request) {
        Member member = check.validateMember(request);
        check.tokenCheck(request, member);
        List<PostResponseDto> response = postQueryRepository.findLikePostByMember(member);
        return ResponseDto.success(response);
    }

}
