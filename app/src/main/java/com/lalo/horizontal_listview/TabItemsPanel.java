package com.mqunar.atom.flight.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.mqunar.atom.flight.R;
import com.mqunar.atom.flight.utils.Action;
import com.mqunar.framework.utils.inject.From;
import com.mqunar.framework.utils.inject.Injector;
import com.mqunar.framework.view.listener.QOnClickListener;
import com.mqunar.tools.log.UELog;

import java.util.List;

/**
 * Created by lalo.zhang on 2014/8/28.
 */
public class TabItemsPanel extends HorizontalScrollView {

	private int itemMinWidth;
	private List<ITabItemView> views;
	private Action<ITabItemView> onItemChoosedListener;

	@From(R.id.atom_flight_panelItems)
	private ViewGroup panelItems;

	public TabItemsPanel(Context context) {
		super(context);
		initialize();
	}

	public TabItemsPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public TabItemsPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public void activeTab(String id) {
		if(views!=null){
			for(ITabItemView itemView : views) {
				if(id.equals(itemView.getIden())) {
					showItemSelected(itemView);
					WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
					DisplayMetrics dm = new DisplayMetrics();
					wm.getDefaultDisplay().getMetrics(dm);
					int[] xy = new int[2];
					((View)itemView).getLocationOnScreen(xy);
					if(xy[0]<0){
						this.smoothScrollBy(xy[0],0);
					}

					if(xy[0]>dm.widthPixels){

						this.smoothScrollBy(xy[0]-dm.widthPixels+((View)itemView).getWidth(),0);
					}
					return;
				}
			}
		}

	}

	public void bindData(List<ITabItemView> views) {
		bindData(views, 0);
	}

	public void bindDataForNum(List<ITabItemView> views, int numPerVisibleLine) {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int tabNum = views.size() < numPerVisibleLine ? views.size() : numPerVisibleLine;
		bindData(views, width / tabNum, false);
	}

	public void bindData(List<ITabItemView> views, int itemMinWidth) {
		bindData(views, itemMinWidth, false);
	}

	public void bindData(List<ITabItemView> views, int itemMinWidth, boolean isFixedWidth) {
		panelItems.removeAllViews();
		this.views = views;
		this.itemMinWidth = itemMinWidth;
		for(final ITabItemView view : views) {
			final QOnClickListener qlistener = new QOnClickListener();
			View itemView = view.getItemView();
            new UELog(getContext()).setUELogtoTag(itemView, view.getIden());
			itemView.setMinimumWidth(itemMinWidth);
			LinearLayout.LayoutParams itemLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			itemView.setMinimumWidth(itemMinWidth);
			if(isFixedWidth) {
				itemLp.width = itemMinWidth;
			}
			panelItems.addView(itemView, itemLp);
			itemView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showItemSelected(view);
					if(onItemChoosedListener != null) {
						onItemChoosedListener.execute(view);
					}
					qlistener.onClick(v);
				}
			});
		}
	}

	private void showItemSelected(ITabItemView view) {
		for(final ITabItemView item : views) {
			if(item == view) {
				item.onSelected();
			} else {
				item.onUnSelected();
			}
		}
	}

	public void setOnItemClickListener(Action<ITabItemView> onItemChoosedListener) {
		this.onItemChoosedListener = onItemChoosedListener;
	}

	private void initialize() {
		Context context = getContext();
		LayoutInflater.from(getContext()).inflate(R.layout.atom_flight_ctl_tabitemspanel, this);
		Injector.inject(this);
		super.setHorizontalScrollBarEnabled(false);
	}
}