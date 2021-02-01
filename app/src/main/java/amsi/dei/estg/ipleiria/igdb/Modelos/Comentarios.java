package amsi.dei.estg.ipleiria.igdb.Modelos;

public class Comentarios {
    private int id, id_jogo, id_utilizador;
    private String descricao, data;

    public Comentarios(int id, int id_jogo, int id_utilizador, String descricao, String data) {
        this.id = id;
        this.id_jogo = id_jogo;
        this.id_utilizador = id_utilizador;
        this.descricao = descricao;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_jogo() {
        return id_jogo;
    }

    public void setId_jogo(int id_jogo) {
        this.id_jogo = id_jogo;
    }

    public int getId_utilizador() {
        return id_utilizador;
    }

    public void setId_utilizador(int id_utilizador) {
        this.id_utilizador = id_utilizador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
