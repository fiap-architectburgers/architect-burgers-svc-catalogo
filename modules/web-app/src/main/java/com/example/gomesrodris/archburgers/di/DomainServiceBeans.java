package com.example.gomesrodris.archburgers.di;

import com.example.gomesrodris.archburgers.controller.CardapioController;
import com.example.gomesrodris.archburgers.domain.datagateway.ItemCardapioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceBeans {

    @Bean
    public CardapioController cardapioController(ItemCardapioGateway itemCardapioGateway) {
        return new CardapioController(itemCardapioGateway);
    }
}
