package com.example.fiap.archburgers.domain.datagateway;

import com.example.fiap.archburgers.domain.entities.ItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.TipoItemCardapio;

import java.util.List;

public interface ItemCardapioGateway {
    ItemCardapio findById(int id);
    List<ItemCardapio> findAll();

    List<ItemCardapio> findByTipo(TipoItemCardapio filtroTipo);

    ItemCardapio salvarNovo(ItemCardapio itemCardapio);

    void atualizar(ItemCardapio itemCardapio);

    void excluir(int idItemCardapio);
}
