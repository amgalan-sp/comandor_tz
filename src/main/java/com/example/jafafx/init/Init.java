package com.example.jafafx.init;

import com.example.jafafx.model.Good;
import com.example.jafafx.repository.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
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
        goodList.add(new Good("Яблоки краснодарские", BigDecimal.valueOf(98.70)));
        goodList.add(new Good("Яблоки Ред  Чиф",BigDecimal.valueOf(99.5)));
        goodList.add(new Good("Мандарины",BigDecimal.valueOf(124.5)));
        goodList.add(new Good("Груши Форелл",BigDecimal.valueOf(124.5)));
        goodList.add(new Good("Груши Пакхам",BigDecimal.valueOf(99.5)));
        goodList.add(new Good("Coca-Cola  0,5 л",BigDecimal.valueOf(95.0)));
        goodList.forEach(good -> goodRepository.saveAndFlush(good));


    }
}
