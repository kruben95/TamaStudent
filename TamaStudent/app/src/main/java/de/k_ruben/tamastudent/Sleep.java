package de.k_ruben.tamastudent;

import java.util.Calendar;

/**
 * Created by Ruben on 26.04.2016.
 */
public class Sleep extends Tasks
{

    //Wie viel die Aufgabe insgesamt ändern soll
    int changeValueSleep;

    //Zeitlich begrenzte Aufgabe
    public Sleep(long expireDate, int changeValueSleep)
    {
        startDate = System.currentTimeMillis();
        lastExecute = System.currentTimeMillis();
        this.expireDate = expireDate;
        this.changeValueSleep = changeValueSleep;
    }

    //unendlich laufende Aufgabe
    public Sleep(long expireDate, boolean isInfinite, int changeValueSleep)
    {
        startDate = System.currentTimeMillis();
        lastExecute = System.currentTimeMillis();
        this.expireDate = expireDate;
        this.isInfinite = isInfinite;
        this.changeValueSleep = changeValueSleep;
    }

    @Override
    public void execute(Student s)
    {
        double changeValue = 0;
        changeValue = calculateChangeValue(changeValueSleep);

        s.setSleepValue(changeValue);
        lastExecute = System.currentTimeMillis();
    }
}