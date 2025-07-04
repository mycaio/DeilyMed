package deilymed.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consulta {
    private Medico medico;
    private Paciente paciente;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private int id;

    public Consulta( Medico medico, Paciente paciente, LocalDateTime dataHora) {
        this.medico = medico;
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.status = StatusConsulta.AGENDADA;
    }

    public void cancelar(){
        this.status = StatusConsulta.CANCELADA;
    }

    public void realizar(){
        this.status = StatusConsulta.REALIZADA;
    }

    public Medico getMedico() {return medico;}

    public Paciente getPaciente() {return paciente;}

    public LocalDateTime getDataHora() {return dataHora;}

    public StatusConsulta getStatus() {return status;}

    public void setId(int id){this.id = id;}
    public int getId(){return this.id;}

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        return "Consulta [" + status + "]" +
                "\n  Data: " + dataHora.format(formatter) +
                "\n  " + medico +
                "\n  " + paciente;
    }
}
