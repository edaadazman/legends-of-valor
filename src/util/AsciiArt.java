package util;

/**
 * Utility class for colorized ASCII art displays.
 * Uses ANSI color codes for terminal output.
 */
public class AsciiArt {
    
    // ANSI Color Codes
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    // Bright/Bold Colors
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
    
    // Text Styles
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    
    /**
     * Display main menu banner with LEGENDS ASCII art.
     */
    public static void displayMainMenu() {
        System.out.println("\n" + BRIGHT_CYAN + "╔════════════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + "                                                                            " + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + BRIGHT_YELLOW + "                    __    ___________________   ______  _____               " + RESET + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + BRIGHT_YELLOW + "                   / /   / ____/ ____/ ____/ | / / __ \\/ ___/               " + RESET + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + YELLOW +        "                  / /   / __/ / / __/ __/ /  |/ / / / /\\__ \\                " + RESET + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + YELLOW +        "                 / /___/ /___/ /_/ / /___/ /|  / /_/ /___/ /                " + RESET + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + BRIGHT_YELLOW + "                /_____/_____/\\____/_____/_/ |_/_____//____/                 " + RESET + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + "                                                                            " + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + BRIGHT_BLUE + BOLD + "                  G A M E   C O L L E C T I O N   2 0 2 5                   " + RESET + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + "                                                                            " + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "╠════════════════════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + "                                                                            " + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + BRIGHT_PURPLE + BOLD + "                          Choose Your Adventure!                            " + RESET + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "║" + RESET + "                                                                            " + BRIGHT_CYAN + "║" + RESET);
        System.out.println(BRIGHT_CYAN + "╚════════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
                                              
    
/**
 * Display Legends of Valor intro - Medieval battlefield theme with crossing swords.
 */
public static void displayLegendsOfValorIntro() {
    System.out.println("\n" + BRIGHT_RED + "╔═════════════════════════════════════════════════════════════════════════════╗" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + "                                                                             " + BRIGHT_RED + "║" + RESET);
    
    // Crossing swords ASCII art
    System.out.println(BRIGHT_RED + "║" + RESET + YELLOW + "                        @@@@                    @@@@                         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + YELLOW + "                        @@@@@    @@@    @@@    @@@@@                         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_YELLOW + "                           @@@@ @@@      @@@ @@@@                            " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_YELLOW + "                            @@@@@@@      @@@@@@@                             " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + YELLOW + "                            @@@@@@@@@  @@@@@@@@@                             " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + YELLOW + "                          @@@  @@@@@@@@@@@@@@  @@@                           " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_YELLOW + "                                 @@@@@@@@@@                                  " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_YELLOW + "                                 @@@@@@@@@@                                  " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + YELLOW + "                                @@@@@@@@@@@@                                 " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + YELLOW + "                              @@@@@@@  @@@@@@@                               " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + YELLOW + "                            @@@@@@@      @@@@@@@                             " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_YELLOW + "                          @@@@@@@          @@@@@@@                           " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_YELLOW + "                        @@@@@@@              @@@@@@@                         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + YELLOW + "                        @@@@@                  @@@@@                         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + "                                                                             " + BRIGHT_RED + "║" + RESET);
    
    // Title - Large block letters
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_RED + BOLD + "         ██╗     ███████╗ ██████╗ ███████╗███╗   ██╗██████╗ ███████╗         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_RED + BOLD + "         ██║     ██╔════╝██╔════╝ ██╔════╝████╗  ██║██╔══██╗██╔════╝         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + RED + BOLD + "         ██║     █████╗  ██║  ███╗█████╗  ██╔██╗ ██║██║  ██║███████╗         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + RED + BOLD + "         ██║     ██╔══╝  ██║   ██║██╔══╝  ██║╚██╗██║██║  ██║╚════██║         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_RED + BOLD + "         ███████╗███████╗╚██████╔╝███████╗██║ ╚████║██████╔╝███████║         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_RED + BOLD + "         ╚══════╝╚══════╝ ╚═════╝ ╚══════╝╚═╝  ╚═══╝╚═════╝ ╚══════╝         " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + "                                                                             " + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_YELLOW + BOLD + "                      ═══════ O F   V A L O R ═══════                        " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + "                                                                             " + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + "                                                                             " + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_YELLOW + BOLD + "              >> Defend thy Castle, vanquish thine enemies! <<               " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + BRIGHT_CYAN + BOLD + "                >> A medieval battle of strategic warfare <<                 " + RESET + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "║" + RESET + "                                                                             " + BRIGHT_RED + "║" + RESET);
    System.out.println(BRIGHT_RED + "╚═════════════════════════════════════════════════════════════════════════════╝" + RESET);
    System.out.println();
}
    
    /**
     * Display Monsters and Heroes intro - Classic dungeon crawler with pixel art.
     */
    public static void displayMonstersAndHeroesIntro() {
        System.out.println("\n" + BRIGHT_PURPLE + "╔══════════════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + "                                                                              " + BRIGHT_PURPLE + "║" + RESET);
        
        // Title
        System.out.println(BRIGHT_PURPLE + "║" + RESET + RED + BOLD + "     ██╗    ██╗ ██████╗ ███╗   ██╗███████╗████████╗███████╗██████╗ ███████╗" + RESET + BRIGHT_PURPLE + "   ║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + RED + "    ████╗ ████║██╔═══██╗████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗██╔════╝" + RESET + BRIGHT_PURPLE + "   ║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BRIGHT_RED + "    ██╔████╔██║██║   ██║██╔██╗ ██║███████╗   ██║   █████╗  ██████╔╝███████╗" + RESET + BRIGHT_PURPLE + "   ║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + RED + "    ██║╚██╔╝██║██║   ██║██║╚██╗██║╚════██║   ██║   ██╔══╝  ██╔══██╗╚════██║" + RESET + BRIGHT_PURPLE + "   ║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + RED + "    ██║ ╚═╝ ██║╚██████╔╝██║ ╚████║███████║   ██║   ███████╗██║  ██║███████║" + RESET + BRIGHT_PURPLE + "   ║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + RED + "    ╚═╝     ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝" + RESET + BRIGHT_PURPLE + "   ║" + RESET);
        
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BRIGHT_YELLOW + "                                    &                                     " + RESET + BRIGHT_PURPLE + "    ║" + RESET);
        
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BLUE + "              ██╗  ██╗███████╗██████╗  ██████╗ ███████╗███████╗               " + RESET + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BLUE + "              ██║  ██║██╔════╝██╔══██╗██╔═══██╗██╔════╝██╔════╝               " + RESET + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BRIGHT_BLUE + "              ███████║█████╗  ██████╔╝██║   ██║█████╗  ███████╗               " + RESET + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BLUE + "              ██╔══██║██╔══╝  ██╔══██╗██║   ██║██╔══╝  ╚════██║               " + RESET + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BLUE + "              ██║  ██║███████╗██║  ██║╚██████╔╝███████╗███████║               " + RESET + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BLUE + "              ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚══════╝               " + RESET + BRIGHT_PURPLE + "║" + RESET);
        
        System.out.println(BRIGHT_PURPLE + "║" + RESET + "                                                                              " + BRIGHT_PURPLE + "║" + RESET);
        
        System.out.println(BRIGHT_PURPLE + "║" + RESET + "                                                                              " + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BRIGHT_GREEN + "                     " + BOLD + "Explore dungeons, battle monsters!" + RESET + "                       " + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + BRIGHT_CYAN + "                   " + BOLD + "A classic turn-based RPG adventure awaits" + RESET + "                  " + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "║" + RESET + "                                                                              " + BRIGHT_PURPLE + "║" + RESET);
        System.out.println(BRIGHT_PURPLE + "╚══════════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Display exit/thanks message.
     */
    public static void displayExitMessage() {
        System.out.println("\n" + BRIGHT_YELLOW + "╔══════════════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + YELLOW + BOLD + "                             T H A N K   Y O U                                " + RESET + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + BRIGHT_CYAN + BOLD + "                                   F O R                                      " + RESET + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + BRIGHT_PURPLE + BOLD + "                              P L A Y I N G !                                 " + RESET + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + WHITE + "                     ════════════════════════════════                         " + RESET + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + BRIGHT_PURPLE + BOLD + "                         Your legend will live on!                            " + RESET + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + BRIGHT_WHITE + BOLD + "                           Until we meet again...                             " + RESET + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + CYAN + "                              Farewell, Hero!                                 " + RESET + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "║" + RESET + "                                                                              " + BRIGHT_YELLOW + "║" + RESET);
        System.out.println(BRIGHT_YELLOW + "╚══════════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
}