package info.androidhive.muteme;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

public class CustomDatePicker implements OnClickListener {

	private DatePicker datePicker;
	private ViewSwitcher viewSwitcher;

	private final int SET_DATE = 100, SET = 102, CANCEL = 103;

	private Button btn_setDate, btn_set, btn_cancel;

	private Calendar calendar_date = null;

	private Activity activity;

	private ICustomDateTimeListener iCustomDateTimeListener = null;

	private Dialog dialog;

	private boolean isAutoDismiss = true;


	public CustomDatePicker(Activity a,
	        ICustomDateTimeListener customDateTimeListener) {
	    activity = a;
	    iCustomDateTimeListener = customDateTimeListener;

	    dialog = new Dialog(activity);

	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    View dialogView = getDateTimePickerLayout();
	    dialog.setContentView(dialogView);
	}

	public View getDateTimePickerLayout() {
	    LinearLayout.LayoutParams linear_match_wrap = new LinearLayout.LayoutParams(
	            LinearLayout.LayoutParams.MATCH_PARENT,
	            LinearLayout.LayoutParams.MATCH_PARENT);
	    LinearLayout.LayoutParams linear_wrap_wrap = new LinearLayout.LayoutParams(
	            LinearLayout.LayoutParams.WRAP_CONTENT,
	            LinearLayout.LayoutParams.WRAP_CONTENT);
	    FrameLayout.LayoutParams frame_match_wrap = new FrameLayout.LayoutParams(
	            FrameLayout.LayoutParams.MATCH_PARENT,
	            FrameLayout.LayoutParams.WRAP_CONTENT);

	    LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(
	            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

	    LinearLayout linear_main = new LinearLayout(activity);
	    linear_main.setLayoutParams(linear_match_wrap);
	    linear_main.setOrientation(LinearLayout.VERTICAL);
	    linear_main.setGravity(Gravity.CENTER);

	    LinearLayout linear_child = new LinearLayout(activity);
	    linear_child.setLayoutParams(linear_wrap_wrap);
	    linear_child.setOrientation(LinearLayout.VERTICAL);

	    LinearLayout linear_top = new LinearLayout(activity);
	    linear_top.setLayoutParams(linear_match_wrap);

	    btn_setDate = new Button(activity);
	    btn_setDate.setLayoutParams(button_params);
	    btn_setDate.setText("Set Date");
	    btn_setDate.setId(SET_DATE);
	    btn_setDate.setOnClickListener(this);

	    linear_top.addView(btn_setDate);

	    viewSwitcher = new ViewSwitcher(activity);
	    viewSwitcher.setLayoutParams(frame_match_wrap);

	    datePicker = new DatePicker(activity);

	    viewSwitcher.addView(datePicker);

	    LinearLayout linear_bottom = new LinearLayout(activity);
	    linear_match_wrap.topMargin = 8;
	    linear_bottom.setLayoutParams(linear_match_wrap);

	    btn_set = new Button(activity);
	    btn_set.setLayoutParams(button_params);
	    btn_set.setText("Set");
	    btn_set.setId(SET);
	    btn_set.setOnClickListener(this);

	    btn_cancel = new Button(activity);
	    btn_cancel.setLayoutParams(button_params);
	    btn_cancel.setText("Cancel");
	    btn_cancel.setId(CANCEL);
	    btn_cancel.setOnClickListener(this);

	    linear_bottom.addView(btn_set);
	    linear_bottom.addView(btn_cancel);

	    linear_child.addView(linear_top);
	    linear_child.addView(viewSwitcher);
	    linear_child.addView(linear_bottom);

	    linear_main.addView(linear_child);

	    return linear_main;
	}

	public void showDialog() {
	    if (!dialog.isShowing()) {
	        if (calendar_date == null)
	            calendar_date = Calendar.getInstance();

	        datePicker.updateDate(calendar_date.get(Calendar.YEAR),
	                calendar_date.get(Calendar.MONTH),
	                calendar_date.get(Calendar.DATE));

	        dialog.show();

	        btn_setDate.performClick();
	    }
	}

	public void setAutoDismiss(boolean isAutoDismiss) {
	    this.isAutoDismiss = isAutoDismiss;
	}

	public void dismissDialog() {
	    if (!dialog.isShowing())
	        dialog.dismiss();
	}

	public void setDate(Calendar calendar) {
	    if (calendar != null)
	        calendar_date = calendar;
	}

	public void setDate(Date date) {
	    if (date != null) {
	        calendar_date = Calendar.getInstance();
	    }
	}

	public void setDate(int year, int month, int day) {
	    if (month < 12 && month >= 0 && day < 32 && day >= 0 && year > 100
	            && year < 3000) {
	        calendar_date = Calendar.getInstance();
	        calendar_date.set(year, month, day);
	    }

	}


	

	public interface ICustomDateTimeListener {
	    public void onSet(Dialog dialog, Calendar calendarSelected,
	            Date dateSelected, int year, String monthFullName,
	            String monthShortName, int monthNumber, int date,
	            String weekDayFullName, String weekDayShortName);

	    public void onCancel();
	}

	@Override
	public void onClick(View v) {
	    switch (v.getId()) {
	    case SET_DATE:
	        btn_setDate.setEnabled(false);
	        viewSwitcher.showNext();
	        break;

	    case SET:
	        if (iCustomDateTimeListener != null) {
	            int month = datePicker.getMonth();
	            int year = datePicker.getYear();
	            int day = datePicker.getDayOfMonth();


	            calendar_date.set(year, month, day);

	            iCustomDateTimeListener.onSet(dialog, calendar_date,
	                    calendar_date.getTime(), calendar_date
	                            .get(Calendar.YEAR),
	                    getMonthFullName(calendar_date.get(Calendar.MONTH)),
	                    getMonthShortName(calendar_date.get(Calendar.MONTH)),
	                    calendar_date.get(Calendar.MONTH), calendar_date
	                            .get(Calendar.DAY_OF_MONTH),
	                    getWeekDayFullName(calendar_date
	                            .get(Calendar.DAY_OF_WEEK)),
	                    getWeekDayShortName(calendar_date
	                            .get(Calendar.DAY_OF_WEEK)));
	        }
	        if (dialog.isShowing() && isAutoDismiss)
	            dialog.dismiss();
	        break;

	    case CANCEL:
	        if (iCustomDateTimeListener != null)
	            iCustomDateTimeListener.onCancel();
	        if (dialog.isShowing())
	            dialog.dismiss();
	        break;
	    }
	}
	public static String convertDate(String date, String fromFormat,
	        String toFormat) {
	    try {
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromFormat);
	        Date d = simpleDateFormat.parse(date);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(d);

	        simpleDateFormat = new SimpleDateFormat(toFormat);
	        simpleDateFormat.setCalendar(calendar);
	        date = simpleDateFormat.format(calendar.getTime());

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return date;
	}

	private String getMonthFullName(int monthNumber) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.MONTH, monthNumber);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
	    simpleDateFormat.setCalendar(calendar);
	    String monthName = simpleDateFormat.format(calendar.getTime());

	    return monthName;
	}

	private String getMonthShortName(int monthNumber) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.MONTH, monthNumber);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
	    simpleDateFormat.setCalendar(calendar);
	    String monthName = simpleDateFormat.format(calendar.getTime());

	    return monthName;
	}

	private String getWeekDayFullName(int weekDayNumber) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
	    simpleDateFormat.setCalendar(calendar);
	    String weekName = simpleDateFormat.format(calendar.getTime());

	    return weekName;
	}

	private String getWeekDayShortName(int weekDayNumber) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE");
	    simpleDateFormat.setCalendar(calendar);
	    String weekName = simpleDateFormat.format(calendar.getTime());

	    return weekName;
	}

	private void resetData() {
	    calendar_date = null;
	}

	public static String pad(int integerToPad) {
	    if (integerToPad >= 10 || integerToPad < 0)
	        return String.valueOf(integerToPad);
	    else
	        return "0" + String.valueOf(integerToPad);
	}
	}