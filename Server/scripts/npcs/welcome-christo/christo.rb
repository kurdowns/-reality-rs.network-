java_import 'com.zionscape.server.model.npcs.NPCHelper'
java_import 'com.zionscape.server.model.content.achievements.Achievements'

CHRISTO_ID = 945
SIR_TIFFY_ID = 11512
MISS_CHEEVERS_ID = 2288
SIGMUND_ID = 1282
BOB_ID = 605
KURADAL_ID = 9085

dialogue :christo_1 do
  type :DIALOGUE_OPTION
  options ['Start the 30 second tutorial.', 'Skip the 30 second tutorial.']
  continue_block do |player, option|
    case option
      when 1
        send_dialogue(player, :christo_2)
      when 2
        send_dialogue(player, :christo_skip_1)
    end
  end
end

dialogue :christo_2 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Hello {player_name}! Welcome to the world of Draynor, my name is Christo.'
  continue :christo_3
end

dialogue :christo_3 do
  type :DIALOGUE_PLAYER
  text 'Hello Christo!'
  continue :christo_4
end

dialogue :christo_4 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'I think I better show you around, there is much you need to know.'
  continue_block do |player|
    player.get_pa().create_player_hints(1, NPCHelper.get_npc_index(SIR_TIFFY_ID))
    player.get_pa().movePlayer(3272, 3162, 0)
    player.setDoingTutorial(true)
    send_dialogue(player, :christo_5)
  end
end

dialogue :christo_5 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is Sir Tiffy, you can do activities in game, that you can than use to unlock special player titles.'
  continue_block do |player|
    player.get_pa().create_player_hints(1, NPCHelper.get_npc_index(MISS_CHEEVERS_ID))
    player.get_pa().movePlayer(3271, 3172, 0)
    send_dialogue(player, :christo_6)
  end
end

dialogue :christo_6 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is Miss Cheevers, she is where you redeem your vote points, to vote simply type ::vote and follow the instructions, you can do it once every 12 hours for amazing rewards.'
  continue_block do |player|
    player.get_pa().create_player_hints(1, NPCHelper.get_npc_index(SIGMUND_ID))
    player.get_pa().movePlayer(3260, 3167, 0)
    send_dialogue(player, :christo_7)
  end
end

dialogue :christo_7 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is the thieving area, collect items from the stalls and sell them back to Sigmund for a profit, the higher your thieving level the more gold you can collect.'
  continue_block do |player|
    player.get_pa().create_player_hints(1, NPCHelper.get_npc_index(BOB_ID))
    player.get_pa().movePlayer(3272, 3172, 0)
    send_dialogue(player, :christo_8)
  end
end

dialogue :christo_8 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is Sir Elite, talk to him to change your game mode to elite, which is 3.5x harder than normal mode.'
  continue_block do |player|
    player.get_pa().create_player_hints(1, 0) ## cancel
    player.get_pa().movePlayer(3278, 3182, 0)
    send_dialogue(player, :christo_30)
  end
end

dialogue :christo_30 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is grandpa jack, you can exchange stardust minned from shooting stars for skilling supplies with perks!'
  continue_block do |player|
    player.get_pa().create_player_hints(1, 0) ## cancel
    player.get_pa().movePlayer(3274, 3172, 0)
    send_dialogue(player, :christo_31)
  end
end

dialogue :christo_31 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is the wilderness chest, you can obtain wilderness keys from killing players in the wild for BIG rewards!'
  continue_block do |player|
    player.get_pa().create_player_hints(1, 0) ## cancel
    player.get_pa().movePlayer(3274, 3162, 0)
    send_dialogue(player, :christo_32)
  end
end

dialogue :christo_32 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is the master cape vendor, obtain 200m in any skill and show it off with your very own master cape!'
  continue_block do |player|
    player.get_pa().create_player_hints(1, 0) ## cancel
    player.get_pa().movePlayer(3269, 3158, 0)
    send_dialogue(player, :christo_9)
  end
end

dialogue :christo_9 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Here you can use the alters to either recharge your prayer points, switch spell books and switch prayer books.'
  continue_block do |player|
    player.get_pa().movePlayer(3273, 3181, 0)
    send_dialogue(player, :christo_10)
  end
end

dialogue :christo_10 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is where the shops are located, all your basic gear to get you started on your journey can be found here.'
  continue_block do |player|
    player.get_pa().create_player_hints(1, NPCHelper.get_npc_index(KURADAL_ID))
    player.get_pa().movePlayer(3276, 3156, 0)
    send_dialogue(player, :christo_11)
  end
end

dialogue :christo_11 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'This is the Slayer master Kuradal, talk to her to receive a Slayer assignment for monsters to slay.'
  continue_block do |player|
    player.get_pa().create_player_hints(1, 0)
    player.get_pa().open_tab(16)
    player.get_pa().movePlayer(3277, 3172, 0)
    send_dialogue(player, :christo_12)
  end
end

dialogue :christo_12 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'In this information tab, which has been selected for you, can be found at the bottom right hand corner of the game frame marked as an orange "I."'
  continue :christo_13
end

dialogue :christo_13 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'You can click the "Teleports" to find different teleports to help you navigate around the map easier.'
  continue :christo_14
end

dialogue :christo_14 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'You can also find useful links that will help you with your in-game experience.'
  continue :christo_15
end

dialogue :christo_15 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'There you have it. Any questions?'
  continue :christo_16
end

dialogue :christo_16 do
  type :DIALOGUE_PLAYER
  text 'Where do I begin training?'
  continue :christo_17
end

dialogue :christo_17 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Click the information tab button, click "Teleports", then select the "Beginners area" teleport.'
  continue :christo_18
end

dialogue :christo_18 do
  type :DIALOGUE_PLAYER
  text 'How do I start making money?'
  continue :christo_19
end

dialogue :christo_19 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Hmm... Here are a few ideas. The first idea is obvious, use the thieving stalls.'
  continue :christo_20
end

dialogue :christo_20 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Once you have a decent combat level, go to the wilderness and kill Green dragons or Blue Dragons in the Tavelry dungeon.'
  continue :christo_21
end

dialogue :christo_21 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Another would be talk to Kuradel and start Slayer. After you get your Slayer level up you can start killing higher level monsters for great drops.'
  continue :christo_22
end

dialogue :christo_22 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'But for the greatest money making guide simply type ::starter.'
  continue :christo_23
end

dialogue :christo_23 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'You can vote to receive some great gear right away, by typing ::vote.'
  continue :christo_24
end

dialogue :christo_24 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'You can donate to receive the best gear instantly, by typing ::donate.'
  continue :christo_25
end

dialogue :christo_25 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'There are so many ways to get rich, just takes some patience to get started.'
  continue :christo_26
end

dialogue :christo_26 do
  type :DIALOGUE_PLAYER
  text 'Wow! Thanks so much for the help! Anything else I should know?'
  continue :christo_27
end

dialogue :christo_27 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Yes, type ::starter & head on over to the Lumbridge farm, and find Jacob. Tell him Christo sent you.'
  continue :christo_28
end

dialogue :christo_28 do
  type :DIALOGUE_PLAYER
  text 'Thank you. Will I be seeing you around?'
  continue :christo_29
end

dialogue :christo_29 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'I have a feeling we will see each other again.'
  continue_block do |player|
      player.get_pa().movePlayer(3277, 3172, 0)
    Achievements.progressMade(player, Achievements::Types::COMPLETE_TUTORIAL);
    if !player.get_data().completedTutorial
      player.get_pa().add_starter()
      player.get_data().completedTutorial = true
    end

    player.remove_attribute('doing_tutorial');

    send_dialogue(player, :close_dialogue)
  end
end

dialogue :christo_skip_1 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Are you sure you want to skip the tutorial?'
  continue :christo_skip_2
end

dialogue :christo_skip_2 do
  type :DIALOGUE_OPTION
  options ['Yes.', 'No.']
  continue_block do |player, option|
    case option
      when 1
        send_dialogue(player, :christo_skip_3)
      when 2
        send_dialogue(player, :christo_2)
    end
  end
end

dialogue :christo_skip_3 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'You are now on your own! If you would like any help ever, feel free to talk to me.'
  continue :christo_skip_5
end

dialogue :christo_skip_5 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'IMPORTANT: To teleport around the world, just click the worldmap icon!'
  continue :christo_skip_4
end

dialogue :christo_skip_4 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'If you need some help, type ::starter & head on over to the Lumbridge farm, where you\'ll find a man named Jacob.'
  continue_block do |player|
    if !player.get_data().completedTutorial
        player.get_pa().add_starter()
        player.get_data().completedTutorial = true
    end
    send_dialogue(player, :close_dialogue)
  end
end


dialogue :christo_click_1 do
  type :DIALOGUE_NPC
  npc CHRISTO_ID
  text 'Hello, would you like to do the welcome tutorial again?'
  continue :christo_click_2
end

dialogue :christo_click_2 do
  type :DIALOGUE_OPTION
  options ['Yes.', 'No.']
  continue_block do |player, option|
    case option
      when 1
        player.set_attribute('doing_tutorial', true)
        send_dialogue(player, :christo_4)
      when 2
        send_dialogue(player, :close_dialogue)
    end
  end
end

## npc clicking
on :message, :first_npc_action do |ctx, player, message|
  if message.npc.type == CHRISTO_ID
    send_dialogue(player, :christo_click_1)
    ctx.break_handler_chain()
  end
end

## button clicking
on :message, :button_clicked do |ctx, player, message|

    if message.id == 75109
        player.get_pa().send_config(728, 2);
        player.set_attribute("mode", 1);
    end
    if message.id == 75108
        player.get_pa().send_config(728, 1);
        player.set_attribute("mode", 2);
    end
    if message.id == 75110
        player.get_pa().send_config(729, 1);
        player.set_attribute("ironman", 1);
    end
    if message.id == 75111
        player.get_pa().send_config(729, 2);
        player.set_attribute("ironman", 2);
    end
    if message.id == 75112
        player.get_pa().send_config(729, 3);
        player.set_attribute("ironman", 3);
    end

    if message.id == 75121 && player.openInterfaceId == 19302
        player.openInterfaceId = 0;
        player.get_pa().save_game_mode()
        send_dialogue(player, :christo_1)
    end

end

## logging in
on :message, :login do |ctx, player, message|
  if !player.get_data().completedTutorial

    player.get_pa().send_config(728, 2);
    player.set_attribute("mode", 1);

    player.get_pa().send_config(729, 1);
    player.set_attribute("ironman", 1);

    player.get_pa().show_interface(19302)
    ##send_dialogue(player, :christo_1)
  end
end