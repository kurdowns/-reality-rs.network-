BEER_ID = 1917

on :message, :first_item_action do |ctx, player, message|

  if message.item.id == BEER_ID

    player.get_items().delete_item(BEER_ID, 1)
    player.get_data().totalBeersDrank += 1
    player.start_animation(3040)
    player.send_message('You drink the beer.')
    player.send_message('You feel dizzy...')

    ctx.break_handler_chain()
  end

end