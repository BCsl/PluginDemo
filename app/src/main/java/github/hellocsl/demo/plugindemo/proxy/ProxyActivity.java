package github.hellocsl.demo.plugindemo.proxy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;
import github.hellocsl.demo.plugindemo.BuildConfig;
import github.hellocsl.demo.proxypluginlib.ProxyPluginActivity;

/**
 * Created by chensuilun on 2017/2/21.
 * 如果继承AppCompactActivity，会出现You need to use a Theme.AppCompat theme (or descendant) with this activity.异常，跟Theme有关，暂时不深入了解
 */
public class ProxyActivity extends Activity {
    private static final String TAG = "ProxyActivity";
    public static final String EXTRA_DEX_PATH = "extra_dex_path";
    public static final String EXTRA_ACTIVITY_NAME = "extra_activity_name";


    private String mClass;
    private String mDexPath;
    private DexClassLoader mDexClassLoader;


    private HashMap<String, Method> mActivityLifeCircleMethods = new HashMap<String, Method>();

    private ProxyPluginActivity mRemoteActivity;

    /**
     * @param context
     * @param dexPath        插件dex路径
     * @param pluginActivity 启动的Activity的完整类名
     * @return
     */
    public static Intent newIntent(Context context, String dexPath, String pluginActivity) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newIntent() called with: dexPath = [" + dexPath + "], pluginActivity = [" + pluginActivity + "]");
        }
        Intent intent = new Intent(context, ProxyActivity.class);
        intent.putExtra(EXTRA_DEX_PATH, dexPath);
        intent.putExtra(EXTRA_ACTIVITY_NAME, pluginActivity);
        return intent;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "setContentView() called with: " + "layoutResID = [" + layoutResID + "]");
        }
        super.setContentView(layoutResID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDexPath = getIntent().getStringExtra(EXTRA_DEX_PATH);
        mClass = getIntent().getStringExtra(EXTRA_ACTIVITY_NAME);
        loadResources(mDexPath);
        Log.i(TAG, "代码路径：" + getPackageCodePath());
        Log.i(TAG, "资源路径：" + getPackageResourcePath());
        performLaunchActivity(savedInstanceState);
    }

    protected void instantiateLifeCircleMethods(Class<?> localClass) {

        String[] methodNames = new String[]{
                "onRestart",
                "onStart",
                "onResume",
                "onPause",
                "onStop",
                "onDestroy"
        };
        for (String methodName : methodNames) {
            Method method = null;
            try {
                method = localClass.getDeclaredMethod(methodName);
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            mActivityLifeCircleMethods.put(methodName, method);
        }

        Method onCreate = null;
        try {
            onCreate = localClass.getDeclaredMethod("onCreate", Bundle.class);
            onCreate.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        mActivityLifeCircleMethods.put("onCreate", onCreate);
    }


    /**
     * @return 插件包的ClassLoader
     */
    public DexClassLoader getDexClassLoader() {
        if (mDexClassLoader == null) {
            mDexClassLoader = new DexClassLoader(mDexPath, getDir("dex", 0)
                    .getAbsolutePath(), null, getClassLoader());
        }
        return mDexClassLoader;
    }


    //替换资源。
    private AssetManager mAssetManager;
    private Resources.Theme mTheme;

    protected void loadResources(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "loadResources error");
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }


    private Resources mResources;

    @Override
    public AssetManager getAssets() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getAssets: " + (mAssetManager == null));
        }
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getResources: " + (mResources == null));
        }
        return mResources == null ? super.getResources() : mResources;
    }


    @Override
    public Resources.Theme getTheme() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getTheme() called " + (mTheme == null));
        }
        return super.getTheme();
    }

    protected void performLaunchActivity(Bundle savedInstanceState) {
        try {
            Class<?> localClass = getDexClassLoader().loadClass(mClass);
            Constructor<?> localConstructor = localClass.getConstructor();
            Object instance = localConstructor.newInstance();
            //因为ClassLoader的父类委托机制，这里加载到的ProxyPluginActivity是和插件的同一个类
            mRemoteActivity = (ProxyPluginActivity) instance;
            mRemoteActivity.setProxy(this);
            mRemoteActivity.onPluginCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRemoteActivity.onPluginStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRemoteActivity.onPluginResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRemoteActivity.onPluginPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRemoteActivity.onPluginStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRemoteActivity.onPluginDestroy();
    }

    /**
     * @param methodName
     * @param arg
     */
    private void performLifeCircle(String methodName, Object... arg) {
        Method method = mActivityLifeCircleMethods.get(methodName);
        if (method != null) {
            try {
                if (arg != null && arg.length > 0) {
                    method.invoke(mRemoteActivity, arg);
                } else {
                    method.invoke(mRemoteActivity);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
