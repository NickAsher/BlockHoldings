package apps.yoo.com.blockholdings.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;
import java.math.RoundingMode;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;

public class Utils {


    public static String showHumanDecimals(String string_W_LotsOfDecimals){
//        removeMinusSign(string_W_LotsOfDecimals) ;
        BigDecimal no = new BigDecimal(string_W_LotsOfDecimals) ;

        // when no. is less than 0
        if(no.compareTo(new BigDecimal(1)) == -1  ){
            return no.setScale(5, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() ;
        } else {
            return no.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() ;
        }
    }




    public static String formatNumber_ie_MarketCap(String num){
        BigDecimal Trillion = new BigDecimal("1000000000000") ;
        BigDecimal Billion = new BigDecimal("1000000000") ;
        BigDecimal Million = new BigDecimal("1000000") ;
        BigDecimal Thousand = new BigDecimal("1000") ;

        BigDecimal myNumber = new BigDecimal(num) ;


        if(myNumber.compareTo(Thousand)>0  && myNumber.compareTo(Million)<0){
            // number is in thousands
            return myNumber.divide(Thousand).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + " K" ;
        }

        else if(myNumber.compareTo(Million)>0  && myNumber.compareTo(Billion)<0){
            // number is in Millions
            return myNumber.divide(Million).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + " M" ;
        }

        else if(myNumber.compareTo(Billion)>0  && myNumber.compareTo(Trillion)<0){
            // number is in billions
            return myNumber.divide(Billion).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + " B" ;
        }

        else if(myNumber.compareTo(Trillion)>0){
            // number is in trillions
            return myNumber.divide(Trillion).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + " T" ;
        }

        else{
            // number is in hundreds
            return myNumber.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() ;
        }


    }




    public static String formatNumber_ie_SingleCoinPriceCurrency(String num){
        BigDecimal no = new BigDecimal(num) ;

        // when no. is less than 0
        if(no.compareTo(new BigDecimal(1)) == -1  ){
            return no.setScale(5, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() ;
        } else {
            return no.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() ;
        }
    }


    public static String formatNumber_ie_PercentageChange(String num){
        return new BigDecimal(num).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() ;
    }

    public static String formatNumber_ie_Commas(String num){

        if(num.indexOf('.') > 0){
            // String is a decimal number
        String partBeforeDecimal = num.substring(0, num.indexOf('.')) ;
        String partAfter_withDecimal = num.substring(num.indexOf('.')) ;

        return formatNumberWithoutDecimal_ie_Comma(partBeforeDecimal) + partAfter_withDecimal ;

        } else {
            // it is not a decimal number
            return formatNumberWithoutDecimal_ie_Comma(num) ;

        }







    }


    private static String formatNumberWithoutDecimal_ie_Comma(String num){
        StringBuilder reve = new StringBuilder(num).reverse() ;


        StringBuilder returnString = new StringBuilder() ;
        for(int i = 0; i<reve.length()  ; i++) {
            returnString.append(reve.charAt(i)) ;
            if((i+1)%3 == 0){
                returnString.append(',') ;
            }
        }

        if(num.length()>=3 && num.length()%3 == 0){
            return returnString.reverse().substring(1) ;
        } else {
            return  returnString.reverse().toString() ;
        }
    }

    public static String removeMinusSign(String no){
        if(no.charAt(0) == '-'){
            return no.substring(1) ;
        } else {
            return no ;
        }
    }











}
