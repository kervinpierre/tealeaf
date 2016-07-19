package com.github.phuonghuynh.repository;

import com.github.phuonghuynh.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by kervin on 2016-07-18.
 */
@Repository
public interface StatusRepository  extends JpaRepository<Status, UUID>
{
    @Query("from Status")
    public List<Status> findAllStatus();
}
