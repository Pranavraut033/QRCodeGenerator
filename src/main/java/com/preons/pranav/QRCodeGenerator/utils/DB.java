package com.preons.pranav.QRCodeGenerator.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.preons.pranav.QRCodeGenerator.code.Code;

import java.util.ArrayList;

import pranav.utilities.DataBaseHelper;

/**
 * Created on 07-08-2017 at 09:23 by Pranav Raut.
 * For QRCodeProtection
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class DB {
    private static final String EXTRA = "extra";

    public final static class EC extends DataBaseHelper {
        public static final SQLiteQuery QUERY;

        static {
            QUERY = new SQLiteQuery("contact");
            QUERY.addCol("name", "TEXT");
            QUERY.addCol("phone", "BIGINT");
            QUERY.addCol("email", "TEXT");
            QUERY.addCol("address", "TEXT");
            QUERY.addCol("note", "TEXT");
            QUERY.addCol("website", "TEXT");
        }

        public EC(Context context) {
            super(context, QUERY);
        }

        @Nullable
        @Override
        public ArrayList getEverything() {
            return null;
        }

        @Nullable
        @Override
        public Object getItem(int index) {
            return null;
        }

        @Override
        protected ContentValues contentVal(Object item) {
            return null;
        }
    }

    public final static class EL extends DataBaseHelper {
        public static final SQLiteQuery QUERY;

        static {
            QUERY = new SQLiteQuery("location");
            QUERY.addCol("latitude", "FLOAT");
            QUERY.addCol("longitude", "FLOAT");
            QUERY.addCol("altitude", "FLOAT");
        }

        public EL(Context context) {
            super(context, QUERY);
        }

        @Nullable
        @Override
        public ArrayList getEverything() {
            return null;
        }

        @Nullable
        @Override
        public Object getItem(int index) {
            return null;
        }

        @Override
        protected ContentValues contentVal(Object item) {
            return null;
        }
    }

    public final static class EW extends DataBaseHelper {
        public static final SQLiteQuery QUERY;

        static {
            QUERY = new SQLiteQuery("wifi");
            QUERY.addCol("bssid", "TEXT");
            QUERY.addCol("pass", "TEXT");
            QUERY.addCol("type", "INT");
        }

        public EW(Context context) {
            super(context, QUERY);
        }

        @Nullable
        @Override
        public ArrayList getEverything() {
            return null;
        }

        @Nullable
        @Override
        public Object getItem(int index) {
            return null;
        }

        @Override
        protected ContentValues contentVal(Object item) {
            return null;
        }
    }

    public final static class EE extends DataBaseHelper {
        public static final SQLiteQuery QUERY;

        static {
            QUERY = new SQLiteQuery("email");
            QUERY.addCol("emailID", "TEXT");
            QUERY.addCol("sub", "TEXT");
            QUERY.addCol("message", "TEXT");
        }

        public EE(Context context) {
            super(context, QUERY);
        }

        @Nullable
        @Override
        public ArrayList getEverything() {
            return null;
        }

        @Nullable
        @Override
        public Object getItem(int index) {
            return null;
        }

        @Override
        protected ContentValues contentVal(Object item) {
            return null;
        }
    }

    public final static class ES extends DataBaseHelper {
        public static final SQLiteQuery QUERY;

        static {
            QUERY = new SQLiteQuery("sms");
            QUERY.addCol("phone", "BIGINT");
            QUERY.addCol("message", "TEXT");
        }

        public ES(Context context) {
            super(context, QUERY);
        }

        @Nullable
        @Override
        public ArrayList getEverything() {
            return null;
        }

        @Nullable
        @Override
        public Object getItem(int index) {
            return null;
        }

        @Override
        protected ContentValues contentVal(Object item) {
            return null;
        }
    }

    public final static class EEV extends DataBaseHelper {
        public static final SQLiteQuery QUERY;

        static {
            QUERY = new SQLiteQuery("event");
            QUERY.addCol("sum", "TEXT");
            QUERY.addCol("startDateTime", "BIGINT");
            QUERY.addCol("endDateTime", "BIGINT");
            QUERY.addCol("location", "TEXT");
            QUERY.addCol("latitude", "FLOAT");
            QUERY.addCol("longitude", "FLOAT");
            QUERY.addCol("organizer", "TEXT");
        }

        public EEV(Context context) {
            super(context, QUERY);
        }

        @Nullable
        @Override
        public ArrayList getEverything() {
            return null;
        }

        @Nullable
        @Override
        public Object getItem(int index) {
            return null;
        }

        @Override
        protected ContentValues contentVal(Object item) {
            return null;
        }
    }

    public static class I extends DataBaseHelper<Item> {
        public static final SQLiteQuery QUERY;
        private static final String INDEX_COLUMN = "dex";
        private static final String TITLE_COLUMN = "title";
        private static final String DISP_COLUMN = "disp";
        private static final String TYPE_COLUMN = "type";
        private static final String DATA_COLUMN = "data";
        private static final String EXTRA_REF_COLUMN = "extra_ref";
        private static final String DATE_COLUMN = "date";
        private static final String IMG_REF_COLUMN = "img_ref";
        private static final String PROTECT_COLUMN = "isProtect";
        private static final String PASSWORD_COLUMN = "password";
        private static final String LABEL_COLUMN = "label";
        private static final String DEFAULT_NAME = "QRG_";

        private static final String TABLE_NAME = "Item";

        static {
            QUERY = new SQLiteQuery(TABLE_NAME);
            QUERY.addCol(INDEX_COLUMN, "INT");
            QUERY.addCol(TITLE_COLUMN, "TEXT");
            QUERY.addCol(DISP_COLUMN, "TEXT");
            QUERY.addCol(TYPE_COLUMN, "INT");
            QUERY.addCol(DATA_COLUMN, "TEXT");
            QUERY.addCol(EXTRA_REF_COLUMN, "INT");
            QUERY.addCol(DATE_COLUMN, "BIGINT");
            QUERY.addCol(IMG_REF_COLUMN, "TEXT");
            QUERY.addCol(PROTECT_COLUMN, "BOOLEAN");
            QUERY.addCol(PASSWORD_COLUMN, "TEXT");
            QUERY.addCol(LABEL_COLUMN, "TEXT");
        }

        public I(Context context) {
            super(context, QUERY);
        }

        @NonNull
        public static String getName(Item item) {
            StringBuilder builder = new StringBuilder(50);
            builder.append(DEFAULT_NAME);
            if (item.isProtected)
                builder.append("SECURE_");
            builder.append(item.dateL).append('_')
                    .append(Code.Types.values()[item.type].name()).append('_')
                    .append(item.title.length() > 4 ? item.title.substring(0, 4) : item.title);
            return builder.toString();
        }

        public int getID(Object object) {
            return super.getID(INDEX_COLUMN, object);
        }

        @NonNull
        public ArrayList<Item> getEverything() {
            ArrayList<Item> temp = new ArrayList<>((int) getRowCount());
            Cursor cursor = getCursor(QUERY.getSelectTableQuery());
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    temp.add(new Item(cursor.getInt(1), cursor.getString(2), cursor.getString(3),
                            cursor.getInt(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getLong(7), cursor.getInt(9) == 1, cursor.getString(10)));
                    cursor.moveToNext();
                }
                cursor.close();
            }
            return temp;
        }

        @Nullable
        @Override
        public Item getItem(int index) {
            Cursor cursor = getReadableDatabase().rawQuery("selectAll * from " + TABLE_NAME + " where " +
                    INDEX_COLUMN + " = '" + index + "'", null);
            if (cursor != null) {
                cursor.moveToFirst();
                Item item = new Item(cursor.getInt(1), cursor.getString(2), cursor.getString(3),
                        cursor.getInt(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getLong(7), cursor.getInt(9) == 1, cursor.getString(10));
                cursor.close();
                return item;
            }
            return null;
        }

        @Override
        protected ContentValues contentVal(Item item) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(INDEX_COLUMN, item.getIndex());
            contentValues.put(TITLE_COLUMN, item.getTitle());
            contentValues.put(DISP_COLUMN, item.getDisp());
            contentValues.put(TYPE_COLUMN, item.getType());
            contentValues.put(DATA_COLUMN, item.getData());
            contentValues.put(EXTRA_REF_COLUMN, item.getExtra_ref());
            contentValues.put(DATE_COLUMN, item.getDateL());
            contentValues.put(IMG_REF_COLUMN, item.getExtra_ref());
            contentValues.put(PROTECT_COLUMN, item.isProtected());
            contentValues.put(PASSWORD_COLUMN, item.getPassword());
            contentValues.put(PASSWORD_COLUMN, item.getPassword());
            return contentValues;
        }
    }

}