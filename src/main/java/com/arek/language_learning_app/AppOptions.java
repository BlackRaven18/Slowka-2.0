package com.arek.language_learning_app;

import com.arek.clock_utils.Time;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;


@SuppressWarnings("unused")
public final class AppOptions {

    public static final int FRAME_EXTRA_WIDTH = 14;
    public static final int FRAME_EXTRA_HEIGHT = 38;

    private static AppOptions instance;

    private final File optionsFile = new File("options.txt");

    public final String APP_VERSION = "v2.0";
    public final String APP_TITLE = "Słówka " + APP_VERSION;
    public final Image APP_ICON = new Image("file:icon.png");
    public final File APP_TRAY_ICON = new File("icon.png");

    private double appX;
    private double appY;

    private long clockHours;
    private long clockMinutes;
    private long clockSeconds;

    private int prefAppWidth;
    private int prefAppHeight;

    private int minAppWidth;
    private int minAppHeight;

    private TextField lastFocusedTextField;
    private int lastFocusedTextFieldCaretPosition;

    private AppOptions(){
        if(Main.getMainStage() != null){
            appX = Main.getMainStage().getX();
            appY = Main.getMainStage().getY();
        } else{
            appX = 100;
            appY = 100;
        }

        readOptionsFromFile();
    }

    public static AppOptions getInstance(){
        if(instance == null){
            instance = new AppOptions();
        }

        return instance;
    }

    private void readOptionsFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(optionsFile))){
            String line = reader.readLine();

            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            ArrayList<String> tokens = new ArrayList<>();
            while(stringTokenizer.hasMoreTokens()){
                tokens.add(stringTokenizer.nextToken());
            }

            prefAppWidth = Integer.parseInt(tokens.get(0));
            prefAppHeight = Integer.parseInt(tokens.get(1));
            minAppWidth = Integer.parseInt(tokens.get(2));
            minAppHeight = Integer.parseInt(tokens.get(3));
            clockHours = Integer.parseInt(tokens.get(4));
            clockMinutes = Integer.parseInt(tokens.get(5));
            clockSeconds = Integer.parseInt(tokens.get(6));

        } catch (IOException e){
            e.printStackTrace();
            setDefaultOptions();
        }
    }

    public void saveOptionsToFile(){
        try(PrintWriter writer = new PrintWriter(optionsFile)){
            String line = String.format("%d,%d,%d,%d,%d,%d,%d", prefAppWidth, prefAppHeight, minAppWidth, minAppHeight, clockHours, clockMinutes, clockSeconds);
            writer.write(line);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setDefaultOptions(){
        prefAppWidth = 720;
        prefAppHeight = 480;

        minAppWidth = 600;
        minAppHeight = 400;

        clockHours = 0;
        clockMinutes = 30;
        clockSeconds = 0;
    }

    public Time getClockTime() {
        return new Time(clockHours, clockMinutes, clockSeconds);
    }


    public int getPrefAppWidth() {
        return prefAppWidth;
    }

    public void setPrefAppWidth(int prefAppWidth) {
        this.prefAppWidth = prefAppWidth;
    }

    public int getPrefAppHeight() {
        return prefAppHeight;
    }

    public void setPrefAppHeight(int prefAppHeight) {
        this.prefAppHeight = prefAppHeight;
    }

    public int getMinAppWidth() {
        return minAppWidth;
    }

    public void setMinAppWidth(int minAppWidth) {
        this.minAppWidth = minAppWidth;
    }

    public int getMinAppHeight() {
        return minAppHeight;
    }

    public void setMinAppHeight(int minAppHeight) {
        this.minAppHeight = minAppHeight;
    }

    public File getOptionsFile() {
        return optionsFile;
    }

    public void setClockHours(long clockHours) {
        this.clockHours = clockHours;
    }

    public void setClockMinutes(long clockMinutes) {
        this.clockMinutes = clockMinutes;
    }

    public void setClockSeconds(long clockSeconds) {
        this.clockSeconds = clockSeconds;
    }

    public TextField getLastFocusedTextField() {
        return lastFocusedTextField;
    }

    public void setLastFocusedTextField(TextField lastFocusedTextField) {
        this.lastFocusedTextField = lastFocusedTextField;
    }

    public int getLastFocusedTextFieldCaretPosition() {
        return lastFocusedTextFieldCaretPosition;
    }

    public void setLastFocusedTextFieldCaretPosition(int lastFocusedTextFieldCaretPosition) {
        this.lastFocusedTextFieldCaretPosition = lastFocusedTextFieldCaretPosition;
    }

    public void clearLastFocusedTextFieldAndCaretPosition(){
        lastFocusedTextField = null;
        lastFocusedTextFieldCaretPosition = 0;
    }

    public double getAppX() {
        return appX;
    }

    public void setAppX(double appX) {
        this.appX = appX;
    }

    public double getAppY() {
        return appY;
    }

    public void setAppY(double appY) {
        this.appY = appY;
    }
}
