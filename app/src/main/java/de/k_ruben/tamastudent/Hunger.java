package de.k_ruben.tamastudent;

/**
 * Created by Ruben on 26.04.2016.
 */
public class Hunger extends Tasks
{

    //Wie viel die Aufgabe insgesamt ändern soll
    int changeValueSleep;


    //Aufgabe
    public Hunger(long expireDate, int changeValueSleep, boolean isInfinite)
    {
        currentTimeMillis = System.currentTimeMillis();
        startDate = currentTimeMillis;
        lastExecute = currentTimeMillis;
        this.expireDate = currentTimeMillis + expireDate;
        this.changeValueSleep = changeValueSleep;
        this.isInfinite = isInfinite;
    }

    @Override
    public void execute(Student s)
    {
        double changeValue = 0;
        changeValue = calculateChangeValue(changeValueSleep);

        s.changeFoodValue(changeValue);
        currentTimeMillis = System.currentTimeMillis();
        lastExecute = currentTimeMillis;
    }
}