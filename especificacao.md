# Especificação de requisitos
## PROJETO: Aplicativo para Recomendação de Filmes, Séries e Livros
### Curso de Sistemas de Informação
### Prática Profissional em ADS
### Turma 05K
### Juliano Gomes 31929990
### 1º semestre de 2021

# Introdução
# Escopo do projeto
# Interessados
Usuários do sistema:

* Colaborador: Funcionários das empresas que poderão se cadastrar e navegar pelo site. Poderão ler e fazer avaliações como também estabelecer amizades na rede social.

* Gerente de Serviço:Terá acesso a estátisticas e gráficos sobre os membros da rede social.


# Objetivos Funcionais
1. O sistema deverá permitir que colaboradores criem cadastros no site com os dados: nome completo, username, senha, data de nascimento, cidade e estado.
2. O sistema deverá permitir que colaboradores avaliem livros, filmes e séries com uma nota de 0 a 10 somente inteiros e uma breve descrição(limite de 1024 caracteres). 
3. O sistema deverá permitir que o colaborador sempre possa alterar seus dados do cadastro.
4. O sistema deverá permitir que o colaborador inicie o cadastro de novos filmes, séries e livros.
5. O sistema deverá permitir que o administrador de conteúdo finalize o cadastro de novos filmes, séries e livros, aprovando ou recusando o cadastro. Caso o item ja exista a avalição segue para o item existente.
6. O sistema deverá disponibilizar estátisticas sobre o número médio de amigos dos membros da rede social, lista com os 10 membros mais bem conectados e um gráfico mostrando a relação entre o número de amigos e o estado onde mora.
9. O sistema deverá permitir que sejam realizadas pesquisas sobre filmes, séries, livros e membros do sistema.
10. O sistema deverá permitir que colaboradores podem estabelecer relações de amizades com outros colaboradores.
11. O sistema deverá permitir que colaboradores comentem em avaliações de seus amigos.
13. O sistema deverá permitir que colaboradores deem e retirem "joinhas" nas avaliações de outras pessoas.
14. O sistema deverá permitir que colaboradores alterem sua avaliações já feitas.
15. O sistema deverá permitir que ao cadastrar um filme forneça os dados: título, diretor, elenco principal, país e ano.
16. O sistema deverá permitir que ao cadastrar um livro forneça título, autores, editora, país e ano.
17. O sistema deverá permitir que ao cadastrar uma série forneça título, diretor, elenco principal, país, ano e número de temporadas.
18. 
// avaliação favorita


# Objetivos Não Funcionais
1. O sistema deverá suportar o cadastro de todos os funcionários da empresa.
2. O sistema deverá possuir um página web responsiva.
3. O sistema deverá possuir uma base de dados.
4. O sistema deverá ser implantado em um provedor de internet.
5. O tempo de carga desejável para a página não pode ultrapassar os 5 segundos.
6. A aplicação precisa estar operante o tempo todo.
7. A aplicação deverá possuir um sistema de segurança.
8. O sistema deverá receber novos cadastros de filmes, séries e livros sem necessidade de nova implementação.
9. O sistema deverá permitir que o gerente de serviço tenho acesso a dados sobre os membros, filmes, séries e livros cadastrados.
10. O sistema deverá exibir os filmes, séries e livros atráves de um histórico.
11. O sistema deverá apresentar recomendações de filmes, séries e livros após existerem 10 membros com 10 avaliações.
12. Cada membro deverá ter uma página pessoal com todas suas avalições ja feitas, lista de amigos e o número de "joinhas" que ja recebeu em suas avaliações.
13. A página de cada avaliação deverá ter os dados do colaborador, item , os comentários e o número de "joinhas".
14. O sistema deverá indicar os amigos em comum nas páginas de outros amigos.
15. O sistema deverá sugerir uma lista de 3 membros compátiveis para o colaborador criar amizades toda vez que ele entrar no sistema.
16. Cada filme, série e livro deverá ter uma página lista de todas as avaliações ja realizadas e ordenada pelo número de "joinhas".
