import java.util.Set;

public class SkipList<T extends Comparable<? super T>>
    implements SkipListInterface<T> {
    private CoinFlipper coinFlipper;
    private int size;
    private Node<T> head;


    /**
     * Constructs a SkipList object that stores data in ascending order
     * when an item is inserted, the flipper is called until it returns a tails
     * if for an item the flipper returns n heads, the corresponding node has
     * n + 1 levels
     *
     * @param coinFlipper the source of randomness
     */
    public SkipList(CoinFlipper coinFlipper) {
        this.coinFlipper = coinFlipper;
    }

    private int getHeight() {
    	int numFlips = 0;
        CoinFlipper.Coin coin = coinFlipper.flipCoin();
        while (coin == CoinFlipper.Coin.HEADS) {
            coin = coinFlipper.flipCoin();
            numFlips++;
        }

        return numFlips + 1;
    }

    public T first() {
        if (size == 0) {
            return null;
        }

        Node<T> currentNode = head;

        while(true) {
            if(currentNode.getNext().getData() != null) {
                return currentNode.getNext().getData();
            } else {
                if (currentNode.getDown() != null) {
                    currentNode = currentNode.getDown();
                } else {
                    return null;
                }
            }
        }
    }

    public T last() {
        if (size == 0) {
            return null;
        }

        Node<T> currentNode = head;
        Node<T> nullNode = new Node<T>(null, 0);

        while(true) {
            while(!(currentNode.getNext().getData() == nullNode.getData())) {
                currentNode = currentNode.getNext();
            }

            if (!currentNode.getDown().getData().equals(null)) {
                currentNode = currentNode.getDown();
            } else {
                return currentNode.getData();
            }
        }

    }

    public boolean contains(T data) {
        
        if (data == null) {
            throw new IllegalArgumentException();
        }

        if (size == 0) {
            return false;
        }

        Node<T> currentNode = head;

        while (true) {
            while (currentNode.getNext().getData() != null) {
                currentNode = currentNode.getNext();
            }

            if (currentNode.getDown() != null) {
                currentNode = currentNode.getDown();
            } else {
                return true;
            }
        }

    }

    public void put(T data) {

        if (data == null) {
            throw new IllegalArgumentException();
        }

        if (size == 0) {

            int colHeight = getHeight();
            head = new Node<T>(null, 1);
            head.setLevel(colHeight);
            Node<T> currentNode = head;

            //creates top line with head sentinel
            head.setLevel(colHeight + 1);
            head.setDown(new Node<T>(null, colHeight));
            head.setNext(new Node<T>(null, colHeight + 1));
            head.setUp(null);

            for (int i = colHeight; i >= 2; i--) {
                currentNode.setNext(new Node<T>(data, i));
                currentNode.setDown(new Node<T>(null, i - 1));
                currentNode.getNext().setNext(new Node<T>(null, i));
                currentNode.getNext().getNext().setDown(new Node<T>(null, i - 1));
                currentNode.getNext().setDown(new Node<T>(data, i - 1));
                currentNode = currentNode.getDown();
            }
        }

        int colHeight = getHeight();
        //creates new top line in case new height is greater
        if (colHeight > head.getLevel()) {
        	head.setLevel(colHeight + 1);
        	head.setDown(new Node<T>(null, colHeight));
        	head.setNext(new Node<T>(null, colHeight + 1));
        	head.setUp(null);
        }

        Node<T> currentNode = head.getDown();
        Node<T> prevNode = null;
        boolean running = true;
        while (running) {
            while (currentNode.getNext() != null && currentNode.getNext().getData().compareTo(data) < 0) {
                prevNode = currentNode;
                currentNode = currentNode.getNext();
            }
            
            if (currentNode.getData() != null) {
                if (currentNode.getData().compareTo(data) < 0) {
                    prevNode.setNext(new Node<T>(data, prevNode.getLevel()));
                    prevNode.getNext().setNext(currentNode);
                }
            }

            if (currentNode.getDown() != null) {
                currentNode = currentNode.getDown();
            } else {
                running = false;
            }
        }

        size++;

    }

    public T get(T data) {
        
        if (data == null) {
            throw new IllegalArgumentException();
        }

        if (size == 0) {
            return null;
        }

        Node<T> currentNode = head;

        while (true) {
            while (currentNode.getNext().getData() != null && currentNode.getNext().getData().compareTo(data) < 0) {
                currentNode = currentNode.getNext();
            }

            if (currentNode.getDown() != null) {
                currentNode = currentNode.getDown();
            } else {
                return currentNode.getData();
            }
        }
    }

    public int size() {
        return size;
    }

    public void clear() {
        head = new Node<T>(null, 1);
        head.setNext(new Node<T>(null, 1));
        head.setUp(new Node<T>(null, 2));
        head.setDown(null);
        head = head.getUp();
        size = 0;
    }

    @Override
    public Set<T> dataSet() {
        Node current = head;
        return null;
    }

    @Override
    public T remove(T data) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static void main(String[] args) {
    	CoinFlipper newFlipper = new CoinFlipper();
    	SkipList newList = new SkipList(newFlipper);
    	newList.put(5);
    	Node newNode = new Node(null, 0);
    	Node newNode2 = new Node(null, 0);
    	//System.out.println(newList.get(5));
    	System.out.println(newNode.getData() == (newNode2.getData()));
    }

}
