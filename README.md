# CS611 - Legends: Monsters and Heroes & Legends of Valor

## Author Information
- Name: Edaad Azman
- Email: edaad@bu.edu
- Student ID: U38459100

- Name: Daniel Silla
- Email: dsilla@bu.edu
- Student ID: U24834540

- Name: Keenan Swenson
- Email: kswenson@bu.edu
- Student ID: U80694517

## Files

---------------------------------------------------------------------------

### Game Package

`Main.java` — Application entry point that launches the GameController.

`GameController.java` — Top-level controller that displays the game selection menu and delegates to GameFactory.

`GameFactory.java` — Factory pattern implementation for creating game instances (Monsters and Heroes or Legends of Valor) based on user selection.

`Game.java` — Abstract base class defining the core game lifecycle methods (start, gameLoop, endGame).

`RPG.java` — Abstract class extending Game that provides shared RPG functionality including party setup, inventory management, and market access for both game modes.

`MonstersAndHeroes.java` — Concrete game implementation for the classic dungeon-crawling mode with random battles and world exploration.

`LegendsOfValor.java` — Concrete game implementation for the MOBA-style lane-based combat mode with hero actions, monster AI, and win/lose conditions.

`MarketEngine.java` — Manages marketplace interactions allowing heroes to buy and sell weapons, armor, potions, and spells.

`Difficulty.java` — Enum defining difficulty levels (EASY, MEDIUM, HARD) with associated monster spawn intervals for Legends of Valor.

### Combat Package

`BattleEngine.java` — Abstract base class for combat systems providing shared attack, spell, and potion logic.

`MHBattleEngine.java` — Concrete battle engine for Monsters and Heroes with turn-based party vs monster group combat.

`ValorBattleEngine.java` — Concrete battle engine for Legends of Valor with range-based targeting, terrain bonuses, and lane-aware monster AI.

`CombatExecutor.java` — Handles execution of combat actions, damage calculations, and defeat handling for both game modes.

`CombatAction.java` — Interface defining the contract for combat actions (execute, getActionName).

`AttackAction.java` — Concrete implementation of physical attacks with terrain-aware damage calculations.

`SpellAction.java` — Concrete implementation of spell casting with mana costs and monster debuffs.

### Character Package

`Character.java` — Abstract base class for all characters defining common attributes (name, level, HP, position).

`Hero.java` — Player character with attributes (mana, strength, dexterity, agility), inventory, equipment, terrain buffs, and spawn tracking for Valor mode.

`Monster.java` — Enemy character with base damage, defense, dodge mechanics, lane tracking, and spell vulnerabilities.

`Party.java` — Container class managing the group of heroes with party-wide operations.

`MonsterFactory.java` — Factory for creating balanced monster groups scaled to party size and level.

`HeroType.java` — Enum defining hero classes (WARRIOR, PALADIN, SORCERER).

`MonsterType.java` — Enum defining monster types (DRAGON, EXOSKELETON, SPIRIT).

### Items Package

`Item.java` — Abstract base class for all items with name, price, level requirement, and uses.

`Weapon.java` — Equipment item with damage and hands-required values.

`Armor.java` — Equipment item providing damage reduction.

`Potion.java` — Consumable item that boosts hero stats (HP, MP, strength, dexterity, agility).

`Spell.java` — Magical attack with mana cost, damage, and debuff effects.

`Inventory.java` — Container managing a hero's item collection with type-specific retrieval.

`PotionType.java` — Enum for potion effects (HEALTH, MANA, STRENGTH, DEXTERITY, AGILITY).

`SpellType.java` — Enum for spell elements (FIRE, ICE, LIGHTNING).

### World Package

`World.java` — Abstract base class for game worlds with grid management and movement delegation via Strategy pattern.

`MHWorld.java` — Concrete world for Monsters and Heroes with market/common/inaccessible tiles.

`ValorWorld.java` — Concrete world for Legends of Valor with 8x8 lane-based layout, Nexus tiles, and terrain types (Bush, Cave, Koulou, Obstacle).

`Tile.java` — Represents individual map cells with type, hero/monster occupancy, and terrain effects.

`TileType.java` — Enum defining tile types (NEXUS, INACCESSIBLE, OBSTACLE, PLAIN, BUSH, CAVE, KOULOU, COMMON, MARKET).

`MovementStrategy.java` — Interface for movement behavior (Strategy pattern).

`MHMovementStrategy.java` — Movement rules for Monsters and Heroes (party-based grid movement).

`ValorMovementStrategy.java` — Movement rules for Legends of Valor (individual hero movement, monster blocking, terrain buffs).

### Data Package

`GameDatabase.java` — Singleton facade providing centralized access to all game data loaded from text files.

`DataLoader.java` — Abstract generic class implementing Template Method pattern for file parsing.

`HeroDataLoader.java` — Loads hero data from Paladins.txt, Sorcerers.txt, Warriors.txt.

`MonsterDataLoader.java` — Loads monster data from Dragons.txt, Exoskeletons.txt, Spirits.txt.

`WeaponDataLoader.java` — Loads weapon data from Weaponry.txt.

`ArmorDataLoader.java` — Loads armor data from Armory.txt.

`PotionDataLoader.java` — Loads potion data from Potions.txt.

`SpellDataLoader.java` — Loads spell data from FireSpells.txt, IceSpells.txt, LightningSpells.txt.

### Utility Package

`InputHelper.java` — Centralized input handling with validation for integers, characters, and strings.

## Design Choices

---------------------------------------------------------------------------

- **Singleton Pattern (GameDatabase)**: Ensures a single source of truth for all game data loaded from text files, preventing redundant file I/O and providing consistent data access across market, battle, and game setup systems.

- **Factory Pattern (GameFactory, MonsterFactory)**: GameFactory creates game instances based on user selection, decoupling game creation from the controller. MonsterFactory generates level-scaled monster groups, encapsulating spawning logic away from battle engines.

- **Template Method Pattern (DataLoader<T>)**: Abstract base class defines the file-loading algorithm while subclasses implement only the line-parsing logic, eliminating duplicate file-handling code across 7 different data loaders.

- **Strategy Pattern (MovementStrategy)**: Decouples movement rules from world structure—MHMovementStrategy handles simple party movement while ValorMovementStrategy implements lane-based blocking, terrain buffs, and individual hero/monster positioning without modifying the World class.

- **Inheritance Hierarchies**: `Game` → `RPG` → `MonstersAndHeroes`/`LegendsOfValor` shares common RPG mechanics. `Character` → `Hero`/`Monster` enables polymorphic combat. `Item` → `Weapon`/`Armor`/`Potion`/`Spell` provides uniform inventory handling. `World` → `MHWorld`/`ValorWorld` separates generation from behavior.

- **Separation of Concerns**: Distinct packages (`game`, `combat`, `characters`, `items`, `world`, `data`) ensure each module has a single responsibility. Combat logic is isolated from world rendering, data loading is hidden behind a facade, and game modes share infrastructure without coupling.

- **Encapsulation**: Classes manage internal state through private fields with controlled public access. Hero terrain buffs are applied/removed internally, Inventory filters items by type, and CombatExecutor handles defeat logic without exposing implementation details.

## Cool Features / Creative Choices

---------------------------------------------------------------------------

- **ASCII Art & Colors**: Stylized ASCII title screens and ANSI color-coded output enhance the visual experience—hero stats, terrain types, and combat feedback are displayed with distinct colors for readability.

- **Dual Game Modes**: A single codebase supports both classic dungeon-crawling (Monsters and Heroes) and MOBA-style lane combat (Legends of Valor), with shared character/item systems and distinct battle engines via polymorphism.

- **How to Play (H Command)**: In Legends of Valor, pressing 'H' displays a comprehensive help screen explaining controls, terrain effects, win/lose conditions, and gameplay mechanics for new players.

- **Difficulty Levels**: Legends of Valor offers Easy/Medium/Hard modes that adjust monster spawn frequency (every 8/6/4 rounds), allowing players to choose their preferred challenge level.

- **Terrain Bonuses in Valor**: Bush tiles grant +10% dexterity, Cave tiles grant +10% agility, and Koulou tiles grant +10% strength—buffs are dynamically applied based on hero position, encouraging strategic lane choices.

- **Hero Respawn System**: In Legends of Valor, fallen heroes automatically respawn at their Nexus with full HP/MP at the start of their next turn, keeping the game flowing without permanent death.

- **Spell Debuffs**: Spells inflict persistent debuffs on monsters (fire reduces defense, ice reduces damage, lightning reduces dodge) that stack throughout battle, rewarding tactical spellcasting.

## How to compile and run (Java 11+)
---------------------------------------------------------------------------

### Compilation
1. Navigate to the project directory

2. Compile to out directory

$ mkdir -p out
$ javac -d out $(find src -name "*.java")

3. Run the main application

$ java -cp out Main

## Input/Output Example


```text

╔════════════════════════════════════════════════════════════════════════════╗
║                                                                            ║
║                    __    ___________________   ______  _____               ║
║                   / /   / ____/ ____/ ____/ | / / __ \/ ___/               ║
║                  / /   / __/ / / __/ __/ /  |/ / / / /\__ \                ║
║                 / /___/ /___/ /_/ / /___/ /|  / /_/ /___/ /                ║
║                /_____/_____/\____/_____/_/ |_/_____//____/                 ║
║                                                                            ║
║                  G A M E   C O L L E C T I O N   2 0 2 5                   ║
║                                                                            ║
╠════════════════════════════════════════════════════════════════════════════╣
║                                                                            ║
║                          Choose Your Adventure!                            ║
║                                                                            ║
╚════════════════════════════════════════════════════════════════════════════╝


==================================================
  SELECT GAME MODE
==================================================
1) Monsters and Heroes
   Classic dungeon crawler with turn-based battles
2) Legends of Valor
   Strategic lane-based MOBA-style gameplay
0) Exit
==================================================
Enter choice: 2
Loading game data...
Game data loaded successfully!


╔═════════════════════════════════════════════════════════════════════════════╗
║                                                                             ║
║                        @@@@                    @@@@                         ║
║                        @@@@@    @@@    @@@    @@@@@                         ║
║                           @@@@ @@@      @@@ @@@@                            ║
║                            @@@@@@@      @@@@@@@                             ║
║                            @@@@@@@@@  @@@@@@@@@                             ║
║                          @@@  @@@@@@@@@@@@@@  @@@                           ║
║                                 @@@@@@@@@@                                  ║
║                                 @@@@@@@@@@                                  ║
║                                @@@@@@@@@@@@                                 ║
║                              @@@@@@@  @@@@@@@                               ║
║                            @@@@@@@      @@@@@@@                             ║
║                          @@@@@@@          @@@@@@@                           ║
║                        @@@@@@@              @@@@@@@                         ║
║                        @@@@@                  @@@@@                         ║
║                                                                             ║
║         ██╗     ███████╗ ██████╗ ███████╗███╗   ██╗██████╗ ███████╗         ║
║         ██║     ██╔════╝██╔════╝ ██╔════╝████╗  ██║██╔══██╗██╔════╝         ║
║         ██║     █████╗  ██║  ███╗█████╗  ██╔██╗ ██║██║  ██║███████╗         ║
║         ██║     ██╔══╝  ██║   ██║██╔══╝  ██║╚██╗██║██║  ██║╚════██║         ║
║         ███████╗███████╗╚██████╔╝███████╗██║ ╚████║██████╔╝███████║         ║
║         ╚══════╝╚══════╝ ╚═════╝ ╚══════╝╚═╝  ╚═══╝╚═════╝ ╚══════╝         ║
║                                                                             ║
║                      ═══════ O F   V A L O R ═══════                        ║
║                                                                             ║
║                                                                             ║
║              >> Defend thy Castle, vanquish thine enemies! <<               ║
║                >> A medieval battle of strategic warfare <<                 ║
║                                                                             ║
╚═════════════════════════════════════════════════════════════════════════════╝


==================================================
  SELECT DIFFICULTY
==================================================
1) EASY - Monsters spawn every 8 rounds
2) MEDIUM - Monsters spawn every 6 rounds
3) HARD - Monsters spawn every 4 rounds
==================================================
Choose difficulty (1-3): 1

Difficulty set to: EASY
Monsters spawn every 8 rounds

=== ASSEMBLE YOUR PARTY ===

Legendary Heroes Available for Recruitment:
1) Gaerdal_Ironhand [WARRIOR]
2) Sehanine_Monnbow [WARRIOR]
3) Muamman_Duathall [WARRIOR]
4) Flandal_Steelskin [WARRIOR]
5) Undefeated_Yoj [WARRIOR]
6) Eunoia_Cyn [WARRIOR]
7) Rillifane_Rallathil [SORCERER]
8) Segojan_Earthcaller [SORCERER]
9) Reign_Havoc [SORCERER]
10) Reverie_Ashels [SORCERER]
11) Kalabar [SORCERER]
12) Skye_Soar [SORCERER]
13) Parzival [PALADIN]
14) Sehanine_Moonbow [PALADIN]
15) Skoraeus_Stonebones [PALADIN]
16) Garl_Glittergold [PALADIN]
17) Amaryllis_Astra [PALADIN]
18) Caliber_Heist [PALADIN]

Recruit hero #1: 1
Gaerdal_Ironhand has joined your party!

Recruit hero #2: 3
Muamman_Duathall has joined your party!

Recruit hero #3: 5
Undefeated_Yoj has joined your party!

M1: Blinky (SPIRIT) spawned at lane 1
M2: Kiaransalee (EXOSKELETON) spawned at lane 2
M3: Phaarthurnax (DRAGON) spawned at lane 3

======================================================================
  ROUND 1
======================================================================
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |    M1 |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |    M3 | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P 
  B - B - B  P - P - P  X - X - X  C - C - C  B - B - B  X - X - X  O - O - O  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  | X X X |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  B - B - B  X - X - X  O - O - O  K - K - K 
  O - O - O  P - P - P  X - X - X  P - P - P  C - C - C  X - X - X  K - K - K  C - C - C 
  | X X X |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  O - O - O  P - P - P  X - X - X  P - P - P  C - C - C  X - X - X  K - K - K  C - C - C 
  C - C - C  B - B - B  X - X - X  B - B - B  O - O - O  X - X - X  P - P - P  P - P - P 
  |       |  |       |  | X X X |  |       |  | X X X |  | X X X |  |       |  |       | 
  C - C - C  B - B - B  X - X - X  B - B - B  O - O - O  X - X - X  P - P - P  P - P - P 
  B - B - B  K - K - K  X - X - X  K - K - K  B - B - B  X - X - X  P - P - P  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  K - K - K  X - X - X  K - K - K  B - B - B  X - X - X  P - P - P  P - P - P 
  P - P - P  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  K - K - K  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  K - K - K  K - K - K 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  | H1    |  |       |  | X X X |  | H2    |  |       |  | X X X |  | H3    |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
M - Market (buy/sell items)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: 

....... (too long so skipped to end of a game) .......

=== Monsters in Range ===
1) M3: Brandobaris | HP: 100 | Level: 1 | Position: (3,7)
0) Cancel
Select target: 1
H3: Undefeated_Yoj attacks M3: Brandobaris for 39 damage!

--- Monster Turn ---
M1: WickedWitch must fight through H1 (Gaerdal_Ironhand)!
M1: WickedWitch attacks H1: Gaerdal_Ironhand for 12 damage!
M2: Andrealphus must fight through H2 (Muamman_Duathall)!
M2: Andrealphus attacks H2: Muamman_Duathall for 30 damage!
M3: Brandobaris must fight through H3 (Undefeated_Yoj)!
M3: Brandobaris attacks H3: Undefeated_Yoj for 17 damage!

--- End of Round Recovery ---
Gaerdal_Ironhand recovers:
  HP: +10 (98/100)
Muamman_Duathall recovers:
  HP: +10 (80/100)
Undefeated_Yoj recovers:
  HP: +10 (86/100)

======================================================================
  ROUND 6
======================================================================
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  | H2    |  |    M2 |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  | H3    |  |    M3 | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  | H1    |  |    M1 |  | X X X |  |       |  | X X X |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
H1: Cannot move past M1 (WickedWitch) in your lane!
You must engage it first.


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: f

=== Monsters in Range ===
1) M1: WickedWitch | HP: 30 | Level: 1 | Position: (4,1)
0) Cancel
Select target: 1
H1: Gaerdal_Ironhand attacks M1: WickedWitch for 35 damage!
WickedWitch has been defeated!

--- Rewards Distributed ---
Gaerdal_Ironhand gained 100 gold and 2 XP!
Muamman_Duathall gained 100 gold and 2 XP!
Undefeated_Yoj gained 100 gold and 2 XP!

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  | H2    |  |    M2 |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  | H3    |  |    M3 | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  | H1    |  |       |  | X X X |  |       |  | X X X |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H2 Turn (Muamman_Duathall) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: t

Select a hero to teleport near:
1. Gaerdal_Ironhand at (4,0)
2. Undefeated_Yoj at (3,6)
Choice (1-2): 1

Select a position:
1. Right of Gaerdal_Ironhand at (4,1)
2. Below of Gaerdal_Ironhand at (5,0)
Choice (1-2): 1
Muamman_Duathall teleported to (4,1)!

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  | H3    |  |    M3 | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  | H1    |  | H2    |  | X X X |  |       |  | X X X |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H3 Turn (Undefeated_Yoj) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: f

=== Monsters in Range ===
1) M3: Brandobaris | HP: 61 | Level: 1 | Position: (3,7)
0) Cancel
Select target: 1
H3: Undefeated_Yoj attacks M3: Brandobaris for 39 damage!

--- Monster Turn ---
M2: Andrealphus advances south toward the Hero Nexus!
M3: Brandobaris must fight through H3 (Undefeated_Yoj)!
M3: Brandobaris attacks H3: Undefeated_Yoj for 17 damage!

--- End of Round Recovery ---
Gaerdal_Ironhand recovers:
  HP: +2 (100/100)
Muamman_Duathall recovers:
  HP: +10 (90/100)
Undefeated_Yoj recovers:
  HP: +10 (79/100)

======================================================================
  ROUND 7
======================================================================
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  | H3    |  |    M3 | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  | H1    |  | H2    |  | X X X |  |       |  | X X X |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
Gaerdal_Ironhand feels more nimble in the bushes! Dexterity increased.

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  | H1    |  |       |  | X X X |  |       |  |    M2 |  | X X X |  | H3    |  |    M3 | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  |       |  | H2    |  | X X X |  |       |  | X X X |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H2 Turn (Muamman_Duathall) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
Muamman_Duathall feels more agile in the cave! Agility increased.

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  | H1    |  | H2    |  | X X X |  |       |  |    M2 |  | X X X |  | H3    |  |    M3 | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  | X X X |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  O - O - O  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H3 Turn (Undefeated_Yoj) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: f

=== Monsters in Range ===
1) M3: Brandobaris | HP: 22 | Level: 1 | Position: (3,7)
0) Cancel
Select target: 1
H3: Undefeated_Yoj attacks M3: Brandobaris for 39 damage!
Brandobaris has been defeated!

--- Rewards Distributed ---
Gaerdal_Ironhand gained 100 gold and 2 XP!
Muamman_Duathall gained 100 gold and 2 XP!
Undefeated_Yoj gained 100 gold and 2 XP!

--- Monster Turn ---
M2: Andrealphus encounters an obstacle and begins clearing it...
Obstacle removed! Tile is now passable.
M2: Andrealphus advances south toward the Hero Nexus!

--- End of Round Recovery ---
Muamman_Duathall recovers:
  HP: +10 (100/100)
Undefeated_Yoj recovers:
  HP: +10 (89/100)

======================================================================
  ROUND 8
======================================================================
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  | H1    |  | H2    |  | X X X |  |       |  |    M2 |  | X X X |  | H3    |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  | H1    |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  | H2    |  | X X X |  |       |  |    M2 |  | X X X |  | H3    |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H2 Turn (Muamman_Duathall) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
Muamman_Duathall feels stronger on the koulou! Strength increased.

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  | H1    |  | H2    |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  | H3    |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H3 Turn (Undefeated_Yoj) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w

--- Monster Turn ---
M2: Andrealphus advances south toward the Hero Nexus!

--- End of Round Recovery ---
Undefeated_Yoj recovers:
  HP: +10 (99/100)

======================================================================
    REINFORCEMENTS ARRIVING! 
  Enemy forces are spawning at the Monster Nexus!
======================================================================
M2: St-Yeenoghu (Level 1 EXOSKELETON) spawned in lane 1!
M3: Taltecuhtli (Level 1 SPIRIT) spawned in lane 2!
M4: Desghidorrah (Level 1 DRAGON) spawned in lane 3!

3 new monster(s) have joined the battle!
Defend your nexus!


======================================================================
  ROUND 9
======================================================================
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |    M2 |  | X X X |  |       |  |    M3 |  | X X X |  |       |  |    M4 | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  | H1    |  | H2    |  | X X X |  |       |  |       |  | X X X |  | H3    |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |    M2 |  | X X X |  |       |  |    M3 |  | X X X |  |       |  |    M4 | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  | H1    |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  | H2    |  | X X X |  |       |  |       |  | X X X |  | H3    |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H2 Turn (Muamman_Duathall) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
Muamman_Duathall feels more nimble in the bushes! Dexterity increased.

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |    M2 |  | X X X |  |       |  |    M3 |  | X X X |  |       |  |    M4 | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  | H1    |  | H2    |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  | H3    |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H3 Turn (Undefeated_Yoj) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
Undefeated_Yoj feels more agile in the cave! Agility increased.

--- Monster Turn ---
M2: Andrealphus advances south toward the Hero Nexus!
M2: St-Yeenoghu advances south toward the Hero Nexus!
M3: Taltecuhtli advances south toward the Hero Nexus!
M4: Desghidorrah advances south toward the Hero Nexus!

--- End of Round Recovery ---
Undefeated_Yoj recovers:
  HP: +1 (100/100)

======================================================================
  ROUND 10
======================================================================
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  | H1    |  | H2 M2 |  | X X X |  |       |  |    M3 |  | X X X |  | H3    |  |    M4 | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: f

=== Monsters in Range ===
1) M2: St-Yeenoghu | HP: 100 | Level: 1 | Position: (1,1)
0) Cancel
Select target: 1
H1: Gaerdal_Ironhand attacks M2: St-Yeenoghu for 33 damage!

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  | H1    |  | H2 M2 |  | X X X |  |       |  |    M3 |  | X X X |  | H3    |  |    M4 | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H2 Turn (Muamman_Duathall) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: f

=== Monsters in Range ===
1) M2: St-Yeenoghu | HP: 67 | Level: 1 | Position: (1,1)
0) Cancel
Select target: 1
H2: Muamman_Duathall attacks M2: St-Yeenoghu for 43 damage!

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  | H1    |  | H2 M2 |  | X X X |  |       |  |    M3 |  | X X X |  | H3    |  |    M4 | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H3 Turn (Undefeated_Yoj) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
H3: Cannot move past M4 (Desghidorrah) in your lane!
You must engage it first.


--- H3 Turn (Undefeated_Yoj) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: f

=== Monsters in Range ===
1) M4: Desghidorrah | HP: 100 | Level: 1 | Position: (1,7)
0) Cancel
Select target: 1
H3: Undefeated_Yoj attacks M4: Desghidorrah for 39 damage!

--- Monster Turn ---
M2: Andrealphus advances south toward the Hero Nexus!
M2: St-Yeenoghu must fight through H1 (Gaerdal_Ironhand)!
M2: St-Yeenoghu attacks H1: Gaerdal_Ironhand for 47 damage!
M3: Taltecuhtli advances south toward the Hero Nexus!
M4: Desghidorrah must fight through H3 (Undefeated_Yoj)!
M4: Desghidorrah attacks H3: Undefeated_Yoj for 15 damage!

--- End of Round Recovery ---
Gaerdal_Ironhand recovers:
  HP: +10 (63/100)
Undefeated_Yoj recovers:
  HP: +10 (95/100)

======================================================================
  ROUND 11
======================================================================
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  | H1    |  | H2 M2 |  | X X X |  |       |  |       |  | X X X |  | H3    |  |    M4 | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |    M3 |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
H1: Cannot move past M2 (St-Yeenoghu) in your lane!
You must engage it first.


--- H1 Turn (Gaerdal_Ironhand) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: f

=== Monsters in Range ===
1) M2: St-Yeenoghu | HP: 24 | Level: 1 | Position: (1,1)
0) Cancel
Select target: 1
H1: Gaerdal_Ironhand attacks M2: St-Yeenoghu for 33 damage!
St-Yeenoghu has been defeated!

--- Rewards Distributed ---
Gaerdal_Ironhand gained 100 gold and 2 XP!
Muamman_Duathall gained 100 gold and 2 XP!
Undefeated_Yoj gained 100 gold and 2 XP!

  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  | H1    |  | H2    |  | X X X |  |       |  |       |  | X X X |  | H3    |  |    M4 | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |    M3 |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 


--- H2 Turn (Muamman_Duathall) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: f
No monsters in range to attack!
Move adjacent to an enemy first.


--- H2 Turn (Muamman_Duathall) ---
W/A/S/D - Move
F - Attack monster
C - Cast spell
T - Teleport to another lane
R - Recall to Nexus
O - Remove adjacent obstacle
V - Inventory Actions (equipment/potions)
I - Info
H - How To Play
P - Pass turn
Q - Quit
Command: w
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  | H2    |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  | H1    |  |       |  | X X X |  |       |  |       |  | X X X |  | H3    |  |    M4 | 
  P - P - P  B - B - B  X - X - X  C - C - C  K - K - K  X - X - X  C - C - C  P - P - P 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  |       |  |       |  | X X X |  |       |  |    M3 |  | X X X |  |       |  |       | 
  P - P - P  K - K - K  X - X - X  K - K - K  P - P - P  X - X - X  P - P - P  C - C - C 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  C - C - C  X - X - X  B - B - B  P - P - P  X - X - X  P - P - P  B - B - B 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  P - P - P  P - P - P  X - X - X  K - K - K  P - P - P  X - X - X  K - K - K  B - B - B 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  B - B - B  P - P - P  X - X - X  C - C - C  C - C - C  X - X - X  P - P - P  K - K - K 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  |       |  |       |  | X X X |  |       |  |    M2 |  | X X X |  |       |  |       | 
  C - C - C  K - K - K  X - X - X  P - P - P  P - P - P  X - X - X  B - B - B  P - P - P 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 
  |       |  |       |  | X X X |  |       |  |       |  | X X X |  |       |  |       | 
  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N  X - X - X  N - N - N  N - N - N 

==================================================
 VICTORY! H2: Muamman_Duathall has reached the enemy nexus!
The heroes have won the battle!
==================================================


Thanks for playing! Safe travels, hero!


```
