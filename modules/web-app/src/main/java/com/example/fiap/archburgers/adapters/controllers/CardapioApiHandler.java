package com.example.gomesrodris.archburgers.adapters.controllers;

import com.example.gomesrodris.archburgers.adapters.dto.GenericOperationResponse;
import com.example.gomesrodris.archburgers.adapters.dto.ItemCardapioDto;
import com.example.gomesrodris.archburgers.adapters.presenters.ItemCardapioPresenter;
import com.example.gomesrodris.archburgers.apiutils.Preconditions;
import com.example.gomesrodris.archburgers.apiutils.WebUtils;
import com.example.gomesrodris.archburgers.controller.CardapioController;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CardapioApiHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(CardapioApiHandler.class);

    private final CardapioController cardapioController;

    @Autowired
    public CardapioApiHandler(CardapioController cardapioController) {
        this.cardapioController = cardapioController;
    }

    @Operation(summary = "Consulta todos os itens do cardápio. Para uso do frontend de pedidos e também do sistema de administração do cardápio",
            parameters = {@Parameter(name = "tipo", description = "Filtro opcional por tipo (LANCHE, ACOMPANHAMENTO, BEBIDA, SOBREMESA)")})
    @GetMapping(path = "/cardapio")
    public ResponseEntity<List<ItemCardapioDto>> listItems(@RequestParam(value = "tipo", required = false) String tipo) {
        TipoItemCardapio filtroTipo;

        try {
            filtroTipo = StringUtils.isEmpty(tipo) ? null : TipoItemCardapio.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, "Tipo inválido: " + tipo);
        }

        List<ItemCardapio> result = cardapioController.listarItensCardapio(filtroTipo);

        return WebUtils.okResponse(result.stream().map(ItemCardapioPresenter::entityToPresentationDto).toList());
    }

    @Operation(summary = "Grava um novo item no cardápio",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Não enviar os campos id e valor.formatted"))
    @PostMapping("/cardapio")
    public ResponseEntity<ItemCardapioDto> salvarNovoItem(@RequestBody ItemCardapioDto itemCardapioDto) {
        try {
            Preconditions.checkArgument(itemCardapioDto != null, "Missing request body");

            ItemCardapio item = itemCardapioDto.toEntity();
            Preconditions.checkArgument(item.id() == null, "Novo objeto não pode ter um ID");

            var saved = cardapioController.salvarItemCardapio(item);
            return WebUtils.okResponse(ItemCardapioPresenter.entityToPresentationDto(saved));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao salvar novo item: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao salvar novo item");
        }
    }

    @Operation(summary = "Atualiza um item do cardápio")
    @PutMapping("/cardapio/{idItemCardapio}")
    public ResponseEntity<ItemCardapioDto> atualizarItem(@RequestBody ItemCardapioDto itemCardapioDto,
                                                         @PathVariable("idItemCardapio") Integer idItemCardapio) {
        try {
            Preconditions.checkArgument(idItemCardapio != null, "Missing idItemCardapio path param");
            Preconditions.checkArgument(itemCardapioDto != null, "Missing request body");

            ItemCardapio item = itemCardapioDto.toEntity().withId(idItemCardapio);

            var saved = cardapioController.salvarItemCardapio(item);
            return WebUtils.okResponse(ItemCardapioPresenter.entityToPresentationDto(saved));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao salvar item: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao salvar item");
        }
    }

    @Operation(summary = "Exclui um item do cardápio")
    @DeleteMapping("/cardapio/{idItemCardapio}")
    public ResponseEntity<GenericOperationResponse> excluirItem(@PathVariable("idItemCardapio") Integer idItemCardapio) {
        try {
            Preconditions.checkArgument(idItemCardapio != null, "Missing idItemCardapio path param");

            cardapioController.excluirItemCardapio(idItemCardapio);
            return WebUtils.okResponse(new GenericOperationResponse(true));
        } catch (IllegalArgumentException iae) {
            return WebUtils.errorResponse(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao excluir item: {}", e, e);
            return WebUtils.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao excluir item");
        }
    }
}
