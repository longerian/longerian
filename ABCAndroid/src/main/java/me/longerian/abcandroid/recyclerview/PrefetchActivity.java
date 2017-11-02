package me.longerian.abcandroid.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import me.longerian.abcandroid.R;

/**
 * Created by longerian on 2017/11/2.
 *
 * @author longerian
 * @date 2017/11/02
 */

public class PrefetchActivity extends Activity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclervie);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new NormalRecyclerViewAdapter(this));
    }

    public static class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalTextViewHolder> {
        private final LayoutInflater mLayoutInflater;
        private final Context mContext;

        public NormalRecyclerViewAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.recyler_text_item, parent, false));
        }

        @Override
        public void onBindViewHolder(NormalTextViewHolder holder, int position) {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            holder.mTextView.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return 100;
        }

    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        NormalTextViewHolder(View view) {
            super(view);
            mTextView = (TextView)view;
        }
    }

}
