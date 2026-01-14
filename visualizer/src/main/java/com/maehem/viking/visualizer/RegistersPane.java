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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class RegistersPane extends BorderPane {

    private final RegisterItem pcItem = new RegisterItem("PC", "Program Counter", 0xFFFF);
    private final RegisterItem nonItem = new RegisterItem("", "", Integer.MAX_VALUE);
    private final RegisterItem spItem = new RegisterItem("SP", "Stack Pointer", 0x01FF);

    private final RegisterItem aItem = new RegisterItem("A", "Accumulator", 0xFF);
    private final RegisterItem xItem = new RegisterItem("Y", "X Register", 0xFF);
    private final RegisterItem yItem = new RegisterItem("X", "Y Register", 0xFF);
    private final FlagsPane flagsPane;

    public RegistersPane() {
        setMinHeight(180);
        setBackground(new Background(new BackgroundFill(
                Color.web("#DDDDDD"),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        Label label = new Label("Registers:");
        GeneralRegisterPane pcPane = new GeneralRegisterPane(
                4, pcItem, nonItem, spItem
        );
        GeneralRegisterPane ayxPane = new GeneralRegisterPane(
                2,
                aItem, yItem, xItem
        );

        HBox.setHgrow(pcPane, Priority.SOMETIMES);
        HBox.setHgrow(ayxPane, Priority.SOMETIMES);

        HBox upperArea = new HBox(pcPane, ayxPane);
        upperArea.setSpacing(4);

        flagsPane = new FlagsPane();
        flagsPane.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(label, upperArea, flagsPane);
        centerBox.setSpacing(4);
        centerBox.setPadding(new Insets(16));

        setCenter(centerBox);
    }

    public void updateValues(int regs[]) {
        pcItem.setValue(regs[0]);
        spItem.setValue(regs[1]);
        aItem.setValue(regs[2]);
        xItem.setValue(regs[3]);
        yItem.setValue(regs[4]);
        flagsPane.setValue(regs[5]);
    }
}
