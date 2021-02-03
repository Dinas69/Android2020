package amsi.dei.estg.ipleiria.igdb.Modelos;

public class Uploadimagem {
    private int id, id_user;
    private String path, nome;

    public Uploadimagem(int id, int id_user, String path, String nome) {
        this.id = id;
        this.id_user = id_user;
        this.path = path;
        this.nome = nome;
    }

    public Uploadimagem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
