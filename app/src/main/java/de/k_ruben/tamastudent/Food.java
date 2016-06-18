package de.k_ruben.tamastudent;

import android.util.Log;

/**
 * Created by Ruben on 08.06.2016.
 */
public class Food extends Tasks
{
    //Wie viel die Aufgabe insgesamt ändern soll
    int changeValueFood;


    /**
     * Normale Aufgabe, die sofort startet und ewig laufen kann
     * @param expireTime            Wie lange die Aufgabe laufen soll
     * @param changeValueFood      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig wiederholt werden soll
     */
    public Food(int expireTime, int changeValueFood, boolean isInfinite)
    {
        this.klasse = "food";
        currentTimeMillis = System.currentTimeMillis();
        startDate = currentTimeMillis;
        lastExecute = currentTimeMillis;
        this.expireDate = currentTimeMillis + expireTime;
        this.changeValueFood = changeValueFood;
        this.isInfinite = isInfinite;
    }

    /**
     * Aufgabe, die mit verzögerung starten soll
     * @param expireTime            Wie lange die Aufgabe laufen soll
     * @param delay                 Mit welcher verzögerung die Aufgabe starten soll
     * @param changeValueFood      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig wiederholt werden soll
     * @param uniqueDelay           Ob der Delay nur einmal beim Start existiert, oder immer (setzt vorraus, dass isInfinte == true)
     */
    public Food(int expireTime, int delay, int changeValueFood, boolean isInfinite, boolean uniqueDelay)
    {
        this.klasse = "food";
        currentTimeMillis = System.currentTimeMillis();
        this.delay = delay;
        startDate = currentTimeMillis + delay;
        lastExecute = startDate;
        this.expireDate = startDate + expireTime;
        this.changeValueFood = changeValueFood;
        this.isInfinite = isInfinite;
        if(!isInfinite) this.uniqueDelay = true;
        else this.uniqueDelay = uniqueDelay;
    }

    /**
     * Tägliche Aufgabe, die innerhalb einer festen Uhrzeit geschehen soll (!WICHTIG: Zeitrahmen von !einem! Tag, sonst resetet dich die Aufgabe andauernd)
     * @param startDate             Anfangs-Uhrzeit
     * @param endDate               End-Uhrzeit
     * @param changeValueFood      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig/in einer Loop ausgeführt werden soll
     * @param calculateBack         Ob bereits verstrichene Zeit nachbrechnet werden soll, wenn die Aufgabe während der
     *                              angegebenen Laufzeit erstellt wird (einmaliges Problem beim erstellen, in der Regel nicht erwünscht -> false)
     */
    public Food(long startDate, long endDate, int changeValueFood, boolean isInfinite, boolean calculateBack)
    {
        this.klasse = "food";
        currentTimeMillis = System.currentTimeMillis();
        this.startDate = startDate;
        this.lastExecute = currentTimeMillis;
        this.expireDate = endDate;
        this.changeValueFood = changeValueFood;
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
            changeValue = calculateChangeValue(changeValueFood);
            s.changeFoodValue(changeValue);
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
