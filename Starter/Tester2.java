public class Tester2 {
    public static void main(String[] args){
        MMBurgersInterface mm = new MMBurgers();
        System.out.println("\n--Started simulation--");

        // Set number of counters and griddle capacity
        try{
            mm.setK(2);
            mm.setM(8);
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

        // t = 0
        try{
            // Customer 1 arrives
            mm.arriveCustomer(1, 0, 5);
            // Customer 2 arrives
            mm.arriveCustomer(2, 0, 6);
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

        // t = 1
        try{
            // Customer 3 arrives
            mm.arriveCustomer(3, 1, 4);
            // Customer 4 arrives
            mm.arriveCustomer(4, 1, 5);
            // Query customer state
            mm.customerState(3, 1);
            // Query customer state
            mm.customerState(4, 1);
            // Query griddle state
            mm.griddleState(1);
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

        // t = 5
        try{
            // Query griddle state
            mm.griddleState(5);
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

        // t = 6
        try{
            // Query customer state
            mm.customerState(2, 6);
            // Query griddle state
            mm.griddleState(6);
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

        // t = 12
        try{
            // Query griddle state
            mm.griddleWait(12);
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

        // t = 25
        try{
            // Query griddle state
            mm.griddleState(25);
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

         // t = 32
         try{
            // Query griddle state
            mm.advanceTime(32);
            mm.isEmpty();
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

        // End of simulation
        System.out.println("\n--End of simulation--");
        mm.isEmpty();

        // Query wait times
        try{
            mm.customerWaitTime(1);
            mm.customerWaitTime(2);
            mm.customerWaitTime(3);
            mm.customerWaitTime(4);
            mm.avgWaitTime();
        }
        catch(IllegalNumberException e){
            System.out.println(e);
        }

    }
}
