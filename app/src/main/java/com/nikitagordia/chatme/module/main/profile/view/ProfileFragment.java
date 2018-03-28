package com.nikitagordia.chatme.module.main.profile.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.FragmentProfileBinding;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;

import java.util.Arrays;

/**
 * Created by nikitagordia on 3/28/18.
 */

public class ProfileFragment extends Fragment {

    FragmentProfileBinding bind;

    private ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = FragmentProfileBinding.inflate(inflater, container, false);

        adapter = new ListAdapter(getContext());
        bind.postList.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.postList.setAdapter(adapter);

        adapter.updatePosts(Arrays.asList(
                new BlogPost("Nikita Gordia", "28 March, 2018", "RecyclerView exposes the common ViewHolder pattern as a first class citizen in its API. In onCreateViewHolder(), the Views are created and the ViewHolder contains references to them so that the data can be set quickly. Then in onBindView(), the specific data is assigned to the Views."),
                new BlogPost("George Mount", "14 March, 2018", "From a UX standpoint, this means that when you run the app, play some media, and hit the home button, the player resources are released. When you switch back to that app, the player is initialized again, and the previous state information should be restored, so that the user can resume playback where they left off (position of previous playback if any, and the item in the playlist that they were consuming, which is a window index)."),
                new BlogPost("Nazmul Idris", "12 March, 2018", "Before the player’s resources are released, the player’s currentWindowIndex, currentPosition, playWhenReady, and playlist or media item information are saved to the PlayerState object. The state is then restored once the player is reinitialized. Here are methods from PlayerHolder class that demonstrate how this can be done."),
                new BlogPost("Leon Nicholls", "7 March, 2018", "Want More? Head over to the Actions on Google community to discuss Actions with other developers. Join the Actions on Google developer community program and you could earn a $200 monthly Google Cloud credit and an Assistant t-shirt when you publish your first app.")
        ));

        return bind.getRoot();
    }
}
