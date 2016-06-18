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
import android.widget.Toast;

import android.view.animation.AnimationUtils;
import android.support.design.widget.FloatingActionButton;


import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ShakeEventManager.ShakeListener
{
    //Liste mit allen aktiven Aktivitäten
    LinkedList<Tasks> mainTaskList = new LinkedList<Tasks>();
    Student s;
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
    TextView highscore;

    TextView speechBubble;
    LinearLayout speechBubbleContainer;

    LinearLayout sleepCircle;
    LinearLayout foodCircle;
    LinearLayout entertainmentCircle;
    LinearLayout educationCircle;
    ImageView studentFace;
    ImageView studentBody;


    //***********  FLOATING ACTION BUTTONS ******************//
    private Boolean isFabOpen = false;
    //****** IV FAB *****//
    //First FabVIew SETTINGS
    private FloatingActionButton fabOVsettings,fabUVsave,fabUVdownload;
    //Second FabVIew DRINKS
    private FloatingActionButton fabOVdrinks,fabUVwater,fabUVenergydrink;
    //Third FabVIew SLEEP
    private FloatingActionButton fabOVsleep,fabUVsleepOneHours,fabUVsleepThreeHours,fabUVsleepFiveHours,fabUVsleepTwelveHours;
    //Fouth FabVIew DRINKS
    private FloatingActionButton fabOVfood,fabUVbacon,fabUVburger;

    //Animations
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    /********************** ENDE FLOATING BUTTONS*****************/

    /********** SHAKE EVENT MANAGER ********************/
    private ShakeEventManager sd;


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
                //Log.d("TAG", "while-loop");
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
                            Log.d("Status", "Schlaf: " + s.getSleepValue() + " Hunger: " + s.getHungerValue() + " Unterhaltung: " + s.getEntertainmentValue() + " Bildung: " + s.getEducationValue());
                            mainTaskList.remove(i);
                        }
                        //Ob die Aufgabe unendlich ist und abgelaufen ist
                        else if (currentTask.isInfinite == true && currentTask.expireDate <= currentTimeMillis)
                        {
                            Log.d("reset", "letzter Aufruf bevor reset");
                            currentTask.execute(s);
                            Log.d("Status", "Schlaf: " + s.getSleepValue() + " Hunger: " + s.getHungerValue() + " Unterhaltung: " + s.getEntertainmentValue() + " Bildung: " + s.getEducationValue());
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

        /****** Shake Event Manager *****/
        sd = new ShakeEventManager();
        sd.setListener(this);
        sd.init(this);
        /**************** FLOATING BUTTONS: initialisieren ***********/
        //First FabVIew SETTINGS
        fabOVsettings = (FloatingActionButton)findViewById(R.id.fabOVsettings);
        fabUVsave = (FloatingActionButton)findViewById(R.id.fabUVsave);
        fabUVdownload = (FloatingActionButton)findViewById(R.id.fabUVdownload);

        fabOVsettings.setOnClickListener(this);
        fabUVsave.setOnClickListener(this);
        fabUVdownload.setOnClickListener(this);

        //second FabVIew drinks
        fabOVdrinks = (FloatingActionButton)findViewById(R.id.fabOVdrinks);
        fabUVwater = (FloatingActionButton)findViewById(R.id.fabUVwater);
        fabUVenergydrink = (FloatingActionButton)findViewById(R.id.fabUVenergydrink);

        fabOVdrinks.setOnClickListener(this);
        fabUVwater.setOnClickListener(this);
        fabUVenergydrink.setOnClickListener(this);

        //Third FabView SLEEP
        fabOVsleep = (FloatingActionButton) findViewById(R.id.fabOVsleep);
        fabUVsleepOneHours = (FloatingActionButton) findViewById(R.id.fabUVsleepOneHours);
        fabUVsleepThreeHours = (FloatingActionButton) findViewById(R.id.fabUVsleepThreeHours);
        fabUVsleepFiveHours = (FloatingActionButton) findViewById(R.id.fabUVsleepFiveHours);
        fabUVsleepTwelveHours = (FloatingActionButton) findViewById(R.id.fabUVsleepTwelveHours);

        fabOVsleep.setOnClickListener(this);
        fabUVsleepOneHours.setOnClickListener(this);
        fabUVsleepThreeHours.setOnClickListener(this);
        fabUVsleepFiveHours.setOnClickListener(this);
        fabUVsleepTwelveHours.setOnClickListener(this);

        //second FabVIew food
        fabOVfood = (FloatingActionButton)findViewById(R.id.fabOVfood);
        fabUVbacon = (FloatingActionButton)findViewById(R.id.fabUVbacon);
        fabUVburger = (FloatingActionButton)findViewById(R.id.fabUVburger);

        fabOVfood.setOnClickListener(this);
        fabUVbacon.setOnClickListener(this);
        fabUVburger.setOnClickListener(this);

        /* Animations */
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        /*************************************************** ENDE FLOATING BUTTONS *****************************/

        s = new Student("Peter");

        //TextView von Stats initialiseren
        sleep = (TextView)findViewById(R.id.tvSleepValue);
        food = (TextView)findViewById(R.id.tvFoodValue);
        entertainment = (TextView)findViewById(R.id.tvEntertainmentValue);
        education = (TextView)findViewById(R.id.tvEducationValue);
        highscore = (TextView)findViewById(R.id.tvHighScore);
        ((TextView) findViewById(R.id.tvStudentName)).setText("Student " + s.name);

        sleepCircle = (LinearLayout) findViewById(R.id.circleSleepSize);
        foodCircle = (LinearLayout) findViewById(R.id.circleFoodSize);
        entertainmentCircle = (LinearLayout) findViewById(R.id.circleEntertainemtSize);
        educationCircle = (LinearLayout) findViewById(R.id.circleEducationSize);
        studentFace = (ImageView) findViewById(R.id.face);
        studentBody = (ImageView) findViewById(R.id.body);

        speechBubble = (TextView) findViewById(R.id.tvSpeechBubble);
        speechBubbleContainer = (LinearLayout) findViewById(R.id.llSpeechBubble);

        saveTasksSleep = new SaveState(this, "saveTasksSleepState");
        saveTasksFood = new SaveState(this, "saveTasksFoodState");
        saveTasksEntertainment = new SaveState(this, "saveTasksEntertainmentState");
        saveTasksEducation = new SaveState(this, "saveTasksEducationState");

        saveSpeechBubblesText = new SaveState(this, "saveSpeechBubbleText", true);

        saveStudentState = new SaveState(this, "saveStatus");

        if(saveSpeechBubblesText.loadAmountOfSpeechBubblesInCategory("energyDrink") == -1)initializeFirstStart();

        //Stats aktualisieren
        updateValues();

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
        education.setText(Integer.toString((int)Math.round(s.getEducationValue())));

        highscore.setText("Am leben: " + s.getScore());

        updateCircle((int)Math.round(s.getSleepValue()), sleepCircle);
        updateCircle((int)Math.round(s.getEntertainmentValue()), entertainmentCircle);
        updateCircle((int)Math.round(s.getHungerValue()), foodCircle);
        updateCircle((int)Math.round(s.getEducationValue()), educationCircle);

        updateFace((int)Math.round(s.getSleepValue()),(int)Math.round(s.getHungerValue()),
                (int)Math.round(s.getEntertainmentValue()),(int)Math.round(s.getEducationValue()), studentFace );
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
        createSpeechBubble(saveSpeechBubblesText.loadRandomSpeechBubbleFromCategory("sleep"), 5000);
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
        createSpeechBubble(saveSpeechBubblesText.loadRandomSpeechBubbleFromCategory("energyDrink"), 5000);
    }

    //erstellt eine neue Aufgabe
    public void mensaEssen(View v)
    {
        //20 Minuten
        Tasks eat = new Food(1200000, 20, false);
        mainTaskList.add(eat);
        createSpeechBubble(saveSpeechBubblesText.loadRandomSpeechBubbleFromCategory("mensaEssen"), 5000);
    }

    public void facebook(View v)
    {
        //1 Minute
        Tasks facebook = new Entertainment(60000, 5, false);
        mainTaskList.add(facebook);
        createSpeechBubble(saveSpeechBubblesText.loadRandomSpeechBubbleFromCategory("facebook"), 5000);
    }

    public void projekt(View v)
    {
        Tasks projekt = new Education(7200000, 5, false);
        mainTaskList.add(projekt);
        createSpeechBubble(saveSpeechBubblesText.loadRandomSpeechBubbleFromCategory("projekt"), 5000);
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
        save();
    }

    public void save()
    {
        LinkedList<Tasks> saveTaskList = mainTaskList;

        LinkedList<Tasks> saveTaskListSleep = new LinkedList<>();
        LinkedList<Tasks> saveTaskListFood = new LinkedList<>();
        LinkedList<Tasks> saveTaskListEntertainment = new LinkedList<>();
        LinkedList<Tasks> saveTaskListEducation = new LinkedList<>();

        for(int i = 0; i < saveTaskList.size(); i++)
        {
            Tasks halter = saveTaskList.get(i);
            if(halter.klasse == "sleep") saveTaskListSleep.add(halter);
            else if(halter.klasse == "food") saveTaskListFood.add(halter);
            else if(halter.klasse == "entertainment") saveTaskListEntertainment.add(halter);
            else if(halter.klasse == "education") saveTaskListEducation.add(halter);
        }


        saveTasksSleep.saveLinkedList(saveTaskListSleep);
        saveTasksFood.saveLinkedList(saveTaskListFood);
        saveTasksEntertainment.saveLinkedList(saveTaskListEntertainment);
        saveTasksEducation.saveLinkedList(saveTaskListEducation);

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
        addToMainList(saveTasksFood.loadAllTasks("food"));
        addToMainList(saveTasksEntertainment.loadAllTasks("entertainment"));
        addToMainList(saveTasksEducation.loadAllTasks("education"));
    }

    public void addToMainList(LinkedList<Tasks> secondList)
    {
        for(int i = 0; i < secondList.size(); i++)
        {
            mainTaskList.add(secondList.get(i));
        }
    }

    public void initializeFirstStart()
    {
        s = new Student("Peter");

        currentTimeMillis = System.currentTimeMillis();
        Tasks sleep = new Sleep(86400000, -70,true);
        Tasks food = new Food(86400000, -110, true);
        Tasks entertainment = new Entertainment(86400000, -90, true);
        Tasks education = new Education(86400000, -5, true);

        mainTaskList.add(sleep);
        mainTaskList.add(food);
        mainTaskList.add(entertainment);
        mainTaskList.add(education);

        initializeSpeechBubbleText();

        save();
    }

    public void initializeSpeechBubbleText()
    {
        saveSpeechBubblesText.saveSpeechBubbleString("sleep", "Ghhhäääään.... Ab ins Bett mit mir.");
        saveSpeechBubblesText.saveSpeechBubbleString("sleep", "Jetzt schnell ins Bett!");
        saveSpeechBubblesText.saveSpeechBubbleString("sleep", "ich bin so müüüüde... schnell ins Bett");
        saveSpeechBubblesText.saveSpeechBubbleString("energyDrink", "mhhhhh... Dieser Energy Drink macht mich wieder richtig wach!");
        saveSpeechBubblesText.saveSpeechBubbleString("energyDrink", "Diese Energy Drinks machen einen sofort wieder TOPFIT!");
        saveSpeechBubblesText.saveSpeechBubbleString("energyDrink", "Gleich hab ich wieder Energie zum lernen... oder fernsehgucken!");
        saveSpeechBubblesText.saveSpeechBubbleString("mensaEssen", "Auf ein neues probier ich das Mensaessen und werde es wie immer bereuen.");
        saveSpeechBubblesText.saveSpeechBubbleString("mensaEssen", "Mal wieder knapp bei Kasse. Da bleibt einem nichts anderes übrig außer in die Mensa zu gehen.");
        saveSpeechBubblesText.saveSpeechBubbleString("mensaEssen", "Wenn die mir wieder Tomatensauce über alles kippen...");
        saveSpeechBubblesText.saveSpeechBubbleString("facebook", "Mal wieder Facebook checken...");
        saveSpeechBubblesText.saveSpeechBubbleString("facebook", "Eigentlich muss ich ja lernen aber Facebook ist auch nicht schlecht...");
        saveSpeechBubblesText.saveSpeechBubbleString("facebook", "Ist das ne Benachrichtung...? JA ist es!");
        saveSpeechBubblesText.saveSpeechBubbleString("projekt", "Jetzt mach ich aber wirklich mal was fürs Projekt...");
        saveSpeechBubblesText.saveSpeechBubbleString("projekt", "Das Projekt muss jetzt wirklich mal Fortschritte machen...");
        saveSpeechBubblesText.saveSpeechBubbleString("projekt", "Ab ans Projekt!");
    }
    /***** SHAKE EVENT******/
    @Override
    public void onShake()
    {
        Toast.makeText(this, "LECK MEINE EIER!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sd.register();
    }


    @Override
    protected void onPause() {
        super.onPause();
        sd.deregister();
    }

    /*************************** FLOATING BUTTONS: Logik und abfrage wecher Button gedrückt wird ************************/
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fabOVsettings:
                animateFabS(fabOVsettings,fabUVsave,fabUVdownload);
                break;
            case R.id.fabUVsave:

                Log.d("Raj", "fabUVsave: save");
                save(v);
                break;
            case R.id.fabUVdownload:

                Log.d("Raj", "fabUVdownload: load");
                load(v);
                break;
            case R.id.fabOVdrinks:

                animateFabS(fabOVdrinks,fabUVwater,fabUVenergydrink);
                break;
            case R.id.fabUVwater:

                Log.d("Raj", "fabUVwater: drinking water....");

                break;
            case R.id.fabUVenergydrink:

                Log.d("Raj", "fabUVenergydrink: drinking EnergyDrink....");
                energyDrink(v);
                break;
            case R.id.fabOVfood:

                animateFabS(fabOVfood,fabUVbacon,fabUVburger);
                break;
            case R.id.fabUVbacon:

                Log.d("Raj", "fabUVbacon: esse bacon....");
                mensaEssen(v);
                break;
            case R.id.fabUVburger:

                Log.d("Raj", "fabUVburger: esse burger....");
                mensaEssen(v);
                break;
            case R.id.fabOVsleep:

                Log.d("Raj", "fabOVsleep: openSLeepFloating Button....");
                animateFabSleep(fabOVsleep,fabUVsleepOneHours,fabUVsleepThreeHours, fabUVsleepFiveHours,fabUVsleepTwelveHours);
                break;
            case R.id.fabUVsleepOneHours:

                Log.d("Raj", "fabOVsleepOne: Schläft jetzt 1 std....");
                sleep(v);
                break;
            case R.id.fabUVsleepThreeHours:

                Log.d("Raj", "fabOVsleepThree: Schläft jetzt 3 std....");
                sleep(v);
                break;
            case R.id.fabUVsleepFiveHours:

                Log.d("Raj", "fabOVsleepFive: Schläft jetzt 5 std....");
                sleep(v);
                break;
            case R.id.fabUVsleepTwelveHours:

                Log.d("Raj", "fabOVsleepTwelve: Schläft jetzt 12 std....");
                sleep(v);
                break;
        }
    }

    public void animateFabS(FloatingActionButton fabOV,FloatingActionButton fabUVeins, FloatingActionButton fabUVzwei){

        if(isFabOpen){
            //fabUVeins.getVisibility() == View.VISIBLE
            fabOV.startAnimation(rotate_backward);
            String test;
            fabUVeins.startAnimation(fab_close);
            fabUVzwei.startAnimation(fab_close);
            fabUVeins.setClickable(false);
            fabUVzwei.setClickable(false);


            isFabOpen = false;
            Log.d("Raj", "close");

        } else {
            //fabUVeins.getVisibility() == View.INVISIBLE
            fabOV.startAnimation(rotate_forward);
            fabUVeins.startAnimation(fab_open);
            fabUVzwei.startAnimation(fab_open);
            fabUVeins.setClickable(true);
            fabUVzwei.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }

    }
    public void animateFabSleep(FloatingActionButton fabOV,FloatingActionButton fabUVeins, FloatingActionButton fabUVzwei,FloatingActionButton fabUVdrei,FloatingActionButton fabUVvier ){

        if(isFabOpen){
            //fabUVeins.getVisibility() == View.VISIBLE
            fabOV.startAnimation(rotate_backward);
            fabUVeins.startAnimation(fab_close);
            fabUVeins.setClickable(false);
            fabUVzwei.startAnimation(fab_close);
            fabUVzwei.setClickable(false);
            fabUVdrei.startAnimation(fab_close);
            fabUVdrei.setClickable(false);
            fabUVvier.startAnimation(fab_close);
            fabUVvier.setClickable(false);

            isFabOpen = false;
            Log.d("Raj", "close");

        } else{
            //fabUVeins.getVisibility() == View.INVISIBLE
            fabOV.startAnimation(rotate_forward);
            fabUVeins.startAnimation(fab_open);
            fabUVeins.setClickable(true);
            fabUVzwei.startAnimation(fab_open);
            fabUVzwei.setClickable(true);
            fabUVdrei.startAnimation(fab_open);
            fabUVdrei.setClickable(true);
            fabUVvier.startAnimation(fab_open);
            fabUVvier.setClickable(true);

            isFabOpen = true;
            Log.d("Raj","open");

        }

    }

}
