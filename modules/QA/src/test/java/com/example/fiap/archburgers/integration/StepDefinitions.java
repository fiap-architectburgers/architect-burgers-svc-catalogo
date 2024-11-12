package com.example.fiap.archburgers.integration;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class StepDefinitions {

    private Response response;

    @Dado("que um cliente irá obter os itens do cardápio")
    public void que_um_cliente_ira_obter_os_itens_do_cardapio() {
        RestAssured.baseURI = "http://localhost:8092";
        RestAssured.basePath = "/cardapio";
    }

    @Quando("o cliente faz a requisição do cardápio")
    public void o_cliente_faz_a_requisicao_do_cardapio() {
        response = given().contentType(ContentType.JSON).when().get();
    }

    @Entao("a resposta é uma lista de itens")
    public void a_resposta_e_uma_lista_de_itens() {
        response.then().body(
                "", isA(List.class),
                "", hasSize(greaterThan(1)));
    }

    @Entao("a lista retornada possui ao menos um item do tipo {string}")
    public void a_lista_retornada_possui_ao_menos_um_item_do_tipo(String tipo) {
        response.then().body(
                "", isA(List.class),
                "", hasItem(hasEntry("tipo", tipo)));
    }
}
