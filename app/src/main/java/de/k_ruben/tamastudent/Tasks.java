package de.k_ruben.tamastudent;

/**
 * Created by Ruben on 26.04.2016.
 */
public abstract class Tasks implements Executeable
{
    //Erstelldatum der Aufgabe
    long startDate;
    //Datum seit letztem ausführen von Methode execute
    long lastExecute;
    //Datum, wann die Aufgabe "abgearbeitet" ist
    long expireDate;
    //Ob Aufgabe ewig laufen soll
    boolean isInfinite = false;
    //Variable, global den selben TimeStamp zu unterschiedlichen Laufzeiten zu haben
    long currentTimeMillis;
    //Ob bei ewig laufender Aufgabe mit Delay, der Delay nicht wiederholt werden soll.
    boolean uniqueDelay = true;
    //Ob die Aufgabe Täglich stattfinden soll (Exklusiv für Aufgaben mit festen Start- und Endpunkten
    boolean dailyTask = false;

    int delay = 0;


    /**
     * Errechnet die Zeit, die seit letztem Aufruf vergangen ist
     *
     * @return Die Zeit, die vergangen ist
     */
    protected double elapsedTime()
    {
        currentTimeMillis = System.currentTimeMillis();
        //Wenn task isInfinite und currentTimeMillis > epireDate
        if (isInfinite && currentTimeMillis >= expireDate)
        {
            //errechnen, wie viel Zeit seit dem letzten execute tatsächlich vergangen ist mit Rücksichtnahme auf
            //delay und täglichen Aufgaben
        }
        if (currentTimeMillis < expireDate) return (currentTimeMillis - lastExecute);
        if (currentTimeMillis >= expireDate) return (expireDate - lastExecute);
        return (currentTimeMillis - lastExecute);
    }

    /**
     * Wie lange die Aufgabe insgesamt läuft
     *
     * @return Laufzeit der Aufgabe
     */
    protected double taskDuration()
    {
        return (expireDate - startDate);
    }

    /**
     * Errechnet den Wert, um den sich ein Status ändert
     *
     * @param value Wert, um den sich ein Status insgesamt ändern soll
     * @return Um wie viel sich ein Status innerhalb der vergangenen Zeit ändert
     */
    protected double calculateChangeValue(int value)
    {
        return (value / (taskDuration() / elapsedTime()));
    }

    /**
     * Setzt eine Aktivität wieder zurück
     */
    protected void reset()
    {
        if (dailyTask) reset(1000 * 60 * 60 * 24);
            //if(dailyTask) reset(3000);
        else
        {
            long diff = expireDate - startDate;
            expireDate += diff + delay;
            startDate += diff + delay;
        }
        lastExecute = startDate;
    }

    /**
     * Setzte die Aufgabe zurück um angebene Zeit
     *
     * @param time
     */
    protected void reset(int time)
    {
        expireDate += time + delay;
        startDate += time + delay;
    }
}