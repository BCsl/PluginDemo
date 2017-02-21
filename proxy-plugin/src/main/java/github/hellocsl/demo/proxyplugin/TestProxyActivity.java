package github.hellocsl.demo.proxyplugin;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by chensuilun on 2017/2/21.
 */
public class TestProxyActivity extends BaseProxyActivity {
    private static final String TAG = "TestProxyActivity";
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: " + that.getResources().getIdentifier("activity_proxy", "layout", "github.hellocsl.demo.proxyplugin"));
        }
        setContentView(R.layout.activity_proxy);
        mTextView = (TextView) findViewById(R.id.proxy_tv);
        mTextView.setCompoundDrawablesWithIntrinsicBounds(that.getResources().getDrawable(R.drawable.ic_gp), null, null, null);
        mTextView.append("onCreate");
    }


    @Override
    protected void onStart() {
        super.onStart();
        mTextView.append("\nonStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextView.append("\nonResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mTextView.append("\nonPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTextView.append("\nonStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTextView.append("\nonDestroy");
    }
}
