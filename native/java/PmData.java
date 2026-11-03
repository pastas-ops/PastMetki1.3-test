package com.pastmetki.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class PmData {
    static SharedPreferences sp(Context c) {
        return c.getSharedPreferences("pm_widget", Context.MODE_PRIVATE);
    }

    // Кэш для виджетов: приложение (WebView) кладёт сюда снимок данных из Firestore.
    static void save(Context c, String chats, String notes, String dls) {
        sp(c).edit().putString("chats", chats).putString("notes", notes).putString("dls", dls).apply();
        refresh(c);
    }

    static String get(Context c, String k) {
        return sp(c).getString(k, "[]");
    }

    static void refresh(Context c) {
        AppWidgetManager m = AppWidgetManager.getInstance(c);
        Class<?>[] lists = new Class<?>[] { ChatsWidget.class, NotesWidget.class, DeadlineWidget.class };
        for (Class<?> k : lists) {
            int[] ids = m.getAppWidgetIds(new ComponentName(c, k));
            if (ids.length > 0) m.notifyAppWidgetViewDataChanged(ids, R.id.list);
        }
    }

    // Нажатие -> открыть приложение по ссылке pastmetki://<path>
    static PendingIntent link(Context c, String path, int rc) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("pastmetki://" + path));
        i.setClass(c, MainActivity.class);
        return PendingIntent.getActivity(c, rc, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    // Шаблон для элементов списка: ссылка дописывается в каждом элементе (fill-in intent)
    static PendingIntent template(Context c, int rc) {
        Intent t = new Intent(Intent.ACTION_VIEW);
        t.setClass(c, MainActivity.class);
        return PendingIntent.getActivity(c, rc, t, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
    }
}
