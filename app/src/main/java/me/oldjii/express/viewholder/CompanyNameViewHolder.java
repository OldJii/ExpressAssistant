package me.oldjii.express.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.oldjii.express.R;
import me.oldjii.express.constants.Extras;
import me.oldjii.express.http.HttpClient;
import me.oldjii.express.model.CompanyEntity;
import me.oldjii.express.model.SearchInfo;
import me.oldjii.express.utils.binding.Bind;
import me.oldjii.express.widget.radapter.RLayout;
import me.oldjii.express.widget.radapter.RViewHolder;

/**
 * Created by oldjii on 2019/4
 */
@RLayout(R.layout.view_holder_company)
public class CompanyNameViewHolder extends RViewHolder<CompanyEntity> implements View.OnClickListener {
    @Bind(R.id.iv_logo)
    private ImageView ivLogo;
    @Bind(R.id.tv_name)
    private TextView tvName;

    public CompanyNameViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void refresh() {
        Glide.with(context)
                .load(HttpClient.urlForLogo(data.getLogo()))
                .dontAnimate()
                .placeholder(R.drawable.ic_default_logo)
                .into(ivLogo);
        tvName.setText(data.getName());
    }

    @Override
    public void onClick(View v) {
        Activity activity = (Activity) context;
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setName(data.getName());
        searchInfo.setLogo(data.getLogo());
        searchInfo.setCode(data.getCode());
        Intent intent = new Intent();
        intent.putExtra(Extras.SEARCH_INFO, searchInfo);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}
