package cn.yayi365.permission;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

/**
 * Created by awq14 on 2017/6/26.
 */

public abstract class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    private static final HashSet<Class<?>> IGNORED = new HashSet<>();

    static {
        IGNORED.add(Object.class);
        IGNORED.add(Activity.class);
        IGNORED.add(FragmentActivity.class);
        IGNORED.add(AppCompatActivity.class);
        IGNORED.add(android.app.Fragment.class);
        IGNORED.add(android.support.v4.app.Fragment.class);
    }

    void inject(Object object, String currentPermission) {
        injectObject(object, object.getClass(), currentPermission);
    }

    private void injectObject(Object handler, Class<?> handlerType, String currentPermission) {

        if (null == handler || IGNORED.contains(handlerType)) {
            return;
        }

        injectObject(handler, handlerType.getSuperclass(), currentPermission);
        Field[] fields = handlerType.getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                if (null == field) continue;
                Class<?> fieldType = field.getType();
                if (fieldType.isPrimitive()
                        || fieldType.isArray()
                        || Modifier.isStatic(field.getModifiers())
                        || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                Permission permission = field.getAnnotation(Permission.class);
                if (null != permission) {
                    try {
                        String value = permission.value();
                        Object view = field.get(handler);
                        onPermissionResult(view, checkPermission(value, currentPermission));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        Log.e(TAG, "injectObject: ", e);
                    }
                }
            }
        }
    }

    public abstract boolean checkPermission(String permission, String currentPermission);

    public abstract void onPermissionResult(Object obj, boolean isHasPermission);
}
