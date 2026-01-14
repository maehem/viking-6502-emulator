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

import java.nio.file.Path;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class InputOutputPane extends SplitPane {

    private static final String FONT_PATH = "/fonts/whitrabt.ttf";
    private static final Font FONT = Font.loadFont(AssemblyEditor2.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() + 1 // Get Font size.
    );

    private final AssemblyEditor2 editor;

    public InputOutputPane() {
        setOrientation(Orientation.VERTICAL);
        TextFlow outputFlow = new TextFlow();
        outputFlow.setLineSpacing(FONT.getSize() * 0.3);
        outputFlow.setPadding(new Insets(FONT.getSize() * 0.5));

        editor = new AssemblyEditor2();

        getItems().addAll(editor, outputFlow);

        setDividerPosition(0, 0.7);

        outputFlow.getChildren().add(outputMessage("Compiler Start...\n"));
        outputFlow.getChildren().add(outputMessage("18 lines compiled in 0.08 seconds.\n"));
        outputFlow.getChildren().add(outputMessage("0 Errors\n"));
    }

    protected void setFilePath(Path filePath) {
        editor.setFile(filePath);
    }

    protected AssemblyEditor2 getEditor() {
        return editor;
    }

    private Text outputMessage(String message) {
        Text txt = new Text(message);
        txt.setFont(FONT);

        return txt;
    }

}
