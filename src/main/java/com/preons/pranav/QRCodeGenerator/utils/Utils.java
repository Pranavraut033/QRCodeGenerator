package com.preons.pranav.QRCodeGenerator.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.preons.pranav.QRCodeGenerator.Views.DividerItemDecor;
import com.preons.pranav.QRCodeGenerator.code.Code;

import java.util.ArrayList;

/**
 * Created on 02-11-2017 at 15:41 by Pranav Raut.
 * For QRCodeProtection
 */

public class Utils {
    private static final String DEFAULT_NAME = "QRCodeG_";

    public static ArrayList<Item> search(ArrayList<Item> items, String key) {
        ArrayList<Item> ref = new ArrayList<>();
        String keys[] = key.replace('_', ' ').split(" ");
        for (Item i : items) {
            String k = i.toSearchString().toLowerCase();
            for (String s : keys)
                if (k.contains(s.toLowerCase()) && !ref.contains(i)) ref.add(i);
        }
        return ref;
    }

    public static void initRec(DividerItemDecor decor, RecyclerView... recyclerViews) {
        for (RecyclerView recyclerView : recyclerViews) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(decor);
        }
    }
}
