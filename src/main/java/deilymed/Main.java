package deilymed;

import deilymed.database.*;
import deilymed.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    // DAOs que serão usados por toda a aplicação
    private static final PacienteDAO pacienteDAO = new PacienteDAO();
    private static final EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
    private static final MedicoDAO medicoDAO = new MedicoDAO();
    private static final ConsultaDAO consultaDAO = new ConsultaDAO();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        // Garante que o banco de dados e as tabelas estão prontos antes de começar
        try {
            DatabaseConnection.initializeDatabase();
        } catch (RuntimeException e) {
            System.err.println("ERRO CRÍTICO: Não foi possível inicializar o banco de dados. O sistema será encerrado.");
            e.printStackTrace();
            return; // Encerra a aplicação se o DB não puder ser iniciado
        }


        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            exibirMenuPrincipal();
            opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    gerenciarAtendimento(scanner);
                    break;
                case 2:
                    gerenciarEstrutural(scanner);
                    break;
                case 3:
                    gerenciarAgendamento(scanner);
                    break;
                case 0:
                    System.out.println("Saindo do sistema DeilyMed. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            if (opcao != 0) {
                pressionarEnterParaContinuar(scanner);
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n===== DEILYMED - MENU PRINCIPAL =====");
        System.out.println("1) Atendimento");
        System.out.println("2) Estrutural (Cadastros)");
        System.out.println("3) Agendamento");
        System.out.println("0) Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void gerenciarEstrutural(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- Menu Estrutural (Cadastros) ---");
            System.out.println("1. Cadastrar Paciente");
            System.out.println("2. Cadastrar Especialidade");
            System.out.println("3. Cadastrar Médico");
            System.out.println("4. Listar Pacientes");
            System.out.println("5. Listar Especialidades");
            System.out.println("6. Listar Médicos");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao(scanner);

            try { // MELHORIA: Capturar erros que podem vir dos DAOs
                switch (opcao) {
                    case 1: cadastrarPaciente(scanner); break;
                    case 2: cadastrarEspecialidade(scanner); break;
                    case 3: cadastrarMedico(scanner); break;
                    case 4: listarPacientes(); break;
                    case 5: listarEspecialidades(); break;
                    case 6: listarMedicos(); break;
                    case 0: break; // Não faz nada, apenas volta
                    default: System.out.println("Opção inválida.");
                }
            } catch (RuntimeException e) {
                System.err.println("\nERRO INESPERADO: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    private static void cadastrarPaciente(Scanner scanner) {
        System.out.println("\n-- Cadastro de Novo Paciente --");
        // MELHORIA: Validação de entrada
        String nome = lerStringNaoVazia(scanner, "Nome: ");
        String cpf = lerStringNaoVazia(scanner, "CPF (somente números): ");
        String telefone = lerStringNaoVazia(scanner, "Telefone: ");
        String email = lerStringNaoVazia(scanner, "E-mail: ");

        Paciente paciente = new Paciente(0, nome, cpf, telefone, email);
        pacienteDAO.salvar(paciente);
        System.out.println("Paciente '" + nome + "' salvo com sucesso com o ID: " + paciente.getId());
    }

    private static void cadastrarEspecialidade(Scanner scanner) {
        System.out.println("\n-- Cadastro de Nova Especialidade --");
        String nome = lerStringNaoVazia(scanner, "Nome da especialidade: ");

        Especialidade especialidade = new Especialidade(0, nome);
        especialidadeDAO.salvar(especialidade);
        System.out.println("Especialidade '" + nome + "' salva com sucesso!");
    }

    private static void cadastrarMedico(Scanner scanner) {
        System.out.println("\n-- Cadastro de Novo Médico --");
        if (!listarEspecialidades()) return; // Se não houver especialidades, não continua

        System.out.print("Digite o ID da especialidade: ");
        int idEspecialidade = lerOpcao(scanner);

        // CORREÇÃO: Usar Optional para buscar e verificar se a especialidade existe
        Optional<Especialidade> espOptional = Optional.ofNullable(especialidadeDAO.buscarPorId(idEspecialidade));
        if (espOptional.isEmpty()) {
            System.out.println("ID da especialidade inválido!");
            return;
        }
        Especialidade esp = espOptional.get();

        String nome = lerStringNaoVazia(scanner, "Nome do Médico: ");
        String cpf = lerStringNaoVazia(scanner, "CPF (somente números): ");
        String crm = lerStringNaoVazia(scanner, "CRM: ");

        // CORREÇÃO: Usar o construtor correto, passando uma lista de horários vazia
        Medico medico = new Medico(0, nome, cpf, crm, esp, new ArrayList<>());
        medicoDAO.salvar(medico);
        System.out.println("Médico " + nome + " salvo com sucesso com o ID: " + medico.getId());
    }

    private static boolean listarPacientes() {
        System.out.println("\n--- Lista de Pacientes ---");
        List<Paciente> pacientes = pacienteDAO.buscarTodos();
        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
            return false;
        }
        pacientes.forEach(p -> System.out.printf("ID: %-3d | Nome: %-20s | CPF: %s%n", p.getId(), p.getNome(), p.getCpf()));
        return true;
    }

    private static boolean listarEspecialidades() {
        System.out.println("\n--- Lista de Especialidades ---");
        List<Especialidade> especialidades = especialidadeDAO.buscarTodos();
        if (especialidades.isEmpty()) {
            System.out.println("Nenhuma especialidade cadastrada. Cadastre uma primeiro.");
            return false;
        }
        especialidades.forEach(e -> System.out.printf("ID: %-3d | Nome: %s%n", e.getId(), e.getNomeEspec()));
        return true;
    }

    private static boolean listarMedicos() {
        System.out.println("\n--- Lista de Médicos ---");
        List<Medico> medicos = medicoDAO.buscarTodos();
        if (medicos.isEmpty()) {
            System.out.println("Nenhum médico cadastrado.");
            return false;
        }
        medicos.forEach(m -> System.out.printf("ID: %-3d | Nome: %-20s | Especialidade: %s%n", m.getId(), m.getNome(), m.getEspecialidade().getNomeEspec()));
        return true;
    }

    private static void gerenciarAgendamento(Scanner scanner) {
        System.out.println("\n--- Agendamento de Consulta ---");

        if (!listarPacientes()) return;
        System.out.print("Digite o ID do paciente: ");
        int idPaciente = lerOpcao(scanner);
        // CORREÇÃO: Usar Optional
        Optional<Object> pacienteOptional = pacienteDAO.buscarPorId(idPaciente);
        if (pacienteOptional.isEmpty()) {
            System.out.println("Paciente não encontrado.");
            return;
        }
        Paciente paciente = (Paciente) pacienteOptional.get();

        if (!listarMedicos()) return;
        System.out.print("Digite o ID do médico: ");
        int idMedico = lerOpcao(scanner);
        // CORREÇÃO: Usar Optional
        Optional<Medico> medicoOptional = medicoDAO.buscarPorId(idMedico);
        if (medicoOptional.isEmpty()) {
            System.out.println("Médico não encontrado.");
            return;
        }
        Medico medico = medicoOptional.get();

        LocalDateTime dataHora = lerDataHora(scanner);
        if (dataHora == null) return;

        Consulta consulta = new Consulta(medico, paciente, dataHora);
        consultaDAO.salvar(consulta);
        System.out.println("Consulta agendada com sucesso para " + dataHora.format(formatter));
    }

    private static void gerenciarAtendimento(Scanner scanner) {
        System.out.println("\n--- Atendimento do Dia ---");
        System.out.println("Consultas agendadas para hoje (" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "):");

        List<Consulta> consultas = consultaDAO.buscarTodos(); // Simplificação
        final boolean[] encontrouConsultaHoje = {false};
        consultas.forEach(c -> {
            if (c.getDataHora().toLocalDate().equals(LocalDate.now())) {
                System.out.printf("ID: %-3d | Hora: %s | Status: %-10s | Paciente: %-20s | Médico: %s%n",
                        c.getId(),
                        c.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                        c.getStatus(),
                        c.getPaciente().getNome(),
                        c.getMedico().getNome());
                encontrouConsultaHoje[0] = true;
            }
        });

        if(!encontrouConsultaHoje[0]){
            System.out.println("Nenhuma consulta encontrada para hoje.");
            return;
        }

        System.out.print("\nDigite o ID da consulta para alterar o status (ou 0 para voltar): ");
        int idConsulta = lerOpcao(scanner);
        if(idConsulta == 0) return;

        // CORREÇÃO: Usar Optional
        Optional<Consulta> consultaOptional = consultaDAO.buscarPorId(idConsulta);
        if(consultaOptional.isEmpty() || !consultaOptional.get().getDataHora().toLocalDate().equals(LocalDate.now())) {
            System.out.println("ID de consulta inválido ou não pertence ao dia de hoje.");
            return;
        }
        Consulta consulta = consultaOptional.get();

        System.out.println("Selecione o novo status: 1) REALIZADA 2) CANCELADA");
        int novoStatusOp = lerOpcao(scanner);
        StatusConsulta novoStatus = null;
        if(novoStatusOp == 1) novoStatus = StatusConsulta.REALIZADA;
        if(novoStatusOp == 2) novoStatus = StatusConsulta.CANCELADA;

        if(novoStatus != null) {
            consultaDAO.atualizarStatus(consulta.getId(), novoStatus);
            System.out.println("Status da consulta " + consulta.getId() + " atualizado para " + novoStatus.name());
        } else {
            System.out.println("Opção de status inválida.");
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private static int lerOpcao(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            return -1; // Retorna um valor inválido para o loop continuar
        }
    }

    private static String lerStringNaoVazia(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Este campo não pode ser vazio. Tente novamente.");
            }
        } while (input.isEmpty());
        return input;
    }

    private static void pressionarEnterParaContinuar(Scanner scanner) {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    private static LocalDateTime lerDataHora(Scanner scanner) {
        while (true) {
            try {
                String dataStr = lerStringNaoVazia(scanner, "Digite a data da consulta (dd/MM/yyyy): ");
                String horaStr = lerStringNaoVazia(scanner, "Digite a hora da consulta (HH:mm): ");
                return LocalDateTime.parse(dataStr + " " + horaStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data/hora inválido. Use dd/MM/yyyy e HH:mm. Tente novamente.");
            }
        }
    }
}