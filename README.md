# Chat-Mediator

Pequeno chat em Java que demonstra o padrão de projeto **Mediator** (GoF).

> *"Definir um objeto que encapsula como um conjunto de objetos interagem.
> Mediator promove acoplamento fraco ao manter objetos que não se referem um ao
> outro explicitamente, permitindo variar sua interação independentemente."* — GoF

Os usuários do chat (`ChatUser`) **não conhecem uns aos outros**: toda
comunicação — broadcast ou privada — passa pela sala (`ChatRoom`), que é o
mediador.

## Estrutura do projeto

```
src/chat/
  ChatMediator.java   # interface Mediator
  ChatRoom.java       # ConcreteMediator: gerencia usuarios e roteia mensagens
  User.java           # Colleague abstrato: guarda referencia ao mediador
  ChatUser.java       # ConcreteColleague: usuario do chat
  Main.java           # demo executavel

test/chat/
  ChatRoomTest.java   # 8 testes (mini framework de assertions, sem JUnit)
```

Mapeamento direto pro diagrama do padrão:

| Papel no padrão (GoF) | Classe                                       |
| --------------------- | -------------------------------------------- |
| `Mediator`            | [`ChatMediator`](src/chat/ChatMediator.java) |
| `ConcreteMediator`    | [`ChatRoom`](src/chat/ChatRoom.java)         |
| `Colleague`           | [`User`](src/chat/User.java)                 |
| `ConcreteColleague`   | [`ChatUser`](src/chat/ChatUser.java)         |

## Como rodar

Requer apenas o JDK (testado com OpenJDK 25).

```powershell
# compilar fontes e testes
javac -d build src\chat\*.java
javac -d build -cp build test\chat\ChatRoomTest.java

# rodar a demo
java -cp build chat.Main

# rodar os testes
java -cp build chat.ChatRoomTest
```

Em bash/zsh, substitua `\` por `/` nos caminhos.

### Saída esperada da demo

```
=== Mensagem em broadcast ===
[Bruno] recebeu de Ana: Oi pessoal!
[Carla] recebeu de Ana: Oi pessoal!

=== Mensagem privada ===
[Ana] recebeu de Bruno: Ana, podemos conversar?

=== Outra mensagem em broadcast ===
[Ana] recebeu de Carla: Bom dia a todos!
[Bruno] recebeu de Carla: Bom dia a todos!
```

## O que os testes verificam

1. **broadcast** entrega a todos *exceto* ao remetente
2. **broadcast** preserva conteúdo e identifica o remetente
3. **mensagem privada** chega só ao destinatário
4. **destinatário inexistente** lança `IllegalArgumentException`
5. **nome duplicado** no registro lança `IllegalArgumentException`
6. **usuário nulo** no registro lança `IllegalArgumentException`
7. `totalUsuarios()` reflete os registros feitos
8. **desacoplamento** — por reflexão, `ChatUser` não tem nenhum campo do tipo
   `User`. Toda comunicação obrigatoriamente passa pelo mediador.

## Prós e contras do padrão (resumo)

**Prós**

- Desacopla os participantes da rede de comunicação.
- Substitui relações *muitos-para-muitos* por *um-para-muitos* contra o mediador.
- Centraliza a política de comunicação — pode mudar sem alterar os colaboradores.

**Contras**

- A centralização pode virar gargalo de performance e ponto único de falha.
