package apps.yoo.com.blockholdings.ui.detail;

import android.util.Log;
import android.widget.LinearLayout;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Helper_Chart {
    private static final String LOG_TAG = "Helper_Chart -->" ;
    static  String jsonPriceObject = "{\"prices\": [\n" +
            "        [\n" +
            "            1548784897889,\n" +
            "            3414.243628434992\n" +
            "        ],\n" +
            "        [\n" +
            "            1548788486159,\n" +
            "            3415.804191696545\n" +
            "        ],\n" +
            "        [\n" +
            "            1548792071658,\n" +
            "            3419.209604539774\n" +
            "        ],\n" +
            "        [\n" +
            "            1548795668912,\n" +
            "            3416.116676633346\n" +
            "        ],\n" +
            "        [\n" +
            "            1548799274519,\n" +
            "            3421.9981423930844\n" +
            "        ],\n" +
            "        [\n" +
            "            1548802815152,\n" +
            "            3418.303781020337\n" +
            "        ],\n" +
            "        [\n" +
            "            1548806422971,\n" +
            "            3415.8144466544527\n" +
            "        ],\n" +
            "        [\n" +
            "            1548810098576,\n" +
            "            3395.6040065036864\n" +
            "        ],\n" +
            "        [\n" +
            "            1548813682513,\n" +
            "            3401.8718060606666\n" +
            "        ],\n" +
            "        [\n" +
            "            1548817207362,\n" +
            "            3408.235207694797\n" +
            "        ],\n" +
            "        [\n" +
            "            1548820810391,\n" +
            "            3418.7158949463974\n" +
            "        ],\n" +
            "        [\n" +
            "            1548824486513,\n" +
            "            3423.840555402942\n" +
            "        ],\n" +
            "        [\n" +
            "            1548828195556,\n" +
            "            3434.4243140198314\n" +
            "        ],\n" +
            "        [\n" +
            "            1548831688625,\n" +
            "            3424.2675036303526\n" +
            "        ],\n" +
            "        [\n" +
            "            1548835204265,\n" +
            "            3421.910868097492\n" +
            "        ],\n" +
            "        [\n" +
            "            1548838888756,\n" +
            "            3433.2972805123545\n" +
            "        ],\n" +
            "        [\n" +
            "            1548842405406,\n" +
            "            3428.9599517458564\n" +
            "        ],\n" +
            "        [\n" +
            "            1548846068633,\n" +
            "            3437.0060419675874\n" +
            "        ],\n" +
            "        [\n" +
            "            1548849719105,\n" +
            "            3424.840046339178\n" +
            "        ],\n" +
            "        [\n" +
            "            1548853291838,\n" +
            "            3454.683079859979\n" +
            "        ],\n" +
            "        [\n" +
            "            1548856905016,\n" +
            "            3452.173853485669\n" +
            "        ],\n" +
            "        [\n" +
            "            1548860407914,\n" +
            "            3457.6279223854\n" +
            "        ],\n" +
            "        [\n" +
            "            1548864086889,\n" +
            "            3466.363412418714\n" +
            "        ],\n" +
            "        [\n" +
            "            1548867658064,\n" +
            "            3458.9606433683443\n" +
            "        ],\n" +
            "        [\n" +
            "            1548871293257,\n" +
            "            3465.8137425070818\n" +
            "        ],\n" +
            "        [\n" +
            "            1548874814722,\n" +
            "            3467.5342836353448\n" +
            "        ],\n" +
            "        [\n" +
            "            1548878426805,\n" +
            "            3458.509226466208\n" +
            "        ],\n" +
            "        [\n" +
            "            1548882017507,\n" +
            "            3454.136641951591\n" +
            "        ],\n" +
            "        [\n" +
            "            1548885673778,\n" +
            "            3455.7460347251617\n" +
            "        ],\n" +
            "        [\n" +
            "            1548889270572,\n" +
            "            3466.267674015678\n" +
            "        ],\n" +
            "        [\n" +
            "            1548892834628,\n" +
            "            3461.668252113643\n" +
            "        ],\n" +
            "        [\n" +
            "            1548896408321,\n" +
            "            3472.322094085972\n" +
            "        ],\n" +
            "        [\n" +
            "            1548900110861,\n" +
            "            3471.905966069453\n" +
            "        ],\n" +
            "        [\n" +
            "            1548903603669,\n" +
            "            3466.889610220917\n" +
            "        ],\n" +
            "        [\n" +
            "            1548907210134,\n" +
            "            3466.1114533134482\n" +
            "        ],\n" +
            "        [\n" +
            "            1548910815328,\n" +
            "            3465.052355018279\n" +
            "        ],\n" +
            "        [\n" +
            "            1548914581512,\n" +
            "            3468.0451648538588\n" +
            "        ],\n" +
            "        [\n" +
            "            1548918049809,\n" +
            "            3475.3028500872592\n" +
            "        ],\n" +
            "        [\n" +
            "            1548921685286,\n" +
            "            3478.4696230408786\n" +
            "        ],\n" +
            "        [\n" +
            "            1548925286611,\n" +
            "            3444.1704451349765\n" +
            "        ],\n" +
            "        [\n" +
            "            1548928810360,\n" +
            "            3439.345851315158\n" +
            "        ],\n" +
            "        [\n" +
            "            1548932501043,\n" +
            "            3427.0689186038294\n" +
            "        ],\n" +
            "        [\n" +
            "            1548936068485,\n" +
            "            3429.2098186886274\n" +
            "        ],\n" +
            "        [\n" +
            "            1548939755565,\n" +
            "            3440.5583334556336\n" +
            "        ],\n" +
            "        [\n" +
            "            1548943209822,\n" +
            "            3441.516500177948\n" +
            "        ],\n" +
            "        [\n" +
            "            1548946809217,\n" +
            "            3428.8849464344144\n" +
            "        ],\n" +
            "        [\n" +
            "            1548950475464,\n" +
            "            3439.077240676446\n" +
            "        ],\n" +
            "        [\n" +
            "            1548954104781,\n" +
            "            3431.0954692624377\n" +
            "        ],\n" +
            "        [\n" +
            "            1548954231000,\n" +
            "            3426.7722167821335\n" +
            "        ]\n" +
            "    ]}" ;


    public static List<Entry> getFormattedList() {
        List<Entry> listOfItems = new ArrayList<>() ;
        try{
            JSONObject jsonObject = new JSONObject(jsonPriceObject) ;
            JSONArray priceArray = jsonObject.getJSONArray("prices") ;
            for(int i = 0 ; i< priceArray.length() ; i++){
                JSONArray insideArray = priceArray.getJSONArray(i) ;
                String date = insideArray.getString(0) ;
                String price = insideArray.getString(1) ;
                listOfItems.add(new Entry(Float.valueOf(date), Float.valueOf(price))) ;
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
        return listOfItems ;
    }





    public static List<Entry> getFormattedList_from_ResponseData(String response){
        List<Entry> listOfItems = new ArrayList<>() ;
        try{
            JSONObject responseObj = new JSONObject(response) ;
            JSONArray priceArray = responseObj.getJSONArray("prices") ;
            for(int i = 0 ; i< priceArray.length() ; i++){
                JSONArray insideArray = priceArray.getJSONArray(i) ;
                String date = insideArray.getString(0) ;
                String price = insideArray.getString(1) ;
                listOfItems.add(new Entry(Float.valueOf(date), Float.valueOf(price))) ;
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
        Log.e(LOG_TAG, "Size of list of items is " + listOfItems.size()) ;
        return listOfItems ;
    }
}
