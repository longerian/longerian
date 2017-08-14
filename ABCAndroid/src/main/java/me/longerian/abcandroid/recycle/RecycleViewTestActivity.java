package me.longerian.abcandroid.recycle;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 15/7/1.
 */
public class RecycleViewTestActivity extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SimpleAdapter mAdapter;

    private Button mAdd;
    private Button mDelete;
    private Button mMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview_test);
        mockDatas.addAll(init);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mLayoutManager = new MultiLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mLayoutManager = new GridLayoutManager(this, 2);
//        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SimpleAdapter(mockDatas, LayoutInflater.from(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListener(mListener);
        mAdd = (Button) findViewById(R.id.add);
        mAdd.setOnClickListener(mAction);
        mDelete = (Button) findViewById(R.id.delete);
        mDelete.setOnClickListener(mAction);
        mMove = (Button) findViewById(R.id.move);
        mMove.setOnClickListener(mAction);
        mRecyclerView.setChildDrawingOrderCallback(new SimpleChildDrawingOrder());
    }

    private View.OnClickListener mAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    mAdapter.addItem("Haha  new", 5);
                    break;
                case R.id.delete:
                    mAdapter.removeItem("Haha  new");
                    break;
                case R.id.move:
                    mAdapter.moveItem(1, 5);
                    break;
            }
        }
    };

    private OnRecyclerViewItemClickListener mListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, String data) {
            Log.d("Longer", data);
        }
    };

    public enum ITEM_TYPE {
        TEXT,
        IMAGE
    }

    private class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            View.OnClickListener {

        private List<String> mData;
        private OnRecyclerViewItemClickListener mListener;
        private LayoutInflater mLayoutInflater;

        public SimpleAdapter(List<String> data, LayoutInflater layoutInflater) {
            this.mData = data;
            this.mLayoutInflater = layoutInflater;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == ITEM_TYPE.TEXT.ordinal()) {
                View view = mLayoutInflater.inflate(R.layout.recyler_text_item, viewGroup, false);
                SimpleViewHolder svh = new SimpleViewHolder(view);
                view.setOnClickListener(this);
                return svh;
            } else if (viewType == ITEM_TYPE.IMAGE.ordinal()) {
                View view = mLayoutInflater.inflate(R.layout.recyler_image_item, viewGroup, false);
                ImageViewHolder ivh = new ImageViewHolder(view);
                view.setOnClickListener(this);
                return ivh;
            } else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder simpleViewHolder, int positoin) {
            int viewType = getItemViewType(positoin);
            if (viewType == ITEM_TYPE.TEXT.ordinal()) {
                SimpleViewHolder svh = (SimpleViewHolder) simpleViewHolder;
                if (positoin % 2 == 0) {
                    svh.itemView.setBackgroundColor(Color.WHITE);
                } else {
                    svh.itemView.setBackgroundColor(Color.LTGRAY);
                }
                svh.mTextView.setText(mData.get(positoin));
                svh.itemView.setTag(mData.get(positoin)
                        + " layout position " + simpleViewHolder.getLayoutPosition()
                        + " adapter position " + simpleViewHolder.getAdapterPosition());

                svh.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            ViewCompat.animate(v).scaleX(1.17f).scaleY(1.17f).start();
                            ViewGroup parent = (ViewGroup) v.getParent();
                            parent.requestLayout();
                            parent.invalidate();
                        }
                    }
                });
            } else if (viewType == ITEM_TYPE.IMAGE.ordinal()) {
                ImageViewHolder ivh = (ImageViewHolder) simpleViewHolder;
                ivh.mImageView.setImageURI(Uri.parse(mData.get(positoin)));
                ivh.mImageView.setTag(mData.get(positoin));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? ITEM_TYPE.IMAGE.ordinal() : ITEM_TYPE.TEXT.ordinal();
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void setListener(
                OnRecyclerViewItemClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, (String) v.getTag());
            }
        }

        public void addItem(String data, int position) {
            mData.add(position, data);
            notifyItemInserted(position);
        }

        public void removeItem(String data) {
            int position = mData.indexOf(data);
            if (position != -1) {
                mData.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void moveItem(int fromPosition, int toPosition) {
            int size = getItemCount();
            if (fromPosition >= 0 && fromPosition <= size - 1
                    && toPosition >= 0 && toPosition <= size - 1) {
                String src = mData.remove(fromPosition);
                mData.add(toPosition, src);
                notifyItemMoved(fromPosition, toPosition);
            }
        }

    }

    private class SimpleChildDrawingOrder implements RecyclerView.ChildDrawingOrderCallback {

        int focusid;

        @Override
        public int onGetChildDrawingOrder(int childCount, int i) {
            View focusedChild = mRecyclerView.getFocusedChild();
            int focusViewIndex = mRecyclerView.indexOfChild(focusedChild);
            if (focusViewIndex == -1) {
                return i;
            }
            if (focusViewIndex == i) {
                focusid = i;
                return childCount - 1;
            } else if (i == childCount - 1) {
                return focusid;
            } else {
                return i;
            }
        }
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    private List<String> mockDatas = new ArrayList<String>();

    private List<String> init =
            Arrays.asList(new String[] {
            "/sdcard/foursquare.png",
            "Recycler",
            "View",
            "Test",
            "Activity",
            "extents",
            "Activity",
            "private",
            "RecyclerView",
            "MlayoutManager",
            "mAdapter",
            "mockDatas",
            "arrays",
            "aslist",
            "new",
            "string[]",
            "onCreate",
            "BUndle",
            "savedInstanceState",
            "Recycler",
            "View",
            "Test",
            "Activity",
            "extents",
            "Activity",
            "private",
            "RecyclerView",
            "MlayoutManager",
            "mAdapter",
            "mockDatas",
            "arrays",
            "aslist",
            "new",
            "string[]",
            "onCreate",
            "BUndle",
            "savedInstanceState",
            "Recycler",
            "View",
            "Test",
            "Activity",
            "extents",
            "Activity",
            "private",
            "RecyclerView",
            "MlayoutManager",
            "mAdapter",
            "mockDatas",
            "arrays",
            "aslist",
            "new",
            "string[]",
            "onCreate",
            "BUndle",
            "savedInstanceState",
    });


}
