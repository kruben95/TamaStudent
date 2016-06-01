package de.k_ruben.tamastudent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;


import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
{
    //Liste mit allen aktiven Aktivitäten
    LinkedList<Tasks> mainTaskList = new LinkedList<Tasks>();
    Student s = new Student();
    boolean pause = false;
    SaveState saveTasks;
    SaveState saveStatValues;

    //private Thread thread;
    private Handler handler = new Handler();

    long currentTimeMillis;


    TextView sleep;
    TextView hunger;
    TextView entertainment;

    ImageView sleepCircle;
    ImageView eatCircle;
    ImageView entertainmentCircle;
    ImageView uniCircle;
    ImageView studentFace;
    ImageView studentBody;

    private Boolean isFabOpen = false;
    //private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    //Game-Loop in einem eigenen Thread
    public Thread thread = new Thread()
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
                    for (int i = 0; i < mainTaskList.size(); i++)
                    {
                        currentTask = mainTaskList.get(i);

                        //Ob die Aufgabe endlich ist und abgelaufen ist
                        if (currentTask.isInfinite == false && currentTask.expireDate <= System.currentTimeMillis())
                        {
                            Log.d("expire","Letzter Aufruf bevor expire");
                            currentTask.execute(s);
                            Log.d("Status", "Schlaf: " + s.getSleepValue() + " Hunger: " + s.getHungerValue());
                            mainTaskList.remove(i);
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
                    if(pause) mainTaskList.clear();
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    Log.e("Tag", "local Thread error", e);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        //rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        //rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);*/

        //TextView von Stats initialiseren
        /*sleep = (TextView)findViewById(R.id.tvSleepValue);
        hunger = (TextView)findViewById(R.id.tvHungerValue);
        entertainment = (TextView)findViewById(R.id.tvEntertainmentValue);*/

        sleepCircle = (ImageView) findViewById(R.id.circleSleep);
        eatCircle = (ImageView) findViewById(R.id.circleEat);
        entertainmentCircle = (ImageView) findViewById(R.id.entertainment);
        studentFace = (ImageView) findViewById(R.id.face);
        studentBody = (ImageView) findViewById(R.id.body);
        uniCircle = (ImageView) findViewById(R.id.circleUni);
        saveTasks = new SaveState(this, "saveTasksState");
        saveStatValues = new SaveState(this, "saveStatus");

        //Stats aktualisieren
        updateValues();     //später ersetzen durch Methode, die neue Werte seit App-Schließung berechnet

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
        updateCircle((int)Math.round(s.getUniValue()), uniCircle);

        updateFace((int)Math.round(s.getSleepValue()),(int)Math.round(s.getHungerValue()),
                (int)Math.round(s.getEntertainmentValue()),(int)Math.round(s.getUniValue()), studentFace );
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

    void updateFace(double sleep, double eat, double entertainment, double uni, ImageView face){

        double n = (sleep + eat + entertainment + uni)/4;

        if(n > 90){
            face.setImageResource(R.drawable.studihead2);
        }else if(n > 80){
            face.setImageResource(R.drawable.studihead2);
        }else if(n > 60) {
            face.setImageResource(R.drawable.studihead3);
        }else if(n > 40){
            face.setImageResource(R.drawable.studihead4);
        }else if(n > 20){
            face.setImageResource(R.drawable.studihead5);
        }else if(n > 10){
            face.setImageResource(R.drawable.studihead6);
        }else
            face.setImageResource(R.drawable.studihead6red);

    }

    //erstellt eine neue Aufgabe
    public void sleep(View v)
    {
        Tasks sleep = new Sleep((1000 * 10), 1, true);
        mainTaskList.add(sleep);
    }

    //erstellt eine neue Aufgabe
    public void energyDrink(View v)
    {
        currentTimeMillis = System.currentTimeMillis();
        Tasks energyDrink = new Sleep(1000, 1, true);
        mainTaskList.add(energyDrink);
    }

    //erstellt eine neue Aufgabe
    public void eat(View v)
    {
        Tasks eat = new Hunger((1000), 20, false);
        mainTaskList.add(eat);
    }

    public void save(View v)
    {
        LinkedList<Tasks> saveTaskList = mainTaskList;
        saveTasks.saveLinkedList(saveTaskList, "TaskList");
        //saveStatValues.saveObject(s, "StatValues");
        pause = true;
    }

    public void load(View v)
    {
        //Spielstand laden
        pause = false;
        s = saveStatValues.loadObject("StatValues");
        mainTaskList = saveTasks.loadAllTasks("TaskList");
    }

    /*
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                Log.d("Raj", "Fab 1");
                break;
            case R.id.fab2:

                Log.d("Raj", "Fab 2");
                break;
        }
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }*/

}
