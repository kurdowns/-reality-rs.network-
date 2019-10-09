BARTENDER_ID = 731

dialogue :bartender_welcome do
  type :DIALOGUE_NPC
  npc BARTENDER_ID
  text 'What can I do yer for?'
  continue :bartender_options
end

dialogue :bartender_options do
  type :DIALOGUE_OPTION
  options ['A glass of your finest ale please.', 'Nothing.']
  continue_block do |player, option|
    case option
      when 1
        send_dialogue(player, :bartender_finest_ale)
      when 2
        send_dialogue(player, :close_dialogue)
    end
  end
end

dialogue :bartender_finest_ale do
  type :DIALOGUE_PLAYER
  text 'A glass of your finest ale please.'
  continue :bartender_no_problem
end

dialogue :bartender_no_problem do
  type :DIALOGUE_NPC
  npc BARTENDER_ID
  text 'No problemo. That\'ll be 2 coins.'
  continue_block do |player|
    if player.get_items().player_has_item(995, 2)
      player.get_items().delete_item(995, 2)
      player.get_items().add_item(1917, 1)
      player.send_message('You buy a pint of beer.')
      send_dialogue(player, :close_dialogue)
    else
      send_dialogue(player, :bartender_not_enough_cash)
    end
  end
end

dialogue :bartender_not_enough_cash do
  type :DIALOGUE_PLAYER
  emote DialogueAnimations::SAD
  text 'Oh dear. I don\'t seem to have enough money.'
  continue :close_dialogue
end

## npc clicking
on :message, :first_npc_action do |ctx, player, message|
  if message.npc.type == BARTENDER_ID
    send_dialogue(player, :bartender_welcome)
    ctx.break_handler_chain()
  end
end