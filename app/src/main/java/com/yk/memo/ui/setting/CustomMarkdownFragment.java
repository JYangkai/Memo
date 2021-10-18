package com.yk.memo.ui.setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.yk.base.eventposter.EventPoster;
import com.yk.markdown.style.MdStyleManager;
import com.yk.memo.R;
import com.yk.memo.data.event.MdStyleChangeEvent;

public class CustomMarkdownFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_custom_markdown, rootKey);
    }

    @Override
    public void onPause() {
        super.onPause();
        MdStyleManager.getInstance().choose(getContext(), "Custom");
        EventPoster.getInstance().post(new MdStyleChangeEvent("Custom"));
    }
}
