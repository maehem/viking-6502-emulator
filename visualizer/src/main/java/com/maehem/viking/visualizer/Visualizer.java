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

import com.maehem.viking.AppProperties;
import com.maehem.viking.logging.Logging;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
public class Visualizer extends Application {

    public static final Logger LOGGER = Logging.LOGGER;

    public static final String WINDOW_POS_X_PROP_KEY = "Window.X";
    public static final String WINDOW_POS_Y_PROP_KEY = "Window.Y";

    private final ResourceBundle MSG;
    private final AppProperties appProperties;
    private Scene scene;
    private final RootPane rootPane;

    public Visualizer() {
        super();
        Logging.configureLogging();

        //Locale.setDefault(Locale.GERMANY);  // uncomment for i18n debug
        MSG = ResourceBundle.getBundle("i18n/App"); // Must be done after super() called.
        LOGGER.setLevel(Level.FINEST);
        LOGGER.log(Level.CONFIG, "Log Start");

        appProperties = AppProperties.getInstance();

        rootPane = new RootPane();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(MSG.getString("TITLE"));
        // Host Services allows opening of browser links inside app.
        appProperties.setHostServices(getHostServices());

        // Add icon for the app
        Image appIcon = new Image(getClass().getResourceAsStream("/icons/viking.png"));
        stage.getIcons().add(appIcon);

        scene = new Scene(rootPane);  // Create the Scene
//        scene.getStylesheets().add(this.getClass().getResource("/style/dark.css").toExternalForm());
        stage.setScene(scene); // Add the scene to the Stage

        rootPane.pullProperties(appProperties); // Load settings from prop file.

        // Configure saved properties.
        pullProperties(appProperties, stage);

        stage.show(); // Display the Stage

        stage.setOnCloseRequest((t) -> {
            pushProperties(appProperties, stage);
            rootPane.pushProperties(appProperties);
            appProperties.save();

            Platform.exit();
        });
    }

    public void pushProperties(AppProperties appProperties, Stage stage) {
        appProperties.setProperty(WINDOW_POS_X_PROP_KEY, String.valueOf(stage.getX()));
        appProperties.setProperty(WINDOW_POS_Y_PROP_KEY, String.valueOf(stage.getY()));
    }

    public void pullProperties(AppProperties appProperties, Stage stage) {
        String posX = appProperties.getProperty(WINDOW_POS_X_PROP_KEY, "50");
        String posY = appProperties.getProperty(WINDOW_POS_Y_PROP_KEY, "20");
        stage.setX(Double.parseDouble(posX));
        stage.setY(Double.parseDouble(posY));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
