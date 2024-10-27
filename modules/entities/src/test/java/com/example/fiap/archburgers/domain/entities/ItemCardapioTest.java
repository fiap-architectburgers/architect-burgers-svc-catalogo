package com.example.fiap.archburgers.domain.entities;

import com.example.fiap.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ItemCardapioTest {

    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        itemCardapio = new ItemCardapio(21, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano",
                new ValorMonetario("23.50"));
    }

    @Test
    void checkAttributes() {
        assertThat(itemCardapio.id()).isEqualTo(21);
        assertThat(itemCardapio.tipo()).isEqualTo(TipoItemCardapio.LANCHE);
        assertThat(itemCardapio.nome()).isEqualTo("Hamburger Vegetariano");
        assertThat(itemCardapio.descricao()).isEqualTo("Hamburger de ervilha com queijo vegano");
        assertThat(itemCardapio.valor()).isEqualTo(new ValorMonetario(new BigDecimal("23.50")));
    }

    @Test
    void withId() {
        var newItem = itemCardapio.withId(45);
        assertThat(newItem).isNotSameAs(itemCardapio);
        assertThat(newItem.id()).isEqualTo(45);
        assertThat(newItem.tipo()).isEqualTo(itemCardapio.tipo());
        assertThat(newItem.nome()).isEqualTo(itemCardapio.nome());
        assertThat(newItem.descricao()).isEqualTo(itemCardapio.descricao());
        assertThat(newItem.valor()).isEqualTo(itemCardapio.valor());
    }
}