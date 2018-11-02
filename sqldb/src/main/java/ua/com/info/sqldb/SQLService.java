package ua.com.info.sqldb;

import android.content.Context;
import android.util.Base64;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import ua.com.info.data.Command;
import ua.com.info.data.Data;
import ua.com.info.data.IDataBase;
import ua.com.info.data.IDataObject;
import ua.com.info.data.JsonHelper;
import ua.com.info.data.Parameters;
import ua.com.info.data.Result;
import ua.com.info.data.Variable;
import ua.com.info.data.Variables;

import static ua.com.info.data.ResultKt.error;

/**
 * Створив VM 03.12.2017.
 */

public class SQLService implements IDataBase {

    private RequestQueue _queue;
    private Context _context;
    private final String _url;

    public SQLService(Context context, String url) {
        _context = context;
        _url = url;
    }

    private RequestQueue getRequestQueue() {
        if (_queue == null) {
            _queue = Volley.newRequestQueue(_context);
        }
        return _queue;
    }

    private void addRequest(String name, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.GET, _url + name, listener, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(5),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.getRequestQueue().add(request);
    }

    private void exec(String method, String cmd, Parameters parameters, final Listener listener) {
        Command command = new Command(cmd, parameters);
        String s = null;
        try {
            s = ZipBase64(JsonHelper.SerializeObject(command)).replace('+', '_').replace('/', '-');
        } catch (IOException e) {
            e.printStackTrace();
        }
        addRequest(method + "?p=" + s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String s = UnzipBase64(response);
                    Result res = JsonHelper.DeserializeObject(s, Result.class);
                    if (res.getVariables() != null) { // Обробка DBNull
                        ArrayList<Variable> vars = res.getVariables().getValues();
                        for (Variable v : vars) {
                            if (v.getValue() == "{__type=DBNull:#System}") {
                                v.setValue(null);
                            }
                        }
                    }
                    listener.onExecuted(res);
                } catch (IOException e) {
                    listener.onExecuted(error("Помилка зв'язку"));
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String s;
                if (error instanceof TimeoutError) s = "Перевищено час очікуванн";
                else
                    s = error.getMessage();
                listener.onExecuted(error(s));
            }
        });
    }

    @Override
    public void Execute(String cmd, Parameters parameters, final Listener listener) {
        exec("execute", cmd, parameters, listener);
    }

    @Override
    public void getTable(String cmd, Parameters parameters, final Listener listener) {
        exec("getTable", cmd, parameters, listener);
    }

    @Override
    public Result getTable(String cmd, Parameters parameters) {
        return null;
    }

    @Override
    public void getData(String cmd, Parameters parameters, final Listener listener) {
        exec("getData", cmd, parameters, listener);
    }

    @Override
    public void Batch(Data data, final Listener listener) {
        String s = null;
        try {
            s = ZipBase64(JsonHelper.SerializeObject(data)).replace('+', '_').replace('/', '-');
        } catch (IOException e) {
            e.printStackTrace();
        }
        addRequest("Batch?p=" + s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String s = UnzipBase64(response);
                    Result res = JsonHelper.DeserializeObject(s, Result.class);
                    listener.onExecuted(res);
                } catch (IOException e) {
                    listener.onExecuted(error("Помилка зв'язку"));
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onExecuted(error(error.getMessage()));
            }
        });
    }

    private static byte[] compress(String string) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(string.getBytes());
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return compressed;
    }

    private static String decompress(byte[] compressed) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is);
        InputStreamReader reader = new InputStreamReader(gis);
        BufferedReader in = new BufferedReader(reader);
        String s;
        StringBuilder string = new StringBuilder();
        while ((s = in.readLine()) != null)
            string.append(s);
        in.close();
        reader.close();
        gis.close();
        is.close();
        return string.toString();
    }

    private static String ZipBase64(String s) throws IOException {
        byte[] bytes = compress(s);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static String UnzipBase64(String compressRequest) throws IOException {
        byte[] bytes = Base64.decode(compressRequest, Base64.DEFAULT);
        return decompress(bytes);
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Необхідно перевизначити метод SQLService.close()");
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void openConnection() {

    }
}
