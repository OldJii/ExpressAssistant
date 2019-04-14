package me.oldjii.express.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import me.oldjii.express.BuildConfig;
import me.oldjii.express.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getFragmentManager().beginTransaction().replace(R.id.ll_fragment_container, new AboutFragment()).commit();
    }

    public static class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private Preference mVersion;
        private Preference mStar;
        private Preference mYouxiang;
        private Preference mBoke;
        private Preference mGithub;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_about);
            
            mVersion = findPreference("version");//必须先获取数据再修改，因为直接修改会报错“变量没有初始化”
            mStar = findPreference("star");
            mYouxiang = findPreference("youxiang");
            mBoke = findPreference("boke");
            mGithub = findPreference("github");

            mVersion.setSummary("v " + BuildConfig.VERSION_NAME);
            setListener();
        }

        private void setListener() {
            mStar.setOnPreferenceClickListener(this);
//            mYouxiang.setOnPreferenceClickListener(this);
            mBoke.setOnPreferenceClickListener(this);
            mGithub.setOnPreferenceClickListener(this);
        }

        //TODO:加上点击发送邮件功能
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
