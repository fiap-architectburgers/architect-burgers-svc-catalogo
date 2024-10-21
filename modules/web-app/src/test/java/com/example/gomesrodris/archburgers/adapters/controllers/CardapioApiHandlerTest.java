package com.example.gomesrodris.archburgers.adapters.controllers;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.adapters.dto.ItemCardapioDto;
import com.example.gomesrodris.archburgers.adapters.dto.ValorMonetarioDto;
import com.example.gomesrodris.archburgers.controller.CardapioController;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CardapioApiHandlerTest {
    private MockMvc mockMvc;

    @Mock
    private CardapioController cardapioController;

    @BeforeEach
    void setUp() {
        CardapioApiHandler cardapioApiHandler = new CardapioApiHandler(cardapioController);
        mockMvc = MockMvcBuilders.standaloneSetup(cardapioApiHandler).build();
    }

    @Test
    void listItemsCardapio() throws Exception {
        List<ItemCardapio> allItems = List.of(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger",
                        "Hamburger com queijo", new ValorMonetario("22.90")),
                new ItemCardapio(2, TipoItemCardapio.ACOMPANHAMENTO, "Batata frita M",
                        "Batata frita porção média", new ValorMonetario("10.00"))
        );
        List<ItemCardapioDto> allItemsPresentation = List.of(
                new ItemCardapioDto(1, "LANCHE", "Hamburger",
                        "Hamburger com queijo", new ValorMonetarioDto("22.90", "R$ 22,90")),
                new ItemCardapioDto(2, "ACOMPANHAMENTO", "Batata frita M",
                        "Batata frita porção média", new ValorMonetarioDto("10.00", "R$ 10,00"))
        );

        when(cardapioController.listarItensCardapio(null)).thenReturn(allItems);

        mockMvc.perform(get("/cardapio"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        new ObjectMapper().writeValueAsString(allItemsPresentation)
                ));
    }

    @Test
    void listItemsCardapio_filtroTipo() throws Exception {
        List<ItemCardapio> filteredItems = List.of(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger",
                        "Hamburger com queijo", new ValorMonetario("22.90"))
        );
        List<ItemCardapioDto> filteredItemsPresentation = List.of(
                new ItemCardapioDto(1, "LANCHE", "Hamburger",
                        "Hamburger com queijo", new ValorMonetarioDto("22.90", "R$ 22,90"))
        );

        when(cardapioController.listarItensCardapio(TipoItemCardapio.LANCHE)).thenReturn(filteredItems);

        mockMvc.perform(get("/cardapio?tipo=LANCHE"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        new ObjectMapper().writeValueAsString(filteredItemsPresentation)
                ));
    }

    @Test
    void listItemsCardapio_filtroTipo_invalido() throws Exception {
        mockMvc.perform(get("/cardapio?tipo=WRONG"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void salvarNovoItem() throws Exception {
        ItemCardapioDto newItemParam = new ItemCardapioDto(null, "LANCHE", "Hamburger",
                "Hamburger com queijo", new ValorMonetarioDto("22.90", null));

        ItemCardapio newItemToSave = new ItemCardapio(null, TipoItemCardapio.LANCHE, "Hamburger",
                "Hamburger com queijo", new ValorMonetario("22.90"));

        ItemCardapio newItemSaved = new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger",
                "Hamburger com queijo", new ValorMonetario("22.90"));

        ItemCardapioDto newItemOutput = new ItemCardapioDto(1, "LANCHE", "Hamburger",
                "Hamburger com queijo", new ValorMonetarioDto("22.90", "R$ 22,90"));

        when(cardapioController.salvarItemCardapio(newItemToSave)).thenReturn(newItemSaved);

        mockMvc.perform(
                        post("/cardapio")
                                .content(new ObjectMapper().writeValueAsString(newItemParam))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        new ObjectMapper().writeValueAsString(newItemOutput)
                ));
    }

    @Test
    void salvarNovoItem_badRequest() throws Exception {
        ItemCardapioDto newItemParam = new ItemCardapioDto(33, "LANCHE", "Hamburger",
                "Não permitido porque já contém ID", new ValorMonetarioDto("22.90", null));

        mockMvc.perform(
                        post("/cardapio")
                                .content(new ObjectMapper().writeValueAsString(newItemParam))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void atualizarItem() throws Exception {
        ItemCardapioDto updatedItemParam = new ItemCardapioDto(null, "LANCHE", "Hamburger",
                "Hamburger com queijo", new ValorMonetarioDto("22.90", null));

        ItemCardapio updatedItemEntity = new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger",
                "Hamburger com queijo", new ValorMonetario("22.90"));

        ItemCardapioDto updatedItemOutput = new ItemCardapioDto(1, "LANCHE", "Hamburger",
                "Hamburger com queijo", new ValorMonetarioDto("22.90", "R$ 22,90"));

        when(cardapioController.salvarItemCardapio(updatedItemEntity)).thenReturn(updatedItemEntity);

        mockMvc.perform(
                        put("/cardapio/{idItemCardapio}", 1)
                                .content(new ObjectMapper().writeValueAsString(updatedItemParam))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        new ObjectMapper().writeValueAsString(updatedItemOutput)
                ));

        verify(cardapioController).salvarItemCardapio(updatedItemEntity);
    }

    @Test
    void atualizarItem_badRequest() throws Exception {
        mockMvc.perform(
                        put("/cardapio/{idItemCardapio}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void excluirItem() throws Exception {
        mockMvc.perform(
                        delete("/cardapio/{idItemCardapio}", 1)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"success\": true}"
                ));

        verify(cardapioController).excluirItemCardapio(1);
    }
}