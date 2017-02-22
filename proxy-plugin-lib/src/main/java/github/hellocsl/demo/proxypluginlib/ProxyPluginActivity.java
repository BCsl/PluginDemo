package github.hellocsl.demo.proxypluginlib;

import android.app.Activity;
import android.os.Bundle;

/**
 * 代理模式下约束好的插件化的Activity
 * 插件Dex
 * Created by chensuilun on 2017/2/22.
 */
public interface ProxyPluginActivity {

    void setProxy(Activity proxy);

    /**
     * {@link android.app.Activity#onCreate(Bundle)}
     *
     * @param savedInstanceState
     */
    void onPluginCreate(Bundle savedInstanceState);

    /**
     * {@link Activity#onStart()}
     */
    void onPluginStart();

    /**
     * {@link Activity#onResume()}
     */
    void onPluginResume();

    /**
     * {@link Activity#onPause()}
     */
    void onPluginPause();

    /**
     * {@link Activity#onStop()}
     */
    void onPluginStop();

    /**
     * {@link Activity#onDestroy()}
     */
    void onPluginDestroy();
}
