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
import com.makeramen.roundedimageview.RoundedImageView;

public class ViewNavDrawerHeader extends RelativeLayout {

    private RoundedImageView ivIcon;
    private TextView txtName;
    private Drawable src;
    private String name;

    /**
     * Constructor
     *
     * @param context The context this view will be contained within
     */
    public ViewNavDrawerHeader(Context context) {
        this(context, null);
    }

    /**
     * XML Constructor
     *
     * @param context The context this view will be contained within
     * @param attrSet The set of all xml style attributes for this view
     */
    public ViewNavDrawerHeader(Context context, AttributeSet attrSet) {
        this(context, attrSet, 0);
    }

    /**
     * XML Constructor with theme defaults
     *
     * @param context The context this view will be contained within
     * @param attrSet The set of all xml style attributes for this view
     */
    public ViewNavDrawerHeader(Context context, AttributeSet attrSet, int defStyle) {
        super(context, attrSet, defStyle);

        if (!this.isInEditMode()) {
            // Inflate our layout from xml
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflater.inflate(R.layout.view_navigation_drawer_header, this, true);

            // Get a searchable array of our custom style attributes
            TypedArray attributes = context.getTheme().obtainStyledAttributes(
                    attrSet,
                    R.styleable.ViewNavDrawerHeader,
                    defStyle,
                    0);

            try {
                // Read our custom attributes
                src = attributes.getDrawable(R.styleable.ViewNavDrawerHeader_src);
                name = attributes.getString(R.styleable.ViewNavDrawerHeader_text);
            }
            finally {
                // Free up shared resource
                attributes.recycle();
            }

            // Get our inner views
            ivIcon = (RoundedImageView) findViewById(R.id.nav_drawer_header_image);
            txtName = (TextView) findViewById(R.id.nav_drawer_header_name);

            // Display title if set via xml
            if (!TextUtils.isEmpty(name)) {
                txtName.setText(name);
            }

            // Display icon if set via xml
            if (src != null) {
                ivIcon.setImageDrawable(src);
            }
        }
    }

    /**
     * Get the Drawable photo of this item
     *
     * @return The photo of the item
     */
    public Drawable getPhoto() {
        return src;
    }

    /**
     * Set the Drawable photo of this item
     *
     * @param resourceId resource id of the drawable to set
     */
    public void setPhoto(int resourceId) {
        src = getResources().getDrawable(resourceId);
        ivIcon.setImageDrawable(src);
    }

    /**
     * Get the name of this user
     *
     * @return The name of this user
     */
    public String getName() {
        return txtName.getText().toString();
    }

    /**
     * Set the name of this item
     *
     * @param text The name to set
     */
    public void setName(String text) {
        this.name = text;
        txtName.setText(this.name);
    }

    /**
     * Get the inner image view reference
     *
     * @return The inner image view
     */
    public ImageView getImageView() {
        return ivIcon;
    }
}
