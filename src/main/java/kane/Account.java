package kane;

import java.math.BigDecimal;

public class Account {

    private BigDecimal balance = BigDecimal.ZERO;

    public Account(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new NegativeAmountException(amount);

        balance = balance.subtract(amount);
    }
}
