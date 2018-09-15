package me.zuichu.androidsuit.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import me.zuichu.androidsuit.R;
import me.zuichu.androidsuit.adapter.TextListAdapter;

public class TextListView extends FrameLayout implements ClipboardManager.OnPrimaryClipChangedListener {
    private View rootView;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ClipboardManager clipboardManager;
    private ClipData clipData;
    private List<String> stringList;
    private TextListAdapter adapter;

    public TextListView(@NonNull Context context) {
        super(context);
        init();
    }

    public TextListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_textlist, this);
        recyclerView = rootView.findViewById(R.id.rv_list);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        stringList = new ArrayList<String>();
        clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipData = clipboardManager.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                stringList.add(clipData.getItemAt(i).getText().toString());
            }
        }
        clipboardManager.addPrimaryClipChangedListener(this);
        adapter = new TextListAdapter(getContext(), stringList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPrimaryClipChanged() {
        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClip().getItemCount() > 0) {
            CharSequence addedText = clipboardManager.getPrimaryClip().getItemAt(0).getText();
            if (addedText != null) {
                stringList.add(addedText.toString());
                adapter.notifyDataSetChanged();
            }
        }
    }
}
