package com.preons.pranav.QRCodeGenerator.Views;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.preons.pranav.QRCodeGenerator.R;
import com.preons.pranav.QRCodeGenerator.utils.Item;

import java.util.ArrayList;

import pranav.utilities.Animations;

import static com.preons.pranav.QRCodeGenerator.utils.c.DI;

/**
 * Created on 20-06-2017 at 11:46 by Pranav Raut.
 * For QRCodeProtection
 */

@SuppressWarnings("unused")
public class ItemViewHolderAdapter extends RecyclerView.Adapter<ItemViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private static final long SAD = 0x4b;
    private static final int B = 0x550091EA;
    private boolean multiModeOn;
    private boolean multiEnabled = true;
    private ArrayList<Item> items;
    private ArrayList<Float> checkItems = new ArrayList<>();
    private Animations.AnimatingColor animatingColor = new Animations.AnimatingColor();
    @Nullable
    private OnClick click;

    {
        animatingColor.setDuration(2 * SAD);
    }

    public ItemViewHolderAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.make(item);
        View view = holder.getParent();
        view.setContentDescription(String.valueOf(position));
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    private void animateSelect(View v) {
        alterVal(v, true);
        animatingColor.setObject(v);
        animatingColor.setColors(0, B).start();
        v.animate().scaleY(.95f).scaleX(.95f).setDuration(SAD)
                .setInterpolator(DI).start();
    }

    private void animateDeselect(View v) {
        alterVal(v, false);
        animatingColor.setObject(v);
        animatingColor.setColors(B, 0).start();
        v.animate().scaleY(1).scaleX(1).setDuration(SAD)
                .setInterpolator(DI).start();
    }

    private void alterVal(View v, boolean isSelected) {
        float f = Integer.parseInt(v.getContentDescription().toString());
        if (isSelected) checkItems.add(f);
        else checkItems.remove(f);
    }

    public void reset() {
        multiModeOn = false;
        checkItems.clear();
    }

    private void quickSelect(View v) {
        v.setBackgroundColor(B);
        v.setScaleY(.95f);
        v.setScaleX(.95f);
    }

    private void quickDeselect(View v) {
        v.setBackgroundColor(0);
        v.setScaleY(1);
        v.setScaleX(1);
    }

    public void selectMode(boolean a) {
        if (a) for (int j = 0; j < items.size(); j++) checkItems.add((float) j);
        else checkItems.clear();
        notifyDataSetChanged();
        if (click != null) click.onMultiSelect(getSelectionCount(), true);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getSelectionCount() {
        checkItems.trimToSize();
        return checkItems.size();
    }

    public ArrayList<Integer> getCheckedItemIndices() {
        ArrayList<Integer> i = new ArrayList<>();
        for (float f : checkItems) i.add((int) f);
        System.out.println(i.toString());
        return i;
    }

    public void setClick(@Nullable OnClick click) {
        this.click = click;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void onViewAttachedToWindow(ItemViewHolder holder) {
        float f = Float.parseFloat(holder.getParent().getContentDescription().toString());
        if (checkItems.contains(f)) quickSelect(holder.getParent());
        else quickDeselect(holder.getParent());
        super.onViewAttachedToWindow(holder);
    }

    private boolean isSelected(View v) {
        float f = Float.parseFloat(v.getContentDescription().toString());
        return checkItems.contains(f);
    }

    @Override
    public void onClick(View v) {
        if (multiModeOn) {
            boolean b = isSelected(v);
            if (!b) animateSelect(v);
                else animateDeselect(v);
            multiModeOn = getSelectionCount() != 0;
            if (click != null) click.onMultiSelect(getSelectionCount(), multiModeOn);
        } else if (click != null)
            click.onClick(Integer.parseInt(v.getContentDescription().toString()));
    }

    @Override
    public boolean onLongClick(View v) {
        if (!multiEnabled)
            return false;
        int i = Integer.parseInt(v.getContentDescription().toString());
        multiModeOn = true;
        if (!isSelected(v))
            animateSelect(v);
        if (click != null) click.onMultiSelect(getSelectionCount(), true);
        return true;
    }

    public boolean isMultiEnabled() {
        return multiEnabled;
    }

    public void setMultiEnabled(boolean multiEnabled) {
        this.multiEnabled = multiEnabled;
    }

    public void clearSelection() {
        checkItems.clear();
    }

    public interface OnClick {
        void onClick(int position);

        void onMultiSelect(int count, boolean multiOn);
    }
}