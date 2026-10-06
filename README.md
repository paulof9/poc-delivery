# POC Delivery

Trabalho da disciplina — Solicitação de Mudança #1 (autenticação, tela principal, clientes e usuários).

Integrantes: ver [alunos.md](alunos.md).

## Como executar

Requer JDK 21 ou superior.

Com Maven:

```
mvn compile exec:java
```

Sem Maven:

```
sh build.sh
java -jar target/poc-delivery-1.0.0.jar
```

## Usuários para teste

| Usuário    | Senha       | Perfil        | Situação     |
|------------|-------------|---------------|--------------|
| admin      | admin123    | Administrador | Habilitado   |
| fernanda   | fernanda123 | Atendente     | Habilitado   |
| anasouza   | ana12345    | Cliente       | Habilitado   |
| diego      | diego123    | Atendente     | Desabilitado |

O login também aceita o e-mail (ex.: `admin@pocdelivery.com`). Os dados ficam em memória e são recarregados a cada execução.
