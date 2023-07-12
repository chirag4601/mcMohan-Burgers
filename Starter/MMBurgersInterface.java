public interface MMBurgersInterface {
	public boolean isEmpty();  
    /* Returns true if there is no further event to simulate */

    public void setK(int k) throws IllegalNumberException;  
    /* The number of billing queues in the restaurant is k. 
    This will remain constant for the whole of simulation */

    public void setM(int m) throws IllegalNumberException; 
     /* At most m burgers can be cooked in the griddle at a given time. 
     This will remain constant for the whole of simulation */

    public void advanceTime(int t) throws IllegalNumberException;  
    /* Run the simulation forward simulating all events until (and including) time t. */

    public void arriveCustomer(int id, int t, int numb) throws IllegalNumberException;  
    /* A customer with ID=id arrives at time t. 
    They want to order numb number of burgers. Note that an id cannot be repeated in a simulation. 
    Time cannot be lower than the time mentioned in the previous command. Numb must be positive. 
    ID can be any value and does not have to be consecutive integers. */

    public int customerState(int id, int t) throws IllegalNumberException;  
    /* Print the state of the customer id at time t. 
    Output 0 if customer has not arrived until time t. 
    Output the queue number k (between 1 to K) if customer is waiting in the kth billing queue. 
    Output K+1 if customer is waiting for food. 
    Output K+2 if customer has received their order by time t. 
    Note that time cannot be lower than the time mentioned in the previous command. */

    public int griddleState(int t) throws IllegalNumberException;  
    /* Print the number of burger patties on the griddle at time t. 
    Note that t cannot be lower than the time mentioned in the previous command. */

    public int griddleWait(int t) throws IllegalNumberException;  
    /* Print the number of burger patties waiting to be cooked at time t. 
    I.e., number of burgers for which order has been placed but cooking hasn’t started. 
    Note that t cannot be lower than the time mentioned in the previous command. */

    public int customerWaitTime(int id) throws IllegalNumberException;  
    /* Print the total wait time of customer id from arriving at the restaurant to getting the food. 
    These queries will be made at the end of the simulation */

	public float avgWaitTime();  
    /* Returns the average wait time per customer after the simulation completes. 
    This query will be made at the end of the simulation. */ 

}
