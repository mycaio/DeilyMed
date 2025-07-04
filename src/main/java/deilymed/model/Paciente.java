package deilymed.model;

public class Paciente extends Pessoa{
    private String Telefone;
    private String Email;
    private int id;

    public Paciente(int id, String nome, String cpf, String telefone, String email){
        super(nome, cpf);
        this.Telefone = telefone;
        this.Email = email;
        this.id = id;
    }

    public String getTelefone() {return Telefone;}
    public void setTelefone(String Telefone) {this.Telefone = Telefone;}
    public String getEmail() {return Email;}
    public void setEmail(String Email) {this.Email = Email;}

    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    @Override
    public String toString(){
        return "Paciente: " + getNome() + "\nCPF: " + getCpf() + "\nTelefone: " + getTelefone() + "\nEmail: " + getEmail();
    }
}
