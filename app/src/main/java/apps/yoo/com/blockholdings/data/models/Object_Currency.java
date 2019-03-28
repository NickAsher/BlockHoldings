package apps.yoo.com.blockholdings.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "table_currency")
public class Object_Currency {

    @PrimaryKey
    @ColumnInfo(name = "currencyId")
    @NonNull  String currencyId ;

    @ColumnInfo(name = "currencyName")
    String currencyName ;

    @ColumnInfo(name = "currencySymbol")
    String currencySymbol ;




    public Object_Currency(String currencyId, String currencyName, String currencySymbol) {
        this.currencyId = currencyId;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
    }


    public String getCurrencyId() {
        return currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    @Override
    public String toString() {
        return currencyId + " - " + currencyName + " - " + currencySymbol ;
    }

    public static List<Object_Currency> getListOfAllCurrencies(){
        List<Object_Currency> listOfCurrencies = new ArrayList<>() ;
        listOfCurrencies.add(new Object_Currency("aed", "United Arab Emirates Dirham", "DH")) ;
        listOfCurrencies.add(new Object_Currency("ars", "Argentine Peso", "$")) ;
        listOfCurrencies.add(new Object_Currency("aud", "Australian Dollar", "A$")) ;
        listOfCurrencies.add(new Object_Currency("bdt", "Bangladeshi Taka", "\u09F3")) ;
        listOfCurrencies.add(new Object_Currency("bhd", "Bahraini Dinar", "BD")) ;
        listOfCurrencies.add(new Object_Currency("bmd", "Bermudian Dollar", "$")) ;
        listOfCurrencies.add(new Object_Currency("brl", "Brazilian Real", "R$")) ;
        listOfCurrencies.add(new Object_Currency("cad", "Canadian Dollar", "CA$")) ;
        listOfCurrencies.add(new Object_Currency("chf", "Swiss Franc", "Fr.")) ;
        listOfCurrencies.add(new Object_Currency("clp", "Chilean Peso", "CLP$")) ;
        listOfCurrencies.add(new Object_Currency("cny", "Chinese Yuan", "\u00A5")) ;
        listOfCurrencies.add(new Object_Currency("czk", "Czech Koruna", "K\u010D")) ;
        listOfCurrencies.add(new Object_Currency("dkk", "Danish Krone", "kr.")) ;
        listOfCurrencies.add(new Object_Currency("eur", "Euro", "\u20AC")) ;
        listOfCurrencies.add(new Object_Currency("gbp", "Bristish Pound", "\u00A3")) ;
        listOfCurrencies.add(new Object_Currency("hkd", "Hong Kong Dollar", "HK$")) ;
        listOfCurrencies.add(new Object_Currency("huf", "Hungarian Forint", "Ft")) ;
        listOfCurrencies.add(new Object_Currency("idr", "Indonesian Rupiah", "Rp")) ;
        listOfCurrencies.add(new Object_Currency("ils", "Israeli Shekel", "\u20AA")) ;
        listOfCurrencies.add(new Object_Currency("inr", "Indian Rupee", "\u20B9")) ;
        listOfCurrencies.add(new Object_Currency("jpy", "Japanese Yen", "\u00A5")) ;
        listOfCurrencies.add(new Object_Currency("krw", "South Korean Won", "\u20A9")) ;
        listOfCurrencies.add(new Object_Currency("kwd", "Kuwaiti Dinar" ,"KD")) ;
        listOfCurrencies.add(new Object_Currency("lkr", "Sri Lankan Rupee", "Rs")) ;
        listOfCurrencies.add(new Object_Currency("mnk", "Burmese Kyat", "K")) ;
        listOfCurrencies.add(new Object_Currency("mxn", "Mexican Peso", "MX$")) ;
        listOfCurrencies.add(new Object_Currency("myr", "Malaysian Ringitt", "RM")) ;
        listOfCurrencies.add(new Object_Currency("nok", "Norwegian Krone", "kr")) ;
        listOfCurrencies.add(new Object_Currency("nzd", "New Zealand Dollar", "NZ$")) ;
        listOfCurrencies.add(new Object_Currency("php", "Philippine Peso", "\u20B1")) ;
        listOfCurrencies.add(new Object_Currency("pkr", "Pakistani Rupee", "Rs")) ;
        listOfCurrencies.add(new Object_Currency("pln", "Polish Zloty", "z\u0142")) ;
        listOfCurrencies.add(new Object_Currency("rub", "Russian Ruble", "\u20BD")) ;
        listOfCurrencies.add(new Object_Currency("sar", "Saudi Arabian Riyal", "\uFDFC")) ;
        listOfCurrencies.add(new Object_Currency("sek", "Swedish Krone", "kr")) ;
        listOfCurrencies.add(new Object_Currency("sgd", "Singapore Dollar", "S$" )) ;
        listOfCurrencies.add(new Object_Currency("thb", "Thai Baht", "\u0E3F")) ;
        listOfCurrencies.add(new Object_Currency("try", "Turkish Lira", "\u20BA")) ;
        listOfCurrencies.add(new Object_Currency("twd", "Taiwan New Dollar", "NT$")) ;
        listOfCurrencies.add(new Object_Currency("usd", "United States Dollar", "$")) ;
        listOfCurrencies.add(new Object_Currency("vef", "Venezuelan Bolívar", "Bs.")) ;
        listOfCurrencies.add(new Object_Currency("zar", "South African" , "R")) ;


        return listOfCurrencies ;

    }
}
