package com.atherys.quests;

import com.atherys.core.views.ViewManager;
import com.atherys.quests.dialog.Dialog;
import com.atherys.quests.listeners.EntityListener;
import com.atherys.quests.listeners.InventoryListener;
import com.atherys.quests.listeners.MasterEventListener;
import com.atherys.quests.managers.QuestManager;
import com.atherys.quests.quest.Quest;
import com.atherys.quests.quest.objective.KillEntityObjective;
import com.atherys.quests.quest.reward.SingleItemReward;
import com.atherys.quests.views.DialogView;
import com.atherys.quests.views.QuestView;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.Optional;

import static com.atherys.quests.AtherysQuests.*;

@Plugin( id = ID, name = NAME, description = DESCRIPTION, version = VERSION )
public class AtherysQuests {
    public static final String ID = "atherysquests";
    public static final String NAME = "A'therys Quests";
    public static final String DESCRIPTION = "A quest plugin written for the A'therys Horizons server.";
    public static final String VERSION = "1.0.0a";

    private static AtherysQuests instance = new AtherysQuests();
    private static boolean init = false;
    private static QuestsConfig config;

    private void init() {
        // TODO: Dump assets into config file
        // TODO: Load dialogs from files
        init = true;

        Sponge.getEventManager().registerListeners( this, new EntityListener() );
        Sponge.getEventManager().registerListeners( this, new InventoryListener() );
        Sponge.getEventManager().registerListeners( this, new MasterEventListener() );
    }

    private void start() {

        Quest dummyQuest = Quest.builder( "dummyQuest", 1 )
                .name( Text.of("This is a dummy quest.") )
                .description( Text.of( "The purpose of this quest is to demonstrate that quests work. So uhh.. kill 3 unnamed creepers and 4 unnamed zombies. You'll get a magical anvil at the end for it." ) )
                .add( KillEntityObjective.of( "creeper", 3 ) )
                .add( KillEntityObjective.of( "zombie", 4 ) )
                .add( new SingleItemReward( ItemStack.builder().itemType(ItemTypes.ANVIL).quantity(1).add( Keys.DISPLAY_NAME, Text.of("The Magical Anvil") ).build() ) )
                .build();

        QuestManager.getInstance().registerQuest( dummyQuest );
        //QuestManager.getInstance().unregisterQuest ( dummyQuest );

        try {
            config = new QuestsConfig("config/" + ID, "config.conf");
            config.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ViewManager.getInstance().registerView( Quest.class, QuestView.class );
        ViewManager.getInstance().registerView( Dialog.class, DialogView.class );
    }

    private void stop() {

    }

    @Listener
    public void onInit (GameInitializationEvent event) {
        init();
    }

    @Listener
    public void onStart (GameStartingServerEvent event) {
        if ( init ) start();
    }

    @Listener
    public void onStop (GameStoppingServerEvent event) {
        stop();
    }

    public static AtherysQuests getInstance() {
        return instance;
    }

    public static QuestsConfig getConfig() {
        return config;
    }

    public Optional<EconomyService> getEconomyService() {
        return Sponge.getServiceManager().provide( EconomyService.class );
    }
}
