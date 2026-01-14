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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class AYXPane extends GridPane {

    private static final String FONT_PATH = "/fonts/whitrabt.ttf";
    private final Font FONT = Font.loadFont(
            AssemblyEditor.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() // Get Font size.
    );
    private final Font FONT_L = Font.loadFont(
            AssemblyEditor.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() + 2 // Get Font size.
    );

    public AYXPane() {
        setPadding(new Insets(4, 24, 4, 24));
        setVgap(4);
        setHgap(16);
        //setStyle("-fx-background-color: darkkhaki;");
        setBorder(new Border(new BorderStroke(
                Color.DARKSLATEGRAY, BorderStrokeStyle.SOLID,
                new CornerRadii(12), new BorderWidths(1)
        )));
        setAlignment(Pos.CENTER);

        // Label Column
        Label aLabel = new FlagLabel("A:", "Accumulator");
        Label xLabel = new FlagLabel("Y:", "Y Register");
        Label yLabel = new FlagLabel("X:", "X Register");

        add(aLabel, 0, 0);
        add(xLabel, 0, 1);
        add(yLabel, 0, 2);

        // Value Column
        Label aVal = new FlagValue("FF", "Accumulator");
        Label xVal = new FlagValue("FF", "Y Register");
        Label yVal = new FlagValue("FF", "X Register");

        add(aVal, 1, 0);
        add(xVal, 1, 1);
        add(yVal, 1, 2);

    }

    private class FlagLabel extends Label {

        public FlagLabel(String label, String tooltip) {
            super(label);
            setTooltip(new Tooltip(tooltip));
            setFont(FONT);
            setStyle("-fx-font-weight: bold;");
        }

    }

    private class FlagValue extends Label {

        public FlagValue(String label, String tooltip) {
            super(label);
            setTooltip(new Tooltip(tooltip));
            setFont(FONT_L);
            //setStyle("-fx-font-size: 1.2em;");
        }

    }
}
