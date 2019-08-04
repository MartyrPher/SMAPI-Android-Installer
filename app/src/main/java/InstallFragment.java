import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.MartyrPher.smapiandroidinstaller.ApkExtractor;
import com.MartyrPher.smapiandroidinstaller.BackgroundTask;
import com.MartyrPher.smapiandroidinstaller.DialogFrag;
import com.MartyrPher.smapiandroidinstaller.MainActivity;
import com.MartyrPher.smapiandroidinstaller.R;

//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;

public class InstallFragment extends Fragment {

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
//    {
//        final Button start_button = view.findViewById(R.id.start_button);
//        start_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                start_button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//                if (true)
//                {
//                    boolean[] foundGame;
//                    ApkExtractor apkExtractor = new ApkExtractor(null);
//
//                    foundGame = apkExtractor.checkForInstallOrUpgrade();
//
//                    if((foundGame[0] || foundGame[1]))
//                    {
//                        BackgroundTask backgroundTask = new BackgroundTask(null, apkExtractor, foundGame[0]);
//                        backgroundTask.execute();
//                    }
//                    else
//                    {
//                        DialogFrag.showDialog(null, R.string.cant_find, 1);
//                        start_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    }
//
//                }
//            }
//        });
//
//    }
}
