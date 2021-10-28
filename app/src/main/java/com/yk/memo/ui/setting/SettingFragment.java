package com.yk.memo.ui.setting;

import android.Manifest;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.yk.eventposter.EventPoster;
import com.yk.memo.R;
import com.yk.memo.data.event.MdStyleChangeEvent;
import com.yk.memo.utils.SpManager;
import com.yk.permissionrequester.PermissionFragment;
import com.yk.permissionrequester.PermissionRequester;

import java.util.List;

public class SettingFragment extends PreferenceFragmentCompat {
    private ListPreference markdownListPref;
    private Preference customMarkdownPref;
    private SwitchPreferenceCompat outputMarkdownPref;

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
        outputMarkdownPref = findPreference("key_output_markdown");
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

                EventPoster.getInstance().post(new MdStyleChangeEvent(style));
                return true;
            }
        });

        outputMarkdownPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;

                if (value) {
                    PermissionRequester.build(getActivity())
                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                            .request(new PermissionFragment.OnPermissionRequestListener() {
                                @Override
                                public void onRequestSuccess(boolean success) {
                                    outputMarkdownPref.setChecked(success);
                                }

                                @Override
                                public void onGrantedList(List<String> grantedList) {

                                }

                                @Override
                                public void onDeniedList(List<String> deniedList) {

                                }
                            });
                }

                return true;
            }
        });
    }

    private void showCustomMarkdownPref(String style) {
        customMarkdownPref.setVisible("Custom".equals(style));
    }
}
