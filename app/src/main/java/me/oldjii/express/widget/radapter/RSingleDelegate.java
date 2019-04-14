package me.oldjii.express.widget.radapter;

/**
 * Created by oldjii on 2019/4
 */
public class RSingleDelegate<T> implements RAdapterDelegate<T> {
    private Class<? extends RViewHolder<T>> clazz;

    public RSingleDelegate(Class<? extends RViewHolder<T>> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<? extends RViewHolder<T>> getViewHolderClass(int position) {
        return clazz;
    }
}
