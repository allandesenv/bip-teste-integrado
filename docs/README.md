# Documentacao do Desafio

## Status dos itens

1. Banco de dados (schema + seed): concluido
2. Correcao EJB (validacao, saldo, locking): concluido
3. Backend CRUD + integracao EJB: concluido
4. Frontend Angular: concluido
5. Testes EJB e Backend: concluido
6. Swagger + README: concluido

## Qualidade tecnica entregue

- Arquitetura em camadas no backend
- Regra critica de transferencia centralizada no EJB
- Tratamento global de erros HTTP (400, 404, 409, 500)
- Testes de servico e controller no backend
- Testes unitarios do EJB
- Frontend Angular integrado com CRUD e transferencia
- Ambiente containerizado para banco, backend e frontend

## Evidencias de execucao

- Build e testes Java: `mvn -B test`
- Frontend Angular build: `cd frontend && npm run build`
- API: `http://localhost:8080`
- Frontend: `http://localhost:4200`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
