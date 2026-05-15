package chat;

public class Main {
    public static void main(String[] args) {
        ChatRoom sala = new ChatRoom("Sala Geral");

        ChatUser ana = new ChatUser(sala, "Ana");
        ChatUser bruno = new ChatUser(sala, "Bruno");
        ChatUser carla = new ChatUser(sala, "Carla");

        sala.registrarUsuario(ana);
        sala.registrarUsuario(bruno);
        sala.registrarUsuario(carla);

        System.out.println("=== Mensagem em broadcast ===");
        ana.enviar("Oi pessoal!");

        System.out.println();
        System.out.println("=== Mensagem privada ===");
        bruno.enviarPrivado("Ana, podemos conversar?", "Ana");

        System.out.println();
        System.out.println("=== Outra mensagem em broadcast ===");
        carla.enviar("Bom dia a todos!");
    }
}
