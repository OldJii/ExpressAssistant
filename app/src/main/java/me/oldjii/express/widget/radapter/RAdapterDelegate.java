package me.oldjii.express.widget.radapter;

/**
 * Created by oldjii on 2019/4
 */
public interface RAdapterDelegate<T> {
    Class<? extends RViewHolder<T>> getViewHolderClass(int position);
}
