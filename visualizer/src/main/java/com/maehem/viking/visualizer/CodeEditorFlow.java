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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class CodeEditorFlow extends TextFlow {

    public static final Logger LOGGER = Logging.LOGGER;

    private static final String FONT_PATH = "/fonts/whitrabt.ttf";
    private static final Font FONT = Font.loadFont(AssemblyEditor2.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() + 2 // Get Font size.
    );

    public CodeEditorFlow() {
        setStyle("-fx-line-spacing: 0.5em;");
        setPadding(new Insets(8));
        setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY
        )));

        setOnMouseClicked((t) -> {
            LOGGER.log(Level.SEVERE, "CodeEditorFlow: Mouse Clicked at : {0},{1}", new Object[]{t.getX(), t.getY()});

        });
    }

    public void add(String s) {
        Text codeLineText = codeLineText(s);
        getChildren().add(codeLineText);
        codeLineText.setOnMouseClicked((ev) -> {
            LOGGER.log(Level.SEVERE, "Code Line: Key Pressed: " + codeLineText.getText());
            TextField editTextField = editTextField(codeLineText.getText());
            editTextField.setOnKeyPressed((t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    t.consume();
                    codeLineText.setText(editTextField.getText() + "\n");
                    getChildren().remove(editTextField);
                }
            });
            int indexOf = getChildren().indexOf(codeLineText);
            codeLineText.setText("\n");
            getChildren().add(indexOf, editTextField);
        });
    }

    private static Text codeLineText(String text) {
        Text t = new Text(text);
        t.setFont(FONT);

        return t;
    }

    private TextField editTextField(String text) {
        TextField tf = new TextField(text);
        tf.setPrefWidth(getWidth() - 20.0);
        tf.setFont(FONT);

        return tf;
    }
}
