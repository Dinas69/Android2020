package amsi.dei.estg.ipleiria.igdb.Modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class IGDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "IGDb";
    private static final int DB_VERSION = 1;
    //TABELA JOGO
    private static final String TABLE_NAME_1 = "jogos";
    private static final String ID_JOGO = "id";
    private static final String NOME_JOGO = "nome";
    private static final String DESCRICAO_JOGO = "descricao";
    private static final String DATA_JOGO = "data";
    private static final String TRAILER_JOGO = "trailer";
    private static final String NOMETIPO_JOGO = "Nometipo";
    private static final String IMAGEM_JOGO = "imagem";
    private static final String ID_TIPOJOGO_JOGO = "id_tipojogo";
    //TABELA TIPO JOGO
    private static final String TABLE_NAME_2 = "tipojogo";
    private static final String ID_TIPOJOGO = "id";
    private static final String NOME_TIPOJOGO = "nome";
    private static final String DESCRICAO_TIPOJOGO = "descricao";
    //TABELA COMENTARIOS
    private static final String TABLE_NAME_3 = "comentarios";
    private static final String ID_COMENT = "id";
    private static final String DATA_COMENT = "data";
    private static final String DESCRICAO_COMENT = "descricao";
    private static final String ID_UTILIZADOR_COMENT = "id_utilizador";
    private static final String ID_JOGO_COMNET = "id_jogo";
    //TABELA REVIEWS
    private static final String TABLE_NAME_4 = "reviews";
    private static final String ID_REVIEWS = "id";
    private static final String SCORE_REVIEW = "score";
    private static final String DATA_REVIEWS = "data";
    private static final String DESCRICAO_REVIEWS = "descricao";
    private static final String ID_UTILIZADOR_REVIEWS = "id_utilizador";
    private static final String ID_JOGO_REVIEWS = "id_jogo";
    //TABELA REVIEWS
    private static final String TABLE_NAME_5 = "uploads";
    private static final String ID_UP = "id";
    private static final String NOME_UP = "nome";
    private static final String PATH_UP = "path";
    private static final String ID_USER_UP = "id_user";

    private final SQLiteDatabase db;

    public IGDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = this.getWritableDatabase();
        //db.execSQL("DROP DATABASE IGDb");
        //context.deleteDatabase(DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //código sql de criação de tabela
        String createTableJogos = "CREATE TABLE " + TABLE_NAME_1 + "( " +
                ID_JOGO + " INTEGER PRIMARY KEY, " +
                NOME_JOGO + " TEXT NOT NULL, " +
                DESCRICAO_JOGO + " TEXT NOT NULL, " +
                DATA_JOGO + " TEXT NOT NULL, " +
                TRAILER_JOGO + " TEXT NOT NULL, " +
                NOMETIPO_JOGO + " TEXT NOT NULL, " +
                ID_TIPOJOGO_JOGO + " INTEGER NOT NULL, " +
                IMAGEM_JOGO + " TEXT  NOT NULL);";
        db.execSQL(createTableJogos);

        String createTableTipoJogo = "CREATE TABLE " + TABLE_NAME_2 + "( " +
                ID_TIPOJOGO + " INTEGER PRIMARY KEY, " +
                NOME_TIPOJOGO + " TEXT NOT NULL, " +
                DESCRICAO_TIPOJOGO + " TEXT NOT NULL);";
        db.execSQL(createTableTipoJogo);

        String createTableComentarios = "CREATE TABLE " + TABLE_NAME_3 + "( " +
                ID_COMENT + " INTEGER PRIMARY KEY, " +
                DATA_COMENT + " TEXT NOT NULL, " +
                ID_UTILIZADOR_COMENT + " INTEGER NOT NULL, " +
                ID_JOGO_COMNET + " INTEGER NOT NULL, " +
                DESCRICAO_COMENT + " TEXT NOT NULL);";
        db.execSQL(createTableComentarios);

        String createTableReviews = "CREATE TABLE " + TABLE_NAME_4 + "( " +
                ID_REVIEWS + " INTEGER PRIMARY KEY, " +
                DATA_REVIEWS + " TEXT NOT NULL, " +
                SCORE_REVIEW + " TEXT NOT NULL, " +
                ID_UTILIZADOR_REVIEWS + " INTEGER NOT NULL, " +
                ID_JOGO_REVIEWS + " INTEGER NOT NULL, " +
                DESCRICAO_REVIEWS + " TEXT NOT NULL);";
        db.execSQL(createTableReviews);

        String createTableUpload = "CREATE TABLE " + TABLE_NAME_5 + "( " +
                ID_UP + " INTEGER PRIMARY KEY, " +
                PATH_UP + " TEXT NOT NULL, " +
                NOME_UP + " TEXT NOT NULL, " +
                ID_USER_UP + " INTEGER NOT NULL);";
        db.execSQL(createTableUpload);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteTableJogos = "DROP TABLE IF EXISTS " + TABLE_NAME_1;
        db.execSQL(deleteTableJogos);
        String deleteTableTipoJogos = "DROP TABLE IF EXISTS " + TABLE_NAME_2;
        db.execSQL(deleteTableTipoJogos);
        String deleteTableComentarios = "DROP TABLE IF EXISTS " + TABLE_NAME_3;
        db.execSQL(deleteTableTipoJogos);
        String deleteTableReviews = "DROP TABLE IF EXISTS " + TABLE_NAME_4;
        db.execSQL(deleteTableTipoJogos);
        this.onCreate(db);
    }

    /*****CRUD*****/
    //JOGOS
    public void adicionarJogoBD(Jogos jogos) {
        ContentValues values = new ContentValues();
        values.put(ID_JOGO, jogos.getId());
        values.put(NOME_JOGO, jogos.getNome());
        values.put(DESCRICAO_JOGO, jogos.getDescricao());
        values.put(DATA_JOGO, jogos.getData());
        values.put(TRAILER_JOGO, jogos.getTrailer());
        values.put(IMAGEM_JOGO, jogos.getImagem());
        values.put(NOMETIPO_JOGO, jogos.getNometipo());
        values.put(ID_TIPOJOGO_JOGO, jogos.getId_jogo());

        this.db.insert(TABLE_NAME_1, null, values);
    }

    //COMENTARIOS
    public void addComentariosBD(Comentarios comentarios) {
        ContentValues values = new ContentValues();
        values.put(ID_COMENT, comentarios.getId());
        values.put(DESCRICAO_COMENT, comentarios.getDescricao());
        values.put(ID_JOGO_COMNET, comentarios.getId_jogo());
        values.put(ID_UTILIZADOR_COMENT, comentarios.getId_utilizador());
        values.put(DATA_COMENT, comentarios.getData());
        this.db.insert(TABLE_NAME_3, null, values);
    }


    //REVIEWS
    public void addReviewsBD(Reviews reviews) {
        ContentValues values = new ContentValues();
        values.put(ID_REVIEWS, reviews.getId());
        values.put(DESCRICAO_REVIEWS, reviews.getDescricao());
        values.put(ID_JOGO_REVIEWS, reviews.getId_jogo());
        values.put(ID_UTILIZADOR_REVIEWS, reviews.getId_utilizador());
        values.put(DATA_REVIEWS, reviews.getData());
        values.put(SCORE_REVIEW, reviews.getScore());
        this.db.insert(TABLE_NAME_4, null, values);
    }

    //UPLOAD
    public void addUploadBD(Uploadimagem uploadimagem) {
        ContentValues values = new ContentValues();
        values.put(ID_UP, uploadimagem.getId());
        values.put(NOME_UP, uploadimagem.getNome());
        values.put(PATH_UP, uploadimagem.getPath());
        values.put(ID_USER_UP, uploadimagem.getId_user());
        this.db.insert(TABLE_NAME_5, null, values);
    }

    //UPDATE REVIEWS
    public boolean editarReviewsBD(Reviews reviews) {

        ContentValues values = new ContentValues();
        values.put(ID_REVIEWS, reviews.getId());
        values.put(DESCRICAO_REVIEWS, reviews.getDescricao());
        values.put(ID_JOGO_REVIEWS, reviews.getId_jogo());
        values.put(ID_UTILIZADOR_REVIEWS, reviews.getId_utilizador());
        values.put(DATA_REVIEWS, reviews.getData());
        values.put(SCORE_REVIEW, reviews.getScore());

        int nRows = this.db.update(TABLE_NAME_4, values, "id = ?", new String[]{reviews.getId() + ""});

        return (nRows > 0);
    }

    //DELETE REVIEW
    public boolean removerReviewBD(int id) {
        int nRows = this.db.delete(TABLE_NAME_4, "id = ?", new String[]{id + ""});
        return (nRows > 0);
    }

    //UPDATE COMENTARIOS
    public boolean editarComentarioBD(Comentarios comentarios) {

        ContentValues values = new ContentValues();
        values.put(ID_COMENT, comentarios.getId());
        values.put(DESCRICAO_COMENT, comentarios.getDescricao());
        values.put(ID_JOGO_COMNET, comentarios.getId_jogo());
        values.put(ID_UTILIZADOR_COMENT, comentarios.getId_utilizador());
        values.put(DATA_COMENT, comentarios.getData());

        int nRows = this.db.update(TABLE_NAME_4, values, "id = ?", new String[]{comentarios.getId() + ""});

        return (nRows > 0);
    }

    //DELETE COMENTARIO
    public boolean removerComentarioBD(int id) {
        int nRows = this.db.delete(TABLE_NAME_3, "id = ?", new String[]{id + ""});
        return (nRows > 0);
    }
    //DELETE UPLOAD SINGLE
    public boolean removerUploadBD(int id) {
        int nRows = this.db.delete(TABLE_NAME_5, "id = ?", new String[]{id + ""});
        return (nRows > 0);
    }

    public void removeAllComentariosBD() {
        this.db.delete(TABLE_NAME_3, null, null);
    }

    public void removeAllReviewsBD() {
        this.db.delete(TABLE_NAME_4, null, null);
    }

    public void removeAllJogosBD() {
        this.db.delete(TABLE_NAME_1, null, null);
    }
    public void removeAllUploadsBD() {
        this.db.delete(TABLE_NAME_5, null, null);
    }


    //OBTEM TODOS OS COMENTÁRIOS
    public ArrayList<Comentarios> getALLComentariosBD() {
        ArrayList<Comentarios> comentarios = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_3, new String[]{ID_COMENT, DATA_COMENT, DESCRICAO_COMENT, ID_JOGO_COMNET, ID_UTILIZADOR_COMENT}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Comentarios auxComent = new Comentarios(cursor.getInt(0), cursor.getInt(4), cursor.getInt(5), cursor.getString(1), cursor.getString(2));
                comentarios.add(auxComent);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return comentarios;
    }

    //OBTEM TODOS OS REVIEWS
    public ArrayList<Reviews> getALLReviewsBD() {
        ArrayList<Reviews> reviews = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_4, new String[]{ID_COMENT, DATA_COMENT, DESCRICAO_COMENT, ID_JOGO_COMNET, ID_UTILIZADOR_COMENT, SCORE_REVIEW}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Reviews auxReview = new Reviews(cursor.getInt(0), cursor.getInt(4), cursor.getInt(5), cursor.getString(1), cursor.getString(2), cursor.getString(6));
                reviews.add(auxReview);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviews;
    }

    //OBTEM TODOS OS JOGOS
    public ArrayList<Jogos> getALLJogosBD() {
        ArrayList<Jogos> jogos = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_1, new String[]{ID_JOGO, NOME_JOGO, DESCRICAO_JOGO, DATA_JOGO, TRAILER_JOGO, IMAGEM_JOGO, NOMETIPO_JOGO, ID_TIPOJOGO_JOGO}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Jogos auxJogo = new Jogos(cursor.getInt(0), cursor.getInt(6), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(1));
                jogos.add(auxJogo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return jogos;
    }

    //OBTEM TODOS OS JOGOS
    public ArrayList<Uploadimagem> getALLUploadsBD() {
        ArrayList<Uploadimagem> uploadimagems = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_5, new String[]{ID_UP, ID_USER_UP, PATH_UP, NOME_UP}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Uploadimagem auxUpload = new Uploadimagem(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3));
                uploadimagems.add(auxUpload);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return uploadimagems;
    }
}
