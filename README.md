# WelcomeBack
Plugin that allows people to get points for welcoming people back on the server

#### Download [here](https://github.com/prosteDeniGC/WelcomeBack/releases)

## Commands

* **/welcomeback check** - *checks own balance*
* **/welcomeback check (player)** - *checks balance of player*
* **/welcomeback set (player) (amount)** - *sets balance of player*
* **/welcomeback give (player) (amount)** - *gives specified player the amount*
* **/welcomeback take (player) (amount)** - *takes the amount from the player*
* **/welcomeback help** - *displays a help message*
* **/welcomeback reload** - *reloads config, help message and all other messages*

## Permissions

* **welcomeback.checkown** - *permission for /welcomeback check command*
* **welcomeback.admin** - *permission for all commands including the previous one*

## Configs

### FriendlyConfig.yml
  * *main config file with many options*
#### FriendlyMessages.yml
  * *how all messages look*
#### FriendlyHelp.yml
  * *how does help message look*
  
## PlaceholderAPI

* *WelcomeBack has it's own placeholder, _**`%welcomeback_balance%`**_*
 * no need to install any extensions, just have [PAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) installed and use the placeholder

### For developers
* add these to pom.xml

  `<dependency>`\
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`<groupId>me.clip</groupId>`\
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`<artifactId>placeholderapi</artifactId>`\
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`<version>`[CURRENTVERSION](https://github.com/PlaceholderAPI/PlaceholderAPI)`</version>`\
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`<scope>provided</scope>`\
   `</dependency>`
          
    `<repository>`\
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`<id>placeholderapi</id>`\
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`<url>https://repo.extendedclip.com/content/repositories/placeholderapi/</url>`\
    `</repository>`
    
* if using Gradle or not sure just look at PAPI's dependency [manual](https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Hook-into-PlaceholderAPI)        
