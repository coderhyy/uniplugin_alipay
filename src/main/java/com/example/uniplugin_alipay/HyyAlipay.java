package com.example.uniplugin_alipay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.OpenAuthTask;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class HyyAlipay extends UniModule {

    private static JSONObject bundleToString(Bundle bundle) {
        final JSONObject data = new JSONObject();
        if (bundle == null) {
            return data;
        }

        for (String key : bundle.keySet()) {
            data.put(key, bundle.get(key));
        }
        return data;
    }

    /**
     * 通用跳转授权业务 Demo
     */
    @UniJSMethod(uiThread = false)
    public void openAuthScheme(JSONObject options, UniJSCallback callback) {
        // 传递给支付宝应用的业务参数
        final Map<String, String> bizParams = new HashMap<>();
        bizParams.put("url", String.format("https://authweb.alipay.com/auth?auth_type=PURE_OAUTH_SDK&app_id=%s&scope=auth_user&state=init", options.getString("appId")));
        // 支付宝回跳到您的应用时使用的 Intent Scheme。
        // 请设置为一个不和其它应用冲突的值，并在 AndroidManifest.xml 中为 AlipayResultActivity 的 android:scheme 属性
        // 指定相同的值。实际使用时请勿设置为 __alipaysdkdemo__ 。
        // 如果不设置，OpenAuthTask.execute() 在用户未安装支付宝，使用网页完成业务流程后，将无法回跳至您的应用。
        final String scheme = options.getString("scheme");
        // 防止在支付宝客户端被强行退出等意外情况下，OpenAuthTask.Callback 一定时间内无法释放，导致
        // Activity 泄漏
        final WeakReference<HyyAlipay> ctxRef = new WeakReference<>(this);
        // 唤起授权业务
        final OpenAuthTask task = new OpenAuthTask((Activity) mUniSDKInstance.getContext());
        task.execute(
                scheme,    // Intent Scheme
                OpenAuthTask.BizType.AccountAuth, // 业务类型
                bizParams, // 业务参数
                new OpenAuthTask.Callback() {
                    @Override
                    public void onResult(int i, String s, Bundle bundle) {
                        final HyyAlipay ref = ctxRef.get();
                        if (ref != null) {
                            Log.v("log", String.format("结果码: %s\n结果信息: %s\n结果数据: %s", i, s, bundleToString(bundle)));
                            callback.invoke(bundleToString(bundle));
                        }
                    }
                }, // 业务结果回调
                true); // 是否需要在用户未安装支付宝客户端时，使用 H5 中间页中转。建议设置为 true。
    }
}
