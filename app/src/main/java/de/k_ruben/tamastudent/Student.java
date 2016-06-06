package de.k_ruben.tamastudent;


/**
 * Created by Ruben on 23.04.2016.
 */
public class Student
{
    public String name;
    public double sleep;
    public double hunger;
    public double uni;
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
    public Student()
    {
        if(name == null)
        {
            //lege neuen Studenten an
            //Frage nach einem Namen der mehr als 1 Zeichen hat
            this.name = "Peter"; //hier wird die Einagbe rein gespeichert
            //Wahrscheinlich lieber schlechte Stats machen, damit man beim erstmaligen Spielen auch was zu tun hat
            this.sleep = 20;
            this.hunger = 20;
            this.uni = 35;
            this.entertainment = 42;
            this.startDate = System.currentTimeMillis();
        }
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
    void changeHungerValue(double changeValue)
    {
        if((hunger + changeValue) < 0) hunger = 0;
        else if((hunger + changeValue) <= 100) hunger += changeValue;
        else hunger = 100;
    }

    /**
     * Gibt den Hunger-Wert zurück
     * @return                  Hunger-Wert
     */
    double getHungerValue()
    {
        return hunger;
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
    void changeUniValue(double changeValue)
    {
        if((uni + changeValue) < 0) uni = 0;
        else if((uni + changeValue) <= 100)
        {
            uni += changeValue;
        }
        else uni = 100;
    }

    double getUniValue()
    {
        return uni;
    }

    int getHighscore()
    {
        return (int) ((System.currentTimeMillis() - startDate)/1000/60/60);
    }
}

