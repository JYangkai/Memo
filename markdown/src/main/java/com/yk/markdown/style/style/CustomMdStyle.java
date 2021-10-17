package com.yk.markdown.style.style;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.yk.markdown.style.bean.MdStyleBase;
import com.yk.markdown.style.bean.MdStyleBold;
import com.yk.markdown.style.bean.MdStyleBoldItalics;
import com.yk.markdown.style.bean.MdStyleCode;
import com.yk.markdown.style.bean.MdStyleCodeBlock;
import com.yk.markdown.style.bean.MdStyleItalics;
import com.yk.markdown.style.bean.MdStyleNormal;
import com.yk.markdown.style.bean.MdStyleOrderedList;
import com.yk.markdown.style.bean.MdStyleQuote;
import com.yk.markdown.style.bean.MdStyleSeparator;
import com.yk.markdown.style.bean.MdStyleTitle;
import com.yk.markdown.style.bean.MdStyleUnorderedList;

public class CustomMdStyle extends BaseMdStyle {
    private SharedPreferences sharedPreferences;

    private Context context;

    public CustomMdStyle(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        init();
    }

    @Override
    public void init() {
        // 基础
        setBase(
                new MdStyleBase(
                        Color.BLACK,
                        64
                )
        );

        // 普通
        setNormal(
                new MdStyleNormal(
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 引用
        setQuote(
                new MdStyleQuote(
                        Color.parseColor("#30888888"),
                        20,
                        30,
                        Color.parseColor("#A0000000"),
                        getBase().getTextSize()
                )
        );

        // 代码块
        setCodeBlock(
                new MdStyleCodeBlock(
                        30,
                        Color.parseColor("#30888888"),
                        Color.parseColor("#A0000000"),
                        getBase().getTextSize()
                )
        );

        // 有序列表
        setOrderedList(
                new MdStyleOrderedList(
                        Color.BLACK,
                        56,
                        30,
                        30,
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 无序列表
        setUnorderedList(
                new MdStyleUnorderedList(
                        Color.BLACK,
                        8,
                        30,
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 标题
        setTitle(
                new MdStyleTitle(
                        getBase().getTextColor(),
                        getBase().getTextSize() + 16 * 4,
                        getBase().getTextSize() + 16 * 3,
                        getBase().getTextSize() + 16 * 2,
                        getBase().getTextSize() + 16,
                        getBase().getTextSize()
                )
        );

        // 分隔符
        setSeparator(
                new MdStyleSeparator(
                        Color.parseColor("#30888888"),
                        2
                )
        );

        // 代码
        setCode(
                new MdStyleCode(
                        Color.parseColor("#10FF0000"),
                        Color.parseColor("#A0FF0000"),
                        getBase().getTextSize()
                )
        );

        // 粗斜体
        setBoldItalics(
                new MdStyleBoldItalics(
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 粗体
        setBold(
                new MdStyleBold(
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );

        // 斜体
        setItalics(
                new MdStyleItalics(
                        getBase().getTextColor(),
                        getBase().getTextSize()
                )
        );
    }

    private interface Sp {
        interface BASE {
            String KEY_TEXT_COLOR = "key_base_text_color";
            String KEY_TEXT_SIZE = "key_base_text_size";
        }

        interface NORMAL {
            String KEY_TEXT_COLOR = "key_normal_text_color";
            String KEY_TEXT_SIZE = "key_normal_text_size";
        }

        interface Quote {
            String KEY_STRIPE_COLOR = "key_quote_stripe_color";
            String KEY_STRIPE_WIDTH = "key_quote_stripe_width";
            String KEY_GAP_WIDTH = "key_quote_gap_width";
            String KEY_TEXT_COLOR = "key_quote_text_color";
            String KEY_TEXT_SIZE = "key_quote_text_size";
        }

        interface CodeBlock {
            String KEY_GAP_WIDTH = "key_code_block_gap_width";
            String KEY_BACKGROUND_COLOR = "key_code_block_background_color";
            String KEY_TEXT_COLOR = "key_code_block_text_color";
            String KEY_TEXT_SIZE = "key_code_block_text_size";
        }

        interface OrderedList {
            String KEY_INDEX_COLOR = "key_ordered_list_index_color";
            String KEY_INDEX_SIZE = "key_ordered_list_index_size";
            String KEY_INDEX_WIDTH = "key_ordered_list_index_width";
            String KEY_GAP_WIDTH = "key_ordered_list_gap_width";
            String KEY_TEXT_COLOR = "key_ordered_list_text_color";
            String KEY_TEXT_SIZE = "key_ordered_list_text_size";
        }

        interface UnorderedList {
            String KEY_CIRCLE_COLOR = "key_unordered_list_circle_color";
            String KEY_CIRCLE_RADIUS = "key_unordered_list_circle_radius";
            String KEY_GAP_WIDTH = "key_unordered_list_gap_width";
            String KEY_TEXT_COLOR = "key_unordered_list_text_color";
            String KEY_TEXT_SIZE = "key_unordered_list_text_size";
        }

        interface Title {
            String KEY_TEXT_COLOR = "key_title_text_color";
            String KEY_TEXT_SIZE_1 = "key_title_text_size_1";
            String KEY_TEXT_SIZE_2 = "key_title_text_size_2";
            String KEY_TEXT_SIZE_3 = "key_title_text_size_3";
            String KEY_TEXT_SIZE_4 = "key_title_text_size_4";
            String KEY_TEXT_SIZE_5 = "key_title_text_size_5";
        }

        interface Separator {
            String KEY_COLOR = "key_separator_color";
            String KEY_SIZE = "key_separator_size";
        }

        interface Code {
            String KEY_TEXT_COLOR = "key_code_text_color";
            String KEY_TEXT_SIZE = "key_code_text_size";
        }

        interface BoldItalics {
            String KEY_TEXT_COLOR = "key_bold_italics_text_color";
            String KEY_TEXT_SIZE = "key_bold_italics_text_size";
        }

        interface Bold {
            String KEY_TEXT_COLOR = "key_bold_text_color";
            String KEY_TEXT_SIZE = "key_bold_text_size";
        }

        interface Italics {
            String KEY_TEXT_COLOR = "key_italics_text_color";
            String KEY_TEXT_SIZE = "key_italics_text_size";
        }
    }
}
