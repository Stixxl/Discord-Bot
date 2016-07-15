/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package help;

import com.corbi.robot.utilities.UtilityMethods;

/**
 *
 * @author PogChamp
 */
public class Help {
//command stats
    CommandHelp stats = new CommandHelp("stats", "Zeigt wer den längsten hat und wie lang er eigentlich ist.", new CommandHelp[]{
        new CommandHelp("me", "Gibt deine eigenen Stats aus."),
        new CommandHelp("ranking", "Wählt die besten der besten aus. Gib eine Zahl an um die besten n Personen auszuwählen, etwa so: " + UtilityMethods.highlightStringItalic("!hydra stats ranking 3")),
        new CommandHelp("name", "Wählt eine Person nach Namen aus, etwa so: " + UtilityMethods.highlightStringItalic("!hydra stats name Mr. Poopybutthole")),
        new CommandHelp("all", "FUSION! GENKIDAMA, Kombiniert eure Kraft. Gibt die kombinierten Stats von euch an.")});
//command sounds
    CommandHelp sounds = new CommandHelp("sounds", "Spielt danke Memes ab.", new CommandHelp[]{
        new CommandHelp("rko", "WATCH OUT WATCH OUT WATCH OUT!"),
        new CommandHelp("cena", "What's his name?"),
        new CommandHelp("faker", "reaction to Corbi's daily plays.")
    });
    CommandHelp binsenweissheiten = new CommandHelp("binsenweißheit", "Gibt Tipps und Tricks fürs Leben.");
    CommandHelp daniel = new CommandHelp("daniel", "Flamt Daniel.");
    CommandHelp[] commandMans = new CommandHelp[]{stats, sounds, binsenweissheiten, daniel};

    /**
     * 
     * @return a String containing all high level (meaning commands directly after !hydra) commands
     */
    public String showHelp() {
        StringBuilder sb = new StringBuilder("Folgende Befehle existieren:");
        for (CommandHelp help : commandMans) {
            sb.append(System.lineSeparator()).append(formatHelp(help));
        }
        return sb.toString();
    }
    /**
     * returns a String for the queried command
     * @param args contains parts of the queried command
     * @param command the high level (meaning commands diretly after !hydra) command
     * @return a String with Information about the queried command, aswell as itsdirect subcommands
     */
    public String showHelp(String[] args, String command) {
        for (CommandHelp help : commandMans) {
            if (help.getName().equals(command)) {//true if the specified high level command by the user exists; false otherwise
                return showHelpRecursively(args, help, 0);
            }
        }
        return "Netter Versuch, aber den Befehl gibts nicht.";
    }
    /**
     * looks for a given command and returns a String if it exists
     * @param args the command chain
     * @param help the command which is currently being processed
     * @param i points to the next argument
     * @return a String which gives information about the queried command; if the command does not exist it will instead return an error message
     */
    private String showHelpRecursively(String[] args, CommandHelp help, int i) {
        CommandHelp[] subcommands = help.getSubcommands();
        if (args.length == i) {// we reached the end of the command chain and want to give information about the current command
            StringBuilder sb = new StringBuilder(formatHelp(help));
            sb.append(System.lineSeparator()).append("Der Befehl besitzt folgende Unterbefehle: ");
            for (CommandHelp subhelp : subcommands) {
                sb.append(System.lineSeparator()).append(formatHelp(subhelp));
            }
            return sb.toString();
        } else {
            for (CommandHelp subhelp : subcommands) { //calls itself if and only if a subcommand with the specified name exists
                if (subhelp.getName().equals(args[i])) {
                    return showHelpRecursively(args, subhelp, ++i); // we move on to the next command in the chain
                }
            }
            //no command with the name exists
            return "Ne, Ne , Ne, das sagt mir so garnix. Dir ist klar, dass du nur Befehle eingeben solltest, die auch existieren...";
        }
    }
    /**
     * formats a CommandHelp Object like this: <b>name</b> - description
     * @param help the help for an command, which should be formatted
     * @return a formatted String for a given help of a command
     */
    private String formatHelp(CommandHelp help) {
        return UtilityMethods.highlightStringBold(help.getName()) + " - " + help.getDescription();
    }
}
