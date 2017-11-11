package com.preons.pranav.QRCodeGenerator.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.preons.pranav.QRCodeGenerator.R;
import com.preons.pranav.QRCodeGenerator.utils.Item;

/**
 * Created on 27-06-2017 at 00:35 by Pranav Raut.
 * For QRCodeProtection
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView type;
    private final TextView title;
    private final TextView subText;
    private final TextView time;
    private final View parent;
    private Item item;

    ItemViewHolder(View parent) {
        super(parent);
        this.parent = parent;
        title = parent.findViewById(R.id.title);
        subText = parent.findViewById(R.id.subText);
        time = parent.findViewById(R.id.time);
        type = parent.findViewById(R.id.type);
    }

     void make(Item item) {
        String s = item.getDisp();
        this.item = item;
        if (!item.isProtected()) parent.findViewById(R.id.toggle_protect).setVisibility(View.GONE);
        title.setText(item.getTitle());
        time.setText(item.getDate());
        type.setText(item.getTypeS());
        if (s == null || s.isEmpty()) subText.setVisibility(View.GONE);
        else subText.setText(s);
    }

    public View getParent() {
        return parent;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
