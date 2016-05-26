package de.k_ruben.tamastudent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
{
    //Liste mit allen aktiven Aktivitäten
    LinkedList<Tasks> list = new LinkedList<Tasks>();
    Student s = new Student();

    private Thread thread;
    private Handler handler = new Handler();

    long currentTimeMillis;


    TextView sleep;
    TextView hunger;
    TextView entertainment;

    ImageView sleepCircle;
    ImageView eatCircle;
    ImageView entertainmentCircle;
    ImageView studentFace;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView von Stats initialiseren
        /*sleep = (TextView)findViewById(R.id.tvSleepValue);
        hunger = (TextView)findViewById(R.id.tvHungerValue);
        entertainment = (TextView)findViewById(R.id.tvEntertainmentValue);*/

        sleepCircle = (ImageView) findViewById(R.id.circleSleep);
        eatCircle = (ImageView) findViewById(R.id.circleEat);
        entertainmentCircle = (ImageView) findViewById(R.id.entertainment);
        studentFace = (ImageView) findViewById(R.id.face);

        //Studenten erstellen
        final Student student = new Student();

        //Stats aktualisieren
        updateValues();     //später ersetzen durch Methode, die neue Werte seit App-Schließung berechnet

        //Game-Loop in einem eigenen Thread
        thread = new Thread()
        {
            @Override
            public void run()
            {
                Tasks currentTask;

                while(true)
                {
                    try
                    {
                        //Iterriere durch die Liste
                        for (int i = 0; i < list.size(); i++)
                        {
                            currentTask = list.get(i);

                            //Ob die Aufgabe endlich ist und abgelaufen ist
                            if (currentTask.isInfinite == false && currentTask.expireDate <= System.currentTimeMillis())
                            {
                                Log.d("expire","Letzter Aufruf bevor expire");
                                currentTask.execute(s);
                                Log.d("Status", "Schlaf: " + s.getSleepValue() + " Hunger: " + s.getHungerValue());
                                list.remove(i);
                            }
                            //Ob die Aufgabe unendlich ist und abgelaufen ist
                            else if (currentTask.isInfinite == true && currentTask.expireDate <= System.currentTimeMillis())
                            {
                                Log.d("reset", "letzter Aufruf bevor reset");
                                currentTask.execute(s);
                                Log.d("Status", "Schlaf: " + s.getSleepValue() + " Hunger: " + s.getHungerValue());
                                currentTask.reset();
                            }
                            //Führe die Aufgabe aus
                            else
                            {
                                currentTask.execute(s);
                            }
                        }
                        //Rufe den Handler aus dem UI-Thread auf
                        handler.postDelayed(runnable, 0);

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

    //Handler ruft dieses Ojekt auf
    private Runnable runnable = new Runnable()
    {
        public void run()
        {
            updateValues();
        }
    };

    //Ubdated Werte auf dem Display
    public void updateValues()
    {
        /*sleep.setText(Integer.toString((int)Math.round(s.getSleepValue())));
        hunger.setText(Integer.toString((int)Math.round(s.getHungerValue())));
        entertainment.setText(Integer.toString((int)Math.round(s.getEntertainmentValue())));*/

        updateCircle((int)Math.round(s.getSleepValue()), sleepCircle);
        updateCircle((int)Math.round(s.getEntertainmentValue()), entertainmentCircle);
        updateCircle((int)Math.round(s.getHungerValue()), eatCircle);

        updateFace((int)Math.round(s.getSleepValue()),(int)Math.round(s.getHungerValue()),(int)Math.round(s.getEntertainmentValue()),studentFace );
    }

    void updateCircle(int n, ImageView circle){

        if(n > 90){
            circle.setImageResource(R.drawable.kreis1);
        }else if(n > 80){
            circle.setImageResource(R.drawable.kreis2);
        }else if(n > 70) {
            circle.setImageResource(R.drawable.kreis3);
        }else if(n > 60){
            circle.setImageResource(R.drawable.kreis4);
        }else if(n > 50){
            circle.setImageResource(R.drawable.kreis5);
        }else if(n > 40){
            circle.setImageResource(R.drawable.kreis6);
        }else if(n > 30){
            circle.setImageResource(R.drawable.kreis7);
        }else if(n > 20){
            circle.setImageResource(R.drawable.kreis8);
        }else if(n > 10){
            circle.setImageResource(R.drawable.kreis9);
        }else
            circle.setImageResource(R.drawable.kreis0);
    }

    void updateFace(double sleep, double eat, double entertainment, ImageView face){

        double n = (sleep + eat + entertainment)/3;

        if(n > 90){
            face.setImageResource(R.drawable.smiley1);
        }else if(n > 70){
            face.setImageResource(R.drawable.smiley2);
        }else if(n > 40) {
            face.setImageResource(R.drawable.smiley3);
        }else if(n > 10){
            face.setImageResource(R.drawable.smiley4);
        }else
            face.setImageResource(R.drawable.smiley5);

    }

    //erstellt eine neue Aufgabe
    public void sleep(View v)
    {
        Tasks sleep = new Sleep((1000 * 10), 1, true);
        list.add(sleep);
    }

    //erstellt eine neue Aufgabe
    public void energyDrink(View v)
    {
        currentTimeMillis = System.currentTimeMillis();
        Tasks energyDrink = new Sleep((currentTimeMillis + 2000), (currentTimeMillis + 3000), 20, true, false);
        list.add(energyDrink);
    }

    //erstellt eine neue Aufgabe
    public void eat(View v)
    {
        Tasks eat = new Hunger((1000), 20, false);
        list.add(eat);
    }
}
