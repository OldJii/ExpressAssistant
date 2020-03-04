package me.oldjii.express.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import me.oldjii.express.R;
import me.oldjii.express.database.History;
import me.oldjii.express.utils.DataManager;
import me.oldjii.express.utils.PermissionReq;
import me.oldjii.express.utils.SnackbarUtils;
import me.oldjii.express.utils.binding.Bind;
import me.oldjii.express.viewholder.HistoryViewHolder;
import me.oldjii.express.widget.radapter.RAdapter;
import me.oldjii.express.widget.radapter.RSingleDelegate;

public class ExpressActivity extends BaseActivity implements OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    @Bind(R.id.tv_search)
    private TextView tvSearch;
    @Bind(R.id.tv_sweep)
    private TextView tvSweep;
    @Bind(R.id.rv_un_check)
    private RecyclerView rvUnCheck;
    @Bind(R.id.tv_empty)
    private TextView tvEmpty;

    private List<History> unCheckList = new ArrayList<>();
    private RAdapter<History> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navigationView.setNavigationItemSelectedListener(this);
        tvSearch.setOnClickListener(this);
        tvSweep.setOnClickListener(this);

        adapter = new RAdapter<>(unCheckList, new RSingleDelegate<>(HistoryViewHolder.class));
        rvUnCheck.setLayoutManager(new LinearLayoutManager(this));
        rvUnCheck.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvUnCheck.setAdapter(adapter);
    }

    @Override
    protected boolean shouldSetStatusBarColor() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<History> unCheckList = DataManager.getInstance().getUnCheckList();
        this.unCheckList.clear();
        this.unCheckList.addAll(unCheckList);
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(this.unCheckList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.tv_sweep:
                startCaptureActivity();
                break;
            default:
                break;
        }
    }

    //打开相机 / 动态申请权限
    private void startCaptureActivity() {
        PermissionReq.with(this)
                .permissions(Manifest.permission.CAMERA)
                .result(new PermissionReq.Result() {
                    @Override
                    public void onGranted() {
                        CaptureActivity.start(ExpressActivity.this, false, 0);
                    }

                    @Override
                    public void onDenied() {
                        SnackbarUtils.show(ExpressActivity.this, "没有相机权限，无法打开扫一扫！");
                    }
                })
                .request();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        new Handler().postDelayed(() -> item.setChecked(false), 500);
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_history:
                intent.setClass(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_about:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}
