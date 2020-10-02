package com.google.appinventor.client.editor.simple.components;

import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.appinventor.components.common.ComponentConstants;

public class MockCardview extends MockHVArrangement {

    public static final String TYPE = "Cardview";

    public MockCardview(SimpleEditor editor) {
        super(editor, TYPE, images.cardview(),
		ComponentConstants.LAYOUT_ORIENTATION_HORIZONTAL,
	    ComponentConstants.NONSCROLLABLE_ARRANGEMENT);
	}

}
