package com.developer.onlybuns.service.impl;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.repository.LajkRepository;
import com.developer.onlybuns.service.LajkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LajkServiceImpl implements LajkService {

    @Autowired
    public LajkRepository lajkRepository;

    public LajkServiceImpl(LajkRepository lajkRepository) {
        this.lajkRepository = lajkRepository;
    }

    @Override
    public void saveLajk(Lajk lajk){
        lajkRepository.save(lajk);
    }
}
