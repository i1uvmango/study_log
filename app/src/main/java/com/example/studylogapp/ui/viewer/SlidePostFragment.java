package com.example.studylogapp.ui.viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studylogapp.R;
import com.example.studylogapp.database.AppDatabase;
import com.example.studylogapp.model.StudyPost;

import java.util.ArrayList;
import java.util.List;

public class SlidePostFragment extends Fragment {
    private static final String ARG_POST = "post";
    private StudyPost post;
    private RecyclerView recyclerView;
    private PhotoSlideAdapter adapter;
    private TextView tvSummary, tvKeyword;
    private AppDatabase database;
    private List<StudyPost> allPosts;

    public static SlidePostFragment newInstance(StudyPost post) {
        SlidePostFragment fragment = new SlidePostFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POST, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (StudyPost) getArguments().getSerializable(ARG_POST);
        }
        database = new AppDatabase(requireContext());
        database.open();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_slide_post, container, false);
        
        recyclerView = view.findViewById(R.id.viewpager_photos);
        tvSummary = view.findViewById(R.id.tv_summary);
        tvKeyword = view.findViewById(R.id.tv_keyword);

        if (post != null) {
            // 같은 날짜의 모든 게시물 가져오기
            allPosts = database.getPostsByLogId(post.getStudyLogId());
            List<String> photoUris = new ArrayList<>();
            for (StudyPost p : allPosts) {
                photoUris.add(p.getPhotoUri());
            }

            adapter = new PhotoSlideAdapter(requireContext(), photoUris);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);

            // 현재 게시물의 인덱스 찾기
            int currentIndex = 0;
            for (int i = 0; i < allPosts.size(); i++) {
                if (allPosts.get(i).getId() == post.getId()) {
                    currentIndex = i;
                    break;
                }
            }
            recyclerView.scrollToPosition(currentIndex);

            tvSummary.setText(post.getSummary());
            tvKeyword.setText(post.getKeyword());

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        if (layoutManager != null) {
                            int position = layoutManager.findFirstVisibleItemPosition();
                            if (position >= 0 && position < allPosts.size()) {
                                StudyPost currentPost = allPosts.get(position);
                                tvSummary.setText(currentPost.getSummary());
                                tvKeyword.setText(currentPost.getKeyword());
                            }
                        }
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (database != null) {
            database.close();
        }
    }
}

