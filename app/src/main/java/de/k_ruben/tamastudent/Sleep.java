package de.k_ruben.tamastudent;

/**
 * Created by Ruben on 26.04.2016.
 */
public class Sleep extends Tasks
{

    //Wie viel die Aufgabe insgesamt ändern soll
    int changeValueSleep;


    /**
     * Normale Aufgabe, die sofort startet und ewig laufen kann
     * @param expireTime            Wie lange die Aufgabe laufen soll
     * @param changeValueSleep      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig wiederholt werden soll
     */
    public Sleep(int expireTime, int changeValueSleep, boolean isInfinite)
    {
        this.klasse = "sleep";
        currentTimeMillis = System.currentTimeMillis();
        startDate = currentTimeMillis;
        lastExecute = currentTimeMillis;
        this.expireDate = currentTimeMillis + expireTime;
        this.changeValueSleep = changeValueSleep;
        this.isInfinite = isInfinite;
    }

    /**
     * Aufgabe, die mit verzögerung starten soll
     * @param expireTime            Wie lange die Aufgabe laufen soll
     * @param delay                 Mit welcher verzögerung die Aufgabe starten soll
     * @param changeValueSleep      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig wiederholt werden soll
     * @param uniqueDelay           Ob der Delay nur einmal beim Start existiert, oder immer (setzt vorraus, dass isInfinte == true)
     */
    public Sleep(int expireTime, int delay, int changeValueSleep, boolean isInfinite, boolean uniqueDelay)
    {
        this.klasse = "sleep";
        currentTimeMillis = System.currentTimeMillis();
        this.delay = delay;
        startDate = currentTimeMillis + delay;
        lastExecute = startDate;
        this.expireDate = startDate + expireTime;
        this.changeValueSleep = changeValueSleep;
        this.isInfinite = isInfinite;
        if(!isInfinite) this.uniqueDelay = true;
        else this.uniqueDelay = uniqueDelay;
    }

    /**
     * Tägliche Aufgabe, die innerhalb einer festen Uhrzeit geschehen soll (!WICHTIG: Zeitrahmen von !einem! Tag, sonst resetet dich die Aufgabe andauernd)
     * @param startDate             Anfangs-Uhrzeit
     * @param endDate               End-Uhrzeit
     * @param changeValueSleep      Der Wert, um den sich der Status in der Zeit ändern soll
     * @param isInfinite            Ob die Aufgabe ewig/in einer Loop ausgeführt werden soll
     * @param calculateBack         Ob bereits verstrichene Zeit nachbrechnet werden soll, wenn die Aufgabe während der
     *                              angegebenen Laufzeit erstellt wird (einmaliges Problem beim erstellen, in der Regel nicht erwünscht -> false)
     */
    public Sleep(long startDate, long endDate, int changeValueSleep, boolean isInfinite, boolean calculateBack)
    {
        this.klasse = "sleep";
        currentTimeMillis = System.currentTimeMillis();
        this.startDate = startDate;
        this.lastExecute = currentTimeMillis;
        this.expireDate = endDate;
        this.changeValueSleep = changeValueSleep;
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
            changeValue = calculateChangeValue(changeValueSleep);
            s.changeSleepValue(changeValue);
            lastExecute = currentTimeMillis;
        }
    }
}