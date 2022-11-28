import java.util.*;

public class POS {
    public static void posrandom(){
        float stakes[] = {20,30,10,5,35};
        int count = 0;
        Random rd = new Random();
        int index=-1;
        while(count<5){
           Float max= Float.MIN_VALUE;
           System.out.println("Node\tStake\tRandom\t\tStake*Random");
          for(int i=0;i<5;i++){
              float prob=rd.nextFloat();
              System.out.println("P"+i+"\t"+stakes[i]+"\t"+prob+"\t"+stakes[i]*prob);
              if(stakes[i]*prob>=max){
                  max=stakes[i]*prob;
                  index=i;
                  
              }
              }  
              System.out.println("\nMaximum value is:"+max);
              System.out.println("P"+index+" is chosen");
              System.out.println("-------------------------------");
              stakes[index]=0;
              count++;
        }
          }
         public static void poscoinage(){
             int ages[] = {20,30,10,5,35};
             int count = 0;
             int index=-1;
             while(count<5){
           Integer max= Integer.MIN_VALUE;
           System.out.println("Node\tCoin Age");
          for(int i=0;i<5;i++){
              System.out.println("P"+i+"\t"+ages[i]);
              if(ages[i]>=max){
                  max=ages[i];
                  index=i;
                  
              }
              }  
              System.out.println("\nMaximum coin age is:"+max);
              System.out.println("P"+index+" is chosen");
              System.out.println("--------------");
              ages[index]=0;
              for(int j=0;j<5;j++) ages[j]=ages[j]+1;
              count++;
        }
         }
          public static void main(String args[]) {
          System.out.println("---------------------Random-----------------");
          posrandom();
          System.out.println("---------------------Coin Aging-----------------");
          poscoinage();
          }
      
}
