package info.androidhive.muteme;

import info.androidhive.muteme.adapter.TabsPagerAdapter;
import info.androidhive.muteme.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {


	ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this, ServiceTest.class));

		pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));


	}
}