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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class MemoryPane extends ScrollPane {

    private static final String FONT_PATH = "/fonts/whitrabt.ttf";
    private final Font FONT = Font.loadFont(
            AssemblyEditor.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() // Get Font size.
    );

    TextFlow tf = new TextFlow();

    public MemoryPane() {
        setContent(tf);
        tf.setPadding(new Insets(8));
        tf.setLineSpacing(FONT.getSize() * 0.4);
        viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> bounds, Bounds oldBounds, Bounds newBounds) {
                tf.setPrefWidth(newBounds.getWidth());
            }
        });
        for (int i = 0; i < 128; i++) {
            Text t = new Text("00  ");
            t.setFont(FONT);
            tf.getChildren().add(t);
        }
    }

}
