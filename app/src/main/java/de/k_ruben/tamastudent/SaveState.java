package de.k_ruben.tamastudent;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.util.Log;

import com.google.gson.Gson;


import java.util.LinkedList;


/**
 * Created by Ruben on 26.05.2016.
 */
public class SaveState
{
    //Convert User object  user to Json format
    Gson gson = new Gson();
    Context context;
    public String filename = "";
    SharedPreferences mPrefs;

    public SaveState(Context context, String filename)
    {
        this.filename = filename;
        this.context = context;
        mPrefs = context.getSharedPreferences(filename, context.MODE_PRIVATE);
    }

    public void saveObject(Tasks item, String name)
    {
        //Tasks durch T ersetzten und task durch item
        // Evtl. erst alle bisherigen SharedPreferences löschen
        Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(item);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    public void saveObject(Student s, String name)
    {
        //Doppelt siehe oben.
        //Student durch T ersetzten und s durch item
        // Evtl. erst alle bisherigen SharedPreferences löschen
        Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(s);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    public void saveLinkedList(LinkedList<Tasks> list, String name)
    {
        //Tasks durch T ersetzten und varialennamen durch item
        Tasks item = null;
        int length = list.size();
        // Evtl. erst alle bisherigen SharedPreferences löschen
        for(int n = 0; n < length; n++)
        {
            item = list.get(n);
            Editor prefsEditor = mPrefs.edit();
            String json = gson.toJson(item);
            Log.d("ListItem" + n, json);
            prefsEditor.putString(name + n, json);
            prefsEditor.commit();
        }
    }

    public LinkedList<Tasks> loadAllTasks(String name)
    {
        LinkedList<Tasks> list = new LinkedList<>();
        Tasks item = null;
        int i = 0;
        boolean hasNext = true;

        //load Tasks
        while(hasNext)
        {
            try
            {
                String json = mPrefs.getString(name + i, "Nichts (mehr) gefunden");
                item = gson.fromJson(json, Tasks.class);
                list.add(item);
                i++;
            }
            catch (Exception e)
            {
                hasNext = false;
            }

        }
        return list;
    }

    public Student loadObject(String name)
    {
        Student item = null;
        try
        {
            String json = mPrefs.getString(name, "Nichts (mehr) gefunden");
            item = gson.fromJson(json, Student.class);
        }
        catch (Exception e)
        {}
        return item;
    }
}
