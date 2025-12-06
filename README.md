# CS611 - Legends: Monsters and Heroes

## Author Information
- Name: Edaad Azman
- Email: edaad@bu.edu
- Student ID: U38459100


## Files

---------------------------------------------------------------------------

### Core Classes

`Main.java` — Application entry point that initializes the game database and launches the main game engine.

`GameEngine.java` — Primary game controller managing the main game loop, world navigation, user input handling, and coordination between battle, market, and inventory systems.

`BattleEngine.java` — Handles turn-based combat mechanics including hero actions, monster AI, damage calculations, spell effects, and victory/defeat conditions.

`MarketEngine.java` — Manages marketplace interactions allowing heroes to buy and sell weapons, armor, potions, and spells with level and gold requirements.

`GameDatabase.java` — Singleton class responsible for loading and managing all game data from text files including heroes, monsters, weapons, armor, potions, and spells.

### Character System

`Character.java` — Abstract base class for all game characters defining common attributes like name, level, HP, and basic combat methods.

`Hero.java` — Player character implementation extending Character with attributes (mana, strength, dexterity, agility), inventory management, equipment handling, experience/leveling system, and combat calculations.

`Monster.java` — Enemy character implementation with base damage, defense, dodge mechanics, and spell effect vulnerabilities.

`Party.java` — Container class managing the group of heroes with methods for party-wide operations and status checks.

`HeroType.java` — Enum defining hero classes (WARRIOR, PALADIN, SORCERER) with associated attribute bonuses.

`MonsterType.java` — Enum defining monster types (DRAGON, EXOSKELETON, SPIRIT) with different characteristic modifiers.

`MonsterFactory.java` — Factory pattern implementation for creating balanced monster groups scaled to party size and hero levels.

### Items System

`Item.java` — Abstract base class for all purchasable/usable items with properties like name, price, required level, and durability.

`Weapon.java` — Concrete item implementation for weapons with damage values and hands-required specification for combat calculations.

`Armor.java` — Concrete item implementation providing damage reduction when equipped by heroes during combat.

`Potion.java` — Consumable item that can boost HP, MP, or hero attributes (strength, dexterity, agility) when used.

`Spell.java` — Magical attack item with mana cost, damage calculation, and special effects that debuff monsters.

`Inventory.java` — Container class managing a hero's collection of items with type-specific retrieval methods and item management.

`PotionType.java` — Enum defining potion effects (HEALTH, MANA, STRENGTH, DEXTERITY, AGILITY).

`SpellType.java` — Enum defining spell elements (FIRE, ICE, LIGHTNING) with corresponding monster debuffs.

### World System

`World.java` — Manages the 8x8 game map with procedural tile generation, party movement validation, and tile type distribution.

`Tile.java` — Represents individual map cells with type information (common, market, inaccessible) and party presence tracking.

`TileType.java` — Enum defining the three tile types that determine available actions and battle probability.

### Utility

`InputHelper.java` — Centralized input handling utility with validation for integers, characters, and bounded numeric input to ensure robust user interaction.

## Design Choices

---------------------------------------------------------------------------

- **Singleton Pattern for Data Management**: The GameDatabase class implements the singleton pattern to ensure a single, consistent source of game data loaded from files. This prevents redundant file I/O and maintains data integrity across all game systems.

- **Factory Pattern for Monster Creation**: MonsterFactory encapsulates monster instantiation logic and provides automatic level scaling based on hero levels, ensuring balanced encounters without exposing creation complexity to the game engine.

- **Separation of Concerns**: Clear architectural boundaries between game engines (GameEngine, BattleEngine, MarketEngine), character systems, items, world management, and data loading. Each component has a single, well-defined responsibility.

- **Polymorphic Character System**: The abstract Character class allows uniform treatment of heroes and monsters while enabling specialized behavior through inheritance, making it easy to add new character types.

- **Encapsulation and Information Hiding**: Each class manages its own state with controlled access through public methods, preventing unintended side effects and making the system more robust and testable.

## Cool Features / Creative Choices

---------------------------------------------------------------------------

- **Smart Armor Scaling**: Armor damage reduction values are automatically scaled down by the same factor as monster stats to maintain game balance, demonstrating awareness of the interplay between different game systems.

- **Inventory Management**: Heroes can equip weapons, armor, use potions, and manage spells both in and out of combat, with separate inventory screens accessible anytime via the 'V' command.

- **Spell Effects**: Spells inflict lasting debuffs on monsters (reduced defense, damage, or dodge chance) that persist throughout the battle, rewarding tactical spell use over pure damage output.

- **End of Round Recovery**: Heroes automatically recover 10% HP and MP at the end of each combat round, ensuring longer battles remain viable and reducing the need for constant potion use.

- **Turn Flow**: Battle automatically ends when all monsters are defeated mid-turn, preventing unnecessary prompts for remaining heroes and maintaining smooth gameplay pacing.

## How to compile and run
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

===========================================
  LEGENDS: MONSTERS AND HEROES
  A Tale of Courage, Magic, and Glory
===========================================

The realm is under siege by fearsome monsters...
Brave heroes are needed to restore peace to the land!

=== ASSEMBLE YOUR PARTY ===

How many heroes will join your quest? (1-3): 1

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


Your epic journey begins!
May fortune favor the bold...


  . . . M . M . X 
  . X . M M M . . 
  M M . . M . P . 
  . M . . M M . . 
  . . X M X . . M 
  X . . X M M X M 
  . . X . M . . X 
  . M X X X . M . 

Controls:
W/A/S/D - Move
M - Market
I - Info
V - Inventory
Q - Quit

Enter command: a

The path ahead is quiet. Your party continues onward.

  . . . M . M . X 
  . X . M M M . . 
  M M . . M P . . 
  . M . . M M . . 
  . . X M X . . M 
  X . . X M M X M 
  . . X . M . . X 
  . M X X X . M . 

Controls:
W/A/S/D - Move
M - Market
I - Info
V - Inventory
Q - Quit

Enter command: a

You've discovered a bustling marketplace!
Press [M] to browse wares and trade goods.

  . . . M . M . X 
  . X . M M M . . 
  M M . . P . . . 
  . M . . M M . . 
  . . X M X . . M 
  X . . X M M X M 
  . . X . M . . X 
  . M X X X . M . 

Controls:
W/A/S/D - Move
M - Market
I - Info
V - Inventory
Q - Quit

Enter command: m

=== WELCOME TO THE MARKET ===
A merchant greets you warmly...


=== MARKET MENU ===
1) Buy Items
2) Sell Items
3) View Heroes
4) Leave Market
1

=== Select Hero ===
1) Gaerdal_Ironhand (1354 gold)
1

Gold Available: 1354

=== Item Categories ===
1) Weapons
2) Armor
3) Potions
4) Spells
5) Back
1

=== SHOP INVENTORY ===
1) Sword | Price: 500 | Level: 1
2) Bow | Price: 300 | Level: 2 (Cannot buy)
3) Scythe | Price: 1000 | Level: 6 (Cannot buy)
4) Axe | Price: 550 | Level: 5 (Cannot buy)
5) TSwords | Price: 1400 | Level: 8 (Cannot buy)
6) Dagger | Price: 200 | Level: 1
Choose item number or 0 to cancel
1

Gaerdal_Ironhand purchased Sword for 500 gold!

=== MARKET MENU ===
1) Buy Items
2) Sell Items
3) View Heroes
4) Leave Market
4

  . . . M . M . X 
  . X . M M M . . 
  M M . . P . . . 
  . M . . M M . . 
  . . X M X . . M 
  X . . X M M X M 
  . . X . M . . X 
  . M X X X . M . 

Controls:
W/A/S/D - Move
M - Market
I - Info
V - Inventory
Q - Quit

Enter command: a

The path ahead is quiet. Your party continues onward.

  . . . M . M . X 
  . X . M M M . . 
  M M . P M . . . 
  . M . . M M . . 
  . . X M X . . M 
  X . . X M M X M 
  . . X . M . . X 
  . M X X X . M . 

Controls:
W/A/S/D - Move
M - Market
I - Info
V - Inventory
Q - Quit

Enter command: v

=== Inventory Management ===
Choose hero:
1) Gaerdal_Ironhand
0) Back
1

=== Gaerdal_Ironhand's Inventory ===
1) Equip Weapon
2) Equip Armor
3) Use Potion
4) View Inventory
5) Back
4

=== Gaerdal_Ironhand's Full Inventory ===

Weapons:
  - Sword | Damage: 800 | Hands: 1

Armor:
  None

Potions:
  None

Spells:
  None

Currently Equipped:
  Weapon: None
  Armor: None

=== Gaerdal_Ironhand's Inventory ===
1) Equip Weapon
2) Equip Armor
3) Use Potion
4) View Inventory
5) Back
1

Weapons:
1) Sword | Damage: 800 | Hands: 1
0) Cancel
Choose weapon: 1
Gaerdal_Ironhand equipped Sword.

=== Gaerdal_Ironhand's Inventory ===
1) Equip Weapon
2) Equip Armor
3) Use Potion
4) View Inventory
5) Back
5

  . . . M . M . X 
  . X . M M M . . 
  M M . P M . . . 
  . M . . M M . . 
  . . X M X . . M 
  X . . X M M X M 
  . . X . M . . X 
  . M X X X . M . 

Controls:
W/A/S/D - Move
M - Market
I - Info
V - Inventory
Q - Quit

Enter command: a

=== BATTLE COMMENCES! ===
Monsters emerge from the shadows to challenge your party!


=== YOUR PARTY ===
Gaerdal_Ironhand | HP: 100 | MP: 100 | Status: FIGHTING

=== ENEMIES ===
Chronepsish | Level: 1 | HP: 100 | Damage: 650

--- Gaerdal_Ironhand's Turn ---
1) Attack
2) Cast Spell
3) Use Potion
4) Change Equipment
5) Battle Info
6) Skip Turn
1

Select target:
1) Chronepsish (HP: 100)
1
Gaerdal_Ironhand strikes Chronepsish for 74 damage!

--- Monsters' Turn ---
Chronepsish attacked Gaerdal_Ironhand for 32 damage.

=== YOUR PARTY ===
Gaerdal_Ironhand | HP: 74 | MP: 110 | Status: FIGHTING

=== ENEMIES ===
Chronepsish | Level: 1 | HP: 26 | Damage: 650

--- Gaerdal_Ironhand's Turn ---
1) Attack
2) Cast Spell
3) Use Potion
4) Change Equipment
5) Battle Info
6) Skip Turn
1

Select target:
1) Chronepsish (HP: 26)
1
Gaerdal_Ironhand strikes Chronepsish for 74 damage!
Chronepsish has been slain!
All monsters have been defeated!
Gaerdal_Ironhand gained 100 gold and 2 experience.

  . . . M . M . X 
  . X . M M M . . 
  M M P . M . . . 
  . M . . M M . . 
  . . X M X . . M 
  X . . X M M X M 
  . . X . M . . X 
  . M X X X . M . 

Controls:
W/A/S/D - Move
M - Market
I - Info
V - Inventory
Q - Quit

Enter command: q

Thanks for playing! Safe travels, hero!

```
