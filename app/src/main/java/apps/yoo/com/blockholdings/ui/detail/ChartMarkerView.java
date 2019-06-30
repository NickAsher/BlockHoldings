package apps.yoo.com.blockholdings.ui.detail;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.util.MyGlobals;

public class ChartMarkerView extends MarkerView {

	private TextView tvContent;
	SimpleDateFormat sdf ;
	String currencySymbol ;

	public ChartMarkerView(Context context, int layoutResource) {
		super(context, layoutResource);

		// find your layout components
		tvContent = (TextView) findViewById(R.id.marketChartView_TextView_value);
		sdf = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
		currencySymbol = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencySymbol() ;
	}

	// callbacks everytime the MarkerView is redrawn, can be used to update the
	// content (user-interface)
	@Override
	public void refreshContent(Entry e, Highlight highlight) {


		String dateString = sdf.format(new Date((long)e.getX())) ;

		tvContent.setText("Date: " + dateString +  "\n Price: " + currencySymbol + e.getY());

		// this will perform necessary layouting
		super.refreshContent(e, highlight);
	}

	private MPPointF mOffset;

	@Override
	public MPPointF getOffset() {

		if(mOffset == null) {
			// center the marker horizontally and vertically
			mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
		}

		return mOffset;
	}
}