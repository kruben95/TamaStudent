package de.k_ruben.tamastudent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;


import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
{
    //Liste mit allen aktiven Aktivitäten
    LinkedList<Tasks> mainTaskList = new LinkedList<Tasks>();
    Student s = new Student();
    boolean pause = false;

    SaveState saveTasksSleep;
    SaveState saveTasksFood;
    SaveState saveTasksEntertainment;
    SaveState saveTasksEducation;
    SaveState<Student> saveStudentState;
    SaveState saveSpeechBubblesText;
    Tasks bubble = null;

    long currentTimeMillis;


    TextView sleep;
    TextView food;
    TextView entertainment;
    TextView education;

    TextView speechBubble;
    LinearLayout speechBubbleContainer;

    LinearLayout sleepCircle;
    LinearLayout foodCircle;
    LinearLayout entertainmentCircle;
    LinearLayout educationCircle;
    ImageView studentFace;
    ImageView studentBody;

    private Boolean isFabOpen = false;
    //private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    //private Handler;
    private Handler handler = new Handler();
    private Handler handleSpeechBubble = new Handler();

    //Game-Loop in einem eigenen Thread
    public Thread thread = new Thread()
    {
        @Override
        public void run()
        {
            Tasks currentTask;

            while(true)
            {
                currentTimeMillis = System.currentTimeMillis();
                try
                {
                    //Iterriere durch die Liste
                    for (int i = 0; i < mainTaskList.size(); i++)
                    {
                        currentTask = mainTaskList.get(i);

                        //Ob die Aufgabe endlich ist und abgelaufen ist
                        if (currentTask.isInfinite == false && currentTask.expireDate <= currentTimeMillis)
                        {
                            Log.d("expire","Letzter Aufruf bevor expire");
                            currentTask.execute(s);
                            Log.d("Status", "Schlaf: " + s.getSleepValue() + " Hunger: " + s.getHungerValue());
                            mainTaskList.remove(i);
                        }
                        //Ob die Aufgabe unendlich ist und abgelaufen ist
                        else if (currentTask.isInfinite == true && currentTask.expireDate <= currentTimeMillis)
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
                    //Sprechblase updaten
                    if(bubble != null)
                    {
                        bubble.execute(s);
                        if(bubble.expireDate < currentTimeMillis) bubble = null;
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
        sleep = (TextView)findViewById(R.id.tvSleepValue);
        food = (TextView)findViewById(R.id.tvFoodValue);
        entertainment = (TextView)findViewById(R.id.tvEntertainmentValue);
        education = (TextView)findViewById(R.id.tvEducationValue);

        sleepCircle = (LinearLayout) findViewById(R.id.circleSleepSize);
        foodCircle = (LinearLayout) findViewById(R.id.circleFoodSize);
        entertainmentCircle = (LinearLayout) findViewById(R.id.circleEntertainemtSize);
        educationCircle = (LinearLayout) findViewById(R.id.circleEducationSize);
        studentFace = (ImageView) findViewById(R.id.face);
        studentBody = (ImageView) findViewById(R.id.body);

        speechBubble = (TextView) findViewById(R.id.tvSpeechBubble);
        speechBubbleContainer = (LinearLayout) findViewById(R.id.llSpeechBubble);

        saveTasksSleep = new SaveState(this, "saveTasksSleep");
        //SaveState<Food> saveTasksFood = new SaveState(this, "saveTasksFoodState");
        //SaveState<Entertainment> saveTasksEntertainment = new SaveState(this, "saveTasksEntertainmentState");
        //SaveState<Education> saveTasksEducation = new SaveState(this, "saveTasksEducationState");
        saveSpeechBubblesText = new SaveState(this, "saveSpeechBubbleText", true);
        saveStudentState = new SaveState(this, "saveStatus");

        /*saveSpeechBubblesText.saveSpeechBubbleString("sleep", "Ghhhäääään.... Ab ins Bett mit mir.");
        saveSpeechBubblesText.saveSpeechBubbleString("sleep", "Jetzt schnell ins Bett!");
        saveSpeechBubblesText.saveSpeechBubbleString("sleep", "ich bin so müüüüde... schnell ins Bett");
        saveSpeechBubblesText.saveSpeechBubbleString("energyDrink", "mhhhhh... Dieser Energy Drink macht mich wieder richtig wach!");
        saveSpeechBubblesText.saveSpeechBubbleString("energyDrink", "Diese Energy Drinks machen einen sofort wieder TOPFIT!");
        saveSpeechBubblesText.saveSpeechBubbleString("energyDrink", "Gleich hab ich wieder Energie zum lernen... oder fernsehgucken!");*/


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
        sleep.setText(Integer.toString((int)Math.round(s.getSleepValue())));
        food.setText(Integer.toString((int)Math.round(s.getHungerValue())));
        entertainment.setText(Integer.toString((int)Math.round(s.getEntertainmentValue())));
        education.setText(Integer.toString((int)Math.round(s.getUniValue())));

        updateCircle((int)Math.round(s.getSleepValue()), sleepCircle);
        updateCircle((int)Math.round(s.getEntertainmentValue()), entertainmentCircle);
        updateCircle((int)Math.round(s.getHungerValue()), foodCircle);
        updateCircle((int)Math.round(s.getUniValue()), educationCircle);

        updateFace((int)Math.round(s.getSleepValue()),(int)Math.round(s.getHungerValue()),
                (int)Math.round(s.getEntertainmentValue()),(int)Math.round(s.getUniValue()), studentFace );
    }

    void updateCircle(float n, LinearLayout circle){
        float size = circle.getWidth();
        //Log.d("TAG", "current Circle size: " + size + " Prozent: " + n);
        size = (float)Math.round(size * (n / 100));
        //Log.d("TAG","new Circle size: " + size);

        circle.getLayoutParams().height = (int)size;
        circle.requestLayout();
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
        Tasks sleep = new Sleep((3000), 1, true);
        mainTaskList.add(sleep);
        //createSpeechBubble(saveSpeechBubblesText.loadRandomSpeechBubbleFromCategorie("sleep"), 5000);
    }

    public void decreaseSleep(View v)
    {
        //Innerhalb von 24 Std. soll Schlaf sich um 70 verringern 86400000 == 1 Tag
        Tasks decreaseSleep = new Sleep((86400000), -70, true);
        mainTaskList.add(decreaseSleep);
    }

    public void energyDrink(View v)
    {
        Tasks energyDrink = new Sleep(1000, 20, false);
        mainTaskList.add(energyDrink);
        //createSpeechBubble(saveSpeechBubblesText.loadRandomSpeechBubbleFromCategorie("energyDrink"), 5000);
    }

    //erstellt eine neue Aufgabe
    public void eat(View v)
    {
        Tasks eat = new Hunger(1000, 20, false);
        mainTaskList.add(eat);
    }

    public void test(View v)
    {
        createSpeechBubble("Dies ist ein Test", 10000);
    }

    public void createSpeechBubble(String text, int duration)
    {
        bubble = new Message(text, duration, speechBubbleContainer, this);
        //mainTaskList.add(bubble);
    }

    public void closeSpeechBubble(View v)
    {
        speechBubbleContainer.setVisibility(View.GONE);
        bubble = null;
    }

    public void save(View v)
    {
        LinkedList<Tasks> saveTaskList = mainTaskList;
        LinkedList<Tasks> saveTaskListSleep = new LinkedList<>();
        /*LinkedList<Food> saveTaskListFood = new LinkedList<>();
        LinkedList<Entertainment> saveTaskListEntertainment = new LinkedList<>();
        LinkedList<Education> saveTaskListEducation = new LinkedList<>();*/

        for(int i = 0; i < saveTaskList.size(); i++)
        {
            Tasks halter = saveTaskList.get(i);
            if(halter.klasse == "sleep") saveTaskListSleep.add(halter);
            /*else if(halter.klasse == "food") saveTaskListFood.add(halter);
            else if(halter.klasse == "entertainment") saveTaskListEntertainment.add(halter);
            else if(halter.klasse == "education") saveTaskListEducation.add(halter);*/
        }


        saveTasksSleep.saveLinkedList(saveTaskListSleep);
        //saveTasksFood.saveLinkedList(saveTaskListFood, "foodObject");
        //saveTasksEntertainment.saveLinkedList(saveTaskListEntertainment, "entertainmentObject");
        //saveTasksEntertainment.saveLinkedList(saveTaskListEducation, "educationObject");
        saveStudentState.saveObject(s);
        pause = true;
    }

    public void load(View v)
    {
        //Spielstand laden
        pause = false;
        s = saveStudentState.loadStudentObject();
        mainTaskList.clear();
        addToMainList(saveTasksSleep.loadAllTasks("sleep"));
    }

    public void addToMainList(LinkedList<Tasks> secondList)
    {
        for(int i = 0; i < secondList.size(); i++)
        {
            mainTaskList.add(secondList.get(i));
        }
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
