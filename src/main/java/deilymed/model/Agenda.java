package deilymed.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Agenda {
    private List<Consulta> consultas = new ArrayList<>();
    private Inotificador notificador;

    public Agenda(Inotificador notificador){
        this.notificador = notificador;
    }

    public Consulta agendar(Medico medico, Paciente paciente, LocalDateTime dataHora) {
        if (!medico.verificarDisponibilidade(dataHora)) {
            System.err.println("ERRO: O médico " + medico.getNome() + " não atende neste horário.");
            return null;
        }

        boolean horarioOcupado = consultas.stream()
                .anyMatch(c -> c.getMedico().equals(medico) &&
                        c.getDataHora().equals(dataHora) &&
                        c.getStatus() == StatusConsulta.AGENDADA);

        Consulta novaConsulta = new Consulta(medico, paciente, dataHora);
        this.consultas.add(novaConsulta);

        // Notifica o paciente sobre o agendamento
        String mensagem = "Olá, " + paciente.getNome() + "! Sua consulta com " + medico.getNome() + " foi agendada para " + dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".";
        notificador.notificar(mensagem, paciente);

        return novaConsulta;
    }

    public void cancelar(Consulta consulta){
        if(consulta != null && consulta.getStatus() == StatusConsulta.AGENDADA){
            consulta.cancelar();
            String mensagem = "Atenção, " + consulta.getPaciente().getNome() + "! Sua consulta do dia " + consulta.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " foi cancelada.";
            notificador.notificar(mensagem, consulta.getPaciente());
        }
    }

    public List<Consulta> buscarConsultasPorCrmMedico(String crm){
        return consultas.stream()
                .filter(c -> c.getMedico().getCrm().equals(crm))
                .collect(Collectors.toList());
    }

    public List<Consulta> getTodasConsultas() {
        return this.consultas;
    }

}
