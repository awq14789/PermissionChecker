package cn.yayi365.permission;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by awq14 on 2017/6/26.
 */

public final class PermissionChecker {
    private Object object;
    private String currentPermission;
    private PermissionUtil util;

    private PermissionChecker(Object object, String currentPermission, PermissionUtil util) {
        this.object = object;
        this.currentPermission = currentPermission;
        this.util = util;
    }

    public void check() {
        util.inject(object, currentPermission);
    }

    public static class Builder {

        private Object obj;
        private PermissionUtil util;
        private String permission;

        public Builder() {
        }

        public Builder(Activity activity) {
            this.obj = activity;
        }

        public Builder(Fragment fragment) {
            this.obj = fragment;
        }

        public Builder(View view) {
            this.obj = view;
        }

        public Builder activity(Activity activity) {
            this.obj = activity;
            return this;
        }

        public Builder fragment(Fragment fragment) {
            this.obj = fragment;
            return this;
        }

        public Builder view(View view) {
            this.obj = view;
            return this;
        }

        public Builder permissionUtil(PermissionUtil util) {
            this.util = util;
            return this;
        }

        public Builder currentPermission(String permission) {
            this.permission = permission;
            return this;
        }

        PermissionChecker build() {
            if (null == obj) {
                throw new IllegalStateException("实体不能为空");
            }
            if (TextUtils.isEmpty(permission)) {
                throw new IllegalStateException("权限不能为空");
            }
            if (null == util) {
                throw new IllegalStateException("工具类不能为空");
            }

            return new PermissionChecker(obj, permission, util);
        }

        public void check() {
            build().check();
        }

    }
}
