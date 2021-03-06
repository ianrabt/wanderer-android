package me.connick.wanderer;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import me.connick.wanderer.networking.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import me.connick.wanderer.networking.ApiRequestTaskSet;
import me.connick.wanderer.networking.Urls;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static PhotosFragment newInstance() {
        PhotosFragment fragment = new PhotosFragment();
        return fragment;
    }

    public PhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        new ApiRequestTaskSet(new ApiRequestTaskSet.LocationSetReceiver() {
            @Override
            public void receive(Set<me.connick.wanderer.networking.Location> locations) {
                Random rand = new Random();
                List<Location.Perspective> pList = new ArrayList<Location.Perspective>();
                for (Location loc : locations)
                    for (Location.Perspective p : loc.perspectives)
                        pList.add(rand.nextInt(pList.size() + 1), p);
                for (Location.Perspective p : pList) {
                    ImageView img = new ImageView(getActivity().getApplicationContext());
                    img.setImageBitmap(p.photo);
                    ((LinearLayout) getView()).addView(img);
                }
            }
        }).execute(Urls.getLocationsURL());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
