package com.master.services.post;

import com.master.data.ICommentRepository;
import com.master.data.ILikeRepository;
import com.master.models.post.Comment;
import com.master.models.post.Like;
import com.master.services.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CommentService extends BaseService<Comment> {
    @Autowired
    ICommentRepository _repository;

    @Override
    protected JpaRepository<Comment, Long> getRepo() {
        return _repository;
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        Optional<List<Comment>> comments = _repository.getCommentsByPostId(postId);
        return comments.orElse(null);
    }
}
