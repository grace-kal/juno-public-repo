package com.master.data;

import com.master.models.post.Comment;
import com.master.models.post.Like;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICommentRepository <T extends Comment> extends IBaseRepository<T> {

    @Query(value = "SELECT * FROM COMMENTS WHERE POST_ID = ?1", nativeQuery = true)
    public Optional<List<Comment>> getCommentsByPostId(Long postId);
}
