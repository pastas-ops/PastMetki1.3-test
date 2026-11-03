package com.pastmetki.app;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "WidgetBridge")
public class WidgetBridgePlugin extends Plugin {

    @PluginMethod
    public void setData(PluginCall call) {
        PmData.save(getContext(),
            call.getString("chats", "[]"),
            call.getString("notes", "[]"),
            call.getString("dls", "[]"));
        call.resolve();
    }

    @PluginMethod
    public void getPending(PluginCall call) {
        JSObject r = new JSObject();
        r.put("ops", "[]");
        call.resolve(r);
    }
}
