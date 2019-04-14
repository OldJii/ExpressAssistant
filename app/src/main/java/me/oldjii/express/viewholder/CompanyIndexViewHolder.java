package me.oldjii.express.viewholder;

import android.view.View;
import android.widget.TextView;

import me.oldjii.express.R;
import me.oldjii.express.model.CompanyEntity;
import me.oldjii.express.utils.binding.Bind;
import me.oldjii.express.widget.radapter.RLayout;
import me.oldjii.express.widget.radapter.RViewHolder;

/**
 * Created by oldjii on 2019/4
 */
@RLayout(R.layout.view_holder_company_index)
public class CompanyIndexViewHolder extends RViewHolder<CompanyEntity> {
    @Bind(R.id.tv_index)
    private TextView tvIndex;

    public CompanyIndexViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void refresh() {
        tvIndex.setText(data.getName());
    }
}
