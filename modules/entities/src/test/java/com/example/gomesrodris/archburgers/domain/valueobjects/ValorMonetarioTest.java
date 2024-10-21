package com.example.gomesrodris.archburgers.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ValorMonetarioTest {

    @Test
    void adjustScaleIfSmaller() {
        assertThat(new ValorMonetario("25.98")).isEqualTo(new ValorMonetario("25.98"));

        assertThat(new ValorMonetario("123456.9")).isEqualTo(new ValorMonetario("123456.90"));
        assertThat(new ValorMonetario("13")).isEqualTo(new ValorMonetario("13.00"));
    }

    @Test
    void rejectInvalidValues() {
        var e = assertThrows(IllegalArgumentException.class, () -> new ValorMonetario((BigDecimal) null));
        assertThat(e).hasMessage("Valor nao pode ser nulo");

        e = assertThrows(IllegalArgumentException.class, () -> new ValorMonetario("-1"));
        assertThat(e).hasMessage("Valor nao pode ser negativo");

        e = assertThrows(IllegalArgumentException.class, () -> new ValorMonetario("25.999"));
        assertThat(e).hasMessage("Valor monetario invalido, mais que 2 digitos decimais: 25.999");

        e = assertThrows(IllegalArgumentException.class, () -> new ValorMonetario("15.0003"));
        assertThat(e).hasMessage("Valor monetario invalido, mais que 2 digitos decimais: 15.0003");
    }

    @Test
    void asBigDecimalTestCases() {
        assertThat(new ValorMonetario("25.98").asBigDecimal()).isEqualByComparingTo(new BigDecimal("25.98"));
        assertThat(new ValorMonetario("123456.9").asBigDecimal()).isEqualByComparingTo(new BigDecimal("123456.90"));
        assertThat(new ValorMonetario("13").asBigDecimal()).isEqualByComparingTo(new BigDecimal("13.00"));
    }

    @Test
    void hashCodeTestCases() {
        assertThat(new ValorMonetario("25.98").hashCode()).isNotNull();
        assertThat(new ValorMonetario("13").hashCode()).isNotNull();

        assertThat(new ValorMonetario("25.98").hashCode()).isEqualTo(new ValorMonetario("025.98").hashCode());
        assertThat(new ValorMonetario("13").hashCode()).isEqualTo(new ValorMonetario("13.00").hashCode());

        assertThat(new ValorMonetario("25.98").hashCode()).isNotEqualTo(new ValorMonetario("25.99").hashCode());
        assertThat(new ValorMonetario("123456.9").hashCode()).isNotEqualTo(new ValorMonetario("123456.8").hashCode());
        assertThat(new ValorMonetario("13").hashCode()).isNotEqualTo(new ValorMonetario("14").hashCode());
    }

    @Test
    void toStringTestCases() {
        assertThat(new ValorMonetario("25.98").toString()).isEqualTo("R$25.98");
        assertThat(new ValorMonetario("123456.9").toString()).isEqualTo("R$123456.90");
        assertThat(new ValorMonetario("13").toString()).isEqualTo("R$13.00");
    }
}
