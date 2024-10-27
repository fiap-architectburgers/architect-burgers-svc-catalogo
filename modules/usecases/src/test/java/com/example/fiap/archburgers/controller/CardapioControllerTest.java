package com.example.fiap.archburgers.controller;

import com.example.fiap.archburgers.domain.entities.ItemCardapio;
import com.example.fiap.archburgers.domain.usecases.CardapioUseCases;
import com.example.fiap.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CardapioControllerTest {
    private CardapioUseCases cardapioUseCases;
    private CardapioController controller;

    @BeforeEach
    void setUp() {
        cardapioUseCases = mock(CardapioUseCases.class);
        controller = new CardapioController(cardapioUseCases);
    }

    @Test
    void listarItensCardapio() {
        List<ItemCardapio> expectedItems = List.of(
                new ItemCardapio(null, TipoItemCardapio.LANCHE, "Burger", "Delicious Burger", new ValorMonetario("10"))
        );
        when(cardapioUseCases.listarItensCardapio(TipoItemCardapio.LANCHE)).thenReturn(expectedItems);

        List<ItemCardapio> actual = controller.listarItensCardapio(TipoItemCardapio.LANCHE);

        assertThat(actual).isEqualTo(expectedItems);
    }

    @Test
    void salvarItemCardapio() {
        ItemCardapio newItem = new ItemCardapio(null, TipoItemCardapio.LANCHE, "Cheeseburger", "Cheeseburger with pickles", new ValorMonetario("15"));
        ItemCardapio expectedItem = newItem.withId(1);
        when(cardapioUseCases.salvarItemCardapio(newItem)).thenReturn(expectedItem);

        ItemCardapio actual = controller.salvarItemCardapio(newItem);

        assertThat(actual).isEqualTo(expectedItem);
    }

    @Test
    void excluirItemCardapio() {
        int itemIdToRemove = 1;
        doNothing().when(cardapioUseCases).excluirItemCardapio(itemIdToRemove);

        controller.excluirItemCardapio(itemIdToRemove);

        verify(cardapioUseCases, times(1)).excluirItemCardapio(itemIdToRemove);
    }
}
