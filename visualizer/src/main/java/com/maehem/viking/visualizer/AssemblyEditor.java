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
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class AssemblyEditor extends BorderPane {

    public static final Logger LOGGER = Logging.LOGGER;

    private static final String FONT_PATH = "/fonts/whitrabt.ttf";
    private final Font FONT = Font.loadFont(
            AssemblyEditor.class.getResource(FONT_PATH).toExternalForm(),
            Font.getDefault().getSize() // Get Font size.
    );

    TableView<Line> table = new TableView<>();
    final ObservableList<Line> data = FXCollections.observableArrayList();
    private List<String> strLines = new ArrayList<>();
    private final TableColumn codeCol;
    private EventHandler<ActionEvent> saveableHandler;
    public boolean hasEdits = false;

    @SuppressWarnings({"unchecked"})
    public AssemblyEditor() {

        Button compileButton = new Button("Compile");
        Button runButton = new Button("Run");

        HBox controls = new HBox(compileButton, runButton);
        controls.setPadding(new Insets(12, 0, 4, 0));
        controls.setAlignment(Pos.CENTER);
        setTop(controls);

        table.setEditable(true);

        // Load the seven segment font
        TableColumn lineNumberCol = new TableColumn("Line");
        lineNumberCol.setMinWidth(40);
        lineNumberCol.setPrefWidth(40);
        lineNumberCol.setResizable(false);
        lineNumberCol.setCellValueFactory(
                new PropertyValueFactory<Line, String>("lineNumber"));

        lineNumberCol.setCellFactory((tableColumn) -> {
            TableCell<Line, String> tableCell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (isEmpty()) {
                        setText("");
                    } else {
                        setTextFill(Color.GRAY);
                        setFont(FONT);
                        setText(item);
                        setAlignment(Pos.BASELINE_RIGHT);
                        setPadding(new Insets(0, 8, 0, 0));
                        this.setStyle("-fx-background-color: lightgrey;");
                    }
                }
            };

            return tableCell;
        });

        codeCol = new TableColumn("Content");
        codeCol.setMinWidth(400);
        codeCol.setCellValueFactory(
                new PropertyValueFactory<Line, String>("code"));
        codeCol.setCellFactory(TextFieldTableCell.<Line>forTableColumn());

//        codeCol.setCellFactory((tableColumn) -> {
//            TableCell<Line, String> tableCell = new TableCell<>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    if (isEmpty()) {
//                        setText("");
//                    } else {
//                        //setTextFill(Color.web("#333"));
//                        setFont(FONT);
//                        setText(item);
//                        // If index is two we set the background color explicitly.
//                        if (getIndex() % 2 == 1) {
//                            this.setStyle("-fx-background-color: #EFE;");
//                        }
//                    }
//                }
//            };
//        });
        codeCol.setOnEditCommit((t) -> {
            LOGGER.log(Level.SEVERE, "Edit commit.");

            EventHandler<CellEditEvent<Line, String>> eventHandler = new EventHandler<CellEditEvent<Line, String>>() {
                @Override
                public void handle(CellEditEvent<Line, String> t) {
                    Line line = ((Line) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    LOGGER.log(Level.SEVERE,
                            "Change line: {0} ==> {1}",
                            new Object[]{t.getTablePosition().getRow(), line.getCode()}
                    );
                    line.setCode(t.getNewValue());
                    table.refresh();
                }
            };
            eventHandler.handle((CellEditEvent<Line, String>) t);
        });
//            codeCol.setOnEditCommit(event -> {
//                String[] row = event.getRowValue();
//                row[index] = event.getNewValue();
//            return tableCell;
//
//        });
        Platform.runLater(() -> {
            codeCol.setPrefWidth(600);
        });

        table.setItems(data);
        table.getColumns().addAll(lineNumberCol, codeCol);

        rebuildTable();

        setCenter(table);
    }

    public void setFile(Path fileUri) {
        try {
            strLines = Files.readAllLines(fileUri);
            rebuildTable();
            hasEdits = false;
            saveableHandler.handle(new ActionEvent());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "File not found! ==> {0}", fileUri.getFileName());
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void rebuildTable() {
        data.removeAll(data);
        int i = 1;
        for (String line : strLines) {
            data.add(new Line(String.valueOf(i), line));
            i++;
        }
        table.refresh();
    }

    public static class Line {

        private final SimpleStringProperty lineNumber;
        private final SimpleStringProperty code;

        private Line(String num, String code) {
            this.lineNumber = new SimpleStringProperty(num);
            this.code = new SimpleStringProperty(code);
        }

        public String getLineNumber() {
            return lineNumber.get();
        }

        public void setLineNumber(String fName) {
            lineNumber.set(fName);
        }

        public String getCode() {
            return code.get();
        }

        public void setCode(String fName) {
            code.set(fName);
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

}
