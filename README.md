💻 Sobre o Projeto
Forum Web é uma aplicação backend que permite a criação, leitura, atualização e exclusão (CRUD) de entidades como tópicos, respostas, cursos e usuários. A aplicação é projetada para fornecer uma plataforma de fórum onde usuários podem interagir e compartilhar conhecimentos em diferentes áreas de estudo.

⚙️ Funcionalidades
CRUD de Tópicos: Permite a criação, leitura, atualização e exclusão de tópicos no fórum.
CRUD de Cursos: Permite a gestão de cursos, incluindo a criação, leitura, atualização e exclusão.
CRUD de Respostas: Facilita a adição, leitura, atualização e exclusão de respostas associadas a tópicos.
CRUD de Usuários: Gerencia as informações dos usuários, incluindo a criação, leitura, atualização e exclusão de perfis.
🛠 Tecnologias
As seguintes tecnologias foram utilizadas no desenvolvimento da API Rest do projeto:

Java 17: Linguagem de programação utilizada para o desenvolvimento da aplicação.
Spring Boot 3: Framework para simplificação do desenvolvimento de aplicações Java.
Maven: Ferramenta de automação de compilação utilizada para gerenciamento de dependências e build.
MySQL: Sistema de gerenciamento de banco de dados relacional utilizado para armazenar os dados da aplicação.
Hibernate: Framework de mapeamento objeto-relacional utilizado para facilitar a interação com o banco de dados.
Flyway: Ferramenta de versionamento de banco de dados utilizada para gerenciamento de migrações.
Lombok: Biblioteca que reduz o código boilerplate, como getters, setters e construtores, através de anotações.
🚀 Como Executar o Projeto
Clone o repositório:

sh
Copiar código
git clone https://github.com/seu-usuario/seu-repositorio.git
Navegue até o diretório do projeto:

sh
Copiar código
cd seu-repositorio
Configure o banco de dados:
Atualize as configurações de banco de dados no arquivo application.properties.

Execute as migrações do banco de dados:

sh
Copiar código
mvn flyway:migrate
Execute a aplicação:

sh
Copiar código
mvn spring-boot:run
Acesse a aplicação:
A aplicação estará disponível em http://localhost:8080.


🤝 Contribuindo
Sinta-se à vontade para abrir issues e enviar pull requests. Toda contribuição é bem-vinda!
