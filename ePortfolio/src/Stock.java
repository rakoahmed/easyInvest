package rako_a3.ePortfolio.src;

/**
 * Represents a stock investment.
 * Extends the Investment superclass.
 * Implements specific methods for buying, selling, and calculating gains for stocks.
 * Includes a commission fee for buying and selling.
 * 
 * @author  Rako Ahmed
 * @version 3.0
 */
public class Stock extends Investment {
    private static final double COMMISSION = 9.99;

    /**
     * Default constructor.
     */
    public Stock() {
        super();
    }

    /**
     * Constructs a new Stock.
     * 
     * @param symbol   The stock symbol.
     * @param name     The name of the stock.
     * @param quantity The quantity purchased.
     * @param price    The price per share.
     * @throws IllegalArgumentException if any arguments are invalid.
     */
    public Stock(String symbol, String name, int quantity, double price) {
        super(symbol, name, quantity, price);
        this.bookValue = quantity * price + COMMISSION;
    }

    @Override
    public void buy(int quantity, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        double additionalBookValue = quantity * price + COMMISSION;
        this.bookValue += additionalBookValue;
        this.quantity += quantity;
        this.price = price;
    }

    @Override
    public double sell(int quantity, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Not enough shares to sell.");
        }
        double payment = quantity * price - COMMISSION;
        double proportion = (double) quantity / this.quantity;
        double soldBookValue = this.bookValue * proportion;
        this.bookValue -= soldBookValue;
        this.quantity -= quantity;
        this.price = price;
        if (this.quantity == 0) {
            this.bookValue = 0;
        }
        return payment;
    }

    @Override
    public double getGain() {
        return (quantity * price) - COMMISSION - bookValue;
    }
}
