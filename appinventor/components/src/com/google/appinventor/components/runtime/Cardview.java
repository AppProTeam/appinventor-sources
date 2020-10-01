package com.google.appinventor.components.runtime;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.cardview.widget.CardView;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.app.Activity;
import android.view.View;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.AlignmentUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.ViewUtil;

import java.io.IOException;

@DesignerComponent(version = 1,
        description = "A view which lets user to add child views as cards",
        category = ComponentCategory.LAYOUT)
@SimpleObject
@UsesLibraries(libraries = "cardview.jar")
public class Cardview extends AndroidViewComponent implements Component,ComponentContainer{
    public Activity activity;
    public CardView cardView;
    public final Handler androidUIHandler = new Handler();
    public Form form;
    public AlignmentUtil alignmentUtil;
    public int backgroundColor;
    private Drawable backgroundImageDrawable;
    private String imagePath = "";
    private Drawable defaultButtonDrawable;
    public boolean isFullClickable = false;
    private final LinearLayout viewLayout;
    private ViewGroup frameContainer;
    public int density;
    private AlignmentUtil alignmentSetter;
    private int horizontalAlignment;
    private int verticalAlignment;
    public int pLeft = 8;
    public int pRight = 8;
    public int pTop = 8;
    public int pBottom = 8;
    public int radius = 5;

    public Cardview(ComponentContainer container){
        super(container);
        activity = container.$context();
        form = container.$form();
        cardView = new CardView(activity);
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    TouchUp();
                }else if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    TouchDown();
                }
                return false;
            }
        });
        density = (int) container.$form().deviceDensity();
        container.$add(this);
        viewLayout = new LinearLayout(activity, ComponentConstants.LAYOUT_ORIENTATION_VERTICAL,
                ComponentConstants.EMPTY_HV_ARRANGEMENT_WIDTH,
                ComponentConstants.EMPTY_HV_ARRANGEMENT_HEIGHT);
        viewLayout.setBaselineAligned(false);
        alignmentSetter = new AlignmentUtil(viewLayout);
        horizontalAlignment = ComponentConstants.HORIZONTAL_ALIGNMENT_DEFAULT;
        verticalAlignment = ComponentConstants.VERTICAL_ALIGNMENT_DEFAULT;
        alignmentSetter.setHorizontalAlignment(horizontalAlignment);
        alignmentSetter.setVerticalAlignment(verticalAlignment);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(-1,-1);
        int top = d2p(4);
        int left = d2p(8);
        layoutParams.setMargins(left,top,left,top);
        frameContainer = new FrameLayout(activity);
        frameContainer.setLayoutParams(new ViewGroup.LayoutParams(ComponentConstants.EMPTY_HV_ARRANGEMENT_WIDTH, ComponentConstants.EMPTY_HV_ARRANGEMENT_HEIGHT));
        frameContainer.addView(cardView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        defaultButtonDrawable = getView().getBackground();
        defaultButtonDrawable = getView().getBackground();
        defaultButtonDrawable = getView().getBackground();
        Clickable(true);
        CornerRadius(5);
        setPaddings();
    }

    public View getView() {
        return (View)frameContainer;
    }

    public Activity $context() {
        return activity;
    }

    public Form $form() {
        return form;
    }

    public void $add(AndroidViewComponent component) {
        viewLayout.add(component);
    }

    public void setChildWidth(AndroidViewComponent component, int width) {
        setChildWidth(component, width, 0);
    }

    public void setChildWidth(final AndroidViewComponent component, final int width, int tryCount) {
        int i = container.$form().Width();
        if (i == 0 && tryCount < 2) {
            final int finalTryCount = tryCount;
            this.androidUIHandler.postDelayed(new Runnable() {
                public final void run() {
                    setChildWidth(component,width, finalTryCount + 1);
                }
            },100L);
        }
        tryCount = width;
        if (width <= -1000)
            tryCount = i * -(width + 1000) / 100;
        component.setLastWidth(tryCount);
        ViewUtil.setChildWidthForVerticalLayout(component.getView(), tryCount);
    }

    public void setChildHeight(AndroidViewComponent component, int height) {
        setChildHeight(component, height, 0);
    }

    public void setChildHeight(final AndroidViewComponent component, final int height, int tryCount) {
        int i = container.$form().Width();
        if (i == 0 && tryCount < 2) {
            final int finalTryCount = tryCount;
            androidUIHandler.postDelayed(new Runnable() {
                public final void run() {
                    setChildHeight(component,height, finalTryCount + 1);
                }
            },100L);
        }
        tryCount = height;
        if (height <= -1000)
            tryCount = i * -(height + 1000) / 100;
        component.setLastWidth(tryCount);
        ViewUtil.setChildWidthForVerticalLayout(component.getView(), tryCount);
    }

    public class Card extends CardView {
        public Card(Context context) {
            super(context);
        }
        public boolean onInterceptTouchEvent(MotionEvent param1MotionEvent){
            return isFullClickable;
        }
    }

    public int d2p(int d){
        return d*density;
    }

    public int p2d(int d){
        return d/density;
    }
    @SimpleProperty(
            category = PropertyCategory.APPEARANCE,
            description = "A number that encodes how contents of the %type% are aligned " +
                    " horizontally. The choices are: 1 = left aligned, 2 = right aligned, " +
                    " 3 = horizontally centered.  Alignment has no effect if the arrangement's width is " +
                    "automatic.")
    public int AlignHorizontal() {
        return horizontalAlignment;
    }

    /**
     * A number that encodes how contents of the `%type%` are aligned horizontally. The choices
     * are: `1` = left aligned, `2` = right aligned, `3` = horizontally centered. Alignment has no
     * effect if the `%type%`'s {@link #Width()} is `Automatic`.
     *
     * @param alignment
     */
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_HORIZONTAL_ALIGNMENT,
            defaultValue = ComponentConstants.HORIZONTAL_ALIGNMENT_DEFAULT + "")
    @SimpleProperty
    public void AlignHorizontal(int alignment) {
        try {
            // notice that the throw will prevent the alignment from being changed
            // if the argument is illegal
            alignmentSetter.setHorizontalAlignment(alignment);
            horizontalAlignment = alignment;
        } catch (IllegalArgumentException e) {
            container.$form().dispatchErrorOccurredEvent(this, "Cardview",
                    ErrorMessages.ERROR_BAD_VALUE_FOR_HORIZONTAL_ALIGNMENT, alignment);
        }
    }

    /**
     * Returns a number that encodes how contents of the %type% are aligned vertically.
     * The choices are: 1 = aligned at the top, 2 = vertically centered, 3 = aligned at the bottom.
     * Alignment has no effect if the arrangement's height is automatic.
     */
    @SimpleProperty(
            category = PropertyCategory.APPEARANCE,
            description = "A number that encodes how the contents of the %type% are aligned " +
                    " vertically. The choices are: 1 = aligned at the top, 2 = vertically centered, " +
                    "3 = aligned at the bottom.  Alignment has no effect if the arrangement's height " +
                    "is automatic.")
    public int AlignVertical() {
        return verticalAlignment;
    }

    /**
     * A number that encodes how the contents of the `%type%` are aligned vertically. The choices
     * are: `1` = aligned at the top, `2` = aligned at the bottom, `3` = vertically centered.
     * Alignment has no effect if the `%type%`'s {@link #Height()} is `Automatic`.
     *
     * @param alignment
     */
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_VERTICAL_ALIGNMENT,
            defaultValue = ComponentConstants.VERTICAL_ALIGNMENT_DEFAULT + "")
    @SimpleProperty
    public void AlignVertical(int alignment) {
        try {
            // notice that the throw will prevent the alignment from being changed
            // if the argument is illegal
            alignmentSetter.setVerticalAlignment(alignment);
            verticalAlignment = alignment;
        } catch (IllegalArgumentException e) {
            container.$form().dispatchErrorOccurredEvent(this, "Cardview",
                    ErrorMessages.ERROR_BAD_VALUE_FOR_VERTICAL_ALIGNMENT, alignment);
        }
    }
    @SimpleProperty(description = "Returns the background color of the %type%")
    public int BackgroundColor() {
        return backgroundColor;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR,defaultValue = ""+COLOR_NONE)
    @SimpleProperty()
    public void BackgroundColor(int bgColor){
        backgroundColor = bgColor;
    }
    @SimpleProperty(
            category = PropertyCategory.APPEARANCE)
    public String Image() {
        return imagePath;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET, defaultValue = "")
    @SimpleProperty(description = "Specifies the path of the background image for the %type%.  " +
            "If there is both an Image and a BackgroundColor, only the Image will be visible.")
    public void Image(String path) {
        // If it's the same as on the prior call and the prior load was successful,
        // do nothing.
        if (path.equals(imagePath) && backgroundImageDrawable != null) {
            return;
        }

        imagePath = path;

        // Clear the prior background image.
        backgroundImageDrawable = null;

        // Load image from file.
        if (imagePath.length() > 0) {
            try {
                backgroundImageDrawable = MediaUtil.getBitmapDrawable(container.$form(), imagePath);
            } catch (IOException ioe) {
                // Fall through with a value of null for backgroundImageDrawable.
            }
        }

        // Update the appearance based on the new value of backgroundImageDrawable.
        updateAppearance();
    }
    private void updateAppearance() {
        // If there is no background image,
        // the appearance depends solely on the background color and shape.
        if (backgroundImageDrawable == null) {
            if (backgroundColor == Component.COLOR_DEFAULT) {
                // If there is no background image and color is default,
                // restore original 3D bevel appearance.
                ViewUtil.setBackgroundDrawable(viewLayout.getLayoutManager(), defaultButtonDrawable);
            } else {
                // Clear the background image.
                ViewUtil.setBackgroundDrawable(viewLayout.getLayoutManager(), null);
                viewLayout.getLayoutManager().setBackgroundColor(backgroundColor);
            }
        } else {
            // If there is a background image
            ViewUtil.setBackgroundImage(viewLayout.getLayoutManager(), backgroundImageDrawable);
        }
    }
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,defaultValue = "true")
    @SimpleProperty()
    public void Clickable(boolean bool){
        cardView.setClickable(bool);
        cardView.setLongClickable(bool);
        if (bool){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnClick();
                }
            });
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    OnLongClick();
                    return false;
                }
            });
        }else{
            cardView.setOnClickListener(null);
            cardView.setOnLongClickListener(null);
        }
    }
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,defaultValue = "true")
    @SimpleProperty()
    public void FullClickable(boolean bool){
        isFullClickable = bool;
        //we need to change cardview in every condition
        if(bool){
            cardView = new Card(activity);
        }else{
            cardView = new Card(activity);
        }
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER,defaultValue = "5")
    @SimpleProperty()
    public void CornerRadius(int p){
        cardView.setRadius(p);
        cardView.invalidate();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER,defaultValue = "8")
    @SimpleProperty()
    public void PaddingLeft(int p){
        pLeft = p;
        setPaddings();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER,defaultValue = "8")
    @SimpleProperty()
    public void PaddingRight(int p){
        pRight = p;
        setPaddings();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER,defaultValue = "8")
    @SimpleProperty()
    public void PaddingTop(int p){
        pTop = p;
        setPaddings();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER,defaultValue = "8")
    @SimpleProperty()
    public void PaddingBottom(int p){
        pBottom = p;
        setPaddings();
    }

    @SimpleProperty()
    public int CornerRadius(){
        return radius;
    }

    @SimpleProperty()
    public int PaddingLeft(){
        return pLeft;
    }

    @SimpleProperty()
    public int PaddingRight(){
        return pRight;
    }

    @SimpleProperty()
    public int PaddingTop(){
        return pTop;
    }

    @SimpleProperty()
    public int PaddingBottom(){
        return pBottom;
    }

    public void setPaddings(){
        cardView.setContentPadding(pLeft,pTop,pRight,pBottom);
        cardView.invalidate();
    }

    @SimpleEvent()
    public void OnClick(){
        EventDispatcher.dispatchEvent(this,"OnClick");
    }

    @SimpleEvent()
    public void OnLongClick(){
        EventDispatcher.dispatchEvent(this,"OnLongClick");
    }

    @SimpleEvent()
    public void TouchUp(){
        EventDispatcher.dispatchEvent(this,"TouchUp");
    }

    @SimpleEvent()
    public void TouchDown(){
        EventDispatcher.dispatchEvent(this,"TouchDown");
    }
}
