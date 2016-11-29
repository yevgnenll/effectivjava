import java.util.Arrays;
import java.util.EmptyStackException;

import sun.misc.Cleaner;

/**
 * Created by yevgnen on 2016-11-22.
 */
public class Stack<E> {
  private E[] elements; // ?
  private int size = 0;
  private static final int DEFAULT_INITIAL_CAPACITY = 16;

  public Stack(){
    elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
  }

  public void push(E e){
   ensureCapacity();
    elements[size++] = e;
  }

  public E pop(){
    if(size == 0)
      throw new EmptyStackException();
    E result = elements[--size];
    elements[size] = null; // 만기참조 제거
    return result;
  }

  public boolean isEmpty(){
    return size == 0;
  }

  public void ensureCapacity(){
    if(elements.length == size)
      elements = Arrays.copyOf(elements, 2 * size + 1);
  }

  @Override
  public Stack clone(){
    try{
      Stack result = (Stack) super.clone();
      result.elements = elements.clone();
      return result;

    } catch (CloneNotSupportedException e){
      throw new AssertionError();
    }
  }
}
