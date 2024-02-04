package com.master.services.post;

import com.master.data.IPostRepository;
import com.master.models.post.Post;
import com.master.services.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PostService extends BaseService<Post> {
    @Autowired
    IPostRepository _repository;

    @Override
    protected JpaRepository<Post, Long> getRepo() {
        return _repository;
    }

    public List<Post> getAllPostsByUserId(Long userId){
        return _repository.getAllPostsByUserId(userId);
    }
}
