# StickCommand

A plugin enabling you to assign commands to sticks and use them on players.

Simply strike a player with a stick and execute the specified command on them.

_If the target player is in creative mode, you will need to use the right mouse button._

Supported languages: ru.yml, en.yml.

## Permissions:
`stickcommand.give` — Permission to obtain the stick.

`stickcommand.reload` — Plugin reload permission.

`stickcommand.use` — Permission to use the stick.

## Example configuration:

```yml
StickCommand:
  StickExample:
    name: '&6Example'
    command: 'kick {player}'
    enchanting: true
    lore:
      - '&7This stick will kick'
      - '&7the player you hit with it.'
  StickExample2:
    name: '&6Example2'
    command: 'tp {player} {x} 200 {z}'
    enchanting: false
    lore:
      - '&7This stick will tp'
      - '&7the player you hit with it.'
```
