package chat;

import java.util.List;

public class ChatRoomTest {

    private static int testesExecutados = 0;
    private static int testesFalhos = 0;

    public static void main(String[] args) {
        executar("broadcast envia para todos exceto remetente",
            ChatRoomTest::testBroadcastNaoEnviaParaRemetente);
        executar("broadcast entrega a mensagem correta",
            ChatRoomTest::testBroadcastConteudoCorreto);
        executar("mensagem privada vai apenas para o destinatario",
            ChatRoomTest::testMensagemPrivada);
        executar("mensagem privada para destinatario inexistente lanca excecao",
            ChatRoomTest::testPrivadaDestinatarioInexistente);
        executar("registro de nome duplicado lanca excecao",
            ChatRoomTest::testRegistroDuplicado);
        executar("registro de usuario nulo lanca excecao",
            ChatRoomTest::testRegistroNulo);
        executar("totalUsuarios reflete registros",
            ChatRoomTest::testTotalUsuarios);
        executar("colaboradores nao se conhecem diretamente (acoplamento via mediador)",
            ChatRoomTest::testDesacoplamento);

        System.out.println();
        System.out.println("Resultado: " + (testesExecutados - testesFalhos)
            + "/" + testesExecutados + " passaram");
        if (testesFalhos > 0) {
            System.exit(1);
        }
    }

    static void testBroadcastNaoEnviaParaRemetente() {
        ChatRoom sala = new ChatRoom("Teste");
        ChatUser a = new ChatUser(sala, "A");
        ChatUser b = new ChatUser(sala, "B");
        ChatUser c = new ChatUser(sala, "C");
        sala.registrarUsuario(a);
        sala.registrarUsuario(b);
        sala.registrarUsuario(c);

        a.enviar("oi");

        assertEquals(0, a.getCaixaDeEntrada().size(), "remetente nao deve receber");
        assertEquals(1, b.getCaixaDeEntrada().size(), "B deve receber 1");
        assertEquals(1, c.getCaixaDeEntrada().size(), "C deve receber 1");
    }

    static void testBroadcastConteudoCorreto() {
        ChatRoom sala = new ChatRoom("Teste");
        ChatUser a = new ChatUser(sala, "A");
        ChatUser b = new ChatUser(sala, "B");
        sala.registrarUsuario(a);
        sala.registrarUsuario(b);

        a.enviar("ola mundo");

        List<String> caixa = b.getCaixaDeEntrada();
        assertTrue(caixa.get(0).contains("ola mundo"), "deve conter o texto da mensagem");
        assertTrue(caixa.get(0).contains("A"), "deve identificar o remetente");
    }

    static void testMensagemPrivada() {
        ChatRoom sala = new ChatRoom("Teste");
        ChatUser a = new ChatUser(sala, "A");
        ChatUser b = new ChatUser(sala, "B");
        ChatUser c = new ChatUser(sala, "C");
        sala.registrarUsuario(a);
        sala.registrarUsuario(b);
        sala.registrarUsuario(c);

        a.enviarPrivado("segredo", "B");

        assertEquals(0, a.getCaixaDeEntrada().size(), "remetente nao recebe");
        assertEquals(1, b.getCaixaDeEntrada().size(), "destinatario recebe");
        assertEquals(0, c.getCaixaDeEntrada().size(), "terceiros nao recebem");
        assertTrue(b.getCaixaDeEntrada().get(0).contains("segredo"),
            "conteudo deve estar correto");
    }

    static void testPrivadaDestinatarioInexistente() {
        ChatRoom sala = new ChatRoom("Teste");
        ChatUser a = new ChatUser(sala, "A");
        sala.registrarUsuario(a);

        try {
            a.enviarPrivado("oi", "Fantasma");
            fail("deveria ter lancado excecao");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Fantasma"), "mensagem deve citar destinatario");
        }
    }

    static void testRegistroDuplicado() {
        ChatRoom sala = new ChatRoom("Teste");
        sala.registrarUsuario(new ChatUser(sala, "A"));
        try {
            sala.registrarUsuario(new ChatUser(sala, "A"));
            fail("nome duplicado deveria falhar");
        } catch (IllegalArgumentException e) {
            // esperado
        }
    }

    static void testRegistroNulo() {
        ChatRoom sala = new ChatRoom("Teste");
        try {
            sala.registrarUsuario(null);
            fail("nulo deveria falhar");
        } catch (IllegalArgumentException e) {
            // esperado
        }
    }

    static void testTotalUsuarios() {
        ChatRoom sala = new ChatRoom("Teste");
        assertEquals(0, sala.totalUsuarios(), "comeca vazia");
        sala.registrarUsuario(new ChatUser(sala, "A"));
        sala.registrarUsuario(new ChatUser(sala, "B"));
        assertEquals(2, sala.totalUsuarios(), "dois apos registros");
    }

    static void testDesacoplamento() {
        // ChatUser nao tem nenhum campo/metodo que referencie outro ChatUser:
        // toda comunicacao passa pelo mediador. Verificamos isso por reflexao.
        for (var f : ChatUser.class.getDeclaredFields()) {
            assertTrue(!User.class.isAssignableFrom(f.getType()),
                "ChatUser nao deve ter referencia direta a outro User: " + f.getName());
        }
    }

    // ---------- mini framework ----------

    private static void executar(String nome, Runnable teste) {
        testesExecutados++;
        try {
            teste.run();
            System.out.println("[OK]   " + nome);
        } catch (AssertionError | Exception e) {
            testesFalhos++;
            System.out.println("[FAIL] " + nome + " -> " + e.getMessage());
        }
    }

    private static void assertEquals(int esperado, int atual, String msg) {
        if (esperado != atual) {
            throw new AssertionError(msg + " (esperado=" + esperado + ", atual=" + atual + ")");
        }
    }

    private static void assertTrue(boolean condicao, String msg) {
        if (!condicao) {
            throw new AssertionError(msg);
        }
    }

    private static void fail(String msg) {
        throw new AssertionError(msg);
    }
}
