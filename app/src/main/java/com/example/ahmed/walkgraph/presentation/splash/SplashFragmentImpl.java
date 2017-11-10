package com.example.ahmed.walkgraph.presentation.splash;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 *
 * Fragment to represent splashScreen.
 */

public class SplashFragmentImpl extends Fragment implements SplashFragment{

    /**
     * CallBack interFace to be implemented by containing activity.
     */
    public interface SplashCallback {
        /**
         * Switch to map.
         */
        void changeToMap();
    }

    /**
     * Fields
     */

    @Inject
    SplashPresenterImpl splashPresenter;
    private SplashCallback callback;

    /**
     * Initializes injected fields, Prevents NPE.
     * @param savedInstance of the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
    }

    /**
     * Inflate the resource this fragment.
     * @param inflater to use in inflating layout resource.
     * @param container to pass to inflate method.
     * @param savedInstanceState of the fragment.
     * @return the view for this fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        splashPresenter.openMapFragment();
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    /**
     * Instantiates the Callback field from the containing activity.
     * @param context passed from the containing activity.
     * @throws RuntimeException when the activity does not implement the callback interface.
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof SplashCallback){
            callback = (SplashCallback) context;
        }else
            throw new RuntimeException(context.toString() + "Must implement SplashCallback");
    }

    /**
     * Switches to MapFragment
     */

    @Override
    public void openMapFragment() {
        callback.changeToMap();
    }

}
