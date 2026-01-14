/*
 * MIT License
 *
 * Copyright (c) 2024 Mark J. Koch ( @maehem on GitHub )
 *
 * Portions of this software are Copyright (c) 2018 Henadzi Matuts and are
 * derived from their project: https://github.com/HenadziMatuts/Reuromancer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.maehem.viking.visualizer;

import com.maehem.viking.AppProperties;
import com.maehem.viking.logging.Logging;
import java.io.File;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class RootPane extends BorderPane {

    public static final Logger LOGGER = Logging.LOGGER;

    public static final String PANE_SIZE_W_PROP_KEY = "RootPane.W";
    public static final String PANE_SIZE_H_PROP_KEY = "RootPane.H";
    public static final String PANE_SPLIT_PROP_KEY = "RootPane.split";

    public static final int ICON_HEIGHT = 24;

    private final FileChooser fileChooser = new FileChooser();

    public Image newIcon = new Image(getClass().getResourceAsStream("/icons/file.png"));
    public Image loadIcon = new Image(getClass().getResourceAsStream("/icons/folder.png"));
    public Image saveIcon = new Image(getClass().getResourceAsStream("/icons/floppy-disk.png"));

    private final ResourceBundle MSG;
    private final SplitPane splitPane;
    private final RegistersPane registersPane;
    String filePath = "/Users/mark/Desktop/test-6502-multiply.asm";
    private final InputOutputPane ioPane;

    public RootPane() {
        //setPrefSize(640, 480);
        MSG = ResourceBundle.getBundle("i18n/Root"); // Must be done after super() called.
        LOGGER.log(Level.CONFIG, "Create Root Pane.");

        ioPane = new InputOutputPane();

        ButtonPane topPane = new ButtonPane();

        setTop(topPane);

        splitPane = new SplitPane();

        registersPane = new RegistersPane();
        Label memorylabel = new Label("Memory:    ");
        StartAddressWidget startAddressWidget = new StartAddressWidget();
        Button setButton = new Button("Set");
        setButton.setTextAlignment(TextAlignment.CENTER);
        HBox buttonBox = new HBox(setButton);
        buttonBox.setPadding(new Insets(16, 8, 8, 8));
        HBox memoryHeaderBox = new HBox(
                memorylabel,
                startAddressWidget,
                buttonBox
        );
        memoryHeaderBox.setAlignment(Pos.CENTER);
        memoryHeaderBox.setSpacing(32);
        memoryHeaderBox.setPadding(new Insets(8, 8, 0, 8));
        HBox.setHgrow(setButton, Priority.ALWAYS);

        MemoryPane memPane = new MemoryPane();
        VBox.setVgrow(memPane, Priority.ALWAYS);

        VBox rightControl = new VBox(
                registersPane,
                memoryHeaderBox,
                memPane);

        splitPane.getItems().addAll(ioPane, rightControl);

        setCenter(splitPane);

        Label copyrightText = new Label("Copyright ©2024 by  Mark J. Koch");
        Region midPane = new Region();
        midPane.setPrefSize(24, 24);
        HBox.setHgrow(midPane, Priority.ALWAYS);
        Label licenseText = new Label("Released under Apache 2.0 License.");
        HBox bottomBox = new HBox(copyrightText, midPane, licenseText);
        bottomBox.setPadding(new Insets(4, 8, 0, 8));
        setBottom(bottomBox);

        Platform.runLater(() -> {
            ioPane.setFilePath(Paths.get(filePath));
        });
        // initListeners();

    }

    private class ButtonPane extends HBox {

        public ButtonPane() {
            Button newButton = new Button(null, getIcon(newIcon));
            newButton.setTooltip(new Tooltip("New File..."));
            Button loadButton = new Button(null, getIcon(loadIcon));
            loadButton.setTooltip(new Tooltip("Load File..."));
            Button saveButton = new Button(null, getIcon(saveIcon));
            saveButton.setTooltip(new Tooltip("Save File..."));
            saveButton.setDisable(true);

            setMinHeight(ICON_HEIGHT);
            setSpacing(ICON_HEIGHT / 8);

            getChildren().addAll(newButton, loadButton, saveButton);

            loadButton.setOnAction((final ActionEvent e) -> {
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(getScene().getWindow());
                if (file != null) {
                    ioPane.setFilePath(Paths.get(file.getAbsolutePath()));
                }
            });
            ioPane.getEditor().setOnSavable((final ActionEvent e) -> {
                saveButton.setDisable(!ioPane.getEditor().hasEdits);
            });
        }

    }

    private static ImageView getIcon(Image image) {
        ImageView icon = new ImageView(image);

        icon.setPreserveRatio(true);
        icon.setFitHeight(ICON_HEIGHT);

        return icon;
    }

    public void pushProperties(AppProperties appProperties) {
        appProperties.setProperty(PANE_SIZE_W_PROP_KEY, String.valueOf(getWidth()));
        appProperties.setProperty(PANE_SIZE_H_PROP_KEY, String.valueOf(getHeight()));
        appProperties.setProperty(PANE_SPLIT_PROP_KEY, String.valueOf(splitPane.getDividerPositions()[0]));
    }

    public void pullProperties(AppProperties appProperties) {
        String sizeW = appProperties.getProperty(PANE_SIZE_W_PROP_KEY, "1280");
        String sizeH = appProperties.getProperty(PANE_SIZE_H_PROP_KEY, "960");
        setPrefSize(Double.parseDouble(sizeW), Double.parseDouble(sizeH));

        String split = appProperties.getProperty(PANE_SPLIT_PROP_KEY, "0.66");
        splitPane.setDividerPosition(0, Double.parseDouble(split));
    }

    public void refresh() {
    }

    private class StartAddressWidget extends VBox {

        public StartAddressWidget() {
            Label label = new Label("Start Address");
            label.setStyle("-fx-font-size: 0.7em; -fx-font-weight: bold;");
            TextField field = new TextField("0000");
            setSpacing(4);
            field.setPrefWidth(80);

            getChildren().addAll(label, field);
        }

    }

    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View Assembly Files");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("ASM", "*.asm")
        );
    }
}
