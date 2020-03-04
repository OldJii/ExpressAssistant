package me.oldjii.express.utils.binding;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * 利用了Java反射，避免在代码中写大量的findviewbyid()的重复代码
 */

public class ViewBinder {

    public static void bind(Activity activity) {
        bind(activity, activity.getWindow().getDecorView());
    }

    //getDeclearFields：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段
    public static void bind(Object target, View source) {
        Field[] fields = target.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);  //CaptureActivity类中的成员变量有的为private,故必须进行此操作
                    if (field.get(target) != null) {    //Field.get(obj)返回，当前Field在相应对象中的值
                        continue;
                    }

                    Bind bind = field.getAnnotation(Bind.class);
                    if (bind != null) {
                        int viewId = bind.value();
                        field.set(target, source.findViewById(viewId));     //关键代码
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
