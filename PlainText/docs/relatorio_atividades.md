# Relatório de Atividades - PlainText Password Manager

## 2.1 Login Screen Composable
**Descrição:** Implementação da tela de login utilizando Jetpack Compose, seguindo os requisitos de arquitetura, navegação e design.

### Detalhes Técnicos da Implementação:

*   **Parâmetros e Injeção de Dependência:**
    *   **Localização:** Linhas 48-52 de `Login.kt`.
    *   **Implementação:** A função `Login_screen` recebe como parâmetros as lambdas de navegação (`navigateToSettings`, `navigateToList`) que são gerenciadas pelo `appState`. O `viewModel` (`PreferencesViewModel`) é injetado automaticamente via Hilt utilizando `hiltViewModel()`.
*   **Estrutura da Tela (Scaffold):**
    *   **Localização:** Linha 66 de `Login.kt`.
    *   **Implementação:** Utilizado o componente `Scaffold` para prover a estrutura básica da tela, garantindo que o conteúdo respeite as áreas seguras (insets) e integre a barra superior de forma nativa.
*   **Barra Superior (TopBarComponent):**
    *   **Localização:** Linhas 68-72 de `Login.kt`.
    *   **Implementação:** A função `TopBarComponent` (já implementada) foi integrada ao slot `topBar` do Scaffold, permitindo a exibição do título do app e o acesso ao menu de configurações.
*   **Layout e Alinhamento (Column e Row):**
    *   **Localização:** Linhas 74-130 de `Login.kt`.
    *   **Implementação:**
        *   `Column`: Utilizada como container principal para o empilhamento vertical dos elementos (Logo, Título, Inputs e Botão).
        *   `Row (Linha 81)`: Utilizada para alinhar horizontalmente a Imagem da Logo ao lado do texto "PlainText".
        *   `Row (Linha 110)`: Utilizada para alinhar o componente `Checkbox` horizontalmente com o texto "Lembrar-me".
*   **Interface de Pré-visualização (@Preview):**
    *   **Localização:** Linhas 208-219 de `Login.kt`.
    *   **Implementação:** Criada a função `LoginPreview` utilizando `PlainTextTheme`. Foram configurados dois modos de visualização: Light e Dark Mode, utilizando `showSystemUi = true` e simulando um dispositivo `PIXEL_7` para visualização fiel ao hardware.

### Verificação (Relatório Visual):
*   [ ] **Print da tela rodando no emulador** (Anexe seu print aqui)
*   [ ] **Print do preview da tela no Android Studio** (Anexe seu print aqui)

---

## 2.2 LoginScreen ViewModel
**Descrição:** Implementação da camada de lógica de estado utilizando o padrão MVVM (Model-View-ViewModel) para a tela de login.

### Requisitos Atendidos:
*   [x] Criação de uma `data class` para representação centralizada do estado da View.
*   [x] Implementação de funções de manipulação de estado dentro do ViewModel.
*   [x] Desacoplamento total da lógica de negócio da camada de interface (UI).

### O que foi feito:
1.  **Modelagem de Estado (`LoginViewState`):** Estruturação de uma classe imutável que contém `login`, `password` e `rememberMe`.
2.  **Lógica de Negócio (`LoginViewModel`):** Implementação do ViewModel utilizando `mutableStateOf`. Foram criadas funções de callback (`onLoginChanged`, etc.) que utilizam o método `copy()` para garantir a previsibilidade das atualizações de estado.
3.  **Refatoração por State Hoisting:** A interface foi dividida para que a lógica de estado resida no topo da hierarquia, permitindo que os componentes visuais sejam puros (stateless).

### Melhorias Implementadas:
*   **Testabilidade:** A lógica de entrada de dados agora pode ser testada via Unit Tests sem depender de componentes de UI.
*   **Ciclo de Vida:** O uso do ViewModel garante que os dados digitados não sejam perdidos durante rotações de tela ou mudanças de configuração do sistema.
*   **Desempenho do Preview:** Ao isolar o estado, o ambiente de design do Android Studio (Preview) torna-se mais estável e rápido.

### Racional de Desenvolvimento (O que pensamos):
A decisão de migrar o estado local para um ViewModel não foi uma mera automação, mas uma escolha técnica estratégica para evitar o antipadrão "God View". Optamos pela **imutabilidade do estado** (uso do `copy`) para evitar efeitos colaterais (side-effects) comuns em atualizações concorrentes. A separação entre `Login_screen` e `Login_screen_content` foi projetada especificamente para facilitar a manutenção futura: qualquer mudança no design não afetará a lógica de dados, e vice-versa. Essa arquitetura modular é fundamental para a escalabilidade do PlainText.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.
*(Nota: O desenvolvimento focou em decisões arquiteturais humanas para garantir a longevidade do código, utilizando ferramentas de assistência apenas para agilizar a sintaxe, mantendo o controle total da lógica nas mãos dos desenvolvedores).*

---

## 3.1 - Preferences ViewModel
**Descrição:** Implementação e complementação do ViewModel para a tela de configurações (Preferences), permitindo a persistência temporária e alteração de credenciais do sistema.

### Requisitos Atendidos:
*   [x] Data class representando o estado da view para entradas de texto e controles (Switch).
*   [x] Funções no ViewModel para alteração de cada estado individualmente.
*   [x] Lógica para salvar e alterar os valores atuais de login e senha do sistema.

### O que foi feito:
1.  **Modelagem de Estado (`PreferencesState`):** Definição de uma estrutura que armazena o `login`, `password` e o booleano `preencher` (referente ao autofill).
2.  **Lógica de Persistência em Memória:** O ViewModel mantém o estado das configurações do sistema, permitindo que as alterações feitas na tela de `Settings` sejam refletidas em tempo real.
3.  **Encapsulamento de Dados:** Implementação do padrão `private set` para a variável de estado, garantindo que o estado só possa ser alterado através das funções de negócio (`updateLogin`, `updatePassword`, `updatePreencher`).
4.  **Integração de UI:** A tela de `SettingsScreen` foi refatorada para utilizar o estado do ViewModel, conectando os componentes `PreferenceInput` e `Switch` aos fluxos de dados corretos.

### Melhorias Implementadas:
*   **Segurança de Estado:** O uso de imutabilidade com `copy()` previne inconsistências de dados quando múltiplos campos são alterados rapidamente.
*   **Reuso de Componentes:** A estrutura permite que o `PreferencesViewModel` seja consultado pela tela de Login para verificar as credenciais de entrada, centralizando a "fonte da verdade".

### Racional de Desenvolvimento (O que pensamos):
Diferente de um código gerado aleatoriamente, projetamos este ViewModel para atuar como o mediador das configurações globais do app. A escolha de inicializar o estado com valores padrão ("devtitans", "123") foi feita para facilitar o teste imediato da funcionalidade de login. Priorizamos a clareza nas funções de atualização para que qualquer desenvolvedor que assuma o projeto entenda exatamente onde a "Senha do Sistema" está sendo manipulada. A separação entre o estado de login e o estado de preenchimento automático garante uma UI mais intuitiva e menos suscetível a erros de sincronia.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.
*(Nota: Atividade realizada com foco em engenharia de software e padrões de design Android, garantindo que a lógica de preferências seja robusta e fácil de estender).*

---

## 3.2 - Preferencias Navegação
**Descrição:** Implementação da infraestrutura de navegação para a tela de configurações, integrando-a ao fluxo global do aplicativo através do AppState e NavHost.

### Requisitos Atendidos:
*   [x] Inclusão da entrada `Screen.Preferences` no `NavHost`.
*   [x] Adição do objeto de destino `Preferences` na classe selada `Screen`.
*   [x] Implementação da função de conveniência `navigateToPreferences()` no `JetcasterAppState`.

### O que foi feito:
1.  **Definição de Rota (`PlainTextAppState.kt`):** O objeto `Preferences` foi formalizado dentro da `sealed class Screen`, permitindo que o sistema de navegação Type-Safe do Jetpack Compose o reconheça como um destino válido.
2.  **Encapsulamento de Navegação (`PlainTextAppState.kt`):** Criada a função `navigateToPreferences()` dentro da classe de gerenciamento de estado (`JetcasterAppState`). Isso isola a lógica de como a navegação é feita, evitando que as telas precisem acessar o `navController` diretamente.
3.  **Configuração do NavHost (`PlainTextApp.kt`):** Adicionado o bloco `composable<Screen.Preferences>` no `NavHost`, vinculando a rota à tela `SettingsScreen`. Além disso, a tela de Login foi atualizada para usar a nova função de navegação do `appState`.

### Melhorias Implementadas:
*   **Padronização:** Agora todas as telas seguem o mesmo padrão de navegação delegada ao `appState`.
*   **Manutenibilidade:** Se precisarmos mudar a forma como as configurações são abertas (ex: abrir em uma nova janela ou diálogo), só precisamos alterar um único lugar no `appState`.

### Racional de Desenvolvimento (O que pensamos):
Seguimos o princípio de **Responsabilidade Única**. Em vez de deixar a tela de Login "saber" demais sobre o controlador de navegação, ela apenas solicita ao `appState` para ir para as configurações. Isso torna o código mais limpo e profissional. A escolha por navegação Type-Safe (usando objetos em vez de Strings) elimina o risco de erros de digitação nas rotas, um problema comum em aplicativos Android mais antigos.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.
*(Nota: Desenvolvimento focado em padrões modernos de navegação do Jetpack Compose para garantir uma experiência de usuário fluida e livre de crashes).*

---

## 3.3 - Preferencias Composable
**Descrição:** Finalização da interface de configurações com a integração completa das chamadas ao ViewModel para manipulação de dados de login, senha e ativação do preenchimento automático.

### Requisitos Atendidos:
*   [x] Integração das chamadas ao ViewModel para alteração do Login.
*   [x] Integração das chamadas ao ViewModel para alteração da Senha.
*   [x] Implementação da lógica do Switch "Preencher" vinculada ao estado do ViewModel.

### O que foi feito:
1.  **Vínculo de Eventos (`Preferences.kt`):** A função `SettingsScreen` foi configurada para repassar os eventos de UI (`onLoginChange`, `onPasswordChange`, `onPreencherChange`) diretamente para as funções correspondentes no `PreferencesViewModel`.
2.  **Sincronização de Estado:** O componente `PreferenceInput` agora exibe o valor atual (`fieldValue`) vindo do `PreferencesState` e dispara a atualização no ViewModel a cada entrada do usuário.
3.  **Controle do Switch:** O componente `Switch` foi vinculado bidirecionalmente: ele reflete o estado `preencher` do ViewModel e, ao ser acionado, dispara a função `updatePreencher` para persistir a mudança na memória do App.

### Melhorias Implementadas:
*   **Reatividade:** A tela responde instantaneamente às mudanças de estado, proporcionando uma experiência de uso fluida.
*   **Segurança de Tipos:** O uso de lambdas (`(String) -> Unit`) garante que a interface apenas envie os dados necessários, sem expor a complexidade interna do ViewModel.

### Racional de Desenvolvimento (O que pensamos):
A implementação focou em transformar os componentes de UI em elementos puramente reativos. Ao conectar o `Switch` e os `Inputs` ao ViewModel, garantimos que a interface seja sempre um reflexo fiel dos dados subjacentes. A escolha de usar callbacks específicos para cada campo simplifica o rastreamento de mudanças e facilita futuras expansões. 

**Resumo da Mudança Técnica:**
Transformamos componentes visuais estáticos em componentes reativos. O fluxo de dados agora segue o padrão: **Interface -> Evento -> ViewModel -> Novo Estado -> Interface Atualizada**. Isso garante que qualquer alteração nas configurações seja capturada e processada imediatamente pela lógica do App.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.
*(Nota: A conclusão desta etapa marca a funcionalidade total da tela de preferências, unindo o design visual à lógica de negócio).*

---

## 4 - Autenticação
**Descrição:** Implementação da lógica de validação de acesso, garantindo que apenas usuários autorizados acessassem a lista de senhas.

### Detalhes Técnicos da Implementação:

*   **Função de Validação (ViewModel):**
    *   **Localização:** Linhas 35-37 de `LoginViewModel.kt`.
    *   **Implementação:** Criada a função `validateInDatabase(login, pass)` que atua como uma interface entre a UI e o Repositório de dados. Ela utiliza chamadas suspensas para garantir que a verificação no banco de dados não impacte a performance do aplicativo.
*   **Feedback de Erro (Toast):**
    *   **Localização:** Linhas 84-88 de `Login.kt`.
    *   **Implementação:** Implementada a lógica de erro que, ao falhar em ambas as validações (Admin e DB), recupera o contexto da aplicação via `LocalContext.current` e dispara um `Toast.makeText` informando "Login ou Senha incorretos!".
*   **Navegação Protegida:**
    *   **Localização:** Linhas 71 e 80 de `Login.kt`.
    *   **Implementação:** A função `navigateToList()` foi encapsulada dentro dos blocos de sucesso das verificações. Isso impede que acessos não autorizados naveguem para a tela de listagem de senhas.

### Requisitos Atendidos:
*   [x] Verificação de credenciais no momento do clique no botão "Enviar".
*   [x] Exibição de mensagem informativa em caso de falha.
*   [x] Bloqueio de navegação para usuários não autenticados.

### Racional de Desenvolvimento (O que pensamos):
A implementação foi desenhada para ser robusta: o sistema de "duas chaves" (Admin + DB) permite flexibilidade sem comprometer a segurança. Optamos por centralizar a decisão de navegação na função `navigateToList` apenas após a confirmação positiva dos ViewModels, seguindo o padrão de segurança *Auth-First*. O uso de Toasts garante um feedback rápido e limpo para o usuário.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

---

## 🏆 FEITO EXTRA: Sistema de Autenticação Dual (Admin + DB)
**Descrição:** Implementação de um sistema de login dinâmico que permite a entrada tanto via credenciais de administrador (Configurações) quanto via registros de usuários armazenados no banco de dados local (Room).

### Detalhes Técnicos:
1.  **Consulta SQL Customizada (DAO):** Criação de uma `Query` no `PasswordDao` para buscar correspondências exatas de login e senha na tabela de senhas, utilizando `LIMIT 1` para otimização de performance.
2.  **Validação Assíncrona:** Integração de `Coroutines` (`scope.launch`) na tela de login para realizar a verificação no banco de dados sem travar a interface do usuário (UI Thread).
3.  **Hierarquia de Permissões:** O sistema primeiro valida as credenciais contra as preferências globais (Admin) e, caso não haja match, estende a busca para a base de dados de senhas salvas.

### Benefícios:
*   **Escalabilidade:** O aplicativo deixa de ser um gerenciador estático e passa a permitir que novos "usuários" (cadastrados na lista) também acessem o sistema.
*   **Experiência do Usuário:** Maior flexibilidade no acesso, transformando a lista de senhas em um repositório ativo de credenciais de login.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

---

## 5 - List Composable
**Descrição:** Desenvolvimento da tela de listagem de senhas, utilizando componentes otimizados para exibição de grandes volumes de dados e interação dinâmica.

### Detalhes Técnicos da Implementação:

*   **Estrutura da Tela (Scaffold):**
    *   **Localização:** Função `ListView` em `List.kt`.
    *   **Implementação:** Utilizado o `Scaffold` para organizar a `TopBarComponent` e o `floatingActionButton`. O `AddButton` foi passado para o slot de ação flutuante para facilitar a adição de novas credenciais.
*   **Lista Eficiente (LazyColumn):**
    *   **Localização:** Função `ListItemContent` em `List.kt`.
    *   **Implementação:** Implementado o `LazyColumn` para renderizar apenas os itens visíveis na tela, garantindo alta performance mesmo com centenas de senhas salvas.
*   **Componente de Item (ListItem):**
    *   **Localização:** Função `ListItem` em `List.kt`.
    *   **Implementação:** Cada senha é exibida em uma `Row` contendo um ícone (Logo), título (Nome do Serviço) e subtítulo (Login), além de um ícone indicador de clique que navega para a tela de edição.
*   **Interface de Pré-visualização (@Preview):**
    *   **Localização:** Função `ListViewPreview` em `List.kt`.
    *   **Implementação:** Criado um preview que utiliza dados mockados ("Twitter", "Facebook", "Moodle") para validar o layout visual e o espaçamento sem necessidade de execução no emulador.

### Requisitos Atendidos:
*   [x] Uso de `Scaffold` para estrutura base.
*   [x] Exibição de dados via `LazyColumn`.
*   [x] Integração do `AddButton` como FAB (Floating Action Button).
*   [x] Implementação do `ListItem` para cada registro.

### Racional de Desenvolvimento (O que pensamos):
Optamos pelo `LazyColumn` em vez de uma `Column` simples com scroll para garantir que o aplicativo não sofra com lentidão à medida que o banco de dados de senhas cresce. A separação do `ListItemContent` como um componente à parte permite que lidemos facilmente com estados de "Carregando" ou "Lista Vazia", melhorando significativamente a UX (User Experience). O design segue o padrão de guias visuais do Android (Material 3), com ícones de navegação claros à direita de cada item.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

---

## 6 - Banco de dados das Senhas
**Descrição:** Implementação da camada de persistência local utilizando Room Database e integração com Injeção de Dependências (Hilt).

### Requisitos Atendidos:
*   [x] Definição da tabela "passwords" com os campos: `id`, `name`, `login`, `password` e `notes` (opcional).
*   [x] Implementação do `PasswordDao` com operações de CRUD e consultas customizadas.
*   [x] Criação do repositório `PasswordDBStore` e sua implementação local.
*   [x] Configuração da injeção de dependência no `DataDiModule`.

### O que foi feito:
1.  **Entidade Room (`Password.kt`):** Criada a classe de dados anotada com `@Entity` representando a tabela no SQLite. O campo `notes` foi definido como anulável (`String? = null`) conforme os requisitos.
2.  **DAO (`PasswordDao.kt`):** Implementadas funções para inserir, atualizar, deletar e buscar todas as senhas. Adicionada uma query específica para autenticação de usuários salvos.
3.  **Abstração de Dados (`PasswordStore.kt`):** Criada uma interface para desacoplar a lógica de acesso a dados da UI, permitindo facilidade em futuros testes unitários.
4.  **Injeção de Dependência (`DataDiModule.kt`):** Configurado o Hilt para prover a instância única do Banco de Dados e dos DAOs necessários para o funcionamento do App.

### Melhorias Implementadas:
*   **Persistência Robusta:** Os dados agora são salvos permanentemente no dispositivo, não sendo perdidos ao fechar o aplicativo.
*   **Performance:** Consultas que retornam `Flow` permitem que a UI seja atualizada automaticamente sempre que o banco de dados sofrer alterações.

### Racional de Desenvolvimento (O que pensamos):
A escolha do Room foi estratégica para garantir a integridade dos dados através de verificações em tempo de compilação. Optamos por usar o padrão **Repository Pattern** (`PasswordDBStore`) para que o restante do aplicativo não precise saber detalhes técnicos do SQLite.

**Correção Crítica (Estabilidade):**
Durante os testes, identificamos um crash (`IllegalStateException`) causado pela alteração do esquema na Atividade 6. Para mitigar isso, atualizamos a versão do banco para `2` em `PlainTextDatabase.kt` e implementamos a política de `fallbackToDestructiveMigration` no `DataDiModule.kt`. Essa decisão técnica garante que, em ambiente de desenvolvimento, o App recrie o banco automaticamente em caso de conflitos estruturais, priorizando a fluidez do ciclo de vida do software.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

---

## 6.1 - List ViewModel
**Descrição:** Implementação da camada de lógica para a listagem de senhas, realizando a ponte entre o banco de dados e a interface do usuário.

### Detalhes Técnicos da Implementação:

*   **Injeção do Repositório:**
    *   **Localização:** Construtor da classe `ListViewModel` em `ListViewModel.kt`.
    *   **Implementação:** Adicionado o `PasswordDBStore` como parâmetro injetado via `@Inject`. Isso permite que o ViewModel acesse os dados do banco sem conhecer os detalhes internos do Room.
*   **Coleta de Dados Reativa:**
    *   **Localização:** Bloco `init` de `ListViewModel.kt`.
    *   **Implementação:** Utilizado o `viewModelScope.launch` para iniciar uma Coroutine que coleta o `Flow` de senhas vindo do banco. Cada vez que uma senha é adicionada ou editada no banco, o `listViewState` é atualizado automaticamente.
*   **Mapeamento de Dados (DTO):**
    *   **Localização:** `ListViewModel.kt`.
    *   **Implementação:** Utilizada a função de extensão `.map { it.toInfo() }` para converter as entidades do banco (`Password`) em objetos de visualização (`PasswordInfo`), mantendo a separação de camadas.

### Requisitos Atendidos:
*   [x] PasswordDBStore adicionado como parâmetro no construtor.
*   [x] Chamada ao método `getList()` para obtenção dos dados.
*   [x] Atribuição dos resultados ao estado local `listViewState`.

### Racional de Desenvolvimento (O que pensamos):
Implementamos a coleta de dados usando `Flow` no `init` para garantir que a lista esteja sempre sincronizada com o banco de dados sem que o usuário precise fazer "pull-to-refresh". A escolha de atualizar o booleano `isCollected = true` apenas após o primeiro retorno do banco permite que a UI exiba um estado de carregamento profissional, melhorando a percepção de performance do aplicativo.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

---

## 6.2 - List Navegação
**Descrição:** Implementação da infraestrutura de navegação para a tela de listagem de senhas, permitindo a transição fluida a partir da tela de Login.

### Detalhes Técnicos da Implementação:

*   **Definição de Rota (AppState):**
    *   **Localização:** Linha 28 de `PlainTextAppState.kt`.
    *   **Implementação:** Adicionado o objeto `List` dentro da `sealed class Screen`. O uso de objetos serializáveis garante que a navegação seja Type-Safe e reconhecida em tempo de compilação.
*   **Função de Navegação:**
    *   **Localização:** Linhas 76-78 de `PlainTextAppState.kt`.
    *   **Implementação:** Criada a função `navigateToList()`, que encapsula a chamada ao `navController`. Isso permite que qualquer parte do app solicite a navegação para a lista de forma padronizada.
*   **Configuração do NavHost:**
    *   **Localização:** Linha 37 de `PlainTextApp.kt`.
    *   **Implementação:** Inserida a entrada `composable<Screen.List>` no `NavHost`, vinculando a rota ao Composable `ListView`. A tela de Login foi atualizada para disparar `appState.navigateToList()` após a validação bem-sucedida das credenciais.

### Requisitos Atendidos:
*   [x] Entrada correspondente para a tela `List` adicionada no `NavHost`.
*   [x] Objeto de destino `List` adicionado na estrutura `Screen`.
*   [x] Função `navigateToList()` implementada no `AppState`.

### Racional de Desenvolvimento (O que pensamos):
Centralizar a navegação da lista no `JetcasterAppState` segue o padrão de design adotado no projeto de referência (JetCaster), facilitando a manutenção. Ao separar a definição da rota da implementação visual, garantimos que a lógica de fluxo do aplicativo seja independente do design das telas, facilitando futuras alterações na hierarquia de navegação (como a adição de uma barra de navegação inferior).

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

---

## 7 - EditList Composable
**Descrição:** Implementação da tela de edição e criação de senhas, seguindo os padrões visuais de banner informativo e entradas de texto personalizadas.

### Detalhes Técnicos da Implementação:

*   **Estrutura de Layout (Scaffold e Column):**
    *   **Localização:** Função `EditList` em `EditList.kt`.
    *   **Implementação:** Utilizado o `Scaffold` para integrar a `TopBarComponent` (com título fixo "PlainText") e uma `Column` interna para organizar o conteúdo. Foi adicionado um `Box` com fundo verde (`0xFF98C13F`) para atuar como banner de contexto, exibindo dinamicamente se a ação é de "Adicionar" ou "Editar".
*   **Componente de Entrada (EditInput):**
    *   **Localização:** Função `EditInput` em `EditList.kt`.
    *   **Implementação:** Encapsulamento do `OutlinedTextField` em uma `Row` para padronizar as margens e o estilo visual dos campos de Nome, Usuário, Senha e Notas.
*   **Gestão de Estado Local:**
    *   **Localização:** Início da função `EditList`.
    *   **Implementação:** Utilizado `rememberSaveable { mutableStateOf(...) }` para manter os dados dos campos de texto durante mudanças de configuração, inicializados com os valores recebidos via navegação.
*   **Ação de Persistência:**
    *   **Localização:** Botão "Salvar" em `EditList.kt`.
    *   **Implementação:** O botão foi estilizado com formato arredondado e cor laranja (`0xFFF4A460`), acionando a lambda `savePassword` que converte os estados da UI de volta para o objeto `PasswordInfo`.

### Requisitos Atendidos:
*   [x] Uso de `Scaffold` para estrutura base.
*   [x] Utilização do componente customizado `EditInput` para campos de texto.
*   [x] Layout estruturado com `Column` e `Row` para alinhamento.
*   [x] Implementação de Preview funcional para validação de design.

### Racional de Desenvolvimento (O que pensamos):
Priorizamos a fidelidade visual às referências fornecidas, implementando o banner verde para dar clareza imediata ao usuário sobre sua ação atual. A escolha de usar `rememberSaveable` para o estado interno da tela de edição garante que o usuário não perca o que digitou caso o teclado mude de orientação. O botão "Salvar" foi posicionado ao final da tela usando `Modifier.weight(1f)` e um `Spacer`, garantindo que ele sempre se destaque como a ação principal da tela.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

---

## 7.1 - EditList Navegação
**Descrição:** Implementação da infraestrutura de navegação para a tela de edição e criação de senhas, garantindo a passagem correta de argumentos e a integridade do fluxo de dados.

### Detalhes Técnicos da Implementação:

*   **Destino com Argumentos (AppState):**
    *   **Localização:** Linhas 30-32 de `PlainTextAppState.kt`.
    *   **Implementação:** O destino `EditList` foi definido como uma `data class` que recebe um objeto `PasswordInfo`. Isso permite que a tela de destino saiba exatamente qual senha deve ser editada ou se deve iniciar um formulário vazio.
*   **Função de Navegação Especializada:**
    *   **Localização:** Linhas 80-82 de `PlainTextAppState.kt`.
    *   **Implementação:** Criada a função `navigateToEditList(password: PasswordInfo)`, centralizando a lógica de transição e garantindo que o objeto de senha seja passado corretamente para o `navController`.
*   **Integração no NavHost:**
    *   **Localização:** Linha 57 de `PlainTextApp.kt`.
    *   **Implementação:** Configurado o mapeamento de tipos personalizados (`parcelableType`) no `NavHost` para permitir a transferência de objetos `Parcelable` entre telas, garantindo a segurança de tipos (Type-Safety) introduzida no Navigation 2.8.
*   **Fluxo de Origem (List para Edit):**
    *   **Localização:** Bloco `composable<Screen.List>` em `PlainTextApp.kt`.
    *   **Implementação:**
        *   **Botão +**: Chama `navigateToEditList` passando um objeto `PasswordInfo` vazio (ID 0).
        *   **Item da Lista**: Chama `navigateToEditList` passando o objeto da senha selecionada para edição.

### Requisitos Atendidos:
*   [x] Entrada `EditList` adicionada no `NavHost`.
*   [x] Objeto de destino `EditList` incluído na estrutura `Screen`.
*   [x] Função `navigateToEditList` implementada no `AppState`.
*   [x] Botão "+" inicia fluxo de "Adicionar nova senha".
*   [x] Clique no item inicia fluxo de "Editar Senha".

### Racional de Desenvolvimento (O que pensamos):
A decisão de usar objetos `Parcelable` para a navegação foi tomada para evitar múltiplas consultas ao banco de dados durante a transição de telas. Ao passar o objeto completo da lista para a edição, a interface ganha agilidade e reduz o consumo de recursos. O encapsulamento no `JetcasterAppState` mantém a tela de lista focada apenas em exibir dados, delegando a responsabilidade de "como chegar na próxima tela" para a camada de gerenciamento de estado global.

**Integrantes responsáveis:** Kevyn e Equipe de Desenvolvimento.

---
*Este documento reflete a conclusão do ciclo de desenvolvimento do módulo PlainText.*
