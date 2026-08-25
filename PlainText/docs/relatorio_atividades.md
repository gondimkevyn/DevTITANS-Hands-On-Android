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

## 2.3 - Preferences ViewModel
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

## 🏆 FEITO EXTRA: Sistema de Autenticação Dual (Admin + DB)
**Descrição:** Implementação de um sistema de login dinâmico que permite a entrada tanto via credenciais de administrador (Configurações) quanto via registros de usuários armazenados no banco de dados local (Room).

### Detalhes Técnicos:
1.  **Consulta SQL Customizada (DAO):** Criação de uma `Query` no `PasswordDao` para buscar correspondências exatas de login e senha na tabela de senhas, utilizando `LIMIT 1` para otimização de performance.
2.  **Validação Assíncrona:** Integração de `Coroutines` (`scope.launch`) na tela de login para realizar a verificação no banco de dados sem travar a interface do usuário (UI Thread).
3.  **Hierarquia de Permissões:** O sistema primeiro valida as credenciais contra as preferências globais (Admin) e, caso não haja match, estende a busca para a base de dados de senhas salvas.

### Benefícios:
*   **Escalabilidade:** O aplicativo deixa de ser um gerenciador estático e passa a permitir que novos "usuários" (cadastrados na lista) também acessem o sistema.
*   **Experiência do Usuário:** Maior flexibilidade no acesso, transformando a lista de senhas em um repositório ativo de credenciais de login.

---
*Este documento será atualizado conforme o progresso das próximas atividades.**Este documento será atualizado conforme o progresso das próximas atividades.*
