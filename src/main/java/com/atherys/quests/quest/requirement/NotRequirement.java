package com.atherys.quests.quest.requirement;

import com.atherys.quests.quester.Quester;
import org.spongepowered.api.text.Text;

public class NotRequirement implements Requirement {

    private Requirement requirement;


    public NotRequirement ( Requirement requirement ) {
        this.requirement = requirement;
    }

    @Override
    public boolean check( Quester quester ) {
        return !requirement.check( quester );
    }

    @Override
    public Requirement copy() {
        return new NotRequirement( requirement.copy() );
    }

    @Override
    public Text toText() {
        return Text.of( "NOT ", requirement.toText() );
    }
}
