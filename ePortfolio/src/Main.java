package rako_a3.ePortfolio.src;

import javax.swing.SwingUtilities;

/**
 * The Main class serves as the entry point for the ePortfolio application.
 * It initializes the portfolio and launches the GUI.
 * 
 * @author Rako Ahmed
 * @version 3.0
 * 
 * Commands to compile and run:
 * Compile: javac -d ePortfolio/bin ePortfolio/src/*.java
 * Run: java -cp ePortfolio/bin rako_a3.ePortfolio.src.Main investments.txt 
 */
public class Main {
    public static void main(String[] args) {
        String filename = "investments.txt"; // Default filename
        if (args.length > 0) {
            filename = args[0];
        }

        Portfolio portfolio = new Portfolio();
        portfolio.loadInvestments(filename);

        SwingUtilities.invokeLater(() -> {
            EPortfolioGUI gui = new EPortfolioGUI(portfolio, "investments.txt");
        });
    }
}
