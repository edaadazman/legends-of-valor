package combat;

import characters.Character;

/**
 * Interface for combat actions that can be performed by any character.
 */
public interface CombatAction {
    /**
     * Execute the combat action.
     */
    boolean execute(Character attacker, Character defender);
    
    /**
     * Get the name of this action.
     */
    String getActionName();
}