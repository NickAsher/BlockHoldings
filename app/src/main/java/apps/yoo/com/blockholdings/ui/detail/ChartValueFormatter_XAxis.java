package apps.yoo.com.blockholdings.ui.detail;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ChartValueFormatter_XAxis implements IAxisValueFormatter {

    private SimpleDateFormat sdf;

    public static final int TYPE_1DAY = 1 ;
    public static final int TYPE_3DAY = 2 ;
    public static final int TYPE_WEEK = 3 ;
    public static final int TYPE_MONTH = 4 ;
    public static final int TYPE_6MONTH = 5 ;
    public static final int TYPE_YEAR = 6 ;
    public static final int TYPE_MAX = 7 ;



    public ChartValueFormatter_XAxis(int type) {

        // format values to 1 decimal digit
        switch (type){
            case TYPE_1DAY:
                sdf = new SimpleDateFormat("hh:mm a");
                break;
            case TYPE_3DAY :
                sdf = new SimpleDateFormat("dd-MMM hh:a");
                break;
            case TYPE_WEEK :
                sdf = new SimpleDateFormat("dd-MMM");
                break;
            case TYPE_MONTH :
                sdf = new SimpleDateFormat("dd-MMM");
                break;
            case TYPE_6MONTH :
                sdf = new SimpleDateFormat("MMM-yyyy");
                break;
            case TYPE_YEAR :
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