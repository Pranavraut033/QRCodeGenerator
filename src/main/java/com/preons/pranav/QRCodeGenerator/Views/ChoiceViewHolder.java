package com.preons.pranav.QRCodeGenerator.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.preons.pranav.QRCodeGenerator.R;
import com.preons.pranav.QRCodeGenerator.utils.Choice;

/**
 * Created on 25-06-2017 at 14:31 by Pranav Raut.
 * For QRCodeProtection
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ChoiceViewHolder extends RecyclerView.ViewHolder {

    private final View parent;
    private final ImageView preview;
    private final TextView title;
    private final TextView dis;

    public ChoiceViewHolder(View parent) {
        super(parent);
        this.parent = parent;
        title = parent.findViewById(R.id.title);
        dis = parent.findViewById(R.id.dis);
        preview = parent.findViewById(R.id.preview);
    }

    public void make(Choice choice) {
        title.setText(choice.getTitle());
        if (choice.getDis() == null || choice.getDis().isEmpty())
            dis.setVisibility(View.GONE);
        else
            dis.setText(choice.getDis());
        int i = (int) choice.getPadding();
        if (i != -1)
            preview.setPadding(i, i, i, i);
        preview.setImageDrawable(choice.getDrawable());
    }

    public View getParent() {
        return parent;
    }

    public TextView getDis() {
        return dis;
    }

    public ImageView getPreview() {
        return preview;
    }

    public TextView getTitle() {
        return title;
    }
}