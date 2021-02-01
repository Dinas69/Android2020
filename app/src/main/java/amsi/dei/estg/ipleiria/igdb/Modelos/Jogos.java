package amsi.dei.estg.ipleiria.igdb.Modelos;

public class Jogos {
    private int id, id_jogo;
    private String nome, descricao, data, trailer, imagem, Nometipo;

    public String getNometipo() {
        return Nometipo;
    }

    public void setNometipo(String nometipo) {
        Nometipo = nometipo;
    }

    private static int autoIncrementedId = 1;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public static int getAutoIncrementedId() {
        return autoIncrementedId;
    }

    public static void setAutoIncrementedId(int autoIncrementedId) {
        Jogos.autoIncrementedId = autoIncrementedId;
    }

    public Jogos(int id, int id_jogo, String nome, String descricao, String data, String trailer, String imagem, String Nometipo) {
        this.id = id;
        this.id_jogo = id_jogo;
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.trailer = trailer;
        this.imagem = imagem;
        this.Nometipo = Nometipo;
    }
}
