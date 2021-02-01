package amsi.dei.estg.ipleiria.igdb.Modelos;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.igdb.Listeners.ComentListener;
import amsi.dei.estg.ipleiria.igdb.Listeners.JogosListener;
import amsi.dei.estg.ipleiria.igdb.Listeners.LoginListener;
import amsi.dei.estg.ipleiria.igdb.Listeners.ReviewsListener;
import amsi.dei.estg.ipleiria.igdb.R;
import amsi.dei.estg.ipleiria.igdb.utils.IGDbJsonParser;

public class SingletonGestorIGDb {

    private static final int ADICIONAR_BD = 1;
    private static final int EDITAR_BD = 2;
    private static final int REMOVER_BD = 3;

    private static SingletonGestorIGDb instance = null;

    private ArrayList<Jogos> jogos;
    private ArrayList<Comentarios> comentarios;
    private ArrayList<Reviews> reviews;
    private IGDbHelper igDbHelper;

    //ACESSO Á API
    private static RequestQueue volleyQueue = null;
    //LINK DA API
    private static final String publicToken = "?access-token=F2_v997ZflzhGaY63aKMiY-MCHYNKogP";
    private static final String mUrlAPIComentarios = "http://192.168.1.102:8888/v1/jogos/topcomentario/";
    private static final String mUrlAPIReviews = "http://192.168.1.102:8888/v1/jogos/topreview/";
    private static final String mUrlAPIJogos = "http://192.168.1.102:8888/v1/jogos/jogosandtipojogo?access-token=F2_v997ZflzhGaY63aKMiY-MCHYNKogP";
    private static final String mUrlAPILogin = "http://192.168.1.102:8888/v1/user/loginuser/";
    private static final String mUrlAPIComentariosAdd = "http://192.168.1.102:8888/v1/comentarios?access-token=";
    private static final String mUrlAPIReviewsAdd = "http://192.168.1.102:8888/v1/review?access-token=";

    //LISTENERS
    private LoginListener loginListener;
    private JogosListener jogosListener;
    private ComentListener comentListener;
    private ReviewsListener reviewsListener;

    public static synchronized SingletonGestorIGDb getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorIGDb(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    public SingletonGestorIGDb(Context context) {
        // this.igDbHelper = igDbHelper;
        //jogos = new ArrayList<>();
        igDbHelper = new IGDbHelper(context);
    }

    //GET JOGOS COMENTARIOS REVIEWS ETC
    public Jogos getJogo(int id) {
        for (Jogos j : jogos)
            if (j.getId() == id)
                return j;
        return null;
    }

    public Comentarios getComentario(int id) {
        for (Comentarios c : comentarios)
            if (c.getId() == id)
                return c;
        return null;
    }

    //LISTENERS
    public void setJogosListener(JogosListener jogosListener) {
        this.jogosListener = jogosListener;
    }

    public void setComentariosListener(ComentListener comentariosListener) {
        this.comentListener = comentariosListener;
    }

    public void setReviewsListener(ReviewsListener reviewsListener) {
        this.reviewsListener = reviewsListener;
    }

    //ADICIONAR JOGO Á BASE DE DADOS
    public void adicionarJogoBD(Jogos jogos) {
        igDbHelper.adicionarJogoBD(jogos);
    }

    public void adicionarComentarioBD(Comentarios comentarios) {
        igDbHelper.addComentariosBD(comentarios);
    }

    public void adicionarReviewBD(Reviews reviews) {
        igDbHelper.addReviewsBD(reviews);
    }

    public void adicionarJogosBD(ArrayList<Jogos> jogos) {
        igDbHelper.removeAllJogosBD();
        for (Jogos j : jogos) {
            adicionarJogoBD(j);
        }
    }

    public void adicionarComentariosBD(ArrayList<Comentarios> coment) {
        igDbHelper.removeAllComentariosBD();
        for (Comentarios c : coment) {
            adicionarComentarioBD(c);
        }
    }

    public void adicionarReviewsBD(ArrayList<Reviews> reviews) {
        igDbHelper.removeAllReviewsBD();
        for (Reviews r : reviews) {
            adicionarReviewBD(r);
        }
    }

    //API REVIEWS
    public void getReviewsJogo(final Context context, int id) {
        if (!IGDbJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não existe ligação à internet.", Toast.LENGTH_SHORT).show();

            if (reviewsListener != null)
                reviewsListener.onRefreshListaReviews(igDbHelper.getALLReviewsBD());
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIReviews + id + publicToken, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    reviews = IGDbJsonParser.parserJsonReviews(response);
                    adicionarReviewsBD(reviews);

                    if (reviewsListener != null)
                        reviewsListener.onRefreshListaReviews(reviews);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Este jogo ainda não tem Reviews", Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    //API COMENTARIOS GET
    public void getComentariosJogo(final Context context, int id) {
        if (!IGDbJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não existe ligação à internet.", Toast.LENGTH_SHORT).show();

            if (comentListener != null)
                comentListener.onRefreshListaComentarios(igDbHelper.getALLComentariosBD());
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIComentarios + id + publicToken, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    comentarios = IGDbJsonParser.parserJsonComentarios(response);
                    adicionarComentariosBD(comentarios);

                    if (comentListener != null)
                        comentListener.onRefreshListaComentarios(comentarios);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Este jogo ainda não tem comentários", Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    //API JOGOS GET
    public void getAllJogosAPI(final Context context) {
        if (!IGDbJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não existe ligação à internet.", Toast.LENGTH_SHORT).show();

            if (jogosListener != null)
                jogosListener.onRefreshListaJogos(igDbHelper.getALLJogosBD());
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIJogos, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    jogos = IGDbJsonParser.parserJsonJogos(response);
                    adicionarJogosBD(jogos);

                    if (jogosListener != null)
                        jogosListener.onRefreshListaJogos(jogos);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }

    }

    //API LOGIN GET
    public void loginAPI(final String email, final String password, final Context context) {
        StringRequest req = new StringRequest(
                Request.Method.GET,
                mUrlAPILogin + email + "/" + password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String search = "FAIL";
                        if (response.toLowerCase().indexOf(search.toLowerCase()) != -1) {
                            String token = "fail";
                            String id = "fail";
                            if (loginListener != null) {
                                loginListener.onValidateLogin(token, email, id);
                            }
                        } else {
                            String token = IGDbJsonParser.parserJsonLogin(response);
                            String id = IGDbJsonParser.parserJsonLoginId(response);
                            if (loginListener != null) {
                                loginListener.onValidateLogin(token, email, id);
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        volleyQueue.add(req);
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;

    }

    //API ADD COMENTARIO POST
    public void adicionarComentariosAPI(final Comentarios comentarios, final Context context, final String token, final int id_user) {
        Log.e("POSTCOMENT", mUrlAPIComentariosAdd + token);
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIComentariosAdd + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("POSTCOMENT", response);
                Comentarios c = IGDbJsonParser.parserJsonComentario(response);
                adicionarComentarioBD(c);

                if (comentListener != null)
                    comentListener.onRefreshDetalhes();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("POSTCOMENT", error.toString());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Descricao", comentarios.getDescricao());
                params.put("Id_utilizador", comentarios.getId_utilizador() + "");
                params.put("Id_jogo", comentarios.getId_jogo() + "");
                params.put("Data", comentarios.getData() + "");
                params.put("token", token);
                return params;
            }
        };
        volleyQueue.add(req);
    }

    //API ADD REVIEWS POST
    public void adicionarReviewsAPI(final Reviews reviews, final Context context, final String token, final int id_user) {
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIReviewsAdd + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Reviews r = IGDbJsonParser.parserJsonReview(response);
                adicionarReviewBD(r);

                if (reviewsListener != null)
                    reviewsListener.onRefreshDetalhes();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("REVIEWSTESTE", error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Descricao", reviews.getDescricao());
                params.put("Id_Utilizador", reviews.getId_utilizador() + "");
                params.put("Id_Jogo", reviews.getId_jogo() + "");
                params.put("Data", reviews.getData() + "");
                params.put("Score", reviews.getScore());
                params.put("token", token);
                return params;
            }
        };
        volleyQueue.add(req);
    }
}
