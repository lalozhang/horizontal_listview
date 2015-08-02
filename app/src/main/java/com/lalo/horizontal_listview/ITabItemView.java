package com.mqunar.atom.flight.view;

import android.view.View;

/**
 * Created by lalo.zhang on 2014/8/28.
 */
public interface ITabItemView {
	String getIden();

	View getItemView();

	void onSelected();

	void onUnSelected();
}
