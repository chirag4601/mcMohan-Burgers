import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class MMBurgers implements MMBurgersInterface {

    private int globalTime = 0;
    public int numCounters;
    private int numGriddles;
    private int numCustomers;
    private float totalWaitingTime;

    Queue<customer>[] counter;
    Queue<customer> griddle = new LinkedList<>();
    Queue<customer> griddlePending = new LinkedList<>();

    Vector<customer> waiting = new Vector<>();

    Heap timeHeap = new Heap();
    AVLTree tree = new AVLTree();

    public boolean isEmpty(){
        return timeHeap.isEmpty();
    }

    public void setK(int k) throws IllegalNumberException{
        if(k<=0){
            throw new IllegalNumberException("Counters should be positive");}
        numCounters = k;
        counter = new Queue[k+1];
        for(int i = 1 ; i<k+1 ; i++)
            {counter[i] = new LinkedList<>();}
    }

    public void setM(int m) throws IllegalNumberException{
        if(m<=0)
            throw new IllegalNumberException("Griddles should be positive");
        numGriddles = m;
    }

    public void advanceTime(int t) throws IllegalNumberException{
        if(t<globalTime)
            throw new IllegalNumberException("Incorrect time");
        while(!timeHeap.isEmpty() && timeHeap.findMin().time <= t) {

            customer temp = timeHeap.findMin();

            if (temp.processNum == 3) {
                temp.timeOfDeparture = temp.time;
                waiting.remove(temp);
                tree.setState(temp.id,3);
                tree.setWaitingTime(temp.id, temp.timeOfArrival, temp.timeOfDeparture);
                totalWaitingTime = totalWaitingTime + temp.timeOfDeparture - temp.timeOfArrival;
                timeHeap.deleteMin();
                continue;
            }
            if (temp.processNum == 2) {
                timeHeap.deleteMin();
                griddle.remove(temp);
                customer temp2 = temp.parent;
                temp2.burgersCooked++;

                if (temp2.burgersCooked == temp2.burgersOnGrill) {
                    temp2.timeOfGettingAllBurgers = temp.time;
                    temp2.time = temp.time;
                    temp2.processNum = 3;
                    timeHeap.insert(temp2.time + 1, temp2);
                }

                if(!griddlePending.isEmpty()) {
                    customer temp3 = griddlePending.remove();
                    customer temp4 = temp3.parent;

                    temp4.burgersLeft--;
                    temp4.burgersOnGrill++;

                    temp3.time = temp.time;
                    temp3.processNum = 2;
                    griddle.add(temp3);
                    timeHeap.insert(temp3.time + 10, temp3);
                }
            }

            if (temp.processNum == 1) {

                counter[temp.counter].remove();
                waiting.add(temp);
                tree.setState(temp.id,2);

                if (griddle.size() != numGriddles) {
                    while (griddle.size() != numGriddles && temp.burgersLeft!=0) {

                        temp.burgersOnGrill++;
                        temp.burgersLeft--;
                        tree.setState(temp.id,2);

                        customer burg = new customer(temp.id);
                        burg.parent = temp;
                        burg.time = temp.time;
                        burg.counter = temp.counter;
                        burg.processNum = 2;
                        griddle.add(burg);
                        timeHeap.insert(burg.time + 10, burg);
                    }
                }
                for (int i = 0; i < temp.burgersLeft; i++) {
                    customer burg = new customer(temp.id);
                    burg.counter = temp.counter;
                    burg.parent = temp;
                    griddlePending.add(burg);
                }
                timeHeap.deleteMin();
                continue;
            }
        }
        globalTime = t;
    }

    public void arriveCustomer(int id, int t, int numb) throws IllegalNumberException{

        if(t<globalTime)
            throw new IllegalNumberException("Incorrect time");
        if(numb<0)
            throw new IllegalNumberException("Number of burgers should be positive");

        advanceTime(t);
        customer c = new customer(id);
        numCustomers++;

        c.timeOfArrival = t;
        c.burgersLeft = numb;
        int min = counter[1].size();
        int minCounter = 1;
        for(int i=1; i<=numCounters; i++){
            if(counter[i].size()<min) {
                min = counter[i].size();
                minCounter = i;
            }
        }

        c.processNum = 1;
        c.counter = minCounter;
        counter[minCounter].add(c);

        if(counter[minCounter].size()==1){
            c.timeOfPlacingOrder = t+minCounter;
        }else{
            int temp = counter[minCounter].peek().timeOfPlacingOrder;
            int size = counter[minCounter].size()-1;
            c.timeOfPlacingOrder = temp+size*minCounter;
        }

        timeHeap.insert(c.timeOfPlacingOrder, c);
        tree.insert(c.id,c.counter);
        tree.setState(c.id,c.processNum);
    }

    public int customerState(int id, int t) throws IllegalNumberException{
        if(t<globalTime)
            throw new IllegalNumberException("Incorrect time");
        advanceTime(t);

        int state = tree.getState(id);
        AVLTree.Node temp = tree.search(id);

        if(temp==null)
            return 0;

        if(state==temp.avlCounter+1){
            state =  numCounters+1;
            return state;
        }
        if(state==temp.avlCounter+2){
            state =  numCounters+2;
            return state;
        }
        return state;
    }

    public int griddleState(int t) throws IllegalNumberException{
        if(t<globalTime)
            throw new IllegalNumberException("Incorrect time");
        advanceTime(t);
        return griddle.size();
    }

    public int griddleWait(int t) throws IllegalNumberException{
        if(t<globalTime)
            throw new IllegalNumberException("Incorrect time");
        advanceTime(t);
        return griddlePending.size();
    }

    public int customerWaitTime(int id) throws IllegalNumberException{
        if(tree.search(id)==null)
            throw new IllegalNumberException("ID not found");
        return tree.getWaitingTime(id);
    }

    public float avgWaitTime(){
        return totalWaitingTime/numCustomers;
    }
}

class Heap{

    private Vector<customer> v = new Vector<>();

    public boolean isEmpty(){
        return v.isEmpty();
    }

    public void insert(int value, customer c){
        c.time = value;
        v.add(c);
        percolateUp();
    }

    private void percolateUp(){
        int index = v.size()-1;
        int parentIndex = (index-1)/2;

        while(index>0 && priority(index,parentIndex)) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    private boolean priority(int index, int parentIndex){
        if(Value(parentIndex)>=Value(index)){
            if(Value(parentIndex)==Value(index)){
                if(v.get(index).processNum<v.get(parentIndex).processNum)
                    return true;
                if(v.get(index).processNum==1 && v.get(parentIndex).processNum==1)
                    return v.get(index).counter>v.get(parentIndex).counter;
                if(v.get(index).processNum>v.get(parentIndex).processNum)
                    return false;
            }
            return true;
        }
        return false;
    }

    private Vector swap(int index, int parentIndex) {
        customer temp = v.get(index);
        v.set(index, v.get(parentIndex));
        v.set(parentIndex, temp);
        return v;
    }

    public void deleteMin(){
        v.set(0,v.get(v.size()-1));
        v.removeElementAt(v.size()-1);
        percolateDown();
    }

    public customer findMin(){
        return v.get(0);
    }

    private void percolateDown(){
        int index = 0;
        while(index < v.size() && !isValidParent(index)){
            int smallerChild = smallerChild(index);
            swap(index, smallerChild);
            index = smallerChild;
        }
    }

    private boolean isValidParent(int index){
        if(!hasLeftChild(index))
            return true;
        if(!hasRightChild(index))
            return priority(index,leftChild(index));
        return priority(index,leftChild(index)) && priority(index,rightChild(index));
    }

    private int Value(int index){
        return v.get(index).time;
    }

    private int leftChild(int index){
        return 2*index+1;
    }

    private int rightChild(int index){
        return 2*index+2;
    }

    private boolean hasLeftChild(int index){
        return leftChild(index)<v.size();
    }

    private boolean hasRightChild(int index){
        return rightChild(index)<v.size();
    }

    private int smallerChild(int index){
        if(!hasLeftChild(index))
            return index;
        if(!hasRightChild(index))
            return leftChild(index);

        if(priority(leftChild(index),rightChild(index)))
            return leftChild(index);
        else
            return rightChild(index);
    }
}

class customer{
    public int id;
    public int timeOfArrival;
    public int timeOfPlacingOrder;
    public int timeOfGettingAllBurgers;
    public int counter;
    public int burgersCooked = 0;
    public int burgersOnGrill = 0;
    public int burgersLeft;
    public int time;
    public int timeOfDeparture;
    public int processNum;
    public customer parent;

    public customer(int id){
        this.id = id;
    }
}

class AVLTree {

    private Node root;

    public class Node{
        private int key;
        private int balance;
        private int height;
        private Node left, right, parent;
        public int avlCounter;
        public boolean waitingAtCounter = false;
        public boolean waitingForFood = false;
        public boolean receivedFood = false;
        public int waitingTime;

        Node(int k, Node p, int c) {
            key = k;
            parent = p;
            avlCounter = c;
        }
    }

    public void setState(int key,int avlProcessNum){
        Node n = search(key);
        if(avlProcessNum==1)
            n.waitingAtCounter=true;
        if(avlProcessNum==2)
            n.waitingForFood=true;
        if(avlProcessNum==3)
            n.receivedFood=true;
    }

    public int getState(int key){
        Node n = search(key);
        if(n==null)
            return 0;
        if(n.waitingAtCounter){
            if(n.waitingForFood){
                if(n.receivedFood)
                    return n.avlCounter+2;
                return n.avlCounter+1;
            }
            return n.avlCounter;
        }
        return -1;
    }

    public void setWaitingTime(int key, int arrive, int departure){
        Node n = search(key);
        n.waitingTime = departure-arrive;
    }

    public int getWaitingTime(int key){
        Node n = search(key);
        return n.waitingTime;
    }


    public boolean insert(int key, int avlCounter) {
        if (root == null) root = new Node(key, null, avlCounter);
        else {
            Node n = root;
            Node parent;
            while (true) {
                if (n.key == key) return false;

                parent = n;

                boolean goLeft = n.key > key;
                n = goLeft ? n.left : n.right;

                if (n == null) {
                    if (goLeft) {
                        parent.left = new Node(key, parent,avlCounter);
                    } else {
                        parent.right = new Node(key, parent, avlCounter);
                    }
                    rebalance(parent);
                    break;
                }
            }
        }
        return true;
    }

    private void delete(Node node) {
        if (node.left == null && node.right == null) {
            if (node.parent == null) root = null;
            else {
                Node parent = node.parent;
                if (parent.left == node) {
                    parent.left = null;
                } else parent.right = null;
                rebalance(parent);
            }
            return;
        }
        if (node.left != null) {
            Node child = node.left;
            while (child.right != null) child = child.right;
            node.key = child.key;
            delete(child);
        } else {
            Node child = node.right;
            while (child.left != null) child = child.left;
            node.key = child.key;
            delete(child);
        }
    }

    public void delete(int delKey) {
        if (root == null) return;
        Node node = root;
        Node child = root;

        while (child != null) {
            node = child;
            child = delKey >= node.key ? node.right : node.left;
            if (delKey == node.key) {
                delete(node);
                return;
            }
        }
    }

    private void rebalance(Node n) {
        setBalance(n);

        if (n.balance == -2) {
            if (height(n.left.left) >= height(n.left.right)) n = rotateRight(n);
            else n = rotateLeftThenRight(n);

        } else if (n.balance == 2) {
            if (height(n.right.right) >= height(n.right.left)) n = rotateLeft(n);
            else n = rotateRightThenLeft(n);
        }

        if (n.parent != null) {
            rebalance(n.parent);
        } else {
            root = n;
        }
    }

    private Node rotateLeft(Node a) {

        Node b = a.right;
        b.parent = a.parent;

        a.right = b.left;

        if (a.right != null) a.right.parent = a;

        b.left = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }

    private Node rotateRight(Node a) {

        Node b = a.left;
        b.parent = a.parent;

        a.left = b.right;

        if (a.left != null) a.left.parent = a;

        b.right = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }

    private Node rotateLeftThenRight(Node n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    private Node rotateRightThenLeft(Node n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }

    private int height(Node n) {
        if (n == null) return -1;
        return n.height;
    }

    private void setBalance(Node... nodes) {
        for (Node n : nodes) {
            reheight(n);
            n.balance = height(n.right) - height(n.left);
        }
    }

    public void printBalance() {
        printBalance(root);
    }

    private void printBalance(Node n) {
        if (n != null) {
            printBalance(n.left);
            System.out.printf("%s ", n.balance);
            printBalance(n.right);
        }
    }

    private void reheight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    public Node search(int key) {
        Node result = searchHelper(this.root, key);
        if (result != null) return result;

        return null;
    }

    private Node searchHelper(Node root, int key) {
        // root is null or key is present at root
        if (root == null || root.key == key) return root;

        // key is greater than root's key
        if (root.key > key)
            return searchHelper(root.left, key); // call the function on the node's left child

        // key is less than root's key then
        // call the function on the node's right child as it is greater
        return searchHelper(root.right, key);
    }
}





