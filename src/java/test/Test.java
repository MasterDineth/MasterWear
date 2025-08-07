
package test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

 class Test {
    
     public static void main(String[] args) {
         
         LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");

           String dateStr = now.format(dateFormatter);
        String timeStr = now.format(timeFormatter);


         
     }
}
