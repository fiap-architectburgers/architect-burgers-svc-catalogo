package com.example.fiap.archburgers.adapters.datasource;

import com.example.fiap.archburgers.domain.datasource.ItemCardapioDataSource;
import com.example.fiap.archburgers.domain.entities.ItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.fiap.archburgers.domain.valueobjects.ValorMonetario;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the Repository based on a relational database via JDBC
 */
@Repository
public class ItemCardapioRepositoryJdbcImpl implements ItemCardapioDataSource {
    @Language("SQL")
    private static final String SQL_SELECT_BY_ID = """
                select item_cardapio_id, tipo, nome, descricao, valor
                from item_cardapio
                where item_cardapio_id = ?
            """.stripIndent();

    @Language("SQL")
    private static final String SQL_SELECT_ALL_ITEMS = """
                select item_cardapio_id, tipo, nome, descricao, valor
                from item_cardapio
                order by case
                    when tipo = 'L' then 1
                    when tipo = 'A' then 2
                    when tipo = 'B' then 3
                    when tipo = 'S' then 4
                    end
                asc, item_cardapio_id asc
            """.stripIndent();

    @Language("SQL")
    private static final String SQL_SELECT_BY_TIPO = """
                select item_cardapio_id, tipo, nome, descricao, valor
                from item_cardapio
                where tipo = ?
                order by item_cardapio_id asc
            """.stripIndent();

    @Language("SQL")
    private static final String SQL_INSERT = """
                insert into item_cardapio (tipo, nome, descricao, valor)
                values (?,?,?,?)
                returning item_cardapio_id;
            """.stripIndent();

    @Language("SQL")
    private static final String SQL_UPDATE = """
                update item_cardapio set tipo = ?, nome = ?, descricao = ?, valor = ?
                where item_cardapio_id = ?
            """.stripIndent();

    @Language("SQL")
    private static final String SQL_DELETE = """
                delete from item_cardapio where item_cardapio_id = ?
            """.stripIndent();

    private final DatabaseConnection databaseConnection;

    @Autowired
    public ItemCardapioRepositoryJdbcImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public ItemCardapio findById(int id) {
        var result = getItems(SQL_SELECT_BY_ID, id);
        if (result.isEmpty())
            return null;

        return result.getFirst();
    }

    @Override
    public List<ItemCardapio> findAll() {
        return getItems(SQL_SELECT_ALL_ITEMS, null);
    }

    @Override
    public List<ItemCardapio> findByTipo(TipoItemCardapio filtroTipo) {
        return getItems(SQL_SELECT_BY_TIPO, filtroTipo.getAbreviacao());
    }

    @Override
    public ItemCardapio salvarNovo(ItemCardapio itemCardapio) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_INSERT)) {

            stmt.setString(1, itemCardapio.tipo().getAbreviacao());
            stmt.setString(2, itemCardapio.nome());
            stmt.setString(3, itemCardapio.descricao());
            stmt.setBigDecimal(4, itemCardapio.valor().asBigDecimal());

            var rs = stmt.executeQuery();

            boolean hasReturn = rs.next();
            assert hasReturn: "Modification query expected return";

            return itemCardapio.withId(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(ItemCardapio itemCardapio) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, itemCardapio.tipo().getAbreviacao());
            stmt.setString(2, itemCardapio.nome());
            stmt.setString(3, itemCardapio.descricao());
            stmt.setBigDecimal(4, itemCardapio.valor().asBigDecimal());

            stmt.setInt(5, Objects.requireNonNull(itemCardapio.id(), "Saved item is expected in Update"));

            var result = stmt.executeUpdate();

            if (result == 0) {
                throw new IllegalStateException("Nenhum registro encontrado");
            }
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void excluir(int idItemCardapio) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(SQL_DELETE)) {

            stmt.setInt(1, idItemCardapio);

            var result = stmt.executeUpdate();

            if (result == 0) {
                throw new IllegalArgumentException("Nenhum registro encontrado");
            }
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }

    private @NotNull List<ItemCardapio> getItems(@Language("SQL") String query, Object param) {
        try (var connection = databaseConnection.getConnection();
             var stmt = connection.prepareStatement(query)) {

            if (param instanceof Integer) {
                stmt.setInt(1, (Integer) param);
            } else if (param instanceof String) {
                stmt.setString(1, (String) param);
            } // Only the two types above are expected. Otherwise, if the query requires a parameter it will fail

            ResultSet rs = stmt.executeQuery();
            List<ItemCardapio> results = new ArrayList<>();

            while (rs.next()) {
                ItemCardapio itemCardapio = new ItemCardapio(
                        rs.getInt("item_cardapio_id"),
                        TipoItemCardapio.getByAbreviacao(rs.getString("tipo")),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        new ValorMonetario(rs.getBigDecimal("valor"))
                );

                results.add(itemCardapio);
            }

            return results;
        } catch (SQLException e) {
            throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
        }
    }


}
