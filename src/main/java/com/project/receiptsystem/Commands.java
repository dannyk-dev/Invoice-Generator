package com.project.receiptsystem;

import org.apache.commons.cli.*;

public class Commands {
    private Options cliOptions;
    private Option id;
    private Option docNumber;
    private Option print;

    public Commands() {
        this.cliOptions = new Options();

        // sample: "-id {number}"
        this.id = new Option("id", true, "Client ID");
        this.id.setRequired(false);
        cliOptions.addOption(this.id);

        // sample: "-doc <DocNumber>" -> 8012-DVA
        this.docNumber = new Option("doc", "document", true, "Document Number");
        this.docNumber.setRequired(true);
        cliOptions.addOption(this.docNumber);

        this.print = new Option("p", "print", false, "Print Document");
        this.print.setRequired(false);
        cliOptions.addOption(this.print);
    }

    public CommandLine parseCommands(String[] args) {
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(this.cliOptions, args);
            return cmd;
        } catch(ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp("Receipt System info", this.cliOptions);
            System.exit(1);
        }

        return null;
    }
}
