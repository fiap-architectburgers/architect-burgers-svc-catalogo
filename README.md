Architect Burgers - Microsserviço de Catálogo
=============================================



### How-TOs

Preparar DB local para desenvolvimento

Pré-requisitos: instância de PostgreSQL

Executar os seguintes comandos como administrador:

    create user burger_user_catalogo with password 'burgerYeah';
    create database archburgers_svc_catalogo owner burger_user_catalogo;