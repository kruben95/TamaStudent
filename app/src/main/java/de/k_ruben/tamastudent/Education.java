package de.k_ruben.tamastudent;

import android.util.Log;

/**
 * Created by Ruben on 08.06.2016.
 */
public class Education extends Tasks
{
    //Wie viel die Aufgabe insgesamt ändern soll
    int changeValueEducation;


    /**
     * Normale Aufgabe, die sofort startet und ewig laufen kann
     * @param expireTime            Wie lange die Aufgabe laufen soll
     * @param changeValueEducation      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig wiederholt werden soll
     */
    public Education(int expireTime, int changeValueEducation, boolean isInfinite)
    {
        this.klasse = "education";
        currentTimeMillis = System.currentTimeMillis();
        startDate = currentTimeMillis;
        lastExecute = currentTimeMillis;
        this.expireDate = currentTimeMillis + expireTime;
        this.changeValueEducation = changeValueEducation;
        this.isInfinite = isInfinite;
    }

    /**
     * Aufgabe, die mit verzögerung starten soll
     * @param expireTime            Wie lange die Aufgabe laufen soll
     * @param delay                 Mit welcher verzögerung die Aufgabe starten soll
     * @param changeValueEducation      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig wiederholt werden soll
     * @param uniqueDelay           Ob der Delay nur einmal beim Start existiert, oder immer (setzt vorraus, dass isInfinte == true)
     */
    public Education(int expireTime, int delay, int changeValueEducation, boolean isInfinite, boolean uniqueDelay)
    {
        this.klasse = "education";
        currentTimeMillis = System.currentTimeMillis();
        this.delay = delay;
        startDate = currentTimeMillis + delay;
        lastExecute = startDate;
        this.expireDate = startDate + expireTime;
        this.changeValueEducation = changeValueEducation;
        this.isInfinite = isInfinite;
        if(!isInfinite) this.uniqueDelay = true;
        else this.uniqueDelay = uniqueDelay;
    }

    /**
     * Tägliche Aufgabe, die innerhalb einer festen Uhrzeit geschehen soll (!WICHTIG: Zeitrahmen von !einem! Tag, sonst resetet dich die Aufgabe andauernd)
     * @param startDate             Anfangs-Uhrzeit
     * @param endDate               End-Uhrzeit
     * @param changeValueEducation      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig/in einer Loop ausgeführt werden soll
     * @param calculateBack         Ob bereits verstrichene Zeit nachbrechnet werden soll, wenn die Aufgabe während der
     *                              angegebenen Laufzeit erstellt wird (einmaliges Problem beim erstellen, in der Regel nicht erwünscht -> false)
     */
    public Education(long startDate, long endDate, int changeValueEducation, boolean isInfinite, boolean calculateBack)
    {
        this.klasse = "education";
        currentTimeMillis = System.currentTimeMillis();
        this.startDate = startDate;
        this.lastExecute = currentTimeMillis;
        this.expireDate = endDate;
        this.changeValueEducation = changeValueEducation;
        this.isInfinite = isInfinite;
        dailyTask = true;
        if(calculateBack && startDate < currentTimeMillis && expireDate > currentTimeMillis) this.lastExecute = startDate;
    }

    /**
     * Führe die Aufgabe aus
     * @param s                     Student, der die Aufgabe ausführen soll
     */
    @Override
    public void execute(Student s)
    {
        currentTimeMillis = System.currentTimeMillis();
        double changeValue = 0;
        if (startDate < currentTimeMillis)
        {
            changeValue = calculateChangeValue(changeValueEducation);
            s.changeEducationValue(changeValue);
            lastExecute = currentTimeMillis;
        }

        //Ob die Aufgabe endlich ist und abgelaufen ist
        if (isInfinite == false && expireDate <= currentTimeMillis)
        {
            Log.d("expire","Letzter Aufruf bevor expire");
            deleteFlag = true;
        }
        //Ob die Aufgabe unendlich ist und abgelaufen ist
        else if (isInfinite == true && expireDate <= currentTimeMillis)
        {
            Log.d("reset", "letzter Aufruf bevor reset");
            reset();
        }
    }
}
