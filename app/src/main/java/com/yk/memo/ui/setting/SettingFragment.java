package com.yk.memo.ui.setting;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.yk.eventposter.EventPoster;
import com.yk.markdown.style.MdStyleManager;
import com.yk.memo.R;
import com.yk.memo.data.event.MdStyleChangeEvent;
import com.yk.memo.utils.SpManager;

public class SettingFragment extends PreferenceFragmentCompat {
    private ListPreference markdownListPref;
    private Preference customMarkdownPref;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey);
        findPref();
        initData();
        bindEvent();
    }

    private void findPref() {
        markdownListPref = findPreference("key_markdown_style");
        customMarkdownPref = findPreference("custom_markdown");
    }

    private void initData() {
        showCustomMarkdownPref(SpManager.getInstance().getMarkdownStyle());
    }

    private void bindEvent() {
        markdownListPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String style = (String) newValue;

                showCustomMarkdownPref(style);

                MdStyleManager.getInstance().choose(getContext(), style);

                EventPoster.getInstance().post(new MdStyleChangeEvent(style));
                return true;
            }
        });
    }

    private void showCustomMarkdownPref(String style) {
        customMarkdownPref.setVisible("Custom".equals(style));
    }
}
