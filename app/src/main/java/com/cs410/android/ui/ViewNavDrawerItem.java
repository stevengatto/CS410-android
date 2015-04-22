package com.cs410.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs410.android.R;

public class ViewNavDrawerItem extends RelativeLayout {

    private Drawable src;
    private String text;
    private ImageView ivIcon;
    private TextView txtTitle;

    /**
     * Constructor
     *
     * @param context The context this view will be contained within
     */
    public ViewNavDrawerItem(Context context) {
        this(context, null);
    }

    /**
     * XML Constructor
     *
     * @param context The context this view will be contained within
     * @param attrSet The set of all xml style attributes for this view
     */
    public ViewNavDrawerItem(Context context, AttributeSet attrSet) {
        this(context, attrSet, 0);
    }

    /**
     * XML Constructor with theme defaults
     *
     * @param context The context this view will be contained within
     * @param attrSet The set of all xml style attributes for this view
     */
    public ViewNavDrawerItem(Context context, AttributeSet attrSet, int defStyle) {
        super(context, attrSet, defStyle);

        if (!this.isInEditMode()) {
            // Inflate our layout from xml
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflater.inflate(R.layout.view_navigation_drawer_item, this, true);

            // Get a searchable array of our custom style attributes
            TypedArray attributes = context.getTheme().obtainStyledAttributes(
                    attrSet,
                    R.styleable.ViewNavDrawerItem,
                    defStyle,
                    0);

            try {
                // Read our custom attributes
                src = attributes.getDrawable(R.styleable.ViewNavDrawerItem_src);
                text = attributes.getString(R.styleable.ViewNavDrawerItem_text);
            }
            finally {
                // Free up shared resource
                attributes.recycle();
            }

            // Get our inner views
            ivIcon = (ImageView) findViewById(R.id.nav_drawer_item_icon);
            txtTitle = (TextView) findViewById(R.id.nav_drawer_item_text);

            // Display title if set via xml
            if (!TextUtils.isEmpty(text)) {
                setText(text);
            }

            // Display icon if set via xml
            if (src != null) {
                ivIcon.setImageDrawable(src);
            }
        }
    }

    /**
     * Get the Drawable icon of this item
     *
     * @return The icon of the item
     */
    public Drawable getIcon() {
        return src;
    }

    /**
     * Set the Drawable icon of this item
     *
     * @param resourceId resource id of the drawable to set
     */
    public void setIcon(int resourceId) {
        src = getResources().getDrawable(resourceId);
        ivIcon.setImageDrawable(src);
    }

    /**
     * Get the title of this item
     *
     * @return The title of this item
     */
    public String getText() {
        return txtTitle.getText().toString();
    }

    /**
     * Set the title of this item
     *
     * @param text The title to set
     */
    public void setText(String text) {
        this.text = text;
        txtTitle.setText(this.text);
    }
}
