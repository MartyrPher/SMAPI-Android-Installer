package com.MartyrPher.smapiandroidinstaller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Config Adapter used to show the different configs and their views
 */
public class ConfigAdapter extends RecyclerView.Adapter<ConfigAdapter.ConfigViewHolder>
{
    //TAG used for debugging purposes
    private final static String TAG = "ConfigAdapter";

    //List of the mods
    private List<File> mModFiles;

    //ViewHolder class that extends RecyclerView.ViewHolder
    public static class ConfigViewHolder extends RecyclerView.ViewHolder
    {
        //The view that needs to be inflated
        public View mConstraintLayout;

        /**
         * Constructor that sets the layout
         * @param layout = The views layout
         */
        public ConfigViewHolder(View layout)
        {
            super(layout);
            mConstraintLayout = layout;
        }
    }

    /**
     * Constructor that sets the Mod File list
     * @param modFiles = The list of Mod Files
     */
    public ConfigAdapter(List<File> modFiles)
    {
        mModFiles = modFiles;
    }

    /**
     * Override that gets called when a view pops into the RecyclerView
     * @param parent = The parent viewgroup
     * @param viewType = The view type
     * @return The configViewHolder
     */
    @Override
    public ConfigAdapter.ConfigViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        //Inflate the view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_view, parent, false);

        //Create a new ConfigViewHolder
        final ConfigViewHolder configViewHolder = new ConfigViewHolder(view);

        //Set an onClickListner for each of the config options
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //The position in the RecyclerView
                final int position = ConfigEditorFragment.mRecyclerView.getChildLayoutPosition(v);

                //Save button
                final Button saveButton = configViewHolder.mConstraintLayout.findViewById(R.id.save_button);

                //Cancel Button
                final Button cancelButton = configViewHolder.mConstraintLayout.findViewById(R.id.cancel_button);

                //Edit Text
                final EditText editText = configViewHolder.mConstraintLayout.findViewById(R.id.editText);
                try
                {
                    //When clicked read the json and make the buttons VISIBLE
                    editText.setText(readStringFromJson(position));
                    editText.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try
                            {
                                //Try to save the json when the save button is clicked
                                writeStringToJson(editText.getText().toString(), position);

                                //Set the edit text to an empty string and set everything to INVISIBLE
                                editText.setText("");
                                editText.setVisibility(View.INVISIBLE);
                                saveButton.setVisibility(View.INVISIBLE);
                                cancelButton.setVisibility(View.INVISIBLE);
                            }
                            catch(IOException io)
                            {

                            }
                        }
                    });

                    cancelButton.setVisibility(View.VISIBLE);

                    //When cancel is clicked, set everything to INVISIBLE again
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.setText("");
                            editText.setVisibility(View.INVISIBLE);
                            saveButton.setVisibility(View.INVISIBLE);
                            cancelButton.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                catch (IOException io)
                {
                }

            }
        });

        return configViewHolder;
    }

    /**
     * Override that shows the initial mod name when the View is binded to the RecyclerView
     * @param holder = The ConfigViewHolder
     * @param position = The position of the item in the RecyclerView
     */
    @Override
    public void onBindViewHolder(ConfigViewHolder holder, int position)
    {
        //Grab the textView and set the string to the mods name
        TextView textView = holder.mConstraintLayout.findViewById(R.id.textView3);
        textView.setText(mModFiles.get(position).getParentFile().getName());
    }

    /**
     * Override that gets called when a view is recycled from the RecyclerView
     * This is needed or a UI bug shows up when opening a config file (Thanks BluPenDragon!)
     * @param holder = The ConfigViewHolder
     */
    @Override
    public void onViewRecycled(ConfigViewHolder holder)
    {
        //Get the save button, cancel button and edit text from the holder
        Button saveButton = holder.mConstraintLayout.findViewById(R.id.save_button);
        Button cancelButton = holder.mConstraintLayout.findViewById(R.id.cancel_button);
        EditText text = holder.mConstraintLayout.findViewById(R.id.editText);

        //Make them all INVISIBLE and set the edit text to an empty string
        saveButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
        text.setText("");
        text.setVisibility(View.INVISIBLE);
    }

    /**
     * Override that gets the amount of items in the Mod File list
     * @return The size of the Mod File list
     */
    @Override
    public int getItemCount()
    {
        return mModFiles.size();
    }

    /**
     * Tries to write the string in the edit text to json
     * @param text = The text that tries to be saved
     * @param position = The position of the item that is being saved
     * @throws IOException if the output fails
     */
    private void writeStringToJson(String text, int position) throws IOException
    {
        //BufferedWriter to write the string to the json file
        BufferedWriter outConfig = new BufferedWriter(new FileWriter(mModFiles.get(position).getAbsolutePath()));

        try
        {
            //write the text
            outConfig.write(text);
        }
        finally
        {
            //close the file
            outConfig.close();
        }
    }

    /**
     * Reads the json file from the mod folder into a string for editing
     * @param position = The position of the string to edit
     * @return The json content in String form
     * @throws IOException if the read fails
     */
    //Reads the json file from the mod folder into a string for editing
    private String readStringFromJson(int position) throws IOException
    {
        //Use a Scanner with a delimiter to parse into string
        String content = new Scanner(new File(mModFiles.get(position).getAbsolutePath())).useDelimiter("\\Z").next();

        return content;
    }
}
