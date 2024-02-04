package com.master.data;

import com.master.models.shared.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IBaseRepository<T extends BaseModel> extends JpaRepository<T,Long>, JpaSpecificationExecutor<T> {
}
