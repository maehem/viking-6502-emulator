/*
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with this
    work for additional information regarding copyright ownership.  The ASF
    licenses this file to you under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with the
    License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
    License for the specific language governing permissions and limitations
    under the License.
 */
package com.maehem.viking.visualizer;

import com.maehem.viking.logging.Logging;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class AssemblyEditor2 extends BorderPane {

    public static final Logger LOGGER = Logging.LOGGER;

    public static final int ICON_HEIGHT = 16;
    private static final String FONT_PATH = "/fonts/whitrabt.ttf";
    private static final Font FONT = Font.loadFont(AssemblyEditor2.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() + 2 // Get Font size.
    );
    public Image compileIcon = new Image(getClass().getResourceAsStream("/icons/hammer.png"));
    public Image playIcon = new Image(getClass().getResourceAsStream("/icons/play.png"));
    public Image stepIcon = new Image(getClass().getResourceAsStream("/icons/step.png"));
    public Image stopIcon = new Image(getClass().getResourceAsStream("/icons/stop.png"));

    private List<String> strLines = new ArrayList<>();
    private EventHandler<ActionEvent> saveableHandler;
    public boolean hasEdits = false;
    private final CodeEditorFlow codeArea;
    private final TextFlow opCodeArea;

    @SuppressWarnings({"unchecked"})
    public AssemblyEditor2() {

        Button compileButton = new Button(null, getIcon(compileIcon));
        compileButton.setTooltip(new Tooltip("Compile"));
        Button runButton = new Button(null, getIcon(playIcon));
        runButton.setTooltip(new Tooltip("Run"));
        Button stepButton = new Button(null, getIcon(stepIcon));
        stepButton.setTooltip(new Tooltip("Step"));
        Button stopButton = new Button(null, getIcon(stopIcon));
        stopButton.setTooltip(new Tooltip("Stop"));

        HBox controls = new HBox(compileButton, runButton, stepButton, stopButton);
        controls.setPadding(new Insets(12, 0, 8, 0));
        controls.setAlignment(Pos.CENTER);
        controls.setBackground(new Background(new BackgroundFill(
                Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY
        )));
        setTop(controls);

        codeArea = new CodeEditorFlow();
//        codeArea.setStyle("-fx-line-spacing: 0.5em;");
//        codeArea.setPadding(new Insets(8));
//        codeArea.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        ScrollPane codeSp = new ScrollPane(codeArea);
        codeSp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        codeSp.setOnKeyPressed((t) -> {
            LOGGER.log(Level.SEVERE, "ScrollPane: Key Pressed.");
        });

        opCodeArea = new TextFlow();
        opCodeArea.setStyle("-fx-line-spacing: 0.5em;");
        opCodeArea.setPadding(new Insets(8));
        ScrollPane opCodeSp = new ScrollPane(opCodeArea);

        // Bind the scroll areas
        codeSp.vvalueProperty().bindBidirectional(opCodeSp.vvalueProperty());

        SplitPane splitPane = new SplitPane(codeSp, opCodeSp);
        splitPane.setDividerPosition(0, 0.7);
        setCenter(splitPane);
    }

    public void setFile(Path fileUri) {
        try {
            strLines = Files.readAllLines(fileUri);
            hasEdits = false;
            for (String line : strLines) {
                codeArea.add(line + "\n");
                opCodeArea.getChildren().add(opCodeLineText("00 00 00\n"));
            }
            saveableHandler.handle(new ActionEvent());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "File not found! ==> {0}", fileUri.getFileName());
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Savable handler called whenever we edit to ensure the event listener,
     * i.e. SaveButton is set enabled.
     * <pre>
     * if (file has been edited) {
     * saveHandler.handle(new ActionEvent());
     * }
     * </pre>
     *
     * @param eh
     */
    public void setOnSavable(EventHandler<ActionEvent> eh) {
        this.saveableHandler = eh;
    }

    private static Text opCodeLineText(String text) {
        Text t = new Text(text);
        t.setFont(FONT);

        return t;
    }

    private static ImageView getIcon(Image image) {
        ImageView icon = new ImageView(image);

        icon.setPreserveRatio(true);
        icon.setFitHeight(ICON_HEIGHT);

        return icon;
    }

}
