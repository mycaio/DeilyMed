package deilymed.model;

public class Especialidade {
    private String nomeEspec;
    private int id;

    public Especialidade(int id,String nomeEspec){
        this.nomeEspec = nomeEspec;
        this.id = id;
    }

    public String getNomeEspec() {return nomeEspec;}
    public void setNomeEspec(String nomeEspec) {this.nomeEspec = nomeEspec;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return getNomeEspec();
    }
}
