package com.example.fiap.archburgers.domain.entities;

import com.example.fiap.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ItemCardapio(@Nullable Integer id,
                           @NotNull TipoItemCardapio tipo,
                           @NotNull String nome,
                           @NotNull String descricao,
                           @NotNull ValorMonetario valor) {

    public ItemCardapio withId(Integer newId) {
        return new ItemCardapio(newId, tipo, nome, descricao, valor);
    }
}
