package de.k_ruben.tamastudent;
import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import org.w3c.dom.Text;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{
    //Liste mit allen aktiven Aktivitäten
    LinkedList<Tasks> list = new LinkedList<Tasks>();
    Student s = new Student();

    private Thread thread;
    private Handler handler = new Handler();


    TextView sleep;
    TextView hunger;
    TextView entertainment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView von Stats initialiseren
        sleep = (TextView)findViewById(R.id.tvSleepValue);
        hunger = (TextView)findViewById(R.id.tvHungerValue);
        entertainment = (TextView)findViewById(R.id.tvEntertainmentValue);

        //Studenten erstellen
        Student student = new Student();

        //Stats aktualisieren
        updateValues();     //später ersetzen durch Methode, die neue Werte seit App-Schließung berechnet

        thread = new Thread()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    try
                    {
                        //do something here
                        for (int i = 0; i < list.size(); i++)
                        {
                            if (list.get(i).isInfinite == false && list.get(i).expireDate < System.currentTimeMillis())
                                list.remove(i);
                            else if (list.get(i).isInfinite == true && list.get(i).expireDate <= System.currentTimeMillis())
                            {
                                long diff = list.get(i).expireDate - list.get(i).startDate;
                                list.get(i).expireDate += diff;
                                list.get(i).startDate += diff;
                                list.get(i).execute(s);
                            } else list.get(i).execute(s);
                        }

                        handler.postDelayed(runnable, 0);

                        //Log.d("Tag", "local Thread sleeping");
                        Thread.sleep(100);
                    } catch (InterruptedException e)
                    {
                        Log.e("Tag", "local Thread error", e);
                    }

                }
            }
        };
        thread.start();
    }

    private Runnable runnable = new Runnable()
    {
        public void run()
        {
            updateValues();
        }
    };

    public void updateValues()
    {

        sleep.setText(Integer.toString((int)Math.round(s.getSleepValue())));
        hunger.setText(Integer.toString((int)Math.round(s.hunger)));
        entertainment.setText(Integer.toString((int)Math.round(s.entertainment)));
    }

    public void taskLoop(View v)
    {
        /*for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).isInfinite == false && list.get(i).expireDate < System.currentTimeMillis())
                list.remove(i);
            else if (list.get(i).isInfinite == true && list.get(i).expireDate <= System.currentTimeMillis())
            {
                long diff = list.get(i).expireDate - list.get(i).startDate;
                list.get(i).expireDate += diff;
                list.get(i).startDate += diff;
                list.get(i).execute(s);
            } else list.get(i).execute(s);
        }*/
        updateValues();
    }

    public void sleep(View v)
    {
        //7 Stunden schlafen
        long expireDate = System.currentTimeMillis();
        expireDate += 1000 * 10;
        //Tasks sleep = new Sleep(expireDate, 80);
        Tasks sleep = new Sleep(expireDate, true, 1);
        list.add(sleep);
        //sleep.execute(s);
        //updateValues();
    }

    public void energyDrink(View v)
    {
        Tasks energyDrink = new Sleep((System.currentTimeMillis() + 1000), 20);
        list.add(energyDrink);
    }

    public void testSleep(View v)
    {
        //Toast erstellen und ausgeben
        Context context = getApplicationContext();
        //int text = R.string.studentName;
        //String text = Long.toString(list.get(0).expireDate) + " - " + Long.toString(list.get(0).startDate);
        String text = ("Sleep Value ungerundet: " + Double.toString(s.getSleepValue()));
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        if(list.get(0).expireDate < System.currentTimeMillis()) list.remove(0);
        else list.get(0).execute(s);
        updateValues();
    }
}
