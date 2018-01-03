package com.atherys.quests.quest.requirement;

import com.atherys.quests.base.Prototype;
import com.atherys.quests.quester.Quester;
import com.atherys.quests.views.TextViewable;

public interface Requirement extends Prototype<Requirement>, TextViewable {

    boolean check ( Quester quester );

}
