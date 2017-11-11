package com.preons.pranav.QRCodeGenerator.Views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.preons.pranav.QRCodeGenerator.R;
import com.preons.pranav.QRCodeGenerator.utils.Choice;

import java.util.ArrayList;

import pranav.utilities.Utilities;

import static com.preons.pranav.QRCodeGenerator.utils.c.DI;
import static pranav.utilities.Animations.ANIMATION_TIME;
import static pranav.utilities.Animations.AnimatingParameter.animateHeight;

/**
 * Created on 25-06-2017 at 14:32 by Pranav Raut.
 * For QRCodeProtection
 */

public class ChoiceViewHolderAdapter extends RecyclerView.Adapter<ChoiceViewHolder> {

    private ArrayList<Choice> choices;
    @Nullable
    private OnClick onClick;
    private int lastOpened = -1;
    private View.OnLayoutChangeListener l = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            v.removeOnLayoutChangeListener(this);
            animateHeight(v, top, oldTop, bottom, oldBottom);
        }
    };
    @Nullable
    private ChoiceViewHolder oldHolder;
    private float padding;
    private int lastPosition = -1;
    private boolean toAnimate;

    public ChoiceViewHolderAdapter(ArrayList<Choice> choices) {
        this(choices, -1);
    }

    public ChoiceViewHolderAdapter(ArrayList<Choice> choices, float padding) {
        this.choices = choices;
        this.padding = padding;
    }

    @Override
    public ChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChoiceViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choice_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ChoiceViewHolder holder, int position) {
        final Choice choice = choices.get(position);
        holder.make(choice.setPadding(padding));
        final Utilities.DoubleClick doubleClick = new Utilities.DoubleClick() {
            @Override
            protected void doubleClickAction() {
                toggle(holder);
            }

            @Override
            protected void singleClickAction() {
                if (onClick != null) onClick.onSingleClick(holder.getAdapterPosition());
            }
        };
        holder.getParent().setOnClickListener(v -> doubleClick.click());
        holder.getParent().setOnLongClickListener(v -> toggle(holder));
    }

    private boolean toggle(ChoiceViewHolder holder) {
        int i = holder.getAdapterPosition();
        if (lastOpened != i) {
            if (oldHolder != null) {
                oldHolder.getParent().addOnLayoutChangeListener(l);
                close(oldHolder);
            }
            lastOpened = i;
            holder.getParent().addOnLayoutChangeListener(l);
            open(holder);
            oldHolder = holder;
        }
        return true;
    }

    private void open(@NonNull ChoiceViewHolder holder) {
        holder.getDis().setMaxLines(99);
        holder.getTitle().setMaxLines(2);
    }

    private void close(@NonNull ChoiceViewHolder holder) {
        holder.getDis().setMaxLines(2);
        holder.getTitle().setMaxLines(1);
    }

    @Override
    public void onViewAttachedToWindow(ChoiceViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (lastOpened != holder.getAdapterPosition()) close(holder);
        else open(holder);
        if (toAnimate)
            startAnimation(holder.getParent(), holder.getAdapterPosition());
    }

    private void startAnimation(View v, int i) {
        if (i > lastPosition) {
            lastPosition = i;
            v.setAlpha(0);
            v.animate().alpha(1).setDuration(ANIMATION_TIME).setInterpolator(DI)
                    .setStartDelay((long) (i * 37.5)).start();
        }
    }

    @Override
    public void onViewDetachedFromWindow(ChoiceViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getParent().clearAnimation();
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public ChoiceViewHolderAdapter setOnClick(@Nullable OnClick onClick) {
        this.onClick = onClick;
        return this;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public void toAnimate(boolean toAnimate) {
        this.toAnimate = toAnimate;
    }

    public interface OnClick {
        void onSingleClick(int position);
    }
}