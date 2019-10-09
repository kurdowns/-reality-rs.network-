require 'java'

java_import 'com.zionscape.server.scripting.messages.handler.MessageHandler'
java_import 'com.zionscape.server.scripting.Scripting'
java_import 'com.zionscape.server.model.players.dialogue.DialogueAnimations'

# Extends the (Ruby) String class with a method to convert a lower case,
# underscore delimited string to camel-case.
class String
  def camelize
    gsub(/(?:^|_)(.)/) { $1.upcase }
  end
end

class ProcMessageHandler < MessageHandler
  def initialize(block, option)
    super()
    @block = block
    @option = option
  end

  def handle(ctx, player, message)
    if (@option == 0 || @option == message.option)
      @block.call(ctx, player, message)
    end
  end
end

def on(type, *args, &block)
  case type
    when :message then on_message(args, block)
    else raise 'Unknown message type.'
  end
end

# Defines an action to be taken upon a message.
# The message can either be a symbol with the lower-case underscored class name, or the class itself.
def on_message(args, proc)
  raise 'Message must have one or two arguments.' unless (1..2).include?(args.length)
  numbers = [ 'first', 'second', 'third', 'fourth', 'fifth' ]
  message = args[0]; option = 0

  numbers.each_index do |index|
    number = numbers[index]

    if message.to_s.start_with?(number)
      option = index + 1
      message = message[number.length + 1, message.length].to_sym
      break
    end
  end

  if message.is_a?(Symbol)
    class_name = message.to_s.camelize.concat('Message')
    message = Java::JavaClass.for_name("com.zionscape.server.scripting.messages.impl.#{class_name}")
  end

  Scripting.addLastMessageHandler(message, ProcMessageHandler.new(proc, option))
end

:DIALOGUE_CLOSE
:DIALOGUE_PLAYER
:DIALOGUE_NPC
:DIALOGUE_OPTION
:DIALOGUE_STATEMENT

class Dialogue
  attr_reader :name, :type, :npc, :text, :emote, :options, :continue, :continue_block

  def initialize(name)
    @name = name
  end

  def type(type = nil)
    type ? @type = type : @type
  end

  def npc(npc = nil)
    npc ? @npc = npc : @npc
  end

  def text(text = nil)
    text ? @text = text : @text
  end

  def emote(emote = DialogueAnimations::TALKING)
    emote ? @emote = emote : @emote
  end

  def options(options = nil)
    options ? @options = options : @options
  end

  def continue(continue=nil)
    continue ? @continue = continue : @continue
  end

  def continue_block(&continue_block)
    continue_block ? @continue_block = continue_block : @continue_block
  end

end

def dialogue(name, &block)
  dialogue = Dialogue.new(name)
  dialogue.instance_eval(&block)

  Scripting.dialogues.put(name, dialogue)
end

dialogue :close_dialogue do
  type :DIALOGUE_CLOSE
end

def send_dialogue(player, name)
  if Scripting.dialogues.contains_key(name)
    dialogue = Scripting.dialogues.get(name)

    text = ''
    if !dialogue.text.nil?
      text = dialogue.text.sub('{player_name}', player.username)
    end

    player.set_attribute('dialogue', dialogue)
    case dialogue.type
      when :DIALOGUE_CLOSE
        player.reset_dialogue()
      when :DIALOGUE_PLAYER
        player.get_pa().send_player_chat(dialogue.emote.id, text)
      when :DIALOGUE_NPC
        player.get_pa().send_npc_chat(dialogue.npc, dialogue.emote.id, text)
      when :DIALOGUE_STATEMENT
        player.get_pa().send_statement(text)
      when :DIALOGUE_OPTION
        # hacky and needs to be redone
        options = dialogue.options
        case options.length
          when 2
            player.get_pa().send_options(options[0], options[1]);
          when 3
            player.get_pa().send_options(options[0], options[1], options[2]);
          when 4
            player.get_pa().send_options(options[0], options[1], options[2], options[3]);
          when 5
            player.get_pa().send_options(options[0], options[1], options[2], options[3], options[4]);
          else
            raise 'invalid amount of dialogue options'
        end
      else
        raise 'wrong dialogue type'
    end
  else
    raise 'no such dialogue named' + name
  end
end

on :message, :dialogue_continue_clicked do |ctx, player, message|
  if player.attribute_exists('dialogue')

    dialogue = player.get_attribute('dialogue')

    if !dialogue.continue.nil?
      send_dialogue(player, dialogue.continue)
    elsif !dialogue.continue_block.nil?
      dialogue.continue_block.call(player)
    end

    ctx.break_handler_chain()
  end
end

on :message, :dialogue_option_clicked do |ctx, player, message|
  if player.attribute_exists('dialogue')

    dialogue = player.get_attribute('dialogue')
    if dialogue.type != :DIALOGUE_OPTION
      ctx.break_handler_chain()
     # ignore the event instead of throwing an exception
     # raise 'dialogue option clicked but dialogue not of option type'
    end

    if !dialogue.continue_block.nil?
      dialogue.continue_block.call(player, message.option)
    else
      ctx.break_handler_chain()
      # ignore the event instead of throwing an exception
      # raise 'dialogue option clicked but dialogue not of option type'
    end

    ctx.break_handler_chain()
  end
end