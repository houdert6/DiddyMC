# DiddyMC

The official Minecraft plugin for [Diddy Bot](https://canary.discord.com/discovery/applications/1305713838775210015).

___

## Features

- **/rizzme**    
  Receive a funny pickupline from Diddy Bot
- **/oil**    
  Oil up the huzz
- **/diddle**    
  Diddle your friends
- **/getaura** (requires DiscordSRV)    
  See how much aura a player's linked Discord account has, and view their sigma level

### Entity Selectors
`/oil` and `/diddle` allow players to target *any* entity, using vanilla minecraft selectors!
Any entity can use all the plugin's commands through `execute as` too.

For example: `/execute as @n[type=cow] run oil @n[type=pig]` makes the nearest cow oil up the nearest pig

## DiscordSRV Integration
DiddyMC optionally integrates into DiscordSRV to provide more advanced Diddy Bot features.

## Usage & Commands

| Command                              | Description                                                | Permission                |
|--------------------------------------|------------------------------------------------------------|---------------------------|
| `/rizzme`                            | Receive a random pickup line!                              | `diddymc.rizzme`          |
| `/oil <entity>`                      | oil up your friends                                        | `diddymc.oil`             |
| `/diddle <entity>`                   | Diddle your friends                                        | `diddymc.diddle`          |
| `/getaura <member>`                  | display a users aura                                       | `diddymc.getaura`         |

## Configuration (config.yml)

```yaml
# DiddyMC Configuration File

# The prefix sent in chat when DiddyMC commands are run
prefix: '&0[&4&kh&6Diddy-Bot&4&kh&0] &r'

# If true, DiddyMC will not attempt to get pickup lines from the Diddy Bot API or GitHub, and will instead use pickuplines.yml (or a hardcoded value if it can't be loaded)
always-use-offline-pickuplines: false

# Diddy-Bot API base url
diddyapi: 'http://35.208.224.85'
```

## Paper *and* Spigot
DiddyMC officially supports Minecraft servers as outdated as 1.19.4 Spigot, however when used on more modern servers such as 1.21 Paper, it makes full use of new plugin APIs such as Paper Plugin support and Paper's brigadier command system.
