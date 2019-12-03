package com.MartyrPher.smapiandroidinstaller;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment used to handle config editing
 */
public class ConfigEditorFragment extends Fragment {

    //Tag used for debug purposes
    private final static String TAG = "ConfigEditorFragment";

    //Constant string used the mod directory
    private final static String MOD_DIR = Environment.getExternalStorageDirectory() + "/StardewValley/Mods/";

    //The Recycler View
    public static RecyclerView mRecyclerView;

    //The Recycler Views adapter
    private RecyclerView.Adapter mAdapter;

    //The Recycler Views layout manager
    private RecyclerView.LayoutManager mLayoutManager;

    //The list of mod files
    private final static List<File> mModFiles = new ArrayList<>();

    /**
     * Override that gets called when the view needs to be created
     * @param inflater = The layout inflator
     * @param container = The container to inflate the view
     * @param savedInstanceState = The savedInstanceState
     * @return the inflated view/layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the fragment layout
        return inflater.inflate(R.layout.config_editing, container, false);
    }

    /**
     * Override that gets called when the view is created, used to setup the Recycler View
     * @param view = The view that was created
     * @param savedInstanceState = The savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //Find the Recycler View and set the hasFixedSize
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(false);

        //Create a new Layout Manager and set Recyclers View layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Only grab the mod file list once
        //Not having this allows duplicate mods to show (Thanks Minerva!!!)
        if (!MainActivity.mHasFoundMods)
            getModFiles();

        //Create a new Config Adapter and set Recycle Views adapter to the new adapter
        mAdapter = new ConfigAdapter(mModFiles);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Get the mod files using a recursive file check
     */
    private void getModFiles()
    {
        File modFolder = new File(MOD_DIR);

        //Make the directory in case it's not there
        if (!modFolder.exists())
            modFolder.mkdir();

        for (File file : modFolder.listFiles())
        {
            recursiveFileCheck(file);
        }

        //Set that the mods were found and sort the list
        MainActivity.mHasFoundMods = true;
        Collections.sort(mModFiles);
    }

    /**
     * Recursive file check that searches for config files within the mod folder
     * @param file = The file that is being checked
     */
    private void recursiveFileCheck(File file)
    {
        if (file.isDirectory())
        {
            for (File configFile : file.listFiles()) {
                recursiveFileCheck(configFile);
            }
        }
        else
        {
            //If it's a config then add it to the list!
            if (file.getName().equals("config.json"))
                mModFiles.add(file);
        }
    }
}
