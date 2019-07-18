package apps.yoo.com.blockholdings.ui.detail;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import apps.yoo.com.blockholdings.util.Constants;

public class ChartValueFormatter_XAxis implements IAxisValueFormatter {

    private SimpleDateFormat sdf;





    public ChartValueFormatter_XAxis(int type) {

        // format values to 1 decimal digit
        switch (type){
            case Constants.TIMEFRAME_1DAY:
                sdf = new SimpleDateFormat("hh:mm a");
                break;
            case Constants.TIMEFRAME_3DAY :
                sdf = new SimpleDateFormat("dd-MMM hh:a");
                break;
            case Constants.TIMEFRAME_1WEEK :
                sdf = new SimpleDateFormat("dd-MMM");
                break;
            case Constants.TIMEFRAME_1MONTH :
                sdf = new SimpleDateFormat("dd-MMM");
                break;
            case Constants.TIMEFRAME_6MONTH :
                sdf = new SimpleDateFormat("MMM-yyyy");
                break;
            case Constants.TIMEFRAME_1YEAR :
                sdf = new SimpleDateFormat("MMM-yyyy");
                break;
            default :
                sdf = new SimpleDateFormat("MMM-yyyy");
                break;
        }
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
//        value = value/1000 ;
        return sdf.format(new Date((long)value)) ;
    }

    /** this is only needed if numbers are returned, else return 0 */
//    @Override
//    public int getDecimalDigits() { return 0; }
}