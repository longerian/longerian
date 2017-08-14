package me.longerian.abcandroid.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import me.longerian.abcandroid.R;

/**
 * Created by longerian on 2017/8/14.
 *
 * @author longerian
 * @date 2017/08/14
 */

public class RecyclerViewChildOrderActivity extends Activity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclervie);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);

    }



}
