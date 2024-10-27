package com.example.gomesrodris.archburgers.adapters.datasource;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Database Integration Tests
 */
class ItemCardapioRepositoryJdbcImplIT {
    private static RealDatabaseTestHelper realDatabase;
    private DatabaseConnection databaseConnection;

    private ItemCardapioRepositoryJdbcImpl repository;

    @BeforeAll
    static void beforeAll() throws Exception {
        realDatabase = new RealDatabaseTestHelper();
        realDatabase.beforeAll();
    }

    @AfterAll
    static void afterAll() {
        realDatabase.afterAll();
    }

    @BeforeEach
    void setUp() {
        databaseConnection = realDatabase.getConnectionPool();
        repository = new ItemCardapioRepositoryJdbcImpl(databaseConnection);
    }

    @AfterEach
    void tearDown() {
        databaseConnection.close();
    }

    @Test
    void findById() {
        ItemCardapio item = repository.findById(6);
        assertThat(item).isEqualTo(new ItemCardapio(6, TipoItemCardapio.SOBREMESA,
                "Milk shake", "Milk Shake de chocolate, morango ou baunilha. Escolha o sabor na observação",
                new ValorMonetario("12.50")));
    }

    @Test
    void findById_nonExisting() {
        ItemCardapio item = repository.findById(66);
        assertThat(item).isNull();
    }

    @Test
    void findAll() {
        List<ItemCardapio> allItens = repository.findAll();

        assertThat(allItens).hasSizeGreaterThanOrEqualTo(8);

        assertThat(allItens.get(0)).isEqualTo(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato", new ValorMonetario("22.90"))
        );

        assertThat(allItens.get(1)).isEqualTo(
                new ItemCardapio(2, TipoItemCardapio.LANCHE, "Veggie Cheddar",
                        "Hamburger do Futuro com cebolas caramelizadas e cheddar vegano", new ValorMonetario("23.50"))
        );

        // Verifica se ordenação por tipos foi correta
        assertThat(allItens.get(2).tipo()).isEqualTo(TipoItemCardapio.ACOMPANHAMENTO);
        assertThat(allItens.get(3).tipo()).isEqualTo(TipoItemCardapio.ACOMPANHAMENTO);

        assertThat(allItens.get(4).tipo()).isEqualTo(TipoItemCardapio.BEBIDA);
        assertThat(allItens.get(5).tipo()).isEqualTo(TipoItemCardapio.BEBIDA);

        assertThat(allItens.get(allItens.size() - 2).tipo()).isEqualTo(TipoItemCardapio.SOBREMESA);

        assertThat(allItens.getLast()).isEqualTo(
                new ItemCardapio(7, TipoItemCardapio.SOBREMESA, "Mini churros",
                        "Mini churros de doce de leite", new ValorMonetario("0.99"))
        );
    }

    @Test
    void findByTipo() {
        List<ItemCardapio> itens = repository.findByTipo(TipoItemCardapio.SOBREMESA);

        assertThat(itens).hasSize(2);

        assertThat(itens).containsAll(List.of(
                new ItemCardapio(6, TipoItemCardapio.SOBREMESA, "Milk shake",
                        "Milk Shake de chocolate, morango ou baunilha. Escolha o sabor na observação", new ValorMonetario("12.50")),
                new ItemCardapio(7, TipoItemCardapio.SOBREMESA, "Mini churros",
                        "Mini churros de doce de leite", new ValorMonetario("0.99"))
        ));
    }

    @Test
    void salvarNovo() {
        ItemCardapio saved = repository.salvarNovo(new ItemCardapio(null,
                TipoItemCardapio.BEBIDA,
                "Suco de laranja",
                "Suco de laranja natural",
                new ValorMonetario("7.50")));

        assertThat(saved.id()).isNotNull();
        assertThat(saved.id()).isGreaterThan(8);

        assertThat(repository.findById(saved.id())).isEqualTo(new ItemCardapio(saved.id(),
                TipoItemCardapio.BEBIDA,
                "Suco de laranja",
                "Suco de laranja natural",
                new ValorMonetario("7.50")));
    }

    @Test
    void atualizar() {
        var original = new ItemCardapio(2, TipoItemCardapio.LANCHE, "Veggie Cheddar",
                "Hamburger do Futuro com cebolas caramelizadas e cheddar vegano", new ValorMonetario("23.50"));
        var changed = new ItemCardapio(2, TipoItemCardapio.ACOMPANHAMENTO, "Nuggets",
                "Porção de Nuggets de frango vegano", new ValorMonetario("19.99"));

        repository.atualizar(changed);

        assertThat(repository.findById(2)).isEqualTo(changed);

        repository.atualizar(original);
        assertThat(repository.findById(2)).isEqualTo(original);
    }

    @Test
    void excluir() {
        ItemCardapio saved = repository.salvarNovo(new ItemCardapio(null,
                TipoItemCardapio.BEBIDA,
                "Suco de limão",
                "Suco de limão natural",
                new ValorMonetario("7.50")));

        assertThat(saved.id()).isNotNull();

        assertThat(repository.findById(saved.id()).nome()).isEqualTo("Suco de limão");

        ///
        repository.excluir(saved.id());

        assertThat(repository.findById(saved.id())).isNull();
    }

    @Test
    void excluir_naoEncontrado() {
        assertThat(
                assertThrows(IllegalArgumentException.class, () -> repository.excluir(99))
        ).hasMessage("Nenhum registro encontrado");
    }
}