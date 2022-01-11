# finances-control

API desenvolvida com o intuito de implementar segurança e estudar o protocolo OAuth2 utilizando Spring Boot e Spring Security.

A aplicação simula um APP de controle de finanças e o seu principal objetivo é cadastrar usuários suas categorias e seus lancamentos.
O APP conta com a implementação da segurança utilizando o protocolo OAuth2 e o Spring Security, foi implementado dois Grant Types definidos na
especificação do protocolo que são o Grant Type Password Credentials e o Grant Type Authorization Code que são os mais usados. Também foi implementado
a authorização utilizando tokens de acesso do tipo JWT que carregam no seu payload as informações do usuário e também o token é assinado com uma chave simetrica
onde tanto o Authorization Server e o Resource Server tem acesso a chave para validar o token JWT gerado.

## Tecnologias utilizadas

- Linguagem de Programação Java
- Framework Spring Boot
- JPA e Hibernet
- Banco de Dados MySQL
- Lombok
- Model Mapper
- Spring Security
- Spring OAuth2
- JWT - Json Web Token para autenticação e autorização

`Rosivaldo Lucas, estudante de Engenharia de Computação - UFPB`
