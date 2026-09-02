package com.alessandro.silentsunken.engine;

import com.alessandro.silentsunken.engine.server.OutlineManager;
import com.alessandro.silentsunken.engine.server.SoundListenerManager;
import com.alessandro.silentsunken.engine.server.SearchManager;

public class SilentManager {
    public static final SearchManager SEARCH_INSTANCE = new SearchManager();
    public static final OutlineManager OUTLINE_INSTANCE = new OutlineManager();
    public static final SoundListenerManager SOUND_LISTENER_INSTANCE = new SoundListenerManager();
}
