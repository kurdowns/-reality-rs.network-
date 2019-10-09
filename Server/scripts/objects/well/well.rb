DIALOGUE_ACTION = 'objects-well'

on :message, :item_on_object do |ctx, player, message|
  if message.object_id == 26945 and message.item.id == 995

    player.dialogue_owner = DIALOGUE_ACTION
    player.set_attribute('well_amount', message.item.amount)
    player.get_pa().send_statement 'Are you sure you want to put these coins down the well?'

    ctx.break_handler_chain()
  end
end

on :message, :dialogue_continue_clicked do |ctx, player, message|
  if player.dialogue_owner == DIALOGUE_ACTION
    player.get_pa().send_options('Yes', 'No')

    ctx.break_handler_chain()
  end
end

on :message, :dialogue_option_clicked do |ctx, player, message|
  if player.dialogue_owner == DIALOGUE_ACTION

    if message.option == 1 and player.attribute_exists('well_amount')
      player.get_items().delete_item(995, player.get_attribute('well_amount'))
      player.get_data().totalCoinsThrownDownWell += player.get_attribute('well_amount')
      player.remove_attribute('well_amount')
      player.send_message 'You throw the coins down the well.'
    end

    player.reset_dialogue()
    ctx.break_handler_chain()
  end
end