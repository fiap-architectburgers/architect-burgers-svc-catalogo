package com.example.gomesrodris.archburgers.di;

import com.example.gomesrodris.archburgers.controller.CardapioController;
import com.example.gomesrodris.archburgers.domain.datagateway.ItemCardapioGateway;
import com.example.gomesrodris.archburgers.domain.usecases.CardapioUseCases;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceBeans {

    @Bean
    public CardapioUseCases cardapioUseCases(ItemCardapioGateway itemCardapioGateway) {
        return new CardapioUseCases(itemCardapioGateway);
    }

    @Bean
    public CardapioController cardapioController(CardapioUseCases cardapioUseCases) {
        return new CardapioController(cardapioUseCases);
    }
}
