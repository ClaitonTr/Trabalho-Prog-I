package br.com.claiton.trabalho_prog_i.conexao;


import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Usuario on 28/11/2017.
 */

public class ControleConexao {

    public static final String TAG = ControleConexao.class.getSimpleName();

    private static ControleConexao instance;
    private RequestQueue requestQueue;
    private Context contexto;

    public ControleConexao(Context contexto) {
        this.contexto = contexto;
        requestQueue = getRequestQueue();
    }



    public static synchronized ControleConexao getInstance(Context context) {
        if (instance == null) {
            instance = new ControleConexao(context);
        }
        return instance;
    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(contexto);
        }
        return requestQueue;
    }

    public <T> void addRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
