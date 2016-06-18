package de.k_ruben.tamastudent;


import android.util.Log;

/**
 * Created by Ruben on 23.04.2016.
 */
public class Student
{
    public String name;
    public double sleep;
    public double food;
    public double education;
    public double entertainment;
    public long startDate;

    public boolean isSleeping;
    public boolean isEating;
    public boolean isPlaying;

    /**
     * Erstellung des Studenten
     * (Evtl. noch als Übergabe parameter ein Dateiname. Aus dieser Datei werden
     * dann die Stats und Aufgaben ausgelesen (ermöglicht auch mehrere Spielstände)
     */
    public Student(String name)
    {
        //lege neuen Studenten an
        //Frage nach einem Namen der mehr als 1 Zeichen hat
        this.name = name; //hier wird die Einagbe rein gespeichert
        //Wahrscheinlich lieber schlechte Stats machen, damit man beim erstmaligen Spielen auch was zu tun hat
        this.sleep = 20;
        this.food = 20;
        this.education = 35;
        this.entertainment = 42;
        this.startDate = System.currentTimeMillis();
    }

    /**
     * Ändert den Schlaf-Wert
     * @param changeValue       Wert, um den sich der Schlaf-Wert ändern soll
     */
    void changeSleepValue(double changeValue)
    {
        if((sleep + changeValue) < 0) sleep = 0;
        else if((sleep + changeValue) <= 100)
        {
            sleep += changeValue;
        }
        else sleep = 100;
    }

    /**
     * Gibt den Schlaf-Wert zurück
     * @return                  Schlaf-Wert
     */
    double getSleepValue()
    {
        return sleep;
    }

    /**
     * Ändert den Hunger-Wert
     * @param changeValue       Wert, um den sich der Hunger-Wert ändern soll
     */
    void changeFoodValue(double changeValue)
    {
        if((food + changeValue) < 0) food = 0;
        else if((food + changeValue) <= 100) food += changeValue;
        else food = 100;
    }

    /**
     * Gibt den Hunger-Wert zurück
     * @return                  Hunger-Wert
     */
    double getHungerValue()
    {
        return food;
    }

    /**
     * Ändert den Entertainment-Wert
     * @param changeValue       Wert, um den sich der Entertainment-Wert ändern soll
     */
    void changeEntertainmentValue(double changeValue)
    {
        if((entertainment + changeValue) < 0) entertainment = 0;
        else if((entertainment + changeValue) <= 100)
        {
            entertainment += changeValue;
        }
        else entertainment = 100;
    }

    /**
     * Gibt den Entertainment-Entertainment zurück
     * @return                  Schlaf-Wert
     */
    double getEntertainmentValue()
    {
        return entertainment;
    }

    /**
     * Ändert den Schlaf-Wert
     * @param changeValue       Wert, um den sich der Schlaf-Wert ändern soll
     */
    void changeEducationValue(double changeValue)
    {
        if((education + changeValue) < 0) education = 0;
        else if((education + changeValue) <= 100)
        {
            education += changeValue;
        }
        else education = 100;
    }

    double getEducationValue()
    {
        return education;
    }

    String getScore()
    {
        String score = "";
        int cheatTimeAlive = 59900 * 60;
        cheatTimeAlive = 0;
        long timeAlive = (System.currentTimeMillis() + cheatTimeAlive - startDate);

        //Berechne die vergangene Zeit
        int hours = (int) timeAlive/1000/60/60;
        int minutes = (int) timeAlive/1000/60 - hours*60;
        int seconds = (int) (timeAlive/1000 - minutes*60) - hours*60*60;

        //Log.d("TAG", "hours: " + hours + " minutes: " + minutes + " seconds " + seconds);

        return (convertToTime(hours) + ":" + convertToTime(minutes) + ":" + convertToTime(seconds));
    }

    public String convertToTime(int time)
    {
        //erzeuge ein vernünftiges Aussehen bei Werten kleiner 10
        if(time < 10) return "0" + time;
        else return "" + time;
    }
}

