package adriancardenas.com.ehealth.Tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adriancardenas.com.ehealth.R;

public class InformationGeneralFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.tabs, container,false);
       return view;
    }

    public static Fragment newInstance() {
        return new InformationGeneralFragment();
    }
}
