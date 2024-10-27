package com.example.fiap.archburgers.adapters.datasource;

import com.example.fiap.archburgers.domain.entities.ItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemCardapioRepositoryJdbcImplTest {
    /*
    Actual database operations are tested in the Integrated Test (IT) class.
    This one is just for basic path and corner cases that are hard to simulate with real DB
     */

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private DatabaseConnection.ConnectionInstance connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private ItemCardapioRepositoryJdbcImpl itemCardapioRepositoryJdbcImpl;

    @BeforeEach
    public void setup() {
        itemCardapioRepositoryJdbcImpl = new ItemCardapioRepositoryJdbcImpl(databaseConnection);
    }

    @Test
    public void salvarNovo() throws SQLException {
        var itemCardapio = new ItemCardapio(null, TipoItemCardapio.LANCHE, "Test Pizza", "Best Pizza in town", new ValorMonetario(BigDecimal.ONE));
        var savedItemCardapio = new ItemCardapio(1, TipoItemCardapio.LANCHE, "Test Pizza", "Best Pizza in town", new ValorMonetario(BigDecimal.ONE));

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        var result = itemCardapioRepositoryJdbcImpl.salvarNovo(itemCardapio);

        assertEquals(savedItemCardapio, result);
    }

    @Test
    void salvarNovo_SQLException() throws SQLException {
        var itemCardapio = new ItemCardapio(null, TipoItemCardapio.LANCHE, "Test Pizza", "Best Pizza in town", new ValorMonetario(BigDecimal.ONE));

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> itemCardapioRepositoryJdbcImpl.salvarNovo(itemCardapio));
    }

    @Test
    void findAll_SQLException() throws SQLException {
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> itemCardapioRepositoryJdbcImpl.findAll());
    }

    @Test
    void atualizar_SQLException() throws SQLException {
        var itemCardapio = new ItemCardapio(1, TipoItemCardapio.LANCHE, "Test Pizza", "Best Pizza in town", new ValorMonetario(BigDecimal.ONE));

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> itemCardapioRepositoryJdbcImpl.salvarNovo(itemCardapio));
    }

    @Test
    void excluir_SQLException() throws SQLException {
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> itemCardapioRepositoryJdbcImpl.excluir(22));
    }
}