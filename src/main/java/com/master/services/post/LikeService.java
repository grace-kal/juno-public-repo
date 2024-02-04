package com.master.services.post;

import com.master.data.ILikeRepository;
import com.master.models.post.Like;
import com.master.models.post.Post;
import com.master.services.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LikeService extends BaseService<Like> {
    @Autowired
    ILikeRepository _repository;

    @Override
    protected JpaRepository<Like, Long> getRepo() {
        return _repository;
    }

    public Like getLikeByPostIdAndUserId(Long userId, Long postId) {
        Optional<Like> like = _repository.getLikeByPostIdAndUserId(userId, postId);
        return like.orElse(null);
    }

    public Like getLikeByCommentIdAndUserId(Long userId, Long commentId) {
        Optional<Like> like = _repository.getLikeByCommentIdAndUserId(userId, commentId);
        return like.orElse(null);
    }
}
