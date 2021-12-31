package com.dokifusky.screenshotoverrider;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dokifusky.screenshotoverrider.AppInfo.PREFS_FILE;
import static com.dokifusky.screenshotoverrider.AppInfo.UTIL_PREFS_FILE;
import static com.dokifusky.screenshotoverrider.DebugUtil.dbgPrintf;

public class MainActivity extends AppCompatActivity
{
    private List<String> hookPkgList;
    private SharedPreferences prefs;

    private ListView listView;
    private ArrayAdapter adapter;

    private FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Overriden Apps");

        prefs = this.getSharedPreferences(PREFS_FILE, Context.MODE_WORLD_READABLE);

        SharedPreferences utilPref = this.getSharedPreferences(UTIL_PREFS_FILE, Context.MODE_PRIVATE);

        if (!utilPref.getBoolean("appInitialized", false))
        {
            prefs.edit().putBoolean("org.mozilla.firefox", true).commit();
            utilPref.edit().putBoolean("appInitialized", true).commit();
        }

        hookPkgList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,
                hookPkgList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l)
            {
                final String rmPkgName = hookPkgList.get(i);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure?")
                        .setMessage("Are you sure you want to stop overriding " + rmPkgName + "?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                prefs.edit().putBoolean(rmPkgName, false).commit();
                                updateListView();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                            }
                        })
                        .show();
                return true;
            }
        });

        btn = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                spawnAppAddDialogue();
            }
        });

        updateListView();
    }

    public void spawnAppAddDialogue()
    {
        final EditText et = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Add App")
                .setMessage("Please enter the package name of the app to override.")
                .setView(et)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        String name = et.getText().toString().trim();
                        prefs.edit().putBoolean(name, true).commit();
                        updateListView();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                    }
                })
                .show();
    }

    public void updateListView()
    {
        hookPkgList.clear();

        for (String pkgName : prefs.getAll().keySet())
        {
            if (prefs.getBoolean(pkgName, false))
            {
                hookPkgList.add(pkgName);
            }
        }

        Collections.sort(hookPkgList);
        adapter.notifyDataSetChanged();
    }
}