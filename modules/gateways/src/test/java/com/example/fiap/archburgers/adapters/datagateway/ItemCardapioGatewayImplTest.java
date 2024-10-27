package com.example.fiap.archburgers.adapters.datagateway;

import com.example.fiap.archburgers.domain.datasource.ItemCardapioDataSource;
import com.example.fiap.archburgers.domain.entities.ItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ItemCardapioGatewayImplTest {

    private ItemCardapioDataSource itemCardapioDataSourceMock;
    private ItemCardapioGatewayImpl itemCardapioGateway;

    @BeforeEach
    public void setUp() {
        itemCardapioDataSourceMock = mock(ItemCardapioDataSource.class);
        itemCardapioGateway = new ItemCardapioGatewayImpl(itemCardapioDataSourceMock);
    }

    @Test
    public void testFindById() {
        ItemCardapio itemCardapioExpected = new ItemCardapio(1, TipoItemCardapio.BEBIDA, "Test", "Test Description", new ValorMonetario("10.0"));
        when(itemCardapioDataSourceMock.findById(1)).thenReturn(itemCardapioExpected);

        ItemCardapio itemCardapioActual = itemCardapioGateway.findById(1);

        assertThat(itemCardapioActual).isEqualTo(itemCardapioExpected);
    }
    @Test
    public void testFindAll() {
        List<ItemCardapio> itemCardapioListExpected = List.of(
            new ItemCardapio(1, TipoItemCardapio.BEBIDA, "Test", "Test Description", new ValorMonetario("10.0")),
            new ItemCardapio(2, TipoItemCardapio.LANCHE, "Test 2", "Test Description 2", new ValorMonetario("10.0"))
        );
        when(itemCardapioDataSourceMock.findAll()).thenReturn(itemCardapioListExpected);

        List<ItemCardapio> itemCardapioActualList = itemCardapioGateway.findAll();

        assertThat(itemCardapioActualList).isEqualTo(itemCardapioListExpected);
    }

    @Test
    public void testFindByTipo() {
        List<ItemCardapio> expected = List.of(
                new ItemCardapio(1, TipoItemCardapio.BEBIDA, "Test", "Test Description", new ValorMonetario("10.0")),
                new ItemCardapio(2, TipoItemCardapio.BEBIDA, "Test 2", "Test Description 2", new ValorMonetario("10.0"))
        );
        when(itemCardapioDataSourceMock.findByTipo(TipoItemCardapio.BEBIDA)).thenReturn(expected);

        List<ItemCardapio> actual = itemCardapioGateway.findByTipo(TipoItemCardapio.BEBIDA);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testSalvarNovo() {
        ItemCardapio newItem = new ItemCardapio(null, TipoItemCardapio.BEBIDA, "Test", "Test Description", new ValorMonetario("10.0"));
        ItemCardapio saved = new ItemCardapio(1, TipoItemCardapio.BEBIDA, "Test", "Test Description", new ValorMonetario("10.0"));

        when(itemCardapioDataSourceMock.salvarNovo(newItem)).thenReturn(saved);

        ItemCardapio actual = itemCardapioGateway.salvarNovo(newItem);

        assertThat(actual).isEqualTo(saved);
    }

    @Test
    public void testAtualizar() {
        ItemCardapio item = new ItemCardapio(1, TipoItemCardapio.BEBIDA, "Test", "Test Description", new ValorMonetario("10.0"));
        doNothing().when(itemCardapioDataSourceMock).atualizar(item);

        itemCardapioGateway.atualizar(item);

        verify(itemCardapioDataSourceMock, times(1)).atualizar(item);
    }

    @Test
    public void testExcluir() {
        doNothing().when(itemCardapioDataSourceMock).excluir(1);

        itemCardapioGateway.excluir(1);

        verify(itemCardapioDataSourceMock, times(1)).excluir(1);
    }
}
