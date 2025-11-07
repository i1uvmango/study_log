package com.example.studylogapp.ui.viewer;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studylogapp.model.StudyPost;

import java.util.List;

public class SlideViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
    private List<StudyPost> posts;

    public SlideViewPagerAdapter(androidx.fragment.app.FragmentActivity activity, List<StudyPost> posts) {
        super(activity);
        this.posts = posts;
    }

    @NonNull
    @Override
    public androidx.fragment.app.Fragment createFragment(int position) {
        return SlidePostFragment.newInstance(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}

