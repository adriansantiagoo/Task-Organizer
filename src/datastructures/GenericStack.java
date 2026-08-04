package datastructures;

public class GenericStack<T>{
    private int stackSize;
    private T[] customStack;
    private int itemCounter;

    @SuppressWarnings("unchecked")
    public GenericStack(int stackSize){
        this.stackSize = stackSize;
        this.customStack = (T[]) new Object[stackSize];
    }

    public void push(T item){
        throwIfFull();

        customStack[itemCounter] = item;
        itemCounter++;
    }

    public T pop(){
        throwIfEmpty();

        T itemDropped = customStack[itemCounter - 1];
        customStack[itemCounter - 1] = null;
        itemCounter--;
        return itemDropped;
    }

    public T peek(){
        throwIfEmpty();

        return customStack[itemCounter - 1];
    }

    public boolean isEmpty(){
        return itemCounter == 0;
    }

    public void throwIfEmpty(){
        if (isEmpty()){
            throw new EmptyStackException("The stack is empty!");
        }
    }

    public void throwIfFull(){
        if (itemCounter >= stackSize){
            throw new FullStackException("The stack is full!");
        }
    }
}

