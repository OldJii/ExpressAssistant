package me.oldjii.express.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.oldjii.express.R;
import me.oldjii.express.database.History;
import me.oldjii.express.utils.DataManager;
import me.oldjii.express.utils.binding.Bind;
import me.oldjii.express.viewholder.HistoryViewHolder;
import me.oldjii.express.widget.radapter.RAdapter;
import me.oldjii.express.widget.radapter.RSingleDelegate;

public class HistoryActivity extends BaseActivity {
    @Bind(R.id.rv_history_list)
    private RecyclerView rvHistoryList;
    @Bind(R.id.tv_empty)
    private TextView tvEmpty;

    private List<History> historyList = new ArrayList<>();
    private RAdapter<History> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        adapter = new RAdapter<>(historyList, new RSingleDelegate<>(HistoryViewHolder.class));
        rvHistoryList.setLayoutManager(new LinearLayoutManager(this));
        rvHistoryList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvHistoryList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        List<History> historyList = DataManager.getInstance().getHistoryList();
        this.historyList.clear();
        this.historyList.addAll(historyList);
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(this.historyList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
