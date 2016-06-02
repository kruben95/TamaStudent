package de.k_ruben.tamastudent;

/**
 * Created by Ruben on 26.04.2016.
 */
public abstract class Tasks implements Executeable
{
    String klasse = "";
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
        if (dailyTask == true)
        {
            long wait = 0;
            long elapsedTime = 0;
            boolean stop = false;

            /*//ob lastExecute noch vor dem Startzeitpunkt oder now ist -> Wartezeit berechnen
            if(lastExecute < startDate && currentTimeMillis > startDate) wait += startDate - lastExecute;
            else if(lastExecute < startDate && currentTimeMillis < startDate) wait += currentTimeMillis - lastExecute;
            //welcher Zeitpunkt näher am Startdatum liegt. Wenn expireDate, dann wird von startDate bis expireDate berechnet, wenn now
            // näher an startdate und damit vor expireDate, dann wird von startDate bis now berechnet
            if(currentTimeMillis > expireDate) elapsedTime += expireDate - startDate;
            else if(currentTimeMillis <= expireDate) elapsedTime += currentTimeMillis - startDate;*/
            while(!stop)
            {
                if (lastExecute < startDate && currentTimeMillis < startDate)
                {
                    wait += currentTimeMillis - lastExecute; //sobald Now erreicht wurde kann eigentlich schon return gemacht werden
                    lastExecute = currentTimeMillis;
                    stop = true;
                } else if (lastExecute < startDate && currentTimeMillis >= startDate)
                {
                    wait += startDate - lastExecute;
                    lastExecute = startDate;
                }

                //hier wird ab startDate jetzt berechnet
                if (currentTimeMillis < expireDate)
                {
                    elapsedTime += currentTimeMillis - lastExecute; //sobald Now erreicht wurde kann eigentlich schon return gemacht werden
                    lastExecute = currentTimeMillis;
                    stop = true;
                } else if (currentTimeMillis > expireDate)
                {
                    elapsedTime += expireDate - lastExecute;
                    lastExecute = expireDate;
                }
            }
            if(isInfinite) reset();
            stop = false;
            if(wait > elapsedTime) return 0;
            else return (elapsedTime - wait);
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
        if(uniqueDelay == true) delay = 0;
        //setzt Task Start und Endpunkt 24 Stunden in die Zukunft, damit am nächsten Tag wieder ausgeführt wird  86400000 == 1 Tag
        if(dailyTask) reset(86400000);
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