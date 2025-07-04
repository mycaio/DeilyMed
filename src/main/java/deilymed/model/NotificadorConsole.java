package deilymed.model;

public class NotificadorConsole implements Inotificador{
    @Override
    public void notificar(String mensagem, Paciente paciente) {
        System.out.println("Notificação para " + paciente.getNome() + ": " + mensagem);
    }

}
