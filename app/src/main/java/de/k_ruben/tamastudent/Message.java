package de.k_ruben.tamastudent;

/**
 * Created by Ruben on 03.06.2016.
 */

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;

public class Message extends Tasks
{
    LinearLayout bubble;
    TextView tv;
    protected MainActivity context;

    public Message(String Message, int duration, LinearLayout bubble, Context context)
    {
        this.context = (MainActivity) context;
        this.expireDate = duration + System.currentTimeMillis();
        this.startDate = currentTimeMillis;
        this.lastExecute = this.startDate;
        this.bubble = bubble;
        tv = (TextView) bubble.getChildAt(0);
        tv.setText(Message);
        this.klasse = "message";
    }

    @Override
    public void execute(Student s)
    {
        currentTimeMillis = System.currentTimeMillis();
        lastExecute = currentTimeMillis;
        context.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                //Log.d("TAG", "SpeechBubble Test");
                if(expireDate < currentTimeMillis)
                {
                    Log.d("TAG", "SpeechBubble verschwindet jetzt");
                    bubble.setVisibility(View.GONE);
                }
                else if(bubble.getVisibility() == View.GONE) bubble.setVisibility(View.VISIBLE);
            }
        });
    }
}
