# PlainText - Gerenciador de Senhas (Relatório Final)
**Desenvolvedor:** Kevyn Gondim
---

## 🚀 O que foi construído
O projeto consistiu na criação de um gerenciador de senhas moderno usando **Jetpack Compose**, **Hilt** (Injeção de Dependência) e **Room** (Banco de Dados). O foco foi garantir que a aplicação não fosse apenas um conjunto de telas estáticas, mas um sistema que segue os padrões de mercado (MVVM).

### Principais Marcos:
1.  **Arquitetura MVVM:** Separei totalmente a lógica de visualização (Telas) da lógica de dados (ViewModels e Repositories).
2.  **Autenticação Dual:** Implementei um sistema onde o acesso é permitido tanto pela senha mestre do Administrador quanto por qualquer conta de usuário salva na lista de senhas do banco.
3.  **Persistência com Room:** Configurei a base de dados SQLite para salvar as credenciais de forma permanente, incluindo suporte a migrações de banco de dados (Versão 2).
4.  **Navegação Type-Safe:** Toda a troca de telas entre Login, Lista, Edição e Configurações foi feita usando a nova API de navegação do Compose 2.8, garantindo que não existam erros de digitação nas rotas.

---

## 🛠️ Passo a Passo do Desenvolvimento

### 1. Configuração e Correção do Gradle
No início, tive problemas com dependências e plugins que não estavam sincronizando. Precisei ajustar o `build.gradle.kts` da raiz e do módulo `app` para incluir corretamente o Hilt e o KSP. Foi uma etapa chata, mas essencial para o projeto rodar.

### 2. Tela de Login e Segurança (Módulo 2 e 4)
Criei a interface de login focando no design limpo. A maior mudança aqui foi trocar o estado local (`remember`) por um `LoginViewModel`. 
*   **A dificuldade:** Fazer o botão "Enviar" validar dois tipos de login diferentes ao mesmo tempo. Resolvi isso usando `Coroutines` para buscar no banco de dados sem travar a tela se o login de admin falhasse.

### 3. Configurações e Preferências (Módulo 3)
Implementei a tela onde o usuário define a senha mestre. Aqui aprendi a usar o `Switch` do Material 3 e como manter esses dados vivos na memória do App enquanto ele está aberto, integrando isso diretamente na validação do Login.

### 4. Room (Módulo 6)
Defini a tabela `passwords`. Tive um problema de *crash* no meio do caminho porque mudei os campos da tabela e o banco antigo "quebrou". 
*   **A solução:** Atualizei a versão do banco para 2 e ativei o `fallbackToDestructiveMigration`. Isso limpou o banco antigo e criou a estrutura nova corretamente.

### 5. Lista e Edição Dinâmica (Módulo 5 e 7)
Usei `LazyColumn` na tela de listagem para garantir performance. A navegação para a tela de edição foi um desafio à parte, pois precisei passar um objeto inteiro (`PasswordInfo`) entre as telas. Configurei o `Parcelable` para que os dados chegassem intactos na tela de edição.

---

## 🧠 Desafios e Aprendizados

*   **Sincronização da IDE:** No começo, a IDE mostrava muitos erros "fantasmagóricos" (sublinhados em vermelho) mesmo o código estando certo. Aprendi que às vezes o `Invalidate Caches` ou um simples `Rebuild` resolvem problemas que parecem ser de lógica.
*   **Ciclo de Vida:** Entender que o ViewModel protege os dados quando o celular vira (rotação) foi um grande aprendizado prático.
*   **Injeção de Dependência:** O Hilt no começo parece mágico demais, mas depois de configurar o `DataDiModule`, entendi como ele facilita a vida para não termos que criar instâncias de banco de dados manualmente em toda tela.

---

## 📁 Estrutura da Documentação
Todos os detalhes técnicos, linhas de código alteradas e os requisitos específicos de cada atividade (2.1 até 7.1) estão detalhados no arquivo auxiliar:
📄 **[relatorio_atividades.md](file:///home/kevyn/DevTITANS-Hands-On-Android/PlainText/docs/relatorio_atividades.md)**

---
*Este projeto marca a conclusão do ciclo de treinamento em desenvolvimento nativo Android com Jetpack Compose.*
