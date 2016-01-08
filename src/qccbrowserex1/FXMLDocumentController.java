package qccbrowserex1;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLAnchorElement;

/**
 *
 * @author Gabriel Emery (gemery 239288)
 * @version 1.6
 * 
 * 
 */

public class FXMLDocumentController {

    public static final String DEFAULT_BROWSER_URL = "http://www.qcc.edu";
    private int tabCounter = 0;
    WebEngine webEngine1;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button goButton;

    @FXML
    private Button stopButton;

    @FXML
    ComboBox<String> comboBox1;

    @FXML
    private WebView webView1;

    @FXML
    private TabPane tabPane1;

    @FXML
    private Tab newTab1;
    
    @FXML
    private TextArea recordPane1;
    
    @FXML
    private MenuItem userCloseFile;

    @FXML
    private MenuItem userCreateNewFile;

    @FXML
    private MenuItem userOpenFile;

    @FXML
    private MenuItem userSaveFile;
    
    @FXML
    private MenuItem startRecording;
    
    @FXML
    private MenuItem stopRecording;

    @FXML
    void backButtonClickAction(ActionEvent event) {
        int index = webEngine1.getHistory().getCurrentIndex();
        if (index > 0) {
            webEngine1.getHistory().go(-1);
            System.out.println("back button");
        }
    }

    @FXML
    void forwardButtonClickAction(ActionEvent event) {
        int index = webEngine1.getHistory().getCurrentIndex();
        int size = webEngine1.getHistory().getEntries().size();
        if (index < size - 1) {
            webEngine1.getHistory().go(1);
            System.out.println("forward button");
        }
    }

    @FXML
    void goButtonClickAction(ActionEvent event) {
        String url = comboBox1.getValue();
        if (url != null){
        if (url.startsWith("http://www.")) {
            webEngine1.load(url);
        } else if (url.startsWith("www.")) {
            webEngine1.load("http://" + url);
        } else {
            webEngine1.load("http://www." + url);
        }
        System.out.println("go button");
        }
    }

    @FXML
    void comboBox1Action(ActionEvent event) {
        String url = comboBox1.getValue();
        if (url.startsWith("http://www.")) {
            webEngine1.load(url);
        } else if (url.startsWith("www.")) {
            webEngine1.load("http://" + url);
        } else {
            webEngine1.load("http://www." + url);
        }
        System.out.println("comboBox1 button");
    }

    @FXML
    void historyButtonClickAction(ActionEvent event) {
    }

    @FXML
    void stopButtonClickAction(ActionEvent event) {
        webEngine1.getLoadWorker().cancel();
        System.out.println("stop button");
    }

    @FXML
    void initialize() {
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert comboBox1 != null : "fx:id=\"comboBox1\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert forwardButton != null : "fx:id=\"forwardButton\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert goButton != null : "fx:id=\"goButton\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert webView1 != null : "fx:id=\"webView1\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert tabPane1 != null : "fx:id=\"tabPane1\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert newTab1 != null : "fx:id=\"newTab1\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert recordPane1 != null : "fx:id=\"recordPane1\" was not injected: check your FXML file 'FXMLDocument.fxml'.";        
        assert startRecording != null : "fx:id=\"startRecording\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert stopRecording != null : "fx:id=\"stopRecording\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert userCloseFile != null : "fx:id=\"userCloseFile\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert userCreateNewFile != null : "fx:id=\"userCreateNewFile\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert userOpenFile != null : "fx:id=\"userOpenFile\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert userSaveFile != null : "fx:id=\"userSaveFile\" was not injected: check your FXML file 'FXMLDocument.fxml'.";

        comboBox1.getItems().clear();
        createNewTab();
        tabChangeListener();
    }

    private WebEngine getCurrentTabWebEngine() {
        Tab tab1 = tabPane1.getSelectionModel().getSelectedItem();
        WebView wView = (WebView) tab1.getContent();
        WebEngine wEngine = wView.getEngine();
        return wEngine;
    }

    private void historyUpdate() {
        /*
         Updates history
         */
        webEngine1 = getCurrentTabWebEngine();

        final WebHistory history = webEngine1.getHistory();
        history.getEntries().addListener(new ListChangeListener<WebHistory.Entry>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Entry> c) {
                c.next();
                for (Entry e : c.getRemoved()) {
                    comboBox1.getItems().remove(e.getUrl());
                }
                for (Entry e : c.getAddedSubList()) {
                    comboBox1.getItems().add(e.getUrl());
                }
            }
        });
    }

    private void loadPage() {
        /*
         // Process page loading
         */
        webEngine1 = getCurrentTabWebEngine();

        webEngine1.getLoadWorker().stateProperty().addListener(
                new ChangeListener<State>() {
                    @Override
                    public void changed(ObservableValue<? extends State> ov,
                            State oldState, State newState) {

                        if (newState == State.SUCCEEDED) {
                            String url = webEngine1.getLocation();
                            comboBox1.setValue(url); // Display correct & current address

                            HTMLAnchorListener();
                            inputAnchorListener();
                            textAnchorListener();
                        }
                    }
                });
    }

    public void createNewTab() {
        Tab tab1 = new Tab();
        WebView wView = new WebView();
        tab1.setContent(wView);
        tab1.setText("New Tab Method" + " " + tabCounter++);
        tabPane1.getTabs().add(0, tab1);
        tabPane1.getSelectionModel().select(tab1);
        WebEngine newWebEngine = getCurrentTabWebEngine();
        newWebEngine.load(DEFAULT_BROWSER_URL);
        loadPage();
        historyUpdate();
    }

    private void tabChangeListener() {
        tabPane1.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Tab> tab, Tab oldTab, Tab newTab) {
                                if (newTab == newTab1) {
                                    /*
                                     // !Somebody clicked on the "newTab" tab!!!!New tab created with webView, and listeners
                                     */
                                    createNewTab();
                                } else {
                                    /*
                                     // !Somebody clicked existing Tab!// Get the current tab webview & web history// Update comboboxset & url for current tab
                                     */
                                    webEngine1 = getCurrentTabWebEngine();
                                    reloadComboBox();
                                }
                            }
                });
    }

    private void reloadComboBox() {

        if (comboBox1.getValue() != null) {
            comboBox1.getSelectionModel().clearSelection();
            comboBox1.getItems().clear();
        }

        // reload combobox
        ObservableList<WebHistory.Entry> HistoryList = webEngine1.getHistory().getEntries();
        if (HistoryList.size() > 0) {
            List<String> historyList = new ArrayList<>();
            for (WebHistory.Entry e : HistoryList) {
                historyList.add(0, e.getUrl());
            }
            ObservableList<String> HistoryList2 = FXCollections.observableList(historyList);
//            comboBox1.getItems().clear();
            comboBox1.setItems(HistoryList2);
//            comboBox1.editorProperty().getValue().textProperty().setValue(getCurrentTabWebEngine().getLocation());
            comboBox1.setValue(getCurrentTabWebEngine().getLocation());
        }
    }

    public void HTMLAnchorListener() {
        Document document = (Document) webEngine1.executeScript("document");
        NodeList nodeList = document.getElementsByTagName("a");
        for (int i = 0; i < nodeList.getLength(); i++) {
            EventTarget n = (EventTarget) nodeList.item(i);
            n.addEventListener("click", new EventListener() {
                @Override
                public void handleEvent(Event event) {
                    EventTarget eventTarget = event.getTarget();
                    
                    if (eventTarget instanceof HTMLAnchorElement == false) {
                        return;
                    }

                    HTMLAnchorElement hrefObj = (HTMLAnchorElement) event.getTarget();
                    String link = hrefObj.getBaseURI();
                    String type = hrefObj.getHref();
                    String name = hrefObj.getName();
                    String id = hrefObj.getId();
                    
                    recordPane1.appendText("link: " + link + "\n");
                    recordPane1.appendText("type: " + type + "\n");
                    recordPane1.appendText("name: " + name + "\n");
                    recordPane1.appendText("id: " + id + "\n");
                    recordPane1.appendText("xpath: " + getXPath(hrefObj) + "\n");
                }
            }, true);
        }
    }

    public void inputAnchorListener() {
        Document document = (Document) webEngine1.executeScript("document");
        NodeList nodeList = document.getElementsByTagName("input");
        for (int i = 0; i < nodeList.getLength(); i++) {
            EventTarget n = (EventTarget) nodeList.item(i);
            n.addEventListener("click", new EventListener() {
                @Override
                public void handleEvent(Event event) {
                    EventTarget eventTarget = event.getTarget();
                    
                    if (eventTarget instanceof HTMLAnchorElement == false) {
                        return;
                    }

                    HTMLAnchorElement inputObj = (HTMLAnchorElement) event.getTarget();
                    String type = inputObj.getHref();
                    String name = inputObj.getName();
                    String id = inputObj.getId();

                    recordPane1.appendText("type: " + type + "\n");
                    recordPane1.appendText("name: " + name + "\n");
                    recordPane1.appendText("id: " + id + "\n");
                    recordPane1.appendText("xpath: " + getXPath(inputObj) + "\n");

                }
            }, true);
        }
    }

    public void textAnchorListener() {
        Document document = (Document) webEngine1.executeScript("document");
        NodeList nodeList = document.getElementsByTagName("input");
        for (int i = 0; i < nodeList.getLength(); i++) {
            EventTarget n = (EventTarget) nodeList.item(i);
            n.addEventListener("change", new EventListener() {
                @Override
                public void handleEvent(Event event) {
                    EventTarget eventTarget = event.getTarget();

                    if (eventTarget instanceof HTMLAnchorElement == false) {
                        return;
                    }
                    
                    HTMLAnchorElement inputObj = (HTMLAnchorElement) event.getTarget();
                    String type = inputObj.getHref();
                    String name = inputObj.getName();
                    String id = inputObj.getId();

                    recordPane1.appendText("type: " + type + "\n");
                    recordPane1.appendText("name: " + name + "\n");
                    recordPane1.appendText("id: " + id + "\n");
                    recordPane1.appendText("xpath: " + getXPath(inputObj) + "\n");

                }
            }, true);
        }
    }
    
    public String getXPath(Node node){
        return getXPath(node, "");
    }
    
    public String getXPath(Node node, String xpath){
        if (node == null){
            return "";
        }
        String elementName = "";
        if (node instanceof Element) {
            elementName = ((Element) node).getLocalName();
        }
        Node parent = node.getParentNode();
        if (parent == null){
            return xpath;
        }
        return getXPath(parent, "/" + elementName + xpath);
    }
    
//    private JButton getBtnSave() {
//        if (btnSave == null) {
//            btnSave = new JButton();
//            btnSave.setToolTipText("Save scan output");
//            btnSave.setText("Save");
//            btnSave.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(java.awt.event.ActionEvent e) {
//
//                    try {
//                        String s = taOutput.getText();
//
//                        File f = new File("audits.txt");
//                        FileWriter fw = new FileWriter(f);
//                        fw.write(s);
//                    } catch (IOException ioe) {
//                        System.out.println("Exception Caught : " + ioe.getMessage());
//                    }
//                }
//            });
//        }
//        return btnSave;
//    }


}
