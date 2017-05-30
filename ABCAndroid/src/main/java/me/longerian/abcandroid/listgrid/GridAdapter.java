package me.longerian.abcandroid.listgrid;

import me.longerian.abcandroid.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Wrapper adapter and make it like GridView
 * <hr>
 * notice : {@link OnGridItemClickListener} instead of {@link OnItemClickListener}
 * <br>
 * e.g: use {@link #setOnItemClickListener(OnGridItemClickListener)} instead of
 * {@link ListView#setOnItemClickListener(OnItemClickListener)}
 * 
 * @author Chaos	
 * @date 2013-2-21 
 */
public class GridAdapter<T extends BaseAdapter> extends BaseAdapter implements OnClickListener{

    /**
     * The wrapper content --> adapter
     */
    private T mAdapter = null;
    /**
     * The number columns
     */
    private int mColumns = 1;
    /**
     * The real onItemClickListener
     */
    private OnGridItemClickListener mGridListener;
    /**
     * The Context Object
     */
    private Context mContext;
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        if(mColumns == 1){
            
            View child = mAdapter.getView(position, convertView, parent);
            setOnItemClickListenerIfNeed(child, position, position);
            //no need
            return child;
        }
        
        return bindGridView(position, convertView, parent);
    }
    
    protected View bindGridView(int position, View convertView, ViewGroup parent){
        ViewGroup root = null;
        //cache the root
        if(convertView == null || !(convertView instanceof ViewGroup)){
            root = createRoot();
            root.setClickable(false);
            root.setFocusable(false);
        }else{
            root = (ViewGroup) convertView;
        }
        //columns = 3
        //pos --> real pos
        //0   --> 0 1 2
        //1   --> 3 4 5
        //2   --> 6 7 8
        //so do this
        return bindView(root, position);
    }
    
    protected ViewGroup bindView(ViewGroup root, int pos) {
        
        final int count = mAdapter.getCount();
        final int childCount = root.getChildCount();
        
        for (int i = 0; i < mColumns; i++) {
            // real position
            int index = mColumns * pos + i;
            
            if (index == count) {
                // by Chaos at 2012-12-17
                removeCacheChild(root, index);
                break;
            }else if(index > count){
                throw new UnknownError("unknowError");
            }
            
            View child = mAdapter.getView(index, root.getChildAt(i), root);

            setOnItemClickListenerIfNeed(child, pos, index);
            
            if (childCount == mColumns) {
                continue;
            } else {
                if (i == 0 && childCount != 0) {
                    root.removeAllViews();
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        // LinearLayout.LayoutParams.WRAP_CONTENT,
                        mContext.getResources().getDisplayMetrics().widthPixels / mColumns,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                child.setFocusable(true);
                child.setClickable(true);
                if (child instanceof ViewGroup) {
                    ((ViewGroup) child)
                            .setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                }
                root.addView(child, lp);
            }
        }
        return root;
    }
    
    /**
     * remove child view in cache of root
     */
    private void removeCacheChild(ViewGroup root,int index){
        int realColumns = (index - 1) % mColumns;
        int moveCount   = (mColumns - 1) - realColumns;
        
        for (int i = 0; i < moveCount; i++) {
            final View child = root.getChildAt((mColumns - 1 - i));
            if(child != null){
                root.removeView(child);
            }
        }
    }
    
    /**
     * create root for item of ListView
     */
    protected ViewGroup createRoot(){
        LinearLayout root = new LinearLayout(mContext);
        root.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        root.setLayoutParams(lp);
        return root;
    }
    
    private boolean setOnItemClickListenerIfNeed(View child,int pos,int realPos) {
        if(mGridListener == null){
            return false;
        }
        child.setOnClickListener(this);
        child.setTag(R.id.pos, pos);
        child.setTag(R.id.realPos, realPos);
        return true;
    }
    
    public void setOnItemClickListener(OnGridItemClickListener listener){
        this.mGridListener = listener;
    }
    
    public void setNumColumns(int columns){
        this.mColumns = columns;
    }
    
    public int getNumColumns(){
        return mColumns;
    }
    
    public GridAdapter(Context context,T adapter){
        this.mContext = context;
        this.mAdapter = adapter;
    }

    @Override
    public int getCount() {
        int count = (int) Math.ceil(mAdapter.getCount() / (double)mColumns);
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }
    
    public T getWrappedAdapter(){
        return mAdapter;
    }
    
    public static interface OnGridItemClickListener{
        public void onItemClick(int pos,int realPos);
    }

    @Override
    public void onClick(View v) {
        if(mGridListener == null){
            //no need to feedback
        }
        int pos     = (Integer) v.getTag(R.id.pos);
        int realPos = (Integer) v.getTag(R.id.realPos);
        
        mGridListener.onItemClick(pos, realPos);
    }
    
}
