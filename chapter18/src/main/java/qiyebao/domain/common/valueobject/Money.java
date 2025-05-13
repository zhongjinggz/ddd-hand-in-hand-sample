package qiyebao.domain.common.valueobject;

import java.math.BigDecimal;

public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency, int scale) {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("金额和币种不能为空");
        }
        this.amount = amount.setScale(scale, BigDecimal.ROUND_HALF_UP); // 设置小数位数
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, Currency currency, int scale) {
        return new Money(amount, currency, scale);
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency, amount.scale());
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    // 加法：相同币种才能相加
    public Money add(Money other) {
        currencyShouldSame(other);
        int scale = Math.max(this.amount.scale(), other.amount.scale());
        return new Money(this.amount.add(other.amount), this.currency, scale);
    }

    // 减法：相同币种才能相减
    public Money subtract(Money other) {
        currencyShouldSame(other);
        int scale = Math.max(this.amount.scale(), other.amount.scale());
        return new Money(this.amount.subtract(other.amount), this.currency, scale);
    }

    // 乘法：乘以一个实数
    public Money multiply(BigDecimal factor) {
        return new Money(this.amount.multiply(factor), this.currency, this.amount.scale());
    }

    // 检查币种是否一致
    private void currencyShouldSame(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("币种不同，无法进行运算: " + this.currency + ", " + other.currency );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money other = (Money) o;
        return amount.equals(other.amount) && currency == other.currency;
    }

    @Override
    public int hashCode() {
        return 31 * amount.hashCode() + currency.hashCode();
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
