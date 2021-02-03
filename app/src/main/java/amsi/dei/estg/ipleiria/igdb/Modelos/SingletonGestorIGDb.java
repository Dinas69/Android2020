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
import amsi.dei.estg.ipleiria.igdb.Listeners.UploadListener;
import amsi.dei.estg.ipleiria.igdb.R;
import amsi.dei.estg.ipleiria.igdb.Views.Upload_imagem;
import amsi.dei.estg.ipleiria.igdb.utils.IGDbJsonParser;

public class SingletonGestorIGDb {

    private static SingletonGestorIGDb instance = null;

    private ArrayList<Jogos> jogos;
    private ArrayList<Uploadimagem> uploadimagems;
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
    private static final String mUrlAPIReviewsPUT = "http://192.168.1.102:8888/v1/review/";
    private static final String mUrlAPIComentariosPUT = "http://192.168.1.102:8888/v1/comentarios/";
    private static final String mUrlAPIUploadPUT = "http://192.168.1.102:8888/v1/uploadimagem";
    private static final String mUrlAPIUploads = "http://192.168.1.102:8888/v1/uploadimagem?access-token=F2_v997ZflzhGaY63aKMiY-MCHYNKogP";

    //LISTENERS
    private LoginListener loginListener;
    private JogosListener jogosListener;
    private ComentListener comentListener;
    private ReviewsListener reviewsListener;
    private UploadListener uploadListener;

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

    public Reviews getReview(int id) {
        for (Reviews r : reviews)
            if (r.getId() == id)
                return r;
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

    public void setUploadsListener(UploadListener uploadsListener) {
        this.uploadListener = uploadsListener;
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

    public void adicionarUploadBD(Uploadimagem uploadimagem) {
        igDbHelper.addUploadBD(uploadimagem);
    }

    public void adicionarJogosBD(ArrayList<Jogos> jogos) {
        igDbHelper.removeAllJogosBD();
        for (Jogos j : jogos) {
            adicionarJogoBD(j);
        }
    }

    public void adicionarUploadsBD(ArrayList<Uploadimagem> uploadimagems) {
        igDbHelper.removeAllUploadsBD();
        for (Uploadimagem u : uploadimagems) {
            adicionarUploadBD(u);
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

    //EDITAR
    public void editarReviewBD(Reviews reviews) {
        Reviews r = getReview(reviews.getId());

        if (r != null) {
            if (igDbHelper.editarReviewsBD(reviews)) {
                r.setId_utilizador(reviews.getId_utilizador());
                r.setData(reviews.getData());
                r.setId_jogo(reviews.getId_jogo());
                r.setScore(reviews.getScore());
                r.setDescricao(reviews.getDescricao());
            }
        }

    }

    public void editarComentarioBD(Comentarios comentarios) {
        Comentarios c = getComentario(comentarios.getId());

        if (c != null) {
            if (igDbHelper.editarComentarioBD(comentarios)) {
                c.setId_utilizador(comentarios.getId_utilizador());
                c.setData(comentarios.getData());
                c.setId_jogo(comentarios.getId_jogo());
                c.setDescricao(comentarios.getDescricao());
            }
        }

    }

    //REMOVER
    public void removerReviewBD(int id) {
        Reviews r = getReview(id);

        if (r != null)
            if (igDbHelper.removerReviewBD(id))
                reviews.remove(r);
    }

    public void removerComentarioBD(int id) {
        Comentarios c = getComentario(id);

        if (c != null)
            if (igDbHelper.removerComentarioBD(id))
                comentarios.remove(c);
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

    //API REVIEWS PUT
    public void editarReviewAPI(final Reviews reviews, final Context context, final String token) {
        Log.e("ONCLICKITEM", mUrlAPIReviewsPUT + reviews.getId() + "?access-token=" + token);
        StringRequest req = new StringRequest(Request.Method.PUT, mUrlAPIReviewsPUT + reviews.getId() + "?access-token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Reviews r = IGDbJsonParser.parserJsonReview(response);
                editarReviewBD(r);

                if (reviewsListener != null)
                    reviewsListener.onRefreshDetalhes();

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
                params.put("Score", reviews.getScore());
                params.put("Data", reviews.getData());
                params.put("Descricao", reviews.getDescricao());
                params.put("Id_Jogo", reviews.getId_jogo() + "");
                params.put("Id_Utilizador", reviews.getId_utilizador() + "");
                params.put("token", token);
                return params;
            }
        };
        volleyQueue.add(req);
    }

    //API REVIEWS DELETE
    public void removerReviewAPI(final Reviews reviews, final Context context, String token) {
        StringRequest req = new StringRequest(Request.Method.DELETE, mUrlAPIReviewsPUT + reviews.getId() + "?access-token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                removerReviewBD(reviews.getId());

                if (reviewsListener != null)
                    reviewsListener.onRefreshDetalhes();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(req);
    }

    //API COMENTARIOS PUT
    public void editarComentarioAPI(final Comentarios comentarios, final Context context, final String token) {
        Log.e("ONCLICKITEM", mUrlAPIReviewsPUT + comentarios.getId() + "?access-token=" + token);
        StringRequest req = new StringRequest(Request.Method.PUT, mUrlAPIComentariosPUT + comentarios.getId() + "?access-token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Comentarios c = IGDbJsonParser.parserJsonComentario(response);
                editarComentarioBD(c);

                if (comentListener != null)
                    comentListener.onRefreshDetalhes();

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
                params.put("Data", comentarios.getData());
                params.put("Descricao", comentarios.getDescricao());
                params.put("Id_jogo", comentarios.getId_jogo() + "");
                params.put("Id_utilizador", comentarios.getId_utilizador() + "");
                params.put("token", token);
                return params;
            }
        };
        volleyQueue.add(req);
    }

    //API COMENTARIOS DELETE
    public void removerComentarioAPI(final Comentarios comentarios, final Context context, String token) {
        StringRequest req = new StringRequest(Request.Method.DELETE, mUrlAPIComentariosPUT + comentarios.getId() + "?access-token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                removerComentarioBD(comentarios.getId());

                if (comentListener != null)
                    comentListener.onRefreshDetalhes();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(req);
    }

    //API UPLOAD ADD
    public void uploadImagemAPI(Uploadimagem uploadimagem, final Context context, String token) {
        Log.e("UPLOAD", mUrlAPIUploadPUT + "?access-token=" + token);
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIUploadPUT + "?access-token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Uploadimagem u = IGDbJsonParser.parserJsonUpload(response);
                adicionarUploadBD(u);

                if (uploadListener != null)
                    uploadListener.onRefreshDetalhes();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nome", uploadimagem.getNome() + "");
                params.put("path", uploadimagem.getPath() + "");
                params.put("id_user", uploadimagem.getId_user() + "");
                params.put("token", token);
                return params;
            }
        };
        volleyQueue.add(req);
    }

    //API UPLOADS GET
    public void getAllUploadAPI(final Context context) {
        if (!IGDbJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não existe ligação à internet.", Toast.LENGTH_SHORT).show();

            if (uploadListener != null)
                uploadListener.onRefreshListaUpload(igDbHelper.getALLUploadsBD());
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIUploads, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    uploadimagems = IGDbJsonParser.parserJsonUploads(response);
                    adicionarUploadsBD(uploadimagems);

                    if (uploadListener != null)
                        uploadListener.onRefreshListaUpload(uploadimagems);

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
}
