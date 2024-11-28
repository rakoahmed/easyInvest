package rako_a3.ePortfolio.src;

import java.util.Objects;

/**
 * Abstract superclass representing a general investment.
 * Contains common attributes and methods shared by all investments.
 * Subclasses implement abstract methods for buying, selling, and calculating gains.
 * 
 * @author  Rako Ahmed
 * @version 3.0
 */
public abstract class Investment {
    protected String symbol;
    protected String name;
    protected int quantity;
    protected double price;
    protected double bookValue;

    /**
     * Default constructor.
     */
    public Investment() {
    }

    /**
     * Constructs an Investment with the specified details.
     * 
     * @param symbol    The investment symbol.
     * @param name      The investment name.
     * @param quantity  The quantity purchased.
     * @param price     The price per unit.
     * @throws IllegalArgumentException if quantity <= 0 or price < 0, or symbol/name is empty.
     */
    public Investment(String symbol, String name, int quantity, double price) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.symbol = symbol.toUpperCase();
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = 0.0;
    }

    // Getters and setters

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be empty.");
        }
        this.symbol = symbol.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

     public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }
  
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public double getBookValue() {
        return bookValue;
    }

    public void setBookValue(double bookValue) {
        if (bookValue < 0) {
            throw new IllegalArgumentException("Book value cannot be negative.");
        }
        this.bookValue = bookValue;
    }

    /**
     * Buys additional units of the investment.
     * 
     * @param quantity The quantity to buy.
     * @param price    The price per unit.
     * @throws IllegalArgumentException if quantity <= 0 or price < 0.
     */
    public abstract void buy(int quantity, double price);

    /**
     * Sells units of the investment.
     * 
     * @param quantity The quantity to sell.
     * @param price    The price per unit.
     * @return The payment received from the sale.
     * @throws IllegalArgumentException if quantity <= 0, price < 0, or quantity > available quantity.
     */
    public abstract double sell(int quantity, double price);

    /**
     * Calculates the gain for this investment.
     * 
     * @return The gain.
     */
    public abstract double getGain();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Investment))
            return false;
        Investment other = (Investment) obj;
        return symbol.equalsIgnoreCase(other.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol.toLowerCase());
    }

    @Override
    public String toString() {
        return "Investment [symbol=" + symbol + ", name=" + name + ", quantity=" + quantity + ", price=" + price
                + ", bookValue=" + bookValue + "]";
    }
}
