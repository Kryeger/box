package utils;

import java.io.Serializable;
import java.math.BigInteger;

public class Money implements Serializable {

    private BigInteger _amount;

    public Money(String amount){
        setAmount(amount);
    }

    public Money(Money amount){
        setAmount(amount);
    }

    public void setAmount(Money amount){
        _amount = amount._amount;
    }

    public void setAmount(String amount){
        if(amount.equals("0")){
            _amount = new BigInteger("0");
            return;
        }
        _amount = convert(amount);
    }

    public String toString() {
        BigInteger thousand = new BigInteger("1000");
        BigInteger divided = _amount.divide(thousand);
        String remainder = _amount.remainder(thousand).toString();
        return '$' + divided.toString() + '.' + remainder;
    }

    public BigInteger convert(String amount){
        try {

            String[] split = amount.split("\\.");

            if(split.length == 1){
                return new BigInteger(amount + "000");
            }

            if(split.length != 2){
                throw new Exception("Wrong number passed to Money method (" + amount + ")");
            }

            StringBuilder whole = new StringBuilder(split[0]);
            StringBuilder decimal = new StringBuilder(split[1]);

            while(decimal.length() < 3){
                decimal.append('0');
            }

            whole.append(decimal);

            BigInteger finalInt = new BigInteger(whole.toString());
            BigInteger finalNumber = finalInt.multiply(new BigInteger("1000"));

            return new BigInteger(finalNumber.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void plus(int amount){
        plus(String.valueOf(amount));
    }

    public void plus(String amount){
        if(!amount.equals("0"))
            _amount = _amount.add(convert(amount));
    }

    public BigInteger getAmount() {
        return _amount;
    }

    public void plus(Money amount) {
        _amount = _amount.add(amount.getAmount());
    }

    public void minus(Money amount) {
        _amount = _amount.subtract(amount.getAmount());
    }

    public void minus(int amount){
        minus(String.valueOf(amount));
    }

    public void minus(String amount){
        if(!amount.equals("0"))
            _amount = _amount.subtract(convert(amount));
    }

    public boolean lessThan(Money amount){
        return (_amount.compareTo(amount._amount) <= -1);
    }

}
