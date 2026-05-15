package chat;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom implements ChatMediator {
    private final List<User> usuarios = new ArrayList<>();
    private final String nome;

    public ChatRoom(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public void registrarUsuario(User usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario nao pode ser nulo");
        }
        for (User u : usuarios) {
            if (u.getNome().equals(usuario.getNome())) {
                throw new IllegalArgumentException(
                    "Ja existe usuario com o nome: " + usuario.getNome());
            }
        }
        usuarios.add(usuario);
    }

    @Override
    public void enviarMensagem(String mensagem, User remetente) {
        for (User u : usuarios) {
            if (u != remetente) {
                u.receber(mensagem, remetente.getNome());
            }
        }
    }

    @Override
    public void enviarPrivado(String mensagem, User remetente, String destinatario) {
        for (User u : usuarios) {
            if (u.getNome().equals(destinatario)) {
                u.receber(mensagem, remetente.getNome());
                return;
            }
        }
        throw new IllegalArgumentException("Destinatario nao encontrado: " + destinatario);
    }

    public int totalUsuarios() {
        return usuarios.size();
    }
}
