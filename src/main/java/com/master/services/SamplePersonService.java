package com.master.services;

import com.master.data.IUserRepository;
import com.master.models.user.SamplePerson;
import com.master.data.ISamplePersonRepository;
import java.util.Optional;

import com.master.models.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SamplePersonService extends BaseService<SamplePerson> {
    @Autowired
    ISamplePersonRepository repository;
    @Override
    protected JpaRepository<SamplePerson, Long> getRepo() {
        return repository;
    }
}
