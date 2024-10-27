package com.example.fiap.archburgers.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TipoItemCardapioTest {

    @Test
    void getByAbreviacaoLancheTest() {
        assertEquals(TipoItemCardapio.LANCHE, TipoItemCardapio.getByAbreviacao("L"));
    }

    @Test
    void getByAbreviacaoTest() {
        assertEquals(TipoItemCardapio.ACOMPANHAMENTO, TipoItemCardapio.getByAbreviacao("A"));
        assertEquals(TipoItemCardapio.BEBIDA, TipoItemCardapio.getByAbreviacao("B"));
        assertEquals(TipoItemCardapio.SOBREMESA, TipoItemCardapio.getByAbreviacao("S"));
        assertThrows(IllegalArgumentException.class, () -> TipoItemCardapio.getByAbreviacao("Z"));
    }

    @Test
    void getAbreviacaoTest() {
        assertEquals("L", TipoItemCardapio.LANCHE.getAbreviacao());
        assertEquals("A", TipoItemCardapio.ACOMPANHAMENTO.getAbreviacao());
        assertEquals("B", TipoItemCardapio.BEBIDA.getAbreviacao());
        assertEquals("S", TipoItemCardapio.SOBREMESA.getAbreviacao());
    }
}
