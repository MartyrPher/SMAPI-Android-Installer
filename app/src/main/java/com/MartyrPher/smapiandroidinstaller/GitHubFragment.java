package com.MartyrPher.smapiandroidinstaller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class GitHubFragment extends Fragment
{
    //GitHub releases link constant
    private final String mGitHubLink = "https://github.com/MartyrPher/SMAPI-Android-Installer/releases/latest";

    //Mod Compatibility link constant
    private final String mCompatLink = "https://smapi.io/mods/";

    //Stardew Valley nexus home link constant
    private final String mNexusLink = "https://www.nexusmods.com/stardewvalley";

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
        return inflater.inflate(R.layout.github_layout, container, false);
    }

    /**
     * Override that gets called when the view is created, used to setup the buttons
     * @param view = The view that was created
     * @param savedInstanceState = The savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //Set up the github button onClick
        final Button github_button = view.findViewById(R.id.github_button);
        github_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(mGitHubLink);
            }
        });

        //Set up the compat button onClick
        final Button compat_button = view.findViewById(R.id.compat_button);
        compat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(mCompatLink);
            }
        });

        //Set up the nexus button onClick
        final Button nexus_button = view.findViewById(R.id.nexus_button);
        nexus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(mNexusLink);
            }
        });
    }

    /**
     * Opens a link in the default browser on Android
     * @param url = The url to open in the browser
     */
    private void openLink(String url)
    {
        try {
            Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(openBrowser);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Could not find a web browser to open the link.", Toast.LENGTH_LONG).show();
        }
    }
}

