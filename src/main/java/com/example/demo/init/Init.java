package com.example.demo.init;

import com.example.demo.model.Good;
import com.example.demo.repository.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class Init {
    @Autowired
    private GoodRepository goodRepository;

    public Init(GoodRepository goodRepository) {
        this.goodRepository=  goodRepository;
    }
    @PostConstruct
    private void postConstruct(){
        List<Good> goodList = new ArrayList<>();
        goodList.add(new Good("Яблоки краснодарские",98.7));
        goodList.add(new Good("Яблоки Ред  Чиф",99.5));
        goodList.add(new Good("Мандарины",124.5));
        goodList.add(new Good("Груши Форелл",124.5));
        goodList.add(new Good("Груши Пакхам",99.5));
        goodList.add(new Good("Coca-Cola  0,5 л",95.0));
        goodList.forEach(good -> goodRepository.saveAndFlush(good));


    }
}
