package com.master.data;

import com.master.models.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository<T extends com.master.models.user.User> extends IBaseRepository<T> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
}
