package gui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.Screen;

import java.awt.*;
import java.io.*;
import java.security.spec.ECField;
import java.util.*;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

public class GuiMaker {

    private WindowBasedTextGUI _window ;
    private Panel _topPanel;

    public GuiMaker(Screen screen){
        _window = new MultiWindowTextGUI(screen);
    }

    public WindowBasedTextGUI generateGui(String pathToXml){

        try {

            File inputFile = new File(pathToXml);

            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);

            Element root = document.getRootElement();

            if(root == null){
                throw new Exception("Your XML file does not contain a root element.");
            }

            parseRoot(root);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return _window;

    }

    public void parseRoot(Element root){
        try {

            for(Element childElement : root.getChildren()){
                parseWindow(childElement);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseWindow(Element element){
        Window window = new BasicWindow(element.getAttributeValue("name"));

        for(Element childElement : element.getChildren()){
            parseTopPanel(window, childElement);
        }

        _window.addWindowAndWait(window);
    }

    private void parseTopPanel(Window window, Element element){

        Panel panel = createPanel(element);

        for(Element childElement : element.getChildren()){

            if(childElement.getName().equals("panel")){
                parsePanel(panel, childElement);
            } else {
                parseElement(panel, childElement);
            }
        }

        System.out.println(panel.getChildren().toString());

        window.setComponent(panel);
    }

    private void parsePanel(Panel parentPanel, Element element){

        Panel panel = createPanel(element);

        for(Element childElement : element.getChildren()){

            if(childElement.getName().equals("panel")){
                parsePanel(panel, childElement);
            } else {
                parseElement(panel, childElement);
            }

        }

        parentPanel.addComponent(panel);

    }

    private void parseElement(Panel parentPanel, Element element){

        try {

            switch(element.getName()){

                case "label":

                    parentPanel.addComponent(createLabel(element));

                    break;

                default:
                    throw new Exception("Unknown element name.");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Label createLabel(Element element){

        try {

            if(!element.getName().equals("label")) {
                throw new Exception("Element is not a label");
            }

            Label label = new Label(element.getText());

            for(Attribute attribute: element.getAttributes()){

                switch(attribute.getName()){
                    case "bgColor":
                        Color bgColor = Color.decode(attribute.getValue());
                        label.setBackgroundColor(TextColor.Indexed.fromRGB(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
                        break;

                    case "fgColor":
                        Color fgColor = Color.decode(attribute.getValue());
                        label.setForegroundColor(TextColor.Indexed.fromRGB(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue()));
                        break;

                    default:
                        throw new Exception("Unknown attribute '" + attribute.getName() + "' in element 'label'");
                }

            }

            return label;

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private Panel createPanel(Element element){

        try {

            if(!element.getName().equals("panel")){
                throw new Exception("Element is not a panel");
            }

            //make function of this check
            String columnsAttr = element.getAttributeValue("columns");

            int columns;
            if(columnsAttr == null){
                //if no columns attr is defined, fallback to 1 column
                columns = 1;
            } else {
                columns = Integer.valueOf(columnsAttr);
            }

            Panel panel = new Panel(new GridLayout(columns));

            GridLayout panelLayoutManager = (GridLayout)panel.getLayoutManager();

            for(Attribute attribute: element.getAttributes()){
                switch(attribute.getName()){
                    case "columns":
                        //already used this information above
                        break;
                    case "vSpacing":
                        panelLayoutManager.setVerticalSpacing(attribute.getIntValue());
                        break;
                    case "hSpacing":
                        panelLayoutManager.setHorizontalSpacing(attribute.getIntValue());
                        break;

                    default:
                        throw new Exception("Unknown attribute '" + attribute.getName() + "' in element 'panel'");
                }
            }

            return panel;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
