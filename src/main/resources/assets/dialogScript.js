function onDialogRegistration( function() {
    var DIALOG_ID = "test-dialog";

    var tree = dialogTree(
        DIALOG_ID,
        dialogNode(
            id: 0, // the id of the node. Root node id should be 0.
            requirements: null, // the requirements to access this node. If this is the root node, set to null.
            player: null, // how the player initiates the dialog node. If this is root, set to null, since NPC always speaks first.
            npc: [ // the array of text responses the NPC will produce.
                textOf(["Hello, friend!"])
            ],
            quest: null, // the quest to give to the player if this node is selected. If this is root node, probably best to set to null.
            responses: [ // the array of possible responses that the player can reply with
                dialogNode(
                    id: 1,
                    requirements: null,
                    player: textOf(["Greetings, Merchant! Have you any work for me today?"]),
                    npc: [
                        textOf(["Work? Oh, you bet."])
                        textOf(["This morning, as I was setting up my stall, a bunch of children went by and stole an entire stack of cocoa beans!"]),
                        textOf(["I reported this to the guards, of course, but they're either incapable or apathetic to the trouble that has befallen me."]),
                        textOf(["Do me a favor, and find those kids. I want my cocoa beans back!"])
                    ],
                    quest: null,
                    responses: [
                        dialogNode(
                            id: 3,
                            requirements: null,
                            player: textOf(["Oh, now that you mention it, I am a bit busy today, so I don't know if I'll find the time."]),
                            npc: [
                                textOf(["No worries, I'm sure the guards will get to it.. eventually..."])
                            ],
                            quest: null,
                            responses: null
                        ),
                        dialogNode(
                            id: 4,
                            requirements: null,
                            player: textOf(["Kids, huh? Well, how hard could it be. I'll certainly take a look around for you."]),
                            npc: [
                                textOf(["Oh, splendid! Thank you very much, I eagerly away to see what you find out."])
                            ],
                            quest: getQuestById("merchant-cocoa-beans-quest"),
                            responses: null
                        )
                    ]
                ),
                dialogNode(
                    id: 2,
                    requirements: null,
                    player: textOf(["Not now, Merchant."]),
                    npc: [
                        textOf(["Another time, perhaps."])
                    ],
                    quest: null,
                    responses: null // if these are no responses, set to null. Once a dialog node with null responses is reached, the dialog will end.
                )
            ]
        )
    );

    registerDialogTree(tree);
});