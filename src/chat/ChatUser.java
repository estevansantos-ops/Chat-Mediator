package chat;

import java.util.ArrayList;
import java.util.List;

public class ChatUser extends User {
    private final List<String> caixaDeEntrada = new ArrayList<>();

    public ChatUser(ChatMediator mediator, String nome) {
        super(mediator, nome);
    }

    @Override
    public void enviar(String mensagem) {
        mediator.enviarMensagem(mensagem, this);
    }

    @Override
    public void enviarPrivado(String mensagem, String destinatario) {
        mediator.enviarPrivado(mensagem, this, destinatario);
    }

    @Override
    public void receber(String mensagem, String remetente) {
        String linha = "[" + nome + "] recebeu de " + remetente + ": " + mensagem;
        caixaDeEntrada.add(linha);
        System.out.println(linha);
    }

    public List<String> getCaixaDeEntrada() {
        return caixaDeEntrada;
    }
}
