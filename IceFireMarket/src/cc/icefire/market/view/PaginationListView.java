package cc.icefire.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import cc.icefire.market.R;

import com.foound.widget.AmazingListView;

public class PaginationListView extends AmazingListView {

	private View loadingView;
	private boolean hasLoadedData;
	
	public PaginationListView(Context context) {
		super(context);
		init(context);
	}
	
	public PaginationListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public PaginationListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		hasLoadedData = false;
		loadingView = LayoutInflater.from(context).inflate(R.layout.widget_loading_more, null);
		setLoadingView(loadingView);
	}
	
	public boolean hasLoadedData() {
		return hasLoadedData;
	}
	
	public void notifyHasLoadedData() {
		hasLoadedData = true;
	}
	
	public void clearHasLoadedData() {
		hasLoadedData = false;
	}

}
