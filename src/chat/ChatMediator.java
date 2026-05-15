package chat;

public interface ChatMediator {
    void registrarUsuario(User usuario);
    void enviarMensagem(String mensagem, User remetente);
    void enviarPrivado(String mensagem, User remetente, String destinatario);
}
