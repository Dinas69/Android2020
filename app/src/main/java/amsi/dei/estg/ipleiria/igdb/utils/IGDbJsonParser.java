package amsi.dei.estg.ipleiria.igdb.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Comentarios;
import amsi.dei.estg.ipleiria.igdb.Modelos.Jogos;
import amsi.dei.estg.ipleiria.igdb.Modelos.Reviews;

public class IGDbJsonParser {

    public static ArrayList<Jogos> parserJsonJogos(JSONArray response) {
        ArrayList<Jogos> jogos = new ArrayList<>();
        try {
            Log.e("JOGOAPI", "teste");
            for (int i = 0; i < response.length(); i++) {
                Log.e("JOGOAPI", "teste1");
                JSONObject jog = (JSONObject) response.get(i);
                int id = jog.getInt("Id");
                String nome = jog.getString("Nome");
                String descricao = jog.getString("Descricao");
                String data = jog.getString("Data");
                String trailer = jog.getString("Trailer");
                String imagem = jog.getString("Imagem");
                String nometipo = jog.getString("NomeTipo");
                int id_tipo_jogo = jog.getInt("Id_tipojogo");

                Jogos auxJogo = new Jogos(id, id_tipo_jogo, nome, descricao, data, trailer, imagem, nometipo);
                jogos.add(auxJogo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jogos;
    }

    public static ArrayList<Comentarios> parserJsonComentarios(JSONArray response) {
        ArrayList<Comentarios> comentarios = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject coment = (JSONObject) response.get(i);
                int id = coment.getInt("Id");
                String descricao = coment.getString("Descricao");
                String data = coment.getString("Data");
                int id_utilizador = coment.getInt("Id_utilizador");
                int id_jogo = coment.getInt("Id_jogo");

                Comentarios auxComent = new Comentarios(id, id_jogo, id_utilizador, descricao, data);
                comentarios.add(auxComent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    public static ArrayList<Reviews> parserJsonReviews(JSONArray response) {
        Log.e("ReviewTeste", "parser");
        ArrayList<Reviews> reviews = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject review = (JSONObject) response.get(i);
                int id = review.getInt("Id");
                String descricao = review.getString("Descricao");
                String data = review.getString("Data");
                String score = review.getString("Score");
                int id_utilizador = review.getInt("Id_Utilizador");
                int id_jogo = review.getInt("Id_Jogo");

                Reviews auxReview = new Reviews(id, id_jogo, id_utilizador, descricao, data, score);
                reviews.add(auxReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static String parserJsonLogin(String response) {
        String token = null;

        try {
            JSONObject login = new JSONObject(response);
            token = login.getString("auth_key");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Onclicklogin22", "try catch" + token);
        }
        return token;
    }

    public static String parserJsonLoginId(String response) {
        String id = null;

        try {
            JSONObject login = new JSONObject(response);
            id = login.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Onclicklogin22", "try catch" + id);
        }
        return id;
    }

    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }

    public static Comentarios parserJsonComentario(String response) {
        Comentarios auxcoment = null;

        try {
            JSONObject coment = new JSONObject(response);
            int id = coment.getInt("Id");
            String descricao = coment.getString("Descricao");
            String data = coment.getString("Data");
            int id_utilizador = coment.getInt("Id_utilizador");
            int id_jogo = coment.getInt("Id_jogo");

            auxcoment = new Comentarios(id, id_jogo, id_utilizador, descricao, data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return auxcoment;
    }

    public static Reviews parserJsonReview(String response) {
        Reviews auxrev = null;

        try {
            JSONObject review = new JSONObject(response);
            int id = review.getInt("Id");
            Log.e("REVIEWSTESTE", String.valueOf(id));

            String descricao = review.getString("Descricao");
            Log.e("REVIEWSTESTE", descricao);

            String data = review.getString("Data");
            Log.e("REVIEWSTESTE", data);

            int id_utilizador = review.getInt("Id_Utilizador");
            Log.e("REVIEWSTESTE", String.valueOf(id_utilizador));

            int id_jogo = review.getInt("Id_Jogo");
            Log.e("REVIEWSTESTE", String.valueOf(id_jogo));

            String score = review.getString("Score");
            Log.e("REVIEWSTESTE", score);

            auxrev = new Reviews(id, id_jogo, id_utilizador, descricao, data, score);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return auxrev;
    }
}
