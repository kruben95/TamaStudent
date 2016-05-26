package de.k_ruben.tamastudent;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by Ruben on 23.04.2016.
 */
public class Student
{
    public String name;
    public double sleep;
    public int hunger;
    public int thirst;
    public int entertainment;

    public Student()
    {
        if(name == null)
        {
            //lege neuen Studenten an
            //Frage nach einem Namen der mehr als 1 Zeichen hat
            this.name = "Peter"; //hier wird die Einagbe rein gespeichert
            //Wahrscheinlich lieber schlechte Stats machen, damit man beim erstmaligen Spielen auch was zu tun hat
            this.sleep = 23;
            this.hunger = 20;
            this.thirst = 35;
            this.entertainment = 42;
        }
    }

    void setSleepValue(double changeValue)
    {
        if ((sleep + changeValue) <= 100)
        {
            sleep += changeValue;
        }
        else sleep = 100;
    }

    double getSleepValue()
    {
        return sleep;
    }

    void setHungerValue(double changeValue)
    {
        if((hunger + changeValue) <= 100)
        {
            hunger += changeValue;
        }
        else hunger = 100;
    }

    double getHungerValue()
    {
        return hunger;
    }

    void setEntertainmentValue(double changeValue)
    {
        if((entertainment + changeValue) <= 100)
        {
            entertainment += changeValue;
        }
        else entertainment = 100;
    }

    double getEntertainmentValue()
    {
        return entertainment;
    }
}

