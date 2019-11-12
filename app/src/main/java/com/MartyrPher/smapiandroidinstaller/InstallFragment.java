package com.MartyrPher.smapiandroidinstaller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Fragment  that shows the Install Button
 */
public class InstallFragment extends Fragment {

    /**
     * Override that gets called when the view needs to be created
     * @param inflater = The layout inflator
     * @param container = The container to inflate the view in
     * @param savedInstanceState = The savedInstanceState
     * @return the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the fragment layout
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    /**
     * Override that gets called when the view is created, used to setup the Recycler View
     * @param view = The view that was created
     * @param savedInstanceState = The savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //Start button, one click install. Can't beat it.
        final Button start_button = view.findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_button.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                //Did the user give permission to storage
                if (MainActivity.mHasPermissions)
                {
                    boolean[] foundGame;
                    ApkExtractor apkExtractor = new ApkExtractor(getActivity());

                    //Did it find the game.
                    foundGame = apkExtractor.checkForInstallOrUpgrade();

                    if((foundGame[0] || foundGame[1]))
                    {
                        //Start the task
                        BackgroundTask backgroundTask = new BackgroundTask(getActivity(), apkExtractor, foundGame[0]);
                        backgroundTask.execute();
                    }
                    else
                    {
                        //Show the dialog saying that the game can't be found
                        DialogFrag.showDialog(getActivity(), R.string.cant_find, 1);
                        start_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                }
            }
        });

    }
}
