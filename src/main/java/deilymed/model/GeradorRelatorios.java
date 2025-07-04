package deilymed.model;

import java.time.LocalDate;
import java.util.List;

public class GeradorRelatorios {
    public void listarConsultasDoDia(List<Consulta> consultas, LocalDate dia) {
        System.out.println("\n--- RELATÓRIO: Consultas do dia " + dia + " ---");
        consultas.stream()
                .filter(c -> c.getDataHora().toLocalDate().equals(dia) && c.getStatus() == StatusConsulta.AGENDADA)
                .forEach(System.out::println);
        System.out.println("--- Fim do Relatório ---");
    }

    public void listarConsultasPorMedico(List<Consulta> consultas, String crm) {
        System.out.println("\n--- RELATÓRIO: Consultas para o CRM " + crm + " ---");
        consultas.stream()
                .filter(c -> c.getMedico().getCrm().equals(crm))
                .forEach(System.out::println);
        System.out.println("--- Fim do Relatório ---");
    }

    public void listarConsultasPorPaciente(List<Consulta> consultas, String cpf) {
        System.out.println("\n--- RELATÓRIO: Consultas para o CPF " + cpf + " ---");
        consultas.stream()
                .filter(c -> c.getPaciente().getCpf().equals(cpf))
                .forEach(System.out::println);
        System.out.println("--- Fim do Relatório ---");
    }
}


