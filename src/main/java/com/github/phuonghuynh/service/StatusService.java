package com.github.phuonghuynh.service;

import com.github.phuonghuynh.model.Status;
import org.springframework.stereotype.Service;

/**
 * Created by kervin on 2016-07-18.
 */
@Service
public interface StatusService
{
    Status createStatus();

    Status saveAndFlush(Status Status);

    void flush();

    void updateStatus(Status Status);

    void deleteStatus(Status Status);
}
