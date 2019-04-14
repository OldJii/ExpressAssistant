package me.oldjii.express.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.oldjii.express.R;
import me.oldjii.express.model.SearchResult;
import me.oldjii.express.utils.Utils;
import me.oldjii.express.utils.binding.Bind;
import me.oldjii.express.widget.radapter.RLayout;
import me.oldjii.express.widget.radapter.RViewHolder;

/**
 * Created by oldjii on 2019/4
 */
@RLayout(R.layout.view_holder_search_result)
public class ResultViewHolder extends RViewHolder<SearchResult.ResultItem> {
    @Bind(R.id.line)
    private View line;
    @Bind(R.id.iv_logistics)
    private ImageView ivLogistics;
    @Bind(R.id.tv_time)
    private TextView tvTime;
    @Bind(R.id.tv_detail)
    private TextView tvDetail;

    public ResultViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void refresh() {
        tvTime.setText(data.getTime());
        tvDetail.setText(data.getContent());
        boolean first = (position == 0);
        line.setPadding(0, Utils.dp2px(context, first ? 12 : 0), 0, 0);
        ivLogistics.setSelected(first);
        tvTime.setSelected(first);
        tvDetail.setSelected(first);
    }
}
