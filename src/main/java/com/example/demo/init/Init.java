package com.example.demo.init;

import com.example.demo.model.Good;
import com.example.demo.repository.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Init {
    @Autowired
    private GoodRepository goodRepository;

    public Init(GoodRepository goodRepository) {
        this.goodRepository=  goodRepository;
    }
    @PostConstruct
    private void postConstruct(){
        Good appleKrasnodarskie = new Good(1,"Яблоки краснодарские",98.7);
        Good appleRedChief = new Good(2,"Яблоки Ред  Чиф",99.5);
        Good tangerines = new Good(3,"Мандарины",124.5);
        Good pearsForell = new Good(4,"Груши Форелл",124.5);
        Good pearsPackham = new Good(5,"Груши Пакхам",99.5);
        Good juiceCocaCola05 = new Good(6,"Coca-Cola  0,5 л",95.0);
        goodRepository.saveAndFlush(appleKrasnodarskie);
        goodRepository.saveAndFlush(appleRedChief);
        goodRepository.saveAndFlush(tangerines);
        goodRepository.saveAndFlush(pearsForell);
        goodRepository.saveAndFlush(pearsPackham);
        goodRepository.saveAndFlush(juiceCocaCola05);
    }
}
