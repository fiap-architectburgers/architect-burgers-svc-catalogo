package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.datagateway.ItemCardapioGateway;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardapioUseCasesTest {
    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    private CardapioUseCases cardapioUseCases;

    @BeforeEach
    void setUp() {
        cardapioUseCases = new CardapioUseCases(itemCardapioGateway);
    }

    @Test
    void listLanches() {
        when(itemCardapioGateway.findAll()).thenReturn(List.of(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato",
                        new ValorMonetario("20.00")),
                new ItemCardapio(2, TipoItemCardapio.ACOMPANHAMENTO, "Batata frita M",
                        "Batata frita tamanho médio",
                        new ValorMonetario("12.00"))
        ));

        var result = cardapioUseCases.listarItensCardapio(null);

        assertThat(result).containsExactly(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato",
                        new ValorMonetario("20.00")),
                new ItemCardapio(2, TipoItemCardapio.ACOMPANHAMENTO, "Batata frita M",
                        "Batata frita tamanho médio",
                        new ValorMonetario("12.00"))
        );
    }

    @Test
    void listLanches_porTipo() {
        when(itemCardapioGateway.findByTipo(TipoItemCardapio.LANCHE)).thenReturn(List.of(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato",
                        new ValorMonetario("20.00"))
        ));

        var result = cardapioUseCases.listarItensCardapio(TipoItemCardapio.LANCHE);

        assertThat(result).containsExactly(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato",
                        new ValorMonetario("20.00"))
        );
    }

    @Test
    void salvarItemCardapio_newItem() {
        var newItem = new ItemCardapio(null, TipoItemCardapio.BEBIDA, "Coca-Cola",
                "Refrigerante Coca-Cola 350ml",
                new ValorMonetario("5.00"));

        var savedItem = new ItemCardapio(3, TipoItemCardapio.BEBIDA, "Coca-Cola",
                "Refrigerante Coca-Cola 350ml",
                new ValorMonetario("5.00"));

        when(itemCardapioGateway.salvarNovo(newItem)).thenReturn(savedItem);

        var result = cardapioUseCases.salvarItemCardapio(newItem);

        assertThat(result).isEqualTo(savedItem);
    }

    @Test
    void salvarItemCardapio_existingItem() {
        var existingItem = new ItemCardapio(1, TipoItemCardapio.BEBIDA, "Coca-Cola",
                "Refrigerante Coca-Cola 350ml",
                new ValorMonetario("5.00"));

        doNothing().when(itemCardapioGateway).atualizar(existingItem);

        var result = cardapioUseCases.salvarItemCardapio(existingItem);

        assertThat(result).isEqualTo(existingItem);
    }

    @Test
    void excluirItemCardapio() {
        int idItemCardapio = 1;

        doNothing().when(itemCardapioGateway).excluir(idItemCardapio);

        cardapioUseCases.excluirItemCardapio(idItemCardapio);

        verify(itemCardapioGateway).excluir(idItemCardapio);
    }
}
