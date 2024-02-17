# Sistema de Aluguel de livros Escolar (Backend)

Este repositório contém a implementação do backend de um sistema de gerenciamento de aluguel de livros para uma biblioteca escolar. O projeto foi desenvolvido como parte de um teste técnico, utilizando Spring Boot para o backend e Angular para o frontend, embora este repositório se concentre exclusivamente no backend.

## Funcionalidades

O sistema promove as seguintes funcionalidades através de uma API REST:

### 1. Lista de Livros
- **Pesquisa de Livros**: Permite pesquisar livros por nome, autor, etc.
- **Detalhes do Livro**: Exibe detalhes específicos de um livro selecionado.
- **Aluguel de Livros**: Habilita o aluguel de livros disponíveis na biblioteca.
- **Restrição de Aluguel**: Impede o aluguel de livros que já estão alugados.

### 2. CRUD de Livros
- **Cadastro de Livros**: Permite adicionar novos livros ao sistema.
- **Edição de Livros**: Habilita a edição de livros não alugados.
- **Remoção de Livros**: Permite remover livros não alugados do sistema.

### 3. Cadastro de Usuários
- **Registro de Usuários**: Permite o cadastro de usuários no sistema, habilitando o acesso às funcionalidades de aluguel de livros e gerenciamento da biblioteca. Cada usuário pode ter diferentes níveis de acesso, como administrador ou cliente, com permissões específicas.

### 4. CRUD de Autores
- **Cadastro de Autores**: Possibilita adicionar novos autores ao catálogo da biblioteca. Cada autor cadastrado pode estar associado a um ou mais livros.
- **Edição de Informações do Autor**: Permite atualizar os dados cadastrais dos autores, como nome, biografia e obras relacionadas.
- **Remoção de Autores**: Autores podem ser removidos do sistema, desde que não haja livros atualmente alugados associados a eles.
- **Consulta de Autores**: Facilita a visualização de todos os autores cadastrados no sistema, juntamente com suas informações relevantes e lista de livros associados.

### 5. CRUD de Estudantes e Empréstimos de Livros
O sistema também facilita o gerenciamento de estudantes e o processo de empréstimo de livros, oferecendo um conjunto de funcionalidades para controle eficiente dos recursos da biblioteca:

- **Cadastro de Estudantes**: Permite o registro de estudantes no sistema, armazenando informações essenciais como nome, e-mail, e turma. Cada estudante cadastrado pode emprestar livros disponíveis na biblioteca.

- **Edição de Estudantes**: Habilita a atualização dos dados dos estudantes, permitindo manter as informações sempre precisas e atualizadas.

- **Remoção de Estudantes**: Estudantes podem ser removidos do sistema, contanto que não possuam livros atualmente emprestados.

- **Consulta de Estudantes**: Facilita a visualização de todos os estudantes cadastrados, permitindo aos administradores da biblioteca acessar rapidamente as informações dos alunos.

- **Empréstimo de Livros a Estudantes**: Associa livros disponíveis aos estudantes, registrando o empréstimo no sistema. A aplicação automaticamente verifica a disponibilidade do livro para evitar empréstimos duplicados e garante que apenas livros não alugados sejam emprestados.

- **Devolução de Livros**: Registra a devolução de livros emprestados, atualizando o status do livro para disponível e removendo a associação com o estudante.

Estas funcionalidades adicionais permitem que a biblioteca escolar gerencie não apenas o seu catálogo de livros e autores, mas também mantenha um controle detalhado sobre os empréstimos realizados aos estudantes, assegurando uma gestão eficaz dos recursos disponíveis e promovendo a leitura e o acesso ao conhecimento.


## Premissas Técnicas

- **Base de Dados**: Utilização do H2, um banco de dados em memória, para simplicidade e facilidade de testes.
- **Stack Tecnológica**: Spring MVC para o backend e Angular para o frontend, com foco na criação de uma solução simples e funcional.
- **Comunicação**: Todas as comunicações entre o cliente e o servidor são realizadas via mensagens JSON.
- **Geração de Artefato**: O sistema é empacotado como um WAR para deployment em servidores de aplicação JBoss/Wildfly.
- **Spring Security**: Implementação de segurança utilizando Spring Security para proteger endpoints e gerenciar autenticação e autorização.



## Tecnologias Utilizadas

Este projeto foi desenvolvido utilizando um conjunto de tecnologias modernas e eficientes para garantir a robustez, segurança e facilidade de uso da aplicação. Abaixo estão detalhadas as principais tecnologias adotadas:

- **Java 8**: Escolhido por sua maturidade, estabilidade e ampla adoção na comunidade de desenvolvimento. Java 8 introduz várias melhorias significativas na linguagem, incluindo expressões lambda, que facilitam a escrita de código conciso e legível.

- **Spring Boot**: Framework utilizado para a criação de aplicações Spring com configuração mínima, permitindo um rápido desenvolvimento e implantação de serviços RESTful. Spring Boot facilita a gestão de dependências e a configuração de aplicações, tornando o desenvolvimento mais eficiente.

- **JWT (JSON Web Token)**: Utilizado para a implementação de autenticação e autorização no sistema, permitindo a segurança das comunicações entre o cliente e o servidor. Os tokens JWT fornecem um método compacto e independente para transmitir informações de forma segura entre as partes.

- **Spring Security**: Framework robusto para autenticação e controle de acesso, utilizado para proteger os endpoints da API REST contra acessos não autorizados e garantir que apenas usuários autenticados possam realizar operações sensíveis.

- **Swagger**: Ferramenta adotada para documentação da API REST do projeto. O Swagger facilita tanto o desenvolvimento quanto o uso da API, permitindo a visualização e interação com os endpoints da API de forma simples e intuitiva através de uma interface gráfica.

- **Banco de Dados H2**: Banco de dados em memória escolhido pela sua simplicidade e facilidade de configuração, ideal para desenvolvimento e testes. O H2 permite uma rápida prototipagem e testes sem a necessidade de configuração de um ambiente de banco de dados externo.

- **Wildfly**: Servidor de aplicação Java EE escolhido para o deployment da aplicação. Wildfly é conhecido por sua leveza, modularidade e alto desempenho, tornando-o uma escolha adequada para hospedar aplicações Spring Boot.
