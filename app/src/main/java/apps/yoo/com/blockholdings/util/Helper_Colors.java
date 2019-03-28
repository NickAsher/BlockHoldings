package apps.yoo.com.blockholdings.util;

import java.util.ArrayList;

public class Helper_Colors {

    public static ArrayList<Integer> listOfColorStart ;
    public static ArrayList<Integer> listOfColorEnd ;


    public static ArrayList<Integer> getListOfColorStart(){
        if(listOfColorStart == null){
            listOfColorStart = new ArrayList<>() ;

            listOfColorStart.add(0xFFc33764) ; // 30 Celestial
            listOfColorStart.add(0xFFff5f6d ) ; // 28 Sweet Morning
            listOfColorStart.add(0xFF02aab0 ) ; // 15 Green Beach
            listOfColorStart.add(0xFFff5f6d ) ; // 28 Sweet Morning
        }
        return listOfColorStart ;
    }


    public static ArrayList<Integer> getListOfColorEnd(){
        if(listOfColorEnd == null){
            listOfColorEnd = new ArrayList<>() ;

            listOfColorEnd.add(0xFF1d2671) ;
            listOfColorEnd.add(0xFFffc371) ;
            listOfColorEnd.add(0xFF00cdac) ;
            listOfColorEnd.add(0xFFffc371) ;
        }
        return listOfColorEnd ;
    }
}