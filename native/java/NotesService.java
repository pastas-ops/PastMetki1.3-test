package com.pastmetki.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import org.json.JSONArray;
import org.json.JSONObject;

public class NotesService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new Factory(getApplicationContext());
    }

    static class Factory implements RemoteViewsFactory {
        final Context c;
        JSONArray a = new JSONArray();

        Factory(Context c) { this.c = c; }

        @Override public void onCreate() {}

        @Override public void onDataSetChanged() {
            try { a = new JSONArray(PmData.get(c, "notes")); } catch (Exception e) { a = new JSONArray(); }
        }

        @Override public void onDestroy() {}

        @Override public int getCount() { return a.length(); }

        @Override
        public RemoteViews getViewAt(int p) {
            RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_item_note);
            JSONObject o = a.optJSONObject(p);
            if (o == null) return v;
            v.setTextViewText(R.id.t, o.optString("t"));
            String s = o.optString("s");
            v.setTextViewText(R.id.s, s);
            v.setViewVisibility(R.id.s, s.length() > 0 ? View.VISIBLE : View.GONE);
            Intent f = new Intent();
            f.setData(Uri.parse("pastmetki://note/" + o.optLong("i")));
            v.setOnClickFillInIntent(R.id.row, f);
            return v;
        }

        @Override public RemoteViews getLoadingView() { return null; }
        @Override public int getViewTypeCount() { return 1; }
        @Override public long getItemId(int p) { return p; }
        @Override public boolean hasStableIds() { return false; }
    }
}
