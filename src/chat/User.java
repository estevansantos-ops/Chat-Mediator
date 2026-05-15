package chat;

public abstract class User {
    protected ChatMediator mediator;
    protected String nome;

    public User(ChatMediator mediator, String nome) {
        this.mediator = mediator;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract void enviar(String mensagem);
    public abstract void enviarPrivado(String mensagem, String destinatario);
    public abstract void receber(String mensagem, String remetente);
}
