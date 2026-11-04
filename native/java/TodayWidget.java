package com.pastmetki.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import org.json.JSONObject;

// 1x2: процент за сегодня и плюсик для новой заметки
public class TodayWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        JSONObject o = PmData.prog(c);
        int p = o.optInt("p", 0), d = o.optInt("d", 0), n = o.optInt("n", 0);
        for (int id : ids) {
            RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_today);
            PmData.applyBg(c, v, id);
            v.setTextViewText(R.id.pct, p + "%");
            v.setTextViewText(R.id.ps, n > 0 ? d + " из " + n : "задач нет");
            v.setOnClickPendingIntent(R.id.plus, PmData.link(c, "new/note", 61));
            v.setOnClickPendingIntent(R.id.root, PmData.link(c, "home", 62));
            m.updateAppWidget(id, v);
        }
    }
}
