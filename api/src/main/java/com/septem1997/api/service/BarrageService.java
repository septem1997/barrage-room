package com.septem1997.api.service;

import com.septem1997.api.entity.Barrage;
import com.septem1997.api.repository.BarrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarrageService {
    @Autowired
    private BarrageRepository repository;

    public List<Barrage> getLast10Records(){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        PageRequest pagerequest = PageRequest.of(0,10,sort);
        return repository.findAll(pagerequest).getContent();
    }

    public Barrage save(Barrage barrage){
        barrage.setUserNickname(barrage.getUser().getNickname());
        return repository.save(barrage);
    }
}
