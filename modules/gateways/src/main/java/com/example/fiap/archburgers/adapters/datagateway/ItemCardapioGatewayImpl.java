package com.example.fiap.archburgers.adapters.datagateway;

import com.example.fiap.archburgers.domain.datagateway.ItemCardapioGateway;
import com.example.fiap.archburgers.domain.datasource.ItemCardapioDataSource;
import com.example.fiap.archburgers.domain.entities.ItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.TipoItemCardapio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCardapioGatewayImpl implements ItemCardapioGateway {
    private final ItemCardapioDataSource itemCardapioDataSource;

    public ItemCardapioGatewayImpl(ItemCardapioDataSource itemCardapioDataSource) {
        this.itemCardapioDataSource = itemCardapioDataSource;
    }

    @Override
    public ItemCardapio findById(int id) {
        return itemCardapioDataSource.findById(id);
    }

    @Override
    public List<ItemCardapio> findAll() {
        return itemCardapioDataSource.findAll();
    }

    @Override
    public List<ItemCardapio> findByTipo(TipoItemCardapio filtroTipo) {
        return itemCardapioDataSource.findByTipo(filtroTipo);
    }

    @Override
    public ItemCardapio salvarNovo(ItemCardapio itemCardapio) {
        return itemCardapioDataSource.salvarNovo(itemCardapio);
    }

    @Override
    public void atualizar(ItemCardapio itemCardapio) {
        itemCardapioDataSource.atualizar(itemCardapio);
    }

    @Override
    public void excluir(int idItemCardapio) {
        itemCardapioDataSource.excluir(idItemCardapio);
    }
}
