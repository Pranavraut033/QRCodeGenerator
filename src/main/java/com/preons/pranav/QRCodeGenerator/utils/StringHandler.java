package com.preons.pranav.QRCodeGenerator.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created on 08-03-17 at 20:59 by Pranav Raut.
 * For QRCodeProtection
 */

public class StringHandler {
    public static final int WITHOUT_PASS = -2;
    public static final int INITIAL = 0;
    public static final int FINAL = 1;
    public static final int WITHOUT_STD = 2;
    public static final String URL = "**@**";
    public static final String wrongPass = "Wrong Password";
    public static final String P = "|";
    public static final String STD = "**?**";
    private static final String special = "!@#$%^&*!";
    private final String[] a = new String[]{
            "<b>Name:</b> ", "<b>Address:</b> ", "<b>Email:</b> ",
            "<b>Phone No:</b> ", "<b>Gender:</b> ", "<b>Aadhaar No:</b> ",
            "<b>PAN No:</b> ", "<b>Bank Account No:</b> ", "<b>Current Status:</b> "};
    private final String p = "\\|";
    private String initialString = "";
    private String withoutPassword = "";
    private String password = "";
    private String finalText;
    private String[] allInfo;
    private String currentString = "";
    private boolean isSTD = false;
    private String filePath;
    private boolean isImage;

    public StringHandler(@Nullable String s) {
        currentString = initialString = s;
    }

    public static String decrypt(String inputString) {
        String normalString = "";
        char[] chars = inputString.toCharArray();
        for (char aChar : chars) normalString += (char) (aChar + 21 - 4 - 6);
        return normalString;
    }

    public static void setClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public static void makeToast(Context c, View ref) {
        Toast t = new Toast(c);
        t.setGravity(Gravity.NO_GRAVITY, (int) ref.getY() / 2, (int) ref.getX() / 2);
        TextView view1 = new TextView(c);
        view1.setBackgroundColor(Color.parseColor("#000000"));
        view1.setTextColor(Color.parseColor("#ffffff"));
        view1.setText("Enter Password");
        view1.setPadding(5, 5, 5, 5);
        t.setView(view1);
        t.setDuration(Toast.LENGTH_SHORT);
        t.show();
    }

    public String[] getAllInfo(int flag) {
        switch (flag) {
            case WITHOUT_PASS:
                allInfo = withoutPassword.split(p);
                break;
            case WITHOUT_STD:
            case INITIAL:
                allInfo = initialString.split(p);
                break;
            case FINAL:
                allInfo = finalText.split(p);
                break;
        }
        return allInfo;
    }

    public boolean containSTD(boolean b) {
        return initialString.contains((this.isSTD = b) ? encrypt(STD) : STD);
    }

    public boolean containsURL() {
        return isImage = initialString.contains(isSTD ? encrypt(URL) : URL);
    }

    public void replaceSTD() {
        currentString = initialString = initialString.replace((isSTD ? encrypt(STD) : STD), "");
    }

    public void replaceURL() {
        currentString = initialString = initialString.replace((isSTD ? encrypt(URL) : URL), "");
    }

    public String getAllForText() {
        String t = "";
        int i = 0;
        for (; i < 4; i++) t += a[i] + "<br>" + allInfo[i + 1] + "<br>";
        t += a[i++] + (Boolean.getBoolean(allInfo[0]) ? "Female" : "Male") + "<br>";
        for (; i < a.length; i++)
            t += a[i] + allInfo[i] + "<br>";
        if (isImage)
            filePath = allInfo[allInfo.length - 1];
        return currentString = t;
    }

    public String encryptString(String normalString, String password) {
        String encryptString = normalString;
        if (!password.isEmpty()) {
            password = special + hashPassword(password);
            encryptString = password + encrypt(normalString);
        }
        return currentString = encryptString;
    }

    private boolean isPassword(String s) {
        return s.contains(special);
    }

    public boolean isPassword() {
        return isPassword(initialString);
    }

    public String decryptString(String inputString, String password) {
        password = hashPassword(password);
        boolean isEncrypted = isPassword(inputString);
        if (isEncrypted) {
            if (isCorrect(inputString, password)) {
                inputString = replacePassword(inputString, password);
                return decrypt(inputString);
            } else
                return wrongPass;
        } else return currentString = inputString + " (NO PASSWORD SET)";
    }

    public void decrypt() {
        currentString = finalText = decrypt(withoutPassword);
    }

    private String replacePassword(String s, String password) {
        return currentString = s.replaceFirst(password, "");
    }

    public void replacePassword() {
        currentString = withoutPassword = replacePassword(
                initialString = initialString.replace(special, "")
                , password);
    }

    private boolean isCorrect(String s, String password) {
        return s.contains(password);
    }

    public boolean isCorrect(String password) {
        this.password = password = hashPassword(password);
        return isCorrect(initialString, password);
    }

    public String getFinalText() {
        return finalText;
    }

    private String encrypt(String s) {
        String temp = "";
        char[] chars = s.toCharArray();
        for (char aChar : chars) temp += (char) (aChar - 21 + 4 + 6);
        return currentString = temp;
    }

    private String hashPassword(String password) {
        password = encrypt(password);
        try {
            return password.substring(0, 3) + password.substring(5, password.length() - 3);
        } catch (Exception ignored) {
            return password;
        }
    }

    public void interchange(String s, int flag) {
        switch (flag) {
            case WITHOUT_PASS:
                withoutPassword = s;
                break;
            case WITHOUT_STD:
            case INITIAL:
                initialString = s;
                break;
            case FINAL:
                finalText = s;
                break;
        }
        currentString = s;
    }

    public String getCurrentString() {
        return currentString;
    }

    @Nullable
    public String getFilePath() {
        return filePath;
    }
}
