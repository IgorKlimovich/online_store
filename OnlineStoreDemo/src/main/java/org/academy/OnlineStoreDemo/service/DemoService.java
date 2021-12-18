package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.model.entity.UserDemo;

public interface DemoService {

    void load (String fileName);
    UserDemo findById(Integer id);
}
