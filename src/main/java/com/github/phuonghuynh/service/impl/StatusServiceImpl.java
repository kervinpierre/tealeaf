package com.github.phuonghuynh.service.impl;

import com.github.phuonghuynh.model.Status;
import com.github.phuonghuynh.repository.StatusRepository;
import com.github.phuonghuynh.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by kervin on 2016-07-18.
 */
@Component
public class StatusServiceImpl implements StatusService
{
    @Autowired
    private StatusRepository statusRepository;
    
    @Override
    public Status createStatus()
    {
        Status res = new Status();
        res.setId(UUID.randomUUID());
        return res;
    }

    @Override
    public Status saveAndFlush(Status Status)
    {
        Status res = statusRepository.saveAndFlush(Status);

        return res;
    }

    @Override
    public void flush()
    {
        statusRepository.flush();
    }

    @Override
    public void updateStatus(Status Status)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteStatus(Status Status)
    {
        statusRepository.delete(Status);
    }
}
