package combat;

import characters.Hero;
import characters.Monster;
import characters.MonsterFactory;
import characters.Party;
import items.Spell;
import util.InputHelper;
import world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles turn-based combat for Monsters and Heroes game mode.
 * Extends AbstractBattleEngine to reuse common combat logic.
 */
public class MHBattleEngine extends BattleEngine {

    private boolean quitRequested = false;

    public MHBattleEngine(World world, Party party) {
        super(world, party, new ArrayList<>());
    }

    /**
     * Check if quit was requested during battle.
     */
    public boolean isQuitRequested() {
        return quitRequested;
    }

    /**
     * Start a battle between the party and monsters.
     */
    public boolean startBattle(Party party) {
        this.party = party;
        this.monsters = MonsterFactory.createMonsterGroup(party.size(), party.getHighestLevel());
        this.combatExecutor = new CombatExecutor(null, party, monsters);
        this.quitRequested = false;

        System.out.println("\n=== BATTLE COMMENCES! ===");
        System.out.println("Monsters emerge from the shadows to challenge your party!\n");
        displayBattleStatus();

        while (!isBattleOver() && !quitRequested) {
            executeHeroTurn();

            if (quitRequested) {
                return false;
            }

            if (monsters.isEmpty()) {
                handleVictory();
                return true;
            }

            executeMonsterTurn();

            if (party.allFainted()) {
                handleDefeat();
                return false;
            }

            // Heroes recover at end of round
            for (Hero hero : party.getHeroes()) {
                if (hero.isAlive()) {
                    hero.recover();
                }
            }
            displayBattleStatus();
        }

        return !party.allFainted() && !quitRequested;
    }

    /**
     * Execute hero turn - each hero takes an action.
     */
    private void executeHeroTurn() {
        for (Hero hero : party.getHeroes()) {
            if (hero.isFainted() || quitRequested) {
                continue;
            }

            if (monsters.isEmpty()) {
                return;
            }

            boolean actionTaken = false;
            while (!actionTaken && !quitRequested) {
                displayCombatMenu(hero);
                System.out.println("Q) Quit Game");
                char input = Character.toLowerCase(InputHelper.readChar("Choice: "));

                if (input == 'q') {
                    quitRequested = true;
                    System.out.println("\nQuitting game...");
                    return;
                }

                int choice = input - '0'; // Convert char to int
                if (choice >= 1 && choice <= 6) {
                    actionTaken = processHeroAction(hero, choice);
                } else {
                    System.out.println("Invalid choice. Enter 1-6 or Q to quit.");
                }

                if (monsters.isEmpty()) {
                    return;
                }
            }
        }
    }

    /**
     * Execute monster turn - each monster attacks a random hero.
     */
    private void executeMonsterTurn() {
        System.out.println("\n--- Monsters' Turn ---");

        for (Monster monster : new ArrayList<>(monsters)) {
            if (monster.isFainted()) {
                continue;
            }

            List<Hero> aliveHeroes = new ArrayList<>();
            for (Hero hero : party.getHeroes()) {
                if (hero.isAlive()) {
                    aliveHeroes.add(hero);
                }
            }

            if (aliveHeroes.isEmpty()) {
                break;
            }

            Hero target = aliveHeroes.get(random.nextInt(aliveHeroes.size()));
            monsterAttack(monster, target);
        }
    }

    /**
     * Process hero action choice.
     */
    private boolean processHeroAction(Hero hero, int choice) {
        switch (choice) {
            case 1:
                return heroAttackAction(hero);
            case 2:
                return heroCastSpellAction(hero);
            case 3:
                return heroUsePotion(hero);
            case 4:
                heroChangeEquipment(hero);
                return false;
            case 5:
                displayBattleStatus();
                return false;
            case 6:
                System.out.println(hero.getName() + " waits and observes...");
                return true;
            default:
                return false;
        }
    }

    /**
     * Hero attacks - M&H specific implementation.
     */
    private boolean heroAttackAction(Hero hero) {
        Monster target = selectMonsterTarget(monsters);
        if (target == null) {
            return false;
        }
        return heroAttack(hero, target);
    }

    /**
     * Hero casts spell - M&H specific implementation.
     */
    private boolean heroCastSpellAction(Hero hero) {
        Spell spell = selectSpell(hero);
        if (spell == null) {
            return false;
        }

        if (hero.getMana() < spell.getManaCost()) {
            System.out.println("Insufficient mana! Need " + spell.getManaCost() +
                    " MP, have " + hero.getMana() + " MP.");
            return false;
        }

        Monster target = selectMonsterTarget(monsters);
        if (target == null) {
            return false;
        }

        return heroCastSpell(hero, spell, target);
    }

    /**
     * Display current battle status.
     */
    private void displayBattleStatus() {
        System.out.println("\n=== YOUR PARTY ===");
        for (Hero hero : party.getHeroes()) {
            String status = hero.isFainted() ? "FAINTED" : "FIGHTING";
            System.out.println(hero.getName() + " | HP: " + hero.getHp() +
                    " | MP: " + hero.getMana() + " | Status: " + status);
        }

        System.out.println("\n=== ENEMIES ===");
        for (Monster monster : monsters) {
            System.out.println(monster.getName() + " | Level: " + monster.getLevel() +
                    " | HP: " + monster.getHp() + " | Damage: " + monster.getBaseDamage());
        }
    }

    /**
     * Check if battle is over.
     */
    private boolean isBattleOver() {
        return monsters.isEmpty() || party.allFainted();
    }

    /**
     * Handle victory.
     */
    private void handleVictory() {
        System.out.println("\n=== VICTORY! ===");
        System.out.println("All monsters have been defeated!");

        int monsterCount = party.size();

        for (Hero hero : party.getHeroes()) {
            if (hero.isAlive()) {
                int goldGain = party.getHighestLevel() * 100;
                int expGain = monsterCount * 2;

                hero.addGold(goldGain);
                hero.addExperience(expGain);

                System.out.println(hero.getName() + " gained " + goldGain +
                        " gold and " + expGain + " experience.");
            } else {
                hero.revive();
            }
        }
    }

    /**
     * Handle defeat.
     */
    private void handleDefeat() {
        System.out.println("\n=== DEFEAT ===");
        System.out.println("Your party has fallen in battle...");
        System.out.println("GAME OVER");
    }
}
