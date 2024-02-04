package com.master.services.user;

import com.master.data.IUserRepository;
import com.master.models.user.User;
import com.master.services.BaseService;
import com.master.views.user.admin.UsersDashboardView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService extends BaseService<User> {
    @Autowired
    IUserRepository _repository;

    @Override
    protected JpaRepository<User, Long> getRepo() {
        return _repository;
    }

    public User findUserByUsername(String username) {
        Optional<User> user = _repository.findUserByUsername(username);
        return user.orElse(null);
    }

    public User findUserByEmail(String email) {
        Optional<User> user = _repository.findUserByEmail(email);
        return user.orElse(null);
    }

    public Page<User> getAll(Pageable pageable, Specification<User> filter) {
        return _repository.findAll(filter, pageable);
    }
}
