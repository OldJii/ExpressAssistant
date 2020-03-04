package me.oldjii.express.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import me.oldjii.express.BuildConfig;
import me.oldjii.express.R;

/**
 * 关于页面Activity
 *  - 关于页面Fragment
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //将内层布局动态替换为Fragment
        getFragmentManager().beginTransaction().replace(R.id.ll_fragment_container, new AboutFragment()).commit();
    }

    /**
     * 关于页面Fragment（继承PreferenceFragment）
     *  - 点击事件（直接intent实现）
     */
    public static class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private Preference mVersion;
        private Preference mStar;
        private Preference mYouxiang;
        private Preference mBoke;
        private Preference mGithub;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //导入Preferences的布局文件
            addPreferencesFromResource(R.xml.preference_about);
            
            mVersion = findPreference("version");
            mStar = findPreference("star");
            mYouxiang = findPreference("youxiang");
            mBoke = findPreference("boke");
            mGithub = findPreference("github");

            mVersion.setSummary("v " + BuildConfig.VERSION_NAME);
            setListener();
        }

        private void setListener() {
            mStar.setOnPreferenceClickListener(this);
            mBoke.setOnPreferenceClickListener(this);
            mGithub.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
           if (preference == mStar) {
                openUrl(getString(R.string.about_project_url));
                return true;
            } else if (preference == mBoke || preference == mGithub) {
                openUrl(preference.getSummary().toString());
                return true;
            }
            return false;
        }

        private void openUrl(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }
}
