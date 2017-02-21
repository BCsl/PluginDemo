package github.hellocsl.demo.plugindemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import github.hellocsl.demo.plugindemo.proxy.ProxyActivity;

public class MainActivity extends AppCompatActivity {

    public static final String PROXY_DEX_PATH = Environment.getExternalStorageDirectory() + File.separator + "PluginDemo" + File.separator + "proxy-plugin-debug.apk";
    public static final String DEX_PATH = Environment.getExternalStorageDirectory() + File.separator + "PluginDemo" + File.separator + "goldenwatch.zip";

    public static final String PROXY_CLASS = "github.hellocsl.demo.proxyplugin.TestProxyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_proxy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ProxyActivity.newIntent(MainActivity.this, PROXY_DEX_PATH, PROXY_CLASS));
            }
        });
    }
}
