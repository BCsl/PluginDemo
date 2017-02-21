package github.hellocsl.demo.proxyplugin;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by chensuilun on 2017/2/21.
 */
public class BaseProxyActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    protected Activity that;  //指向真实启动的Activity

    /**
     * 将代理Activity传给插件Activity
     *
     * @param proxyActivity
     */
    public void setProxy(Activity proxyActivity) {
        that = proxyActivity;
    }

    //由于插件Activity已经不是真正意义上的Activity了，只能把布局给ProxyActivity来显示
    @Override
    public void setContentView(int layoutResID) {
        that.setContentView(layoutResID);
    }

    @Override
    public View findViewById(@IdRes int id) {
        return that.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: ");
        }
    }

    @Override
    protected void onStart() {
//        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStart: ");
        }
    }

    @Override
    protected void onResume() {
//        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onResume: ");
        }
    }


    @Override
    protected void onPause() {
//        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPause: ");
        }
    }

    @Override
    protected void onStop() {
//        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStop: ");
        }
    }

    @Override
    protected void onDestroy() {
//        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDestroy: ");
        }
    }

    @Override
    public Resources getResources() {
        return that.getResources() == null ? super.getResources() : that.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return that.getAssets() == null ? super.getAssets() : that.getAssets();
    }

    @Override
    public Resources.Theme getTheme() {
        return that.getTheme() == null ? super.getTheme() : that.getTheme();
    }
}
