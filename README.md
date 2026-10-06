# POC Delivery

Trabalho da disciplina — Solicitação de Mudança #1 (autenticação, tela principal, clientes e usuários).

Integrantes: ver [alunos.md](alunos.md).

## Testes

Os cenários de aceite das histórias US17 a US20 estão em `src/test/java/supermercado`, um arquivo por história (`US17AutenticarUsuarioTest`, `US18TelaPrincipalTest`, `US19ManterClientesTest`, `US20ManterUsuariosTest`). Cada teste tem o nome do cenário correspondente no documento.

```
mvn test
```

Os testes que verificam a estrutura das janelas (centralização, menus, janela maximizada) precisam de ambiente gráfico e são ignorados quando executados em modo headless.

## Usuários para teste

| Usuário    | Senha       | Perfil        | Situação     |
|------------|-------------|---------------|--------------|
| admin      | admin123    | Administrador | Habilitado   |
| fernanda   | fernanda123 | Atendente     | Habilitado   |
| anasouza   | ana12345    | Cliente       | Habilitado   |
| diego      | diego123    | Atendente     | Desabilitado |

O login também aceita o e-mail (ex.: `admin@pocdelivery.com`). Os dados ficam em memória e são recarregados a cada execução.
