package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.repository.LajkRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface LajkService {

    void saveLajk(Lajk lajk);
}
