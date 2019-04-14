package me.oldjii.express.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import me.oldjii.express.R;
import me.oldjii.express.activity.CompanyActivity;
import me.oldjii.express.activity.ResultActivity;
import me.oldjii.express.constants.RequestCode;
import me.oldjii.express.model.CompanyEntity;
import me.oldjii.express.model.SearchInfo;
import me.oldjii.express.utils.binding.Bind;
import me.oldjii.express.widget.radapter.RLayout;
import me.oldjii.express.widget.radapter.RViewHolder;

/**
 * Created by oldjii on 2019/4
 */
@RLayout(R.layout.view_holder_suggestion)
public class SuggestionViewHolder extends RViewHolder<CompanyEntity> implements View.OnClickListener {
    @Bind(R.id.tv_suggestion)
    private TextView tvSuggestion;

    public SuggestionViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void refresh() {
        tvSuggestion.setText(Html.fromHtml(data.getName()));
    }

    @Override
    public void onClick(View v) {
        if (position == adapter.getDataList().size() - 1) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(new Intent(activity, CompanyActivity.class), RequestCode.REQUEST_COMPANY);
            return;
        }
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setPost_id((String) adapter.getTag());   //在Edit框输入的时候，传值给adapter的Tag，下载又传给Post_id
        searchInfo.setCode(data.getCode());                 //公司代码
        searchInfo.setName(data.getName());
        searchInfo.setLogo(data.getLogo());
        ResultActivity.start(context, searchInfo);          //调用静态start()方法
    }
}
