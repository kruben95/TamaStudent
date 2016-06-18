package de.k_ruben.tamastudent;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.util.Log;

import com.google.gson.Gson;


import java.util.LinkedList;
import java.util.Random;


/**
 * Created by Ruben on 26.05.2016.
 */
public class SaveState <T>
{
    //Convert User object  user to Json format
    Gson gson = new Gson();
    private String filename = "";
    private SharedPreferences mPrefs;
    private SharedPreferences nPrefs;

    public SaveState(Context context, String filename)
    {
        this.filename = filename;
        mPrefs = context.getSharedPreferences(filename, context.MODE_PRIVATE);
    }

    public SaveState(Context context, String filename, boolean isSpeechBubble)
    {
        if(isSpeechBubble)
        {
            this.filename = filename;
            mPrefs = context.getSharedPreferences(filename, context.MODE_PRIVATE);
            nPrefs = context.getSharedPreferences("saveSpeechBubbleCategories", context.MODE_PRIVATE);
        }
    }

    public void saveObject(T item)
    {
        //alle bisherigen SharedPreferences löschen
        mPrefs.edit().clear().commit();

        Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(item);
        prefsEditor.putString(filename, json);
        prefsEditor.commit();
    }

    public void saveSpeechBubbleString(String categorieName, String text)
    {
        //anzahl an items einer Kategorie auslesen
        int lastItemNumber = nPrefs.getInt(categorieName, -1);
        Log.d("TAG","Anzahl items von " + categorieName + ": " + lastItemNumber);

        //String mit ingenutzter id speichern
        Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(categorieName + (lastItemNumber + 1), text);
        prefsEditor.commit();

        //anzahl items einer Kategorie updaten
        prefsEditor = nPrefs.edit();
        prefsEditor.putInt(categorieName, lastItemNumber + 1);
        prefsEditor.commit();
    }

    public String loadSpeechBubbleString(String categorieName, int index)
    {
        return (mPrefs.getString(categorieName + index, "Fehler"));
    }

    public int loadAmountOfSpeechBubblesInCategory(String categorieName)
    {
        return (nPrefs.getInt(categorieName, -1));
    }

    public String loadRandomSpeechBubbleFromCategory(String categorieName)
    {
        Random r = new Random();
        return loadSpeechBubbleString(categorieName, r.nextInt(loadAmountOfSpeechBubblesInCategory(categorieName)+1));
    }

    public void saveLinkedList(LinkedList<Tasks> list)
    {
        //alle bisherigen SharedPreferences löschen
        mPrefs.edit().clear().commit();

        int length = list.size();
        for(int n = 0; n < length; n++)
        {
            Editor prefsEditor = mPrefs.edit();
            String json = gson.toJson(list.get(n));
            Log.d("Speichere ListItem" + n, json);
            prefsEditor.putString(filename + n, json);
            prefsEditor.commit();
        }
    }

    public LinkedList<Tasks> loadAllTasks(String klasse)
    {
        LinkedList<Tasks> list = new LinkedList<>();
        int i = 0;
        boolean hasNext = true;


        //load Tasks
        while(hasNext)
        {
            try
            {
                String json = mPrefs.getString(filename + i, "Nichts (mehr) gefunden");
                Log.d("TAG", "Lade Task: " + json + ", Filename: " + filename);
                if(klasse == "sleep") list.add(gson.fromJson(json, Sleep.class));
                if(klasse == "food") list.add(gson.fromJson(json, Food.class));
                if(klasse == "entertainment") list.add(gson.fromJson(json, Entertainment.class));
                if(klasse == "education") list.add(gson.fromJson(json, Education.class));
                i++;
            }
            catch (Exception e)
            {
                hasNext = false;
            }

        }
        return list;
    }

    public Student loadStudentObject()
    {
        try
        {
            String json = mPrefs.getString(filename, "Nichts (mehr) gefunden");
            return (gson.fromJson(json, Student.class));
        }
        catch (Exception e)
        {
            Log.d("Tag", "Kein Student (SaveState) gefunden");
            return null;
        }
    }
}
