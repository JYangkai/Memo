package com.yk.memo.ui.setting;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.yk.base.eventposter.EventPoster;
import com.yk.markdown.style.MdStyleManager;
import com.yk.memo.R;
import com.yk.memo.data.event.MdStyleChangeEvent;

public class SettingFragment extends PreferenceFragmentCompat {
    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        ListPreference listPreference = findPreference("key_markdown_style");
        if (listPreference == null) {
            return;
        }
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int value = Integer.parseInt((String) newValue);
                MdStyleManager.Style style = MdStyleManager.Style.STANDARD;
                switch (value) {
                    case 0:
                        style = MdStyleManager.Style.STANDARD;
                        break;
                    case 1:
                        style = MdStyleManager.Style.TYPORA;
                        break;
                    case 2:
                        break;
                }
                MdStyleManager.getInstance().choose(style);

                EventPoster.getInstance().post(new MdStyleChangeEvent(style));
                return true;
            }
        });
    }
}
