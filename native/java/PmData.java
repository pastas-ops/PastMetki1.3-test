package com.pastmetki.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import org.json.JSONArray;
import org.json.JSONObject;

public class PmData {
    static SharedPreferences sp(Context c) {
        return c.getSharedPreferences("pm_widget", Context.MODE_PRIVATE);
    }

    // Кэш для виджетов: приложение (WebView) кладёт сюда снимок данных из Firestore.
    static void save(Context c, String chats, String notes, String dls, String prog) {
        sp(c).edit().putString("chats", chats).putString("notes", notes).putString("dls", dls).putString("prog", prog).apply();
        refresh(c);
    }

    static String get(Context c, String k) {
        return sp(c).getString(k, "prog".equals(k) ? "{}" : "[]");
    }

    static JSONObject prog(Context c) {
        try { return new JSONObject(get(c, "prog")); } catch (Exception e) { return new JSONObject(); }
    }

    static void refresh(Context c) {
        AppWidgetManager m = AppWidgetManager.getInstance(c);
        Class<?>[] lists = new Class<?>[] { ChatsWidget.class, NotesWidget.class, Notes2Widget.class, Notes4Widget.class, DeadlineWidget.class };
        for (Class<?> k : lists) {
            int[] ids = m.getAppWidgetIds(new ComponentName(c, k));
            if (ids.length > 0) m.notifyAppWidgetViewDataChanged(ids, R.id.list);
        }
        Class<?>[] plain = new Class<?>[] { ProgressWidget.class, TodayWidget.class };
        for (Class<?> k : plain) {
            int[] ids = m.getAppWidgetIds(new ComponentName(c, k));
            if (ids.length > 0) {
                Intent i = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                i.setComponent(new ComponentName(c, k));
                i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                c.sendBroadcast(i);
            }
        }
    }

    // Фон виджета: цвет и непрозрачность хранятся отдельно для каждого виджета.
    static void applyBg(Context c, RemoteViews v, int id) {
        SharedPreferences p = sp(c);
        int rgb = p.getInt("bgc_" + id, 0x000000);
        int a = p.getInt("bga_" + id, 242);
        v.setInt(R.id.bg, "setColorFilter", 0xFF000000 | rgb);
        v.setInt(R.id.bg, "setImageAlpha", a);
    }

    // Кнопка ⚙ на виджете -> настройка фона
    static PendingIntent cfg(Context c, int id) {
        Intent i = new Intent(c, WidgetConfigActivity.class);
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        i.setData(Uri.parse("pmcfg://" + id));
        return PendingIntent.getActivity(c, 5000 + id, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    // Нажатие -> открыть приложение по ссылке pastmetki://<path>
    static PendingIntent link(Context c, String path, int rc) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("pastmetki://" + path));
        i.setClass(c, MainActivity.class);
        return PendingIntent.getActivity(c, rc, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    // Шаблон для элементов списка: ссылка дописывается в каждом элементе (fill-in intent)
    static PendingIntent template(Context c, int rc, Class<?> cls) {
        Intent t = new Intent(Intent.ACTION_VIEW);
        t.setClass(c, cls);
        return PendingIntent.getActivity(c, rc, t, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
    }

    static PendingIntent template(Context c, int rc) {
        return template(c, rc, MainActivity.class);
    }

    // Отметка заметки прямо с виджета: меняем кэш и запоминаем действие,
    // приложение применит его к данным в Firestore при следующем запуске.
    static void toggle(Context c, String id) {
        if (id == null) return;
        try {
            JSONArray a = new JSONArray(get(c, "notes"));
            boolean now = false;
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                if (id.equals(String.valueOf(o.optLong("i")))) {
                    now = o.optInt("d") == 0;
                    o.put("d", now ? 1 : 0);
                }
            }
            JSONArray ops = new JSONArray(sp(c).getString("ops", "[]"));
            JSONArray out = new JSONArray();
            for (int i = 0; i < ops.length(); i++) {
                JSONObject o = ops.getJSONObject(i);
                if (!id.equals(String.valueOf(o.optLong("id")))) out.put(o);
            }
            JSONObject op = new JSONObject();
            op.put("id", Long.parseLong(id));
            op.put("done", now);
            out.put(op);
            sp(c).edit().putString("notes", a.toString()).putString("ops", out.toString()).apply();
            refresh(c);
        } catch (Exception e) {
            // игнорируем: виджет просто не изменится
        }
    }

    static String takeOps(Context c) {
        String s = sp(c).getString("ops", "[]");
        sp(c).edit().putString("ops", "[]").apply();
        return s;
    }
}
