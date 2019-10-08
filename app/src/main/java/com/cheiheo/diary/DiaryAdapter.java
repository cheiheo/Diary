package com.cheiheo.diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cheiheo.diary.bean.Diary;
import com.cheiheo.diary.bean.DiaryList;
import com.cheiheo.diary.utils.SendNotification;

import java.util.List;

/**
 * author:chen hao
 * email::
 * time:2019/09/20
 * desc:
 * version:1.0
 */
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private List<Diary> diaries;
    private int editPosition = -1;

    public DiaryAdapter(List<Diary> diaries) {
        this.diaries = diaries;
    }

    public void setDiaries(List<Diary> diaries) {
        this.diaries = diaries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Diary diary = diaries.get(position);
        holder.dateTextView.setText(diary.getDate());
        holder.titleTextView.setText(diary.getTitle());
        holder.contentTextView.setText(diary.getContent());
        // 删除和编辑按钮 显示或隐藏
        //holder.editImageView.setVisibility(View.VISIBLE);
        //holder.deleteImageView.setVisibility(View.VISIBLE);
        if (editPosition == position) {
            holder.editImageView.setVisibility(View.VISIBLE);
            holder.deleteImageView.setVisibility(View.VISIBLE);
        } else {
            holder.editImageView.setVisibility(View.GONE);
            holder.deleteImageView.setVisibility(View.GONE);
        }
        holder.itemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.editImageView.getVisibility() == View.VISIBLE) {
                    holder.editImageView.setVisibility(View.GONE);
                    holder.deleteImageView.setVisibility(View.GONE);
                } else {
                    holder.editImageView.setVisibility(View.VISIBLE);
                    holder.deleteImageView.setVisibility(View.VISIBLE);
                }
                if(editPosition != position){
                    notifyItemChanged(editPosition);
                }
                editPosition = position;
            }
        });

        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentActivity.startActivity(view.getContext(), diary.getId());
            }
        });

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaryList.getInstance(view.getContext()).deleteDiary(diary);
                diaries.remove(diary);
                SendNotification sendNotification = new SendNotification(view.getContext());
                sendNotification.send("Diary:" + diary.getTitle() ,"已删除");
                editPosition = -1;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout itemLinearLayout;
        private TextView dateTextView;
        private TextView titleTextView;
        private TextView contentTextView;
        private ImageView editImageView;
        private ImageView deleteImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView =itemView.findViewById(R.id.date_tv_rv_item);
            titleTextView =itemView.findViewById(R.id.title_tv_rv_item);
            contentTextView =itemView.findViewById(R.id.content_tv_rv_item);
            editImageView = itemView.findViewById(R.id.edit_iv_rv_item);
            deleteImageView = itemView.findViewById(R.id.delete_iv_rv_item);
            itemLinearLayout = itemView.findViewById(R.id.item_rv);
        }
    }
}
