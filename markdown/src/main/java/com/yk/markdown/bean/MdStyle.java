package com.yk.markdown.bean;

import android.graphics.Color;

/**
 * 尺寸
 */
public interface MdStyle {
    interface Base {
        int TEXT_COLOR = Color.BLACK;
        int TEXT_SIZE = 64;
    }

    interface Normal {
        int TEXT_COLOR = Base.TEXT_COLOR;
        int TEXT_SIZE = Base.TEXT_SIZE;
    }

    interface Quote {
        int STRIPE_COLOR = Color.BLACK;
        int STRIPE_WIDTH = 20;

        int GAP_WIDTH = 30;

        int TEXT_COLOR = Base.TEXT_COLOR;
        int TEXT_SIZE = Base.TEXT_SIZE;
    }

    interface CodeBlock {
        int GAP_WIDTH = 30;

        int BACKGROUND_COLOR = Color.BLACK;

        int TEXT_COLOR = Color.WHITE;
        int TEXT_SIZE = Base.TEXT_SIZE;
    }

    interface OrderedList {
        int INDEX_COLOR = Color.BLACK;
        int INDEX_SIZE = 56;
        int INDEX_WIDTH = 30;

        int GAP_WIDTH = 30;

        int TEXT_COLOR = Base.TEXT_COLOR;
        int TEXT_SIZE = Base.TEXT_SIZE;
    }

    interface UnorderedList {
        int CIRCLE_COLOR = Color.BLACK;
        int CIRCLE_RADIUS = 8;

        int GAP_WIDTH = 30;

        int TEXT_COLOR = Base.TEXT_COLOR;
        int TEXT_SIZE = Base.TEXT_SIZE;
    }

    interface Title {
        int TEXT_COLOR = Base.TEXT_COLOR;

        int TEXT_SIZE_1 = Base.TEXT_SIZE + 16 * 4;
        int TEXT_SIZE_2 = Base.TEXT_SIZE + 16 * 3;
        int TEXT_SIZE_3 = Base.TEXT_SIZE + 16 * 2;
        int TEXT_SIZE_4 = Base.TEXT_SIZE + 16;
        int TEXT_SIZE_5 = Base.TEXT_SIZE;
    }

    interface Separator {
        int COLOR = Color.GRAY;
        int SIZE = 2;
    }
}
