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
public class FlagsPane extends GridPane {

    private static final String FONT_PATH = "/fonts/whitrabt.ttf";
    private final Font FONT = Font.loadFont(
            AssemblyEditor.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() // Get Font size.
    );
    private final Font FONT_L = Font.loadFont(
            AssemblyEditor.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() + 2 // Get Font size.
    );
    private final FlagValue negVal;
    private final FlagValue overVal;
    private final FlagValue oneVal;
    private final FlagValue brkVal;
    private final FlagValue decVal;
    private final FlagValue irqDisVal;
    private final FlagValue zeroVal;
    private final FlagValue carryVal;

    public FlagsPane() {
        setPadding(new Insets(4, 12, 4, 12));
        setVgap(8);
        setHgap(20);
        //setStyle("-fx-background-color: darkkhaki;");
        setBorder(new Border(new BorderStroke(
                Color.DARKSLATEGRAY, BorderStrokeStyle.SOLID,
                new CornerRadii(12), new BorderWidths(1)
        )));

        Label label = new Label("Flags:");
        add(label, 0, 0, 8, 1);

        // Label Row
        Label negLabel = new FlagLabel("N", "Negative");
        Label overLabel = new FlagLabel("V", "Overflow");
        Label oneLabel = new FlagLabel("1", "Always 1");
        Label brkLabel = new FlagLabel("B", "Break/IRQB Command\n1=BRK  0=IRQB");
        Label decLabel = new FlagLabel("D", "Decimal");
        Label irqDisLabel = new FlagLabel("I", "IRQB Disable");
        Label zeroLabel = new FlagLabel("Z", "Zero\n1=true, 0=false");
        Label carryLabel = new FlagLabel("C", "Carry");

        add(negLabel, 0, 1);
        add(overLabel, 1, 1);
        add(oneLabel, 2, 1);
        add(brkLabel, 3, 1);
        add(decLabel, 4, 1);
        add(irqDisLabel, 5, 1);
        add(zeroLabel, 6, 1);
        add(carryLabel, 7, 1);

        // Value Row
        negVal = new FlagValue("0", "Negative");
        overVal = new FlagValue("0", "Overflow");
        oneVal = new FlagValue("1", "Always 1");
        brkVal = new FlagValue("0", "Break/IRQB Command\n1=BRK  0=IRQB");
        decVal = new FlagValue("0", "Decimal");
        irqDisVal = new FlagValue("0", "IRQB Disable");
        zeroVal = new FlagValue("0", "Zero\n1=true, 0=false");
        carryVal = new FlagValue("0", "Carry");

        add(negVal, 0, 2);
        add(overVal, 1, 2);
        add(oneVal, 2, 2);
        add(brkVal, 3, 2);
        add(decVal, 4, 2);
        add(irqDisVal, 5, 2);
        add(zeroVal, 6, 2);
        add(carryVal, 7, 2);

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

    public void setValue(int val) {
        negVal.setText(((val & 0x80) > 0) ? "1" : "0");
        overVal.setText(((val & 0x40) > 0) ? "1" : "0");
        // 0x02 always 1.
        brkVal.setText(((val & 0x10) > 0) ? "1" : "0");
        decVal.setText(((val & 0x08) > 0) ? "1" : "0");
        irqDisVal.setText(((val & 0x04) > 0) ? "1" : "0");
        zeroVal.setText(((val & 0x02) > 0) ? "1" : "0");
        carryVal.setText(((val & 0x01) > 0) ? "1" : "0");
    }
}
