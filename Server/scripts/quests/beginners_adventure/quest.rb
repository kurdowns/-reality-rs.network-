require 'java'

java_import 'com.zionscape.server.model.npcs.NPCHandler'
java_import 'com.zionscape.server.model.content.minigames.quests.QuestInterfaceGenerator'
java_import 'com.zionscape.server.model.content.minigames.quests.QuestHandler'
java_import 'com.zionscape.server.model.Direction'

JACOB_NPC_ID = 7121
JYRI_NPC_ID = 3020

dialogue :jacob_other do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Hello adventurer!'
  continue :close_dialogue
end

dialogue :beginners_quest_1 do
  type :DIALOGUE_PLAYER
  text 'Why hello there!'
  continue :beginners_quest_2
end

dialogue :beginners_quest_2 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Hello adventurer and who might you be?'
  continue :beginners_quest_3
end

dialogue :beginners_quest_3 do
  type :DIALOGUE_PLAYER
  text 'My name is {player_name}, glad to mee you.'
  continue :beginners_quest_4
end

dialogue :beginners_quest_4 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Glad to meet you too {player_name}, my name is Jacob.'
  continue :beginners_quest_5
end

dialogue :beginners_quest_5 do
  type :DIALOGUE_PLAYER
  text 'Yes, in fact Christo the guide told me to come here and talk to you.'
  continue :beginners_quest_6
end

dialogue :beginners_quest_6 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Christo sent you?'
  continue :beginners_quest_7
end

dialogue :beginners_quest_7 do
  type :DIALOGUE_PLAYER
  text 'Yes.'
  continue :beginners_quest_8
end

dialogue :beginners_quest_8 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Well if you\'re a friend of Christo\'s you\'re a friend of mine!'
  continue :beginners_quest_9
end

dialogue :beginners_quest_9 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Welcome to the world of Draynor!'
  continue :beginners_quest_10
end

dialogue :beginners_quest_10 do
  type :DIALOGUE_PLAYER
  text 'Awesome! It\'s always nice to have a friend in this world.'
  continue :beginners_quest_11
end

dialogue :beginners_quest_11 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'So what can I do for you?'
  continue :beginners_quest_12
end

dialogue :beginners_quest_12 do
  type :DIALOGUE_PLAYER
  text 'Well I have been having somewhat of a hard time getting on my feet, and Christo said you would be the person to talk to help me out.'
  continue :beginners_quest_13
end

dialogue :beginners_quest_13 do
  type :DIALOGUE_PLAYER
  text 'So I was wondering if you have any work around this lovely farm that I could do?'
  continue :beginners_quest_14
end

dialogue :beginners_quest_14 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Well let me think...'
  continue :beginners_quest_15
end

dialogue :beginners_quest_15 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'As you can see I do own this chicken farm and cow farm to the east, I sell the eggs, cowhide, and some other miscellaneous stuff to the local merchants in Al-Kharid.'
  continue :beginners_quest_16
end

dialogue :beginners_quest_16 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Lately the merchants are buying more and more, almost to the point where I can\'t keep up, I could use an extra hand in collecting egs and cow hide and then delivering them to the merchants.'
  continue :beginners_quest_17
end

dialogue :beginners_quest_17 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'In which you could earn a small profit to get you on your feet.'
  continue :beginners_quest_18
end

dialogue :beginners_quest_18 do
  type :DIALOGUE_PLAYER
  text 'That sounds perfect! How many eggs and cowhide do they want?'
  continue :beginners_quest_19
end

dialogue :beginners_quest_19 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Well since the goblins have recently killed off a lot of farms to the south and the north that were un protected, demand has gone up.'
  continue :beginners_quest_20
end

dialogue :beginners_quest_20 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Collect 40 eggs and 40 cow hides then report back to me.'
  continue :beginners_quest_21
end

dialogue :beginners_quest_21 do
  type :DIALOGUE_PLAYER
  text 'On it!'
  continue_block do |player|
    player.get_data().quest_progress.put('beginners-adventure', 1)
    player.get_pa().send_frame126('@yel@A Beginners Adventure', 19162)
    send_dialogue(player, :close_dialogue)
  end
end

dialogue :beginners_quest_22 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Have you got the eggs and cowhide?'
  continue_block do |player|
    if player.get_items().player_has_item(1945, 40) and player.get_items().player_has_item(1740, 40)
      send_dialogue(player, :beginners_quest_24)
    else
      send_dialogue(player, :beginners_quest_23)
    end
  end
end

dialogue :beginners_quest_23 do
  type :DIALOGUE_PLAYER
  text 'No not yet, I will come back with them soon!'
  continue :close_dialogue
end

dialogue :beginners_quest_24 do
  type :DIALOGUE_PLAYER
  text 'I have got your supplies.'
  continue :beginners_quest_25
end

dialogue :beginners_quest_25 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Very good {player_name}, hand them over.'
  continue_block do |player|
    player.get_items().delete_item(1945, 40)
    player.get_items().delete_item(1740, 40)
    send_dialogue(player, :beginners_quest_26)
  end
end
dialogue :beginners_quest_26 do
  type :DIALOGUE_STATEMENT
  text 'You hand the eggs and cow hides over to Jacob.'
  continue :beginners_quest_27
end

dialogue :beginners_quest_27 do
  type :DIALOGUE_PLAYER
  text 'There you go'
  continue_block do |player|
    if player.get_data().quest_progress.get('beginners-adventure') == 2
      player.get_items().add_item(7959, 1)
      send_dialogue(player, :beginners_quest_33)
    else
      send_dialogue(player, :beginners_quest_28)
    end
  end
end

dialogue :beginners_quest_28 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Alright I will put these together in a box for you, and you can deliver them to the merchants in Al-Kharid!'
  continue :beginners_quest_29
end

dialogue :beginners_quest_29 do
  type :DIALOGUE_PLAYER
  text 'Where do I find these merchants?'
  continue :beginners_quest_30
end

dialogue :beginners_quest_30 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'His name is "Jyri" you can find him in the north west corner of the Al-Kharid palace.'
  continue :beginners_quest_31
end

dialogue :beginners_quest_31 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'I must warn you, he\'s a fast paced guy, and really serious, so just do what he says!'
  continue :beginners_quest_32
end

dialogue :beginners_quest_32 do
  type :DIALOGUE_PLAYER
  text 'Will do!'
  continue_block do |player|
    player.get_data().quest_progress.put('beginners-adventure', 2)
    player.get_items().add_item(7959, 1)
    send_dialogue(player, :beginners_quest_33)
  end
end

dialogue :beginners_quest_33 do
  type :DIALOGUE_STATEMENT
  text 'Jacob hands you a Box.'
  continue :close_dialogue
end

dialogue :beginners_quest_34 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'How can I help you?'
  continue :beginners_quest_35
end

dialogue :beginners_quest_35 do
  type :DIALOGUE_OPTION
  options ['I have lost the box.', 'I cannot find Jyri']
  continue_block do |player, option|
    case option
      when 1
        send_dialogue(player, :beginners_quest_36)
      when 2
        send_dialogue(player, :beginners_quest_39)
    end
  end
end

dialogue :beginners_quest_36 do
  type :DIALOGUE_PLAYER
  text 'I have lost the box.'
  continue :beginners_quest_37
end

dialogue :beginners_quest_37 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'That\'s a shame.. You are going to have to re-pick the eggs and cowhide!'
  continue :beginners_quest_38
end

dialogue :beginners_quest_38 do
  type :DIALOGUE_PLAYER
  text 'Darn me and my stupidity!'
  continue :beginners_quest_22
end

dialogue :beginners_quest_39 do
  type :DIALOGUE_PLAYER
  text 'I cant seem to find Jyri!'
  continue :beginners_quest_40
end

dialogue :beginners_quest_40 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'He is in the Al-Kharid palace, go into the palace go in the far west room, then go to the north corner, he will be right there in a white outfit and red hat!'
  continue :beginners_quest_41
end

dialogue :beginners_quest_41 do
  type :DIALOGUE_PLAYER
  text 'Alright I will look again'
  continue :close_dialogue
end

dialogue :beginners_quest_42 do
  type :DIALOGUE_PLAYER
  text 'Hello!'
  continue :beginners_quest_43
end

dialogue :beginners_quest_43 do
  type :DIALOGUE_NPC
  npc JYRI_NPC_ID
  text 'Who are you?'
  continue :beginners_quest_44
end

dialogue :beginners_quest_44 do
  type :DIALOGUE_PLAYER
  text 'Jacob sent me, I have some eggs and cow hide for you.'
  continue :beginners_quest_45
end

dialogue :beginners_quest_45 do
  type :DIALOGUE_NPC
  npc JYRI_NPC_ID
  text 'Hand it over'
  continue_block do |player|
    if player.get_items().player_has_item(7959, 1)
      player.get_items().delete_item(7959, 1)
      player.get_data().quest_progress.put('beginners-adventure', 3)
      send_dialogue(player, :beginners_quest_48)
    else
      send_dialogue(player, :beginners_quest_46)
    end
  end
end

dialogue :beginners_quest_46 do
  type :DIALOGUE_PLAYER
  text 'Oops! It looks like I forgot it, so sorry be right back.'
  continue :beginners_quest_47
end

dialogue :beginners_quest_47 do
  type :DIALOGUE_NPC
  npc JYRI_NPC_ID
  text 'Hurry! I don\'t have time for this!'
  continue :close_dialogue
end

dialogue :beginners_quest_48 do
  type :DIALOGUE_STATEMENT
  text 'You hand the box over to Jyri.'
  continue :beginners_quest_49
end

dialogue :beginners_quest_49 do
  type :DIALOGUE_NPC
  npc JYRI_NPC_ID
  text 'Ok, tell Jacob to expect the usual payment.'
  continue :beginners_quest_50
end

dialogue :beginners_quest_50 do
  type :DIALOGUE_NPC
  npc JYRI_NPC_ID
  text 'Now leave!'
  continue :beginners_quest_51
end

dialogue :beginners_quest_51 do
  type :DIALOGUE_PLAYER
  text 'Im gone!'
  continue :close_dialogue
end

dialogue :jyri_normal_1 do
  type :DIALOGUE_NPC
  npc JYRI_NPC_ID
  text 'Do you have anything for me?'
  continue :jyri_normal_2
end

dialogue :jyri_normal_2 do
  type :DIALOGUE_PLAYER
  text 'Um... No.'
  continue :jyri_normal_3
end

dialogue :jyri_normal_3 do
  type :DIALOGUE_NPC
  npc JYRI_NPC_ID
  text 'Then leave!'
  continue :close_dialogue
end

dialogue :beginners_quest_52 do
  type :DIALOGUE_PLAYER
  text 'I have sold the eggs and cow hide! Jyri said to expect the usual payment.'
  continue :beginners_quest_53
end

dialogue :beginners_quest_53 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Perfect, thank you {player_name}.'
  continue :beginners_quest_54
end

dialogue :beginners_quest_54 do
  type :DIALOGUE_PLAYER
  text 'No problem, do you happen to have any other jobs in mind?'
  continue :beginners_quest_55
end

dialogue :beginners_quest_55 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Not at the moment, no sorry.'
  continue :beginners_quest_56
end

dialogue :beginners_quest_56 do
  type :DIALOGUE_PLAYER
  text 'No problem.'
  continue :beginners_quest_57
end

dialogue :beginners_quest_57 do
  type :DIALOGUE_PLAYER
  text 'So Jacob, is there anything else I should know about this land?'
  continue :beginners_quest_58
end

dialogue :beginners_quest_58 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'I\'m glad you asked {player_name}, there is some history that I feel everyone should know about.'
  continue :beginners_quest_59
end

dialogue :beginners_quest_59 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'From the dwarves, to the elves, the wizards, the men, the gnomes, the evil, and everything in between.'
  continue :beginners_quest_60
end

dialogue :beginners_quest_60 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Whoops, it seems ive rambled on there, I tend to do that when discussing the past. One of my favorite things to do is study history, as I always like to say, knowing the past helps you navigate the future.'
  continue :beginners_quest_61
end

dialogue :beginners_quest_61 do
  type :DIALOGUE_PLAYER
  text 'That\'s no problem, actually I\'d love it if you told me more'
  continue :beginners_quest_62
end

dialogue :beginners_quest_62 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Where do I even begin... Lets start with the races of Draynor.'
  continue :beginners_quest_63
end

dialogue :beginners_quest_63 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'The world of Draynor consists of 5 main races; Dwarves, Elves, Wizards, Gnomes, and Men. The dwarves are lead by Nulodion, They\'re a great race who keep to themselves mainly...'
  continue :beginners_quest_64
end

dialogue :beginners_quest_64 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'and have a great fascination and love for the earth, what it produces underground, from mining ore to building weapons and buildings, and are quite accomplished in the architecture field.'
  continue :beginners_quest_65
end

dialogue :beginners_quest_65 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'The next races, Elves are lead by Eldrin. They are a close knit race that keeps to themselves far out in the western land, a race which prides themselves in their skill with archery and beauty...'
  continue :beginners_quest_66
end

dialogue :beginners_quest_66 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'A very sophisticated, delicate, pristine and private race.'
  continue :beginners_quest_67
end

dialogue :beginners_quest_67 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Then come the Wizards, lead by Dalamar, they are a very wild, or insane race as some would like to call it. Who dabble in magic and sorcery.'
  continue :beginners_quest_68
end

dialogue :beginners_quest_68 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Although very powerful, they share the views of Men and keep a very good relationship.'
  continue :beginners_quest_69
end

dialogue :beginners_quest_69 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'then comes the gnomes, lead by Grilvro. They are a very technological race, who were the first ones to actually get themselves to fly...'
  continue :beginners_quest_70
end

dialogue :beginners_quest_70 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'In what they call their "Gliders". A very humble, and meek race that for the most part keeps to themselves, but are very defensive.'
  continue :beginners_quest_71
end

dialogue :beginners_quest_71 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Last but not least, men. Headed by Warren. We are a very civilized, simple, and intelligent race, full of people eager to learn, help, and enjoy life.'
  continue :beginners_quest_72
end

dialogue :beginners_quest_72 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Although I don\'t like to brag we are the dominant ones on the social food chain, controlling most of the land and resources...'
  continue :beginners_quest_73
end

dialogue :beginners_quest_73 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'We are a very wealthy race and very militarized, keeping a good relationship with the other races is something we pride ourselves on.'
  continue :beginners_quest_74
end

dialogue :beginners_quest_74 do
  type :DIALOGUE_PLAYER
  text 'Wow, that was pretty interesting, didn\'t know each race had such a culture behind them.'
  continue :beginners_quest_75
end

dialogue :beginners_quest_75 do
  type :DIALOGUE_PLAYER
  text 'I appreciate you telling me that, Im sure it will come in handy some time'
  continue :beginners_quest_76
end

dialogue :beginners_quest_76 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Oh {player_name}, I could go on for days about the history of Draynor, but I believe it will be better for you to discover it on your own.'
  continue :beginners_quest_77
end

dialogue :beginners_quest_77 do
  type :DIALOGUE_PLAYER
  text 'I\'m much looking for to it, by the way, how do you know about all of this anyways?'
  continue :beginners_quest_78
end

dialogue :beginners_quest_78 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'You have much to learn about me {player_name}. We can save that story for another day.'
  continue_block do |player|

    player.get_data().quest_progress.put('beginners-adventure', 4)
    player.get_pa().send_frame126('@gre@A Beginners Adventure', 19162)

    player.questPoints += 1
    player.get_items().add_item(995, 10000000)

    QuestHandler.sendQuestPoints(player)
    QuestHandler.sendQuestRewardInterface(player, 'A beginners Adventure', '+1 Quest Point', '10M GP')

    player.remove_attribute('dialogue')
  end
end

dialogue :beginners_quest_79 do
  type :DIALOGUE_PLAYER
  text 'Hey Jacob!'
  continue :beginners_quest_80
end

dialogue :beginners_quest_80 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Hey there {player_name}.'
  continue :beginners_quest_81
end

dialogue :beginners_quest_81 do
  type :DIALOGUE_PLAYER
  text 'Got anything for me to do on the farm?'
  continue :beginners_quest_82
end

dialogue :beginners_quest_82 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'I\'m afraid not.'
  continue :beginners_quest_83
end

dialogue :beginners_quest_84 do
  type :DIALOGUE_PLAYER
  text 'That\'s alright, I\'ll see you around.'
  continue :beginners_quest_85
end

dialogue :beginners_quest_85 do
  type :DIALOGUE_NPC
  npc JACOB_NPC_ID
  text 'Good day.'
  continue :close_dialogue
end

on :message, :button_clicked do |ctx, player, message|
  if message.id == 74218
    questGenerator = QuestInterfaceGenerator.new('A Beginners Adventure')
    status = player.get_data().quest_progress.get('beginners-adventure')

    questGenerator.add('To begin my adventure I must talk to Jacob at the Lumbridge chicken farm.', !status.nil?)
    if !status.nil?
      if status >= 1
        questGenerator.add('You must collect 40 cowhides and 40 eggs, then return to Jacob.', status > 1)
      end
      if status >= 2
        questGenerator.add('I must find Jyri, who is located in the Al-Kharid palace and hand him the supplies.', status > 2)
      end
      if status >= 3
        questGenerator.add('I should report back to Jacob that I have sold the supplies.', status > 3)
      end
    end

    questGenerator.write_quest(player)
    ctx.break_handler_chain()
  end
end

on :message, :first_npc_action do |ctx, player, message|
  # Jacob
  if message.npc.type == JACOB_NPC_ID
    status = player.get_data().quest_progress.get('beginners-adventure')
    case status
      when 1 then send_dialogue(player, :beginners_quest_22)
      when 2 then send_dialogue(player, :beginners_quest_34)
      when 3 then send_dialogue(player, :beginners_quest_52)
      when 4 then send_dialogue(player, :beginners_quest_79)
      when nil then send_dialogue(player, :beginners_quest_1)
      else send_dialogue(player, :jacob_other)
    end
    ctx.break_handler_chain()
  end
  # Jyri
  if message.npc.type == JYRI_NPC_ID
    status = player.get_data().quest_progress.get('beginners-adventure')
    case status
      when 2 then send_dialogue(player, :beginners_quest_42)
      else send_dialogue(player, :jyri_normal_1)
    end
  end
end

on :message, :login  do |ctx, player, message|
  status = player.get_data().quest_progress.get('beginners-adventure')
  case status
    when 1..3 then player.get_pa().send_frame126('@yel@A Beginners Adventure', 19162)
    when 4 then player.get_pa().send_frame126('@gre@A Beginners Adventure', 19162)
    else player.get_pa().send_frame126('@red@A Beginners Adventure', 19162)
  end
end

# jacob spawn
NPCHandler.newNPC(JACOB_NPC_ID, 3228, 3288, 0, 0, 0, 0, 0, 0, Direction::NONE)

# jyri spawn
NPCHandler.newNPC(JYRI_NPC_ID, 3282, 3176, 0, 0, 0, 0, 0, 0, Direction::NONE)