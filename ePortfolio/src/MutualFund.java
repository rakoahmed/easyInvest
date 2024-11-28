package rako_a3.ePortfolio.src;

/**
 * Represents a mutual fund investment.
 * Extends the Investment superclass.
 * Implements specific methods for buying, selling, and calculating gains for mutual funds.
 * Includes a redemption fee for selling.
 * 
 * @author  Rako Ahmed
 * @version 3.0
 */
public class MutualFund extends Investment {
    private static final double REDEMPTION_FEE = 45.00;

    /**
     * Default constructor.
     */
    public MutualFund() {
        super();
    }

    /**
     * Constructs a new MutualFund.
     * 
     * @param symbol   The mutual fund symbol.
     * @param name     The name of the mutual fund.
     * @param quantity The quantity purchased.
     * @param price    The price per unit.
     * @throws IllegalArgumentException if any arguments are invalid.
     */
    public MutualFund(String symbol, String name, int quantity, double price) {
        super(symbol, name, quantity, price);
        this.bookValue = quantity * price;
    }

    @Override
    public void buy(int quantity, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        double additionalBookValue = quantity * price;
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
            throw new IllegalArgumentException("Not enough units to sell.");
        }
        double payment = quantity * price - REDEMPTION_FEE;
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
        return (quantity * price) - REDEMPTION_FEE - bookValue;
    }
}
