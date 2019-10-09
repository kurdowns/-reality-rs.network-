require 'java'

java_import 'com.zionscape.server.world.shops.Shops'

KING_BOLREN_NPC_ID = 469

dialogue :halloween_1 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Help me!'
  continue :halloween_2
end

dialogue :halloween_2 do
  type :DIALOGUE_PLAYER
  text 'What is it?!'
  continue :halloween_3
end

dialogue :halloween_3 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Well I was taking a walk through my own city of lumbridge, when all of a sudden. . . .'
  continue :halloween_4
end

dialogue :halloween_4 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'BOOM'
  continue :halloween_5
end

dialogue :halloween_5 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'I was hit over the head by some heavy object that knocked me to my feet, i then saw a group of skeletons standing over me!'
  continue :halloween_6
end

dialogue :halloween_6 do
  type :DIALOGUE_PLAYER
  text 'Wow that sounds like a nightmare!'
  continue :halloween_7
end

dialogue :halloween_7 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'It doesn\'t end there... They then started to go through my belongings try to steal anything of value. They stripped me of everything that I had!'
  continue :halloween_8
end

dialogue :halloween_8 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Fortunately nothing of it had any real value to me, except one thing'
  continue :halloween_9
end

dialogue :halloween_9 do
  type :DIALOGUE_PLAYER
  text 'What is it?'
  continue :halloween_10
end

dialogue :halloween_10 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'It\'s the mirror shield of lumbrdige! It has been in my familly for over 100 years! I won\'t be able to return to my city ever again without it. That is why I\'m hiding out here in this desert wasteland.'
  continue :halloween_11
end

dialogue :halloween_11 do
  type :DIALOGUE_PLAYER
  text 'HEY! This desert wasteland that you speak of is my home. And it\'s the best home I\'ve ever seen.'
  continue :halloween_12
end

dialogue :halloween_12 do
  type :DIALOGUE_PLAYER
  text 'Much better than that god awful edgeville home I use to live at. . . '
  continue :halloween_13
end

dialogue :halloween_13 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Well that is beside the point, all I know is that I NEED that shield back. But at this point I\'m way to weak to try and fight a pack of sekeletons to get it back.'
  continue :halloween_14
end

dialogue :halloween_14 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Hey I have an idea, how about a strong young soldier such as yourself get it back for me?'
  continue :halloween_15
end

dialogue :halloween_15 do
  type :DIALOGUE_PLAYER
  text 'Well that depends sir, what would be in it for me?'
  continue :halloween_16
end

dialogue :halloween_16 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Well it just so happens that I obtain certian items that are perfect for this time of year, and ones that you most likely will never see again in this land'
  continue :halloween_17
end

dialogue :halloween_17 do
  type :DIALOGUE_PLAYER
  text 'Hmmmm that sounds tempting, I can get the items for free if I get you your shield back?'
  continue :halloween_18
end

dialogue :halloween_18 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Ha Ha Ha! These items are much to rare to be given out for free, but if you can get my shield back I will certianly offer you a very nice deal on them'
  continue :halloween_19
end

dialogue :halloween_19 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'So are you up to the challenge?'
  continue :halloween_20
end

dialogue :halloween_20 do
  type :DIALOGUE_OPTION
  options ['Yes I\'m up to the challenge', 'No I\'d rather not']
  continue_block do |player, option|
    case option
      when 1
        player.get_data().quest_progress.put('halloween', 1)
        send_dialogue(player, :halloween_22)
      when 2
        send_dialogue(player, :halloween_21)
    end
  end
end

dialogue :halloween_21 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Fine! Then get out of my sight, I have no use for you'
  continue :close_dialogue
end

dialogue :halloween_22 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Great! Head over to the lumbrdige swamps and kill as many skeletons as you can until you can find one that has my shield!'
  continue :halloween_23
end

dialogue :halloween_23 do
  type :DIALOGUE_PLAYER
  text 'Will do! I\'ll report back here shortly'
  continue :close_dialogue
end

dialogue :halloween_24 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Have you found my shield yet?'
  continue_block do |player|
    if player.get_items().player_has_item(4156, 1)
        send_dialogue(player, :halloween_27)
    else
        send_dialogue(player, :halloween_25)
    end
  end
end

dialogue :halloween_25 do
  type :DIALOGUE_PLAYER
  text 'Nope still looking!'
  continue :halloween_26
end

dialogue :halloween_26 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Hurry hurry! We don\'t have much time left'
  continue :close_dialogue
end

dialogue :halloween_27 do
  type :DIALOGUE_PLAYER
  text 'Yes! It wasn\'t the easiest task but I have found it'
  continue_block do |player|
    player.get_items().delete_item(4156, 1)
    player.get_data().quest_progress.put('halloween', 2)
    send_dialogue(player, :halloween_28)
  end
end

dialogue :halloween_28 do
  type :DIALOGUE_STATEMENT
  text 'You hand over the shield to King Bolren'
  continue :halloween_29
end

dialogue :halloween_29 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Thank you so much adventurer! As a thank you I will let you purchase some of my gear.'
  continue :halloween_30
end

dialogue :halloween_30 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Remember though, you will most likely never get a chance to purchase these items again. So just keep that in mind friend'
  continue :halloween_31
end

dialogue :halloween_31 do
  type :DIALOGUE_PLAYER
  text 'Appreciate it Sir!'
  continue :close_dialogue
end

dialogue :halloween_32 do
  type :DIALOGUE_NPC
  npc KING_BOLREN_NPC_ID
  text 'Would you like to take a look at my shop now?'
  continue :halloween_33
end

dialogue :halloween_33 do
  type :DIALOGUE_OPTION
  options ['Yes', 'No']
  continue_block do |player, option|
    case option
      when 1
        Shops.open(player, 30)
      when 2
        send_dialogue(player, :close_dialogue)
    end
  end
end


on :message, :first_npc_action do |ctx, player, message|
  # King Bolren
  if message.npc.type == KING_BOLREN_NPC_ID
    status = player.get_data().quest_progress.get('halloween')
    case status
      when 1 then send_dialogue(player, :halloween_24)
      when 2 then send_dialogue(player, :halloween_32)
      when nil then send_dialogue(player, :halloween_1)
      #else send_dialogue(player, :jacob_other)
    end
    ctx.break_handler_chain()
  end
end