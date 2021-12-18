package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.model.entity.UserDemo;
import org.academy.OnlineStoreDemo.model.repository.DemoRepository;
import org.academy.OnlineStoreDemo.service.DemoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class DemoServiceImpl implements DemoService {

    private final DemoRepository demoRepository;

    public DemoServiceImpl(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    @Override
    public void load(String fileName) {
        UserDemo userDemo = new UserDemo();
        userDemo.setImage(fileName);
        demoRepository.save(userDemo);
    }

    public UserDemo findById(Integer id){
     return    demoRepository.getById(id);
    }
}
