package gui;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.PropertyTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.SeparateTextGUIThread;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.Window.Hint;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.ObjectUtils;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.util.List;

public class Gui {

    private MultiWindowTextGUI _window;
    private SeparateTextGUIThread _thread;

    private HashMap<String, Window> _windows = new HashMap<>();
    private HashMap<String, Panel> _panels = new HashMap<>();
    private HashMap<String, Label> _labels = new HashMap<>();
    private HashMap<String, Button> _buttons = new HashMap<>();
    private HashMap<String, TextBox> _textBoxes = new HashMap<>();
    private HashMap<String, RadioBoxList<String>> _radioBoxLists = new HashMap<>();

    private ArrayList<String> _loadedWindows = new ArrayList<>();

    public Gui(Screen screen, String pathToXml) {
        _window = new MultiWindowTextGUI(new SeparateTextGUIThread.Factory(), screen);
        _thread = (SeparateTextGUIThread) _window.getGUIThread();

        //window control
        _window.addListener(new TextGUI.Listener() {
            @Override
            public boolean onUnhandledKeyStroke(TextGUI textGUI, KeyStroke keyStroke) {
                if((keyStroke.isCtrlDown() && keyStroke.getKeyType() == KeyType.Tab) ||
                        keyStroke.getKeyType() == KeyType.F6) {
                    ((WindowBasedTextGUI)textGUI).cycleActiveWindow(false);
                }
                else if((keyStroke.isCtrlDown() && keyStroke.getKeyType() == KeyType.ReverseTab) ||
                        keyStroke.getKeyType() == KeyType.F7) {
                    ((WindowBasedTextGUI)textGUI).cycleActiveWindow(true);
                }
                else {
                    return false;
                }
                return true;
            }
        });

        init(pathToXml);
    }

    private void init(String pathToXml) {

        try {

            FileInputStream file = null;
            Properties theme = null;

            try {
                file = new FileInputStream("./config/themes/default.properties");
                // create Properties class object
                theme =new Properties();
                // load properties file into it
                theme.load(file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                file.close();
            }

            _window.setTheme(new PropertyTheme(theme));

            File inputFile = new File(pathToXml);

            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);

            Element root = document.getRootElement();

            if (root == null) {
                throw new Exception("Your XML file does not contain a root element.");
            }

            parseRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseRoot(Element root) {
        try {

            for (Element childElement : root.getChildren()) {
                parseWindow(childElement);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseWindow(Element element) {
        Window window = createWindow(element);

        int x = 0, y = 0;

        if(element.getAttribute("left")  != null){
            x = Integer.valueOf(element.getAttributeValue("left"));
        }

        if(element.getAttribute("top")  != null){
            y = Integer.valueOf(element.getAttributeValue("top"));
        }

        for (Element childElement : element.getChildren()) {
            parseTopPanel(window, childElement);
        }

        if (window != null && element.getAttribute("load") != null) {
            this.loadWindow(element.getAttributeValue("id"));
        }

        window.setPosition(new TerminalPosition(x, y));
    }

    private void parseTopPanel(Window window, Element element) {

        Panel panel = createPanel(element);

        for (Element childElement : element.getChildren()) {

            if (childElement.getName().equals("panel")) {
                parsePanel(panel, childElement);
            } else {
                parseElement(panel, childElement);
            }
        }

        window.setComponent(panel);
    }

    private void parsePanel(Panel parentPanel, Element element) {

        Panel panel = createPanel(element);

        if(panel.getFillColorOverride() == null && parentPanel.getFillColorOverride() != null){
            panel.setFillColorOverride(parentPanel.getFillColorOverride());
        }

        for (Element childElement : element.getChildren()) {

            if (childElement.getName().equals("panel")) {
                parsePanel(panel, childElement);
            } else {
                parseElement(panel, childElement);
            }

        }

        if (panel != null) {
            parentPanel.addComponent(panel);
        }

    }

    private void parseElement(Panel parentPanel, Element element) {

        try {

            switch (element.getName()) {

                case "label":

                    Label label = createLabel(element);

                    if(label != null){

                        if(label.getBackgroundColor() == null && parentPanel.getFillColorOverride() != null){
                            label.setBackgroundColor(parentPanel.getFillColorOverride());
                        }

                        parentPanel.addComponent(label);
                    }

                    break;

                case "button":

                    Button button = createButton(element);

                    if(button != null){
                        parentPanel.addComponent(button);
                    }

                    break;

                case "textbox":

                    TextBox textBox = createTextBox(element);

                    if(textBox != null){
                        parentPanel.addComponent(textBox);
                    }

                    break;

                case "radio":

                    RadioBoxList<String> radioBoxList = createRadioBoxList(element);

                    if(radioBoxList != null){
                        parentPanel.addComponent(radioBoxList);
                    }

                    break;

                default:
                    throw new Exception("Unknown element name: '" + element.getName() + "'");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Window createWindow(Element element) {

        try {
            if (!element.getName().equals("window")) {
                throw new Exception("Element is not a window");
            }

            CustomWindow window = new CustomWindow(element.getAttributeValue("title")) {
            };

            ArrayList<Hint> hints = new ArrayList<>();

            String id = null;

            int width = -1, height = -1;

            for (Attribute attribute : element.getAttributes()) {
                switch (attribute.getName()) {
                    case "id":
                        id = attribute.getValue();
                        break;

                    case "hidden":
                        window.setVisible(false);
                        break;

                    case "width":
                        width = attribute.getIntValue();
                        break;
                    case "height":
                        height = attribute.getIntValue();
                        break;

                    case "noDecorations":
                        hints.add(Hint.NO_DECORATIONS);
                        break;
                    case "noPostRendering":
                        hints.add(Hint.NO_POST_RENDERING);
                        break;
                    case "noFocus":
                        hints.add(Hint.NO_FOCUS);
                        break;
                    case "centered":
                        hints.add(Hint.CENTERED);
                        break;
                    case "fixedPosition":
                        hints.add(Hint.FIXED_POSITION);
                        break;
                    case "fixedSize":
                        hints.add(Hint.FIXED_SIZE);
                        break;
                    case "fitWindow":
                        hints.add(Hint.FIT_TERMINAL_WINDOW);
                        break;
                    case "modal":
                        hints.add(Hint.MODAL);
                        break;
                    case "fullscreen":
                        hints.add(Hint.FULL_SCREEN);
                        break;
                    case "expanded":
                        hints.add(Hint.EXPANDED);
                        break;
                }
            }

            window.setHints(hints);

            if (width != -1 && height != -1) {
                //Will only do something if fixedSize flag is set!
                window.setSize(new TerminalSize(width, height));
            }

            if (id == null) {
                id = UUID.randomUUID().toString();
            }

            _windows.put(id, window);

            return window;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Label createLabel(Element element) {

        try {

            if (!element.getName().equals("label")) {
                throw new Exception("Element is not a label");
            }

            Label label = new Label(element.getText());

            //Label default layout data
            GridLayout.Alignment vAlign = GridLayout.Alignment.BEGINNING;
            GridLayout.Alignment hAlign = GridLayout.Alignment.BEGINNING;
            boolean vSpread = false;
            boolean hSpread = false;
            int vSpan = 1;
            int hSpan = 1;

            String id = null;

            for (Attribute attribute : element.getAttributes()) {

                switch (attribute.getName()) {
                    case "id":
                        id = attribute.getValue();
                        break;

                    //color
                    case "bgColor":
                        Color bgColor = Color.decode(attribute.getValue());
                        label.setBackgroundColor(TextColor.Indexed.fromRGB(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
                        break;

                    case "fgColor":
                        Color fgColor = Color.decode(attribute.getValue());
                        label.setForegroundColor(TextColor.Indexed.fromRGB(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue()));
                        break;

                    //align
                    case "vAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                vAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                vAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                vAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    case "hAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                hAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                hAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                hAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    //spread
                    case "vSpread":
                        vSpread = true;
                        break;

                    case "hSpread":
                        hSpread = true;
                        break;

                    case "hSpan":
                        vSpan = attribute.getIntValue();
                        break;

                    case "wSpan":
                        hSpan = attribute.getIntValue();
                        break;

                    default:
                        throw new Exception("Unknown attribute '" + attribute.getName() + "' in element 'label'");
                }

            }

            label.setLayoutData(GridLayout.createLayoutData(
                    hAlign,
                    vAlign,
                    hSpread,
                    vSpread,
                    vSpan,
                    hSpan
            ));

            if (id == null) {
                id = UUID.randomUUID().toString();
            }

            _labels.put(id, label);

            return label;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private RadioBoxList createRadioBoxList(Element element) {

        try {

            if (!element.getName().equals("radio")) {
                throw new Exception("Element is not a radio");
            }

            //Label default layout data
            GridLayout.Alignment vAlign = GridLayout.Alignment.BEGINNING;
            GridLayout.Alignment hAlign = GridLayout.Alignment.BEGINNING;
            boolean vSpread = false;
            boolean hSpread = false;
            int vSpan = 1;
            int hSpan = 1;

            int height = 3, width = 10;

            String id = null;

            for (Attribute attribute : element.getAttributes()) {

                switch (attribute.getName()) {
                    case "id":
                        id = attribute.getValue();
                        break;

                    //align
                    case "vAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                vAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                vAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                vAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    case "hAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                hAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                hAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                hAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    //spread
                    case "vSpread":
                        vSpread = true;
                        break;

                    case "hSpread":
                        hSpread = true;
                        break;

                    case "hSpan":
                        vSpan = attribute.getIntValue();
                        break;

                    case "wSpan":
                        hSpan = attribute.getIntValue();
                        break;

                    case "width":
                        width = attribute.getIntValue();
                        break;

                    case "height":
                        height = attribute.getIntValue();
                        break;

                    default:
                        throw new Exception("Unknown attribute '" + attribute.getName() + "' in element 'label'");
                }

            }

            RadioBoxList radioBoxList = new RadioBoxList(new TerminalSize(width, height));

            for (Element childElement : element.getChildren()) {
                radioBoxList.addItem(childElement.getText());
            }

            radioBoxList.setLayoutData(GridLayout.createLayoutData(
                    hAlign,
                    vAlign,
                    hSpread,
                    vSpread,
                    hSpan,
                    vSpan
            ));

            if (id == null) {
                id = UUID.randomUUID().toString();
            }

            _radioBoxLists.put(id, radioBoxList);

            return radioBoxList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Panel createPanel(Element element) {

        try {

            if (!element.getName().equals("panel")) {
                throw new Exception("Element is not a panel");
            }

            //make function of this check
            String columnsAttr = element.getAttributeValue("columns");

            int columns;
            if (columnsAttr == null) {
                //if no columns attr is defined, fallback to 1 column
                columns = 1;
            } else {
                columns = Integer.valueOf(columnsAttr);
            }

            Panel panel = new Panel(new GridLayout(columns));

            GridLayout panelLayoutManager = (GridLayout) panel.getLayoutManager();

            GridLayout.Alignment vAlign = GridLayout.Alignment.BEGINNING;
            GridLayout.Alignment hAlign = GridLayout.Alignment.BEGINNING;
            boolean vSpread = false;
            boolean hSpread = false;
            int vSpan = 1;
            int hSpan = 1;

            String id = null;

            for (Attribute attribute : element.getAttributes()) {
                switch (attribute.getName()) {
                    case "id":
                        id = attribute.getValue();
                        break;

                    case "columns":
                        //already used this information above
                        break;

                    case "bgColor":
                        Color bgColor = Color.decode(attribute.getValue());
                        panel.setFillColorOverride(TextColor.Indexed.fromRGB(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
                        break;

                    //spacing
                    case "vSpacing":
                        panelLayoutManager.setVerticalSpacing(attribute.getIntValue());
                        break;
                    case "hSpacing":
                        panelLayoutManager.setHorizontalSpacing(attribute.getIntValue());
                        break;

                    //margin
                    case "bMargin":
                        panelLayoutManager.setBottomMarginSize(attribute.getIntValue());
                        break;
                    case "tMargin":
                        panelLayoutManager.setTopMarginSize(attribute.getIntValue());
                        break;
                    case "lMargin":
                        panelLayoutManager.setLeftMarginSize(attribute.getIntValue());
                        break;
                    case "rMargin":
                        panelLayoutManager.setRightMarginSize(attribute.getIntValue());
                        break;
                    case "vMargin":
                        panelLayoutManager.setTopMarginSize(attribute.getIntValue());
                        panelLayoutManager.setBottomMarginSize(attribute.getIntValue());
                        break;
                    case "hMargin":
                        panelLayoutManager.setRightMarginSize(attribute.getIntValue());
                        panelLayoutManager.setLeftMarginSize(attribute.getIntValue());
                        break;
                    case "margin":
                        panelLayoutManager.setRightMarginSize(attribute.getIntValue());
                        panelLayoutManager.setLeftMarginSize(attribute.getIntValue());
                        panelLayoutManager.setTopMarginSize(attribute.getIntValue());
                        panelLayoutManager.setBottomMarginSize(attribute.getIntValue());
                        break;

                    //align
                    case "vAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                vAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                vAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                vAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    case "hAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                hAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                hAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                hAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    //spread
                    case "vSpread":
                        vSpread = true;
                        break;

                    case "hSpread":
                        hSpread = true;
                        break;

                    case "vSpan":
                        vSpan = attribute.getIntValue();
                        break;

                    case "hSpan":
                        hSpan = attribute.getIntValue();
                        break;

                    default:
                        throw new Exception("Unknown attribute '" + attribute.getName() + "' in element 'panel'");
                }
            }

            if (id == null) {
                id = UUID.randomUUID().toString();
            }

            panel.setLayoutData(GridLayout.createLayoutData(
                    hAlign,
                    vAlign,
                    hSpread,
                    vSpread,
                    hSpan,
                    vSpan
            ));

            _panels.put(id, panel);

            return panel;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private TextBox createTextBox(Element element) {

        try {

            if (!element.getName().equals("textbox")) {
                throw new Exception("Element is not a textbox");
            }

            String id = null;
            int width = 10, height = 1;
            boolean multiline = false;

            GridLayout.Alignment vAlign = GridLayout.Alignment.BEGINNING;
            GridLayout.Alignment hAlign = GridLayout.Alignment.BEGINNING;
            boolean vSpread = false;
            boolean hSpread = false;
            int vSpan = 1;
            int hSpan = 1;

            for (Attribute attribute : element.getAttributes()) {
                switch (attribute.getName()) {
                    case "id":
                        id = attribute.getValue();
                        break;

                    case "width":
                        width = attribute.getIntValue();
                        break;

                    case "height":
                        height = attribute.getIntValue();
                        break;

                    case "multiline":
                        multiline = true;
                        break;

                    //align
                    case "vAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                vAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                vAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                vAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    case "hAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                hAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                hAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                hAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    //spread
                    case "vSpread":
                        vSpread = true;
                        break;

                    case "hSpread":
                        hSpread = true;
                        break;

                    case "vSpan":
                        vSpan = attribute.getIntValue();
                        break;

                    case "hSpan":
                        hSpan = attribute.getIntValue();
                        break;

                    default:
                        throw new Exception("Unknown attribute '" + attribute.getName() + "' in element 'panel'");
                }
            }

            if (id == null) {
                id = UUID.randomUUID().toString();
            }

            TextBox textBox = new TextBox(new TerminalSize(width, height), element.getText(), (multiline) ? TextBox.Style.MULTI_LINE : TextBox.Style.SINGLE_LINE);

            textBox.setLayoutData(GridLayout.createLayoutData(
                    hAlign,
                    vAlign,
                    hSpread,
                    vSpread,
                    hSpan,
                    vSpan
            ));

            _textBoxes.put(id, textBox);

            return textBox;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Button createButton(Element element){

        try {

            if (!element.getName().equals("button")) {
                throw new Exception("Element is not a panel");
            }

            Button button = new Button(element.getText());

            GridLayout.Alignment vAlign = GridLayout.Alignment.BEGINNING;
            GridLayout.Alignment hAlign = GridLayout.Alignment.BEGINNING;
            boolean vSpread = false;
            boolean hSpread = false;
            int vSpan = 1;
            int hSpan = 1;

            String id = null;

            for (Attribute attribute : element.getAttributes()) {

                switch (attribute.getName()) {

                    case "id":
                        id = attribute.getValue();
                        break;

                    case "action":
                        break;

                    //align
                    case "vAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                vAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                vAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                vAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    case "hAlign":

                        switch (attribute.getValue()) {
                            case "begin":
                                hAlign = GridLayout.Alignment.BEGINNING;
                                break;
                            case "center":
                                hAlign = GridLayout.Alignment.CENTER;
                                break;
                            case "end":
                                hAlign = GridLayout.Alignment.END;
                                break;
                        }

                        break;

                    //spread
                    case "vSpread":
                        vSpread = true;
                        break;

                    case "hSpread":
                        hSpread = true;
                        break;

                    case "vSpan":
                        vSpan = attribute.getIntValue();
                        break;

                    case "hSpan":
                        hSpan = attribute.getIntValue();
                        break;

                    default:
                        throw new Exception("Unknown attribute '" + attribute.getName() + "' in element 'button'");
                }

            }

            button.setLayoutData(GridLayout.createLayoutData(
                    hAlign,
                    vAlign,
                    hSpread,
                    vSpread,
                    hSpan,
                    vSpan
            ));

            if (id == null) {
                id = UUID.randomUUID().toString();
            }

            _buttons.put(id, button);

            return button;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, RadioBoxList<String>> getRadioBoxLists() {
        return _radioBoxLists;
    }

    public void setRadioBoxLists(HashMap<String, RadioBoxList<String>> radioBoxLists) {
        _radioBoxLists = radioBoxLists;
    }

    /**
     * Load a window on the screen.
     * @param windowId
     */
    public void loadWindow(String windowId){
        _window.addWindow(getWindow(windowId));
        _loadedWindows.add(windowId);
    }

    /**
     * Unload all windows from the screen and then load one.
     * @param windowId
     */
    public void showWindow(String windowId){
        _loadedWindows.forEach((id) -> _window.removeWindow(getWindow(id)));
        _loadedWindows.clear();
        loadWindow(windowId);
    }

    /**
     * Unload all windows from the screen.
     * @param windowId
     */
    public void unloadWindow(String windowId){
        _window.removeWindow(getWindow(windowId));
        _loadedWindows.remove(windowId);
    }

    public void setActiveWindow(String windowId){
        _window.setActiveWindow(getWindow(windowId));
    }

    public ArrayList<String> getLoadedWindows() {
        return _loadedWindows;
    }

    public SeparateTextGUIThread getThread() {
        return _thread;
    }

    public MultiWindowTextGUI getWindow() {
        return _window;
    }

    public Label getLabel(String id){
        return _labels.get(id);
    }

    public Button getButton(String id){
        return _buttons.get(id);
    }

    public TextBox getTextBox(String id){
        return _textBoxes.get(id);
    }

    public RadioBoxList<String> getRadioBoxList(String id){
        return _radioBoxLists.get(id);
    }

    public Panel getPanel(String id){
        return _panels.get(id);
    }

    public Window getWindow(String id){
        return _windows.get(id);
    }
}
