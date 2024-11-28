package rako_a3.ePortfolio.src;

import java.io.*;
import java.util.*;

/**
 * Represents a portfolio of investments.
 * Manages buying, selling, updating, and searching of investments.
 * Also handles loading and saving investments to a file.
 * Includes methods to support GUI interactions.
 * Implements exception handling for robustness.
 * 
 * Note: This class is designed for use with a GUI interface.
 * 
 * @author  Rako Ahemd
 * @version 3.0
 */
public class Portfolio {
    private List<Investment> investments;
    private Map<String, List<Integer>> nameIndex;

    /**
     * Constructs a new Portfolio.
     */
    public Portfolio() {
        investments = new ArrayList<>();
        nameIndex = new HashMap<>();
    }

    /**
     * Loads investments from a file.
     * 
     * @param filename The name of the file to load from.
     */
    public void loadInvestments(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Investment inv = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("type")) {
                    String type = line.split("=")[1].trim().replaceAll("\"", "");
                    if (type.equalsIgnoreCase("stock")) {
                        inv = new Stock();
                    } else if (type.equalsIgnoreCase("mutualfund")) {
                        inv = new MutualFund();
                    }
                } else if (line.startsWith("symbol")) {
                    String symbol = line.split("=")[1].trim().replaceAll("\"", "");
                    inv.setSymbol(symbol);
                } else if (line.startsWith("name")) {
                    String name = line.split("=")[1].trim().replaceAll("\"", "");
                    inv.setName(name);
                } else if (line.startsWith("quantity")) {
                    int quantity = Integer.parseInt(line.split("=")[1].trim().replaceAll("\"", ""));
                    inv.setQuantity(quantity);
                } else if (line.startsWith("price")) {
                    double price = Double.parseDouble(line.split("=")[1].trim().replaceAll("\"", ""));
                    inv.setPrice(price);
                } else if (line.startsWith("bookValue")) {
                    double bookValue = Double.parseDouble(line.split("=")[1].trim().replaceAll("\"", ""));
                    inv.setBookValue(bookValue);
                } else if (line.isEmpty() && inv != null) {
                    investments.add(inv);
                    indexInvestment(inv, investments.size() - 1);
                    inv = null;
                }
            }
            if (inv != null) {
                investments.add(inv);
                indexInvestment(inv, investments.size() - 1);
            }
            System.out.println("Investments loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found. Starting with an empty portfolio.");
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    /**
     * Saves investments to a file.
     * 
     * @param filename The name of the file to save to.
     */
    public void saveInvestments(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Investment inv : investments) {
                writer.println("type = \"" + (inv instanceof Stock ? "stock" : "mutualfund") + "\"");
                writer.println("symbol = \"" + inv.getSymbol() + "\"");
                writer.println("name = \"" + inv.getName() + "\"");
                writer.println("quantity = \"" + inv.getQuantity() + "\"");
                writer.println("price = \"" + inv.getPrice() + "\"");
                writer.println("bookValue = \"" + inv.getBookValue() + "\"");
                writer.println();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Adds an investment to the portfolio.
     * 
     * @param inv The investment to add.
     */
    public void addInvestment(Investment inv) {
        investments.add(inv);
        indexInvestment(inv, investments.size() - 1);
    }

    /**
     * Removes an investment from the portfolio.
     * 
     * @param inv The investment to remove.
     */
    public void removeInvestment(Investment inv) {
        int index = investments.indexOf(inv);
        if (index >= 0) {
            investments.remove(index);
            updateIndexesAfterDeletion(index);
        }
    }

    /**
     * Updates the price of all investments.
     * 
     * @param prices A map of symbols to new prices.
     */
    public void updatePrices(Map<String, Double> prices) {
        for (Investment inv : investments) {
            Double newPrice = prices.get(inv.getSymbol());
            if (newPrice != null) {
                inv.setPrice(newPrice);
            }
        }
    }

    /**
     * Gets the total gain of the portfolio.
     * 
     * @return The total gain.
     */
    public double getTotalGain() {
        double totalGain = 0;
        for (Investment inv : investments) {
            totalGain += inv.getGain();
        }
        return totalGain;
    }

    /**
     * Finds an investment by its symbol.
     * 
     * @param symbol The symbol to search for.
     * @return The Investment object, or null if not found.
     */
    public Investment findInvestmentBySymbol(String symbol) {
        for (Investment inv : investments) {
            if (inv.getSymbol().equalsIgnoreCase(symbol)) {
                return inv;
            }
        }
        return null;
    }

    /**
     * Searches for investments matching the given criteria.
     * 
     * @param symbol    The symbol to search for (can be empty).
     * @param keywords  Keywords to search for in the name (can be empty).
     * @param lowPrice  The lower bound of the price range (can be null).
     * @param highPrice The upper bound of the price range (can be null).
     * @return A list of matching investments.
     */
    public List<Investment> search(String symbol, String keywords, Double lowPrice, Double highPrice) {
        List<Integer> potentialMatches = new ArrayList<>();

        if (!keywords.isEmpty()) {
            String[] keywordArr = keywords.toLowerCase().split("\\s+");
            Set<Integer> indexSet = new HashSet<>(nameIndex.getOrDefault(keywordArr[0], Collections.emptyList()));
            for (int i = 1; i < keywordArr.length; i++) {
                indexSet.retainAll(nameIndex.getOrDefault(keywordArr[i], Collections.emptyList()));
                if (indexSet.isEmpty()) {
                    break;
                }
            }
            potentialMatches.addAll(indexSet);
        } else {
            for (int i = 0; i < investments.size(); i++) {
                potentialMatches.add(i);
            }
        }

        List<Investment> results = new ArrayList<>();
        for (int index : potentialMatches) {
            Investment inv = investments.get(index);
            if (!symbol.isEmpty() && !inv.getSymbol().equalsIgnoreCase(symbol)) {
                continue;
            }
            if (lowPrice != null && inv.getPrice() < lowPrice) {
                continue;
            }
            if (highPrice != null && inv.getPrice() > highPrice) {
                continue;
            }
            results.add(inv);
        }
        return results;
    }

    /**
     * Indexes an investment's name keywords into the nameIndex map.
     * 
     * @param inv      The investment to index.
     * @param position The position of the investment in the list.
     */
    private void indexInvestment(Investment inv, int position) {
        String[] keywords = inv.getName().toLowerCase().split("\\s+");
        for (String keyword : keywords) {
            keyword = keyword.replaceAll("[^a-zA-Z0-9]", "");
            nameIndex.computeIfAbsent(keyword, k -> new ArrayList<>()).add(position);
        }
    }

    /**
     * Updates the nameIndex after an investment is deleted.
     * 
     * @param deletedPosition The position of the deleted investment.
     */
    private void updateIndexesAfterDeletion(int deletedPosition) {
        Iterator<Map.Entry<String, List<Integer>>> iterator = nameIndex.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Integer>> entry = iterator.next();
            List<Integer> positions = entry.getValue();
            positions.removeIf(pos -> pos == deletedPosition);
            for (int i = 0; i < positions.size(); i++) {
                if (positions.get(i) > deletedPosition) {
                    positions.set(i, positions.get(i) - 1);
                }
            }
            if (positions.isEmpty()) {
                iterator.remove();
            }
        }
    }

    /**
     * Returns a copy of the investments list to prevent privacy leaks.
     * 
     * @return A copy of the investments list.
     */
    public List<Investment> getInvestments() {
        List<Investment> copyList = new ArrayList<>();
        for (Investment inv : investments) {
            if (inv instanceof Stock) {
                copyList.add(new Stock(inv.getSymbol(), inv.getName(), inv.getQuantity(), inv.getPrice()));
            } else if (inv instanceof MutualFund) {
                copyList.add(new MutualFund(inv.getSymbol(), inv.getName(), inv.getQuantity(), inv.getPrice()));
            }
        }
        return copyList;
    }
}
