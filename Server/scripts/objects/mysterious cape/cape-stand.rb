
RACK_STAND_ID = 11000

dialogue :cape_stand_1 do
  type :DIALOGUE_OPTION
  options ['Max Cape [50M]', 'Completionist Cape [100M]']
  continue_block do |player, option|
    send_dialogue(player, :close_dialogue)
    if player.get_items().free_inventory_slots() < 2
      player.send_message "You do not have enough free inventory space."
      return
    end
    case option
      when 1
        if player.getItems().player_has_item(995, 50_000_000)
          player.get_items().delete_item(995, 50_000_000);
          player.get_items().add_item(20767, 1)
          player.get_items().add_item(20768, 1)
        else
          player.send_message "You do not have enough cash to buy this cape."
        end
      when 2
        if player.getItems().player_has_item(995, 100_000_000)
          player.get_items().delete_item(995, 100_000_000);
          player.get_items().add_item(20769, 1)
          player.get_items().add_item(20770, 1)
        else
          player.send_message "You do not have enough cash to buy this cape."
        end
    end
  end
end

on :message, :first_object_action do |ctx, player, message|
  if message.object_id == RACK_STAND_ID
    send_dialogue(player, :cape_stand_1)
  end
end