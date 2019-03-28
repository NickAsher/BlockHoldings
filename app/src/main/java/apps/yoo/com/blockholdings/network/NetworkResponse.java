package apps.yoo.com.blockholdings.network;

import com.android.volley.VolleyError;

public interface NetworkResponse {
    public void onResponse(String response) ;
    public void onErrorResponse(VolleyError error) ;
}
