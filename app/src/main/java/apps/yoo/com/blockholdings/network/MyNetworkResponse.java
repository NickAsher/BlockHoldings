package apps.yoo.com.blockholdings.network;

import com.android.volley.VolleyError;

public interface MyNetworkResponse {
    public void onResponse(String response) ;
    public void onErrorResponse(VolleyError error) ;
}
