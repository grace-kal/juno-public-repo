package com.master.data;

import com.master.models.post.Like;
import com.master.models.post.Post;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ILikeRepository<T extends Like> extends IBaseRepository<T> {
    @Query(value = "SELECT * FROM LIKES WHERE GIVEN_BY_USER_ID = ?1 AND POST_ID = ?2", nativeQuery = true)
    public Optional<Like> getLikeByPostIdAndUserId(Long userId, Long postId);

    @Query(value = "SELECT * FROM LIKES WHERE GIVEN_BY_USER_ID = ?1 AND COMMENT_ID = ?2", nativeQuery = true)
    public Optional<Like> getLikeByCommentIdAndUserId(Long userId, Long commentId);
}
