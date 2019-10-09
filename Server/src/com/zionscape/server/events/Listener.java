package com.zionscape.server.events;

import com.google.common.eventbus.Subscribe;
import com.zionscape.server.events.impl.*;

public interface Listener {

    @Subscribe
    default void onClickingButton(ClickingButtonEvent event) { }

    @Subscribe
    default void onDialogueContinue(DialogueContinueEvent event) { }

    @Subscribe
    default void onDialogueOption(DialogueOptionEvent event) { }

    @Subscribe
    default void onNpcClicked(NpcClickedEvent event) {}

    @Subscribe
    default void onPlayerLoggedIn(PlayerLoggedInEvent event) { }

    @Subscribe
    default void onObjectClicked(ClickObjectEvent event) {}

    @Subscribe
    default void onItemOnObject(ItemOnObjectEvent event) { }

    @Subscribe
    default void onPlayerProcess(PlayerProcessEvent event) {}

    @Subscribe
    default void onNpcDied(NpcDiedEvent event) { }

    @Subscribe
    default void onItemClicked(ItemClickedEvent event) { }

}