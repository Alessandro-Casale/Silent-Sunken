package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.data.HistorianProgress;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SilentAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SilentSunken.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<HistorianProgress>> HISTORIAN_PROGRESS = ATTACHMENT_TYPES.register("historian_progress", () -> AttachmentType.serializable(HistorianProgress::new).build());
}
