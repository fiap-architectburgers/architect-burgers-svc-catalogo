# language: pt
Funcionalidade: Consulta e manutenção de cardápio

  Cenário: Consultar lista de itens do cardápio
    Dado que um cliente irá obter os itens do cardápio
    Quando o cliente faz a requisição do cardápio
    Então a resposta é uma lista de itens

  Delineação do Cenário: Verificar tipo dos itens do cardápio
    Dado que um cliente irá obter os itens do cardápio
    Quando o cliente faz a requisição do cardápio
    Então a lista retornada possui ao menos um item do tipo "<Tipo de item>"

    Exemplos:
      | Tipo de item   |
      | LANCHE         |
      | ACOMPANHAMENTO |
      | BEBIDA         |
      | SOBREMESA      |
