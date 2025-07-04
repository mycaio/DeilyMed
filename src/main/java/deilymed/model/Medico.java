package deilymed.model;

import java.time.LocalDateTime;
import java.util.List;

public class Medico extends Pessoa{
    private String crm;
    private Especialidade especialidade;
    private List<LocalDateTime> horariosDisponiveis;
    private int id;

    public Medico(int id,String nome, String cpf, String crm, Especialidade especialidade, List<LocalDateTime> horariosDisponiveis){
        super(nome, cpf);
        this.crm = crm;
        this.especialidade = especialidade;
        this.horariosDisponiveis = horariosDisponiveis;
        this.id = id;
    }

    public String getCrm() {return crm;}
    public void setCrm(String crm) {this.crm = crm;}
    public Especialidade getEspecialidade() {return especialidade;}
    public void setEspecialidade(Especialidade especialidade) {this.especialidade = especialidade;}
    public void setId(int id){this.id =id;}
    public int getId(){return id;}
    public List<LocalDateTime> getHorariosDisponiveis(){return horariosDisponiveis;}

    public boolean verificarDisponibilidade(LocalDateTime dataHora){
        String horaRequisitada = String.format("%02d:%02d", dataHora.getHour(), dataHora.getMinute());
        return this.horariosDisponiveis.contains(horaRequisitada);
    }
    @Override
    public String toString(){
        return "Dr(a). " + getNome() + "\n  CRM: " + getCpf() + "\n  Especialidade: " + getEspecialidade();
    }

}
