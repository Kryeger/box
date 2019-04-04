package gui;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Interactable;

public class CustomWindow extends BasicWindow {
    private Interactable _focused;

    public CustomWindow() {
    }

    public CustomWindow(String title) {
        super(title);
    }

    public Interactable getFocused() {
        return _focused;
    }

    public void setFocused(Interactable focused) {
        _focused = focused;
        setFocusedInteractable(focused);
    }
}
