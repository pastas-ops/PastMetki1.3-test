package com.pastmetki.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

public class NotesWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        for (int id : ids) {
            RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_list);
            v.setTextViewText(R.id.title, "📝 Мои заметки");
            Intent s = new Intent(c, NotesService.class);
            s.setData(Uri.parse(s.toUri(Intent.URI_INTENT_SCHEME)));
            v.setRemoteAdapter(R.id.list, s);
            v.setEmptyView(R.id.list, R.id.empty);
            v.setPendingIntentTemplate(R.id.list, PmData.template(c, 20));
            v.setOnClickPendingIntent(R.id.title, PmData.link(c, "home", 20 + 100));
            v.setOnClickPendingIntent(R.id.add, PmData.link(c, "new/note", 111));
            m.updateAppWidget(id, v);
        }
    }
}
