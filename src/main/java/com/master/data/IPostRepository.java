package com.master.data;

import com.master.models.post.Post;
import com.master.models.user.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IPostRepository <T extends Post> extends IBaseRepository<T>{
//    Optional<List<Post>> findPostsByUSerId(int authorId);
    @Query(value = "SELECT * FROM POSTS WHERE AUTHOR_USER_ID = ?1", nativeQuery = true)
    public List<Post> getAllPostsByUserId(Long userId);
}
