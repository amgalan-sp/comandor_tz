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
        goodList.add(new Good(1,"Яблоки краснодарские",98.7));
        goodList.add(new Good(2,"Яблоки Ред  Чиф",99.5));
        goodList.add(new Good(3,"Мандарины",124.5));
        goodList.add(new Good(4,"Груши Форелл",124.5));
        goodList.add(new Good(5,"Груши Пакхам",99.5));
        goodList.add(new Good(6,"Coca-Cola  0,5 л",95.0));
        goodList.forEach(good -> goodRepository.saveAndFlush(good));


    }
}
