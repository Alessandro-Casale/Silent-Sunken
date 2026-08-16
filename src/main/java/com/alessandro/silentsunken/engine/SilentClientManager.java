package com.alessandro.silentsunken.engine;

import com.alessandro.silentsunken.engine.client.ClientCameraShakeManager;
import com.alessandro.silentsunken.engine.client.ClientOutlineManager;

public class SilentClientManager {
    public static final ClientOutlineManager OUTLINE_INSTANCE = new ClientOutlineManager();
    public static final ClientCameraShakeManager CAMERA_SHAKE_INSTANCE = new ClientCameraShakeManager();
}
