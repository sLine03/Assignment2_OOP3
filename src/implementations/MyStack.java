import utilities.Iterator;
import utilities.StackADT;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;

public class MyStack <E> implements StackADT <E> {
	private MyArrayList<E> list;
	
	public MyStack() {
		list = new MyArrayList<>();
	}
	
	@Override
	public void push(E element) throws NullPointerException {
		if(element == null) throw new NullPointerException("Element cannot be null");
		list.add(element);
	}
	
	@Override
	public E pop() throws EmptyStackException {
		if(isEmpty()) throw new EmptyStackException();
		return list.get(list.size() -1);
	}
	
	@Override
	public E peek() throws EmptyStackException {
		if(isEmpty()) throw new EmptyStackException();
		return list.get(list.size()-1);
	}
	
	@Override
	public void clear() {
		list.clear();
	}
	
	@Override
	public Object[] toArray() {
		Object[]arr = list.toArray();
		Object[] reversed = new Object[arr.length];
		for (int i = 0; i < arr.length, i++) {
			reversed [i] = arr[arr.length-1-i];
		}
		return reversed;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray(E[] copyArray) throws NullPointerException {
		if(copyArray == null) throw new NullPointerException("Array cannot be null or empty");
		Object[] arr = list.toArray();
		if (copyArray.length <arr.length) {
			copyArray=(E[])
					java.lang.reflect.Array.newInstance(copyArray.getClass().getComponentType(),  arr.length);
		}
		for(int i=0;i<arr.length;i++) {
			copyArray[arr.length]=null;
		}
		return copyArray;
	}
	
	@Override
	public boolean contains(E element) throws NullPointerException {
		if (element == null) throw new NullPointerException("Element cannot be null :p");
		return list.contains(element);
	}
	
	@Override
	public int search(E element) {
		for (int i = list.size() - 1; i>= 0; i--) {
			if(list.get(i).equals(element)) {
				return list.size() -i;
			}
		}
		return -1;
	}
	
	@Override
	public Iterator<E> iterator(){
		return new StackIterator();
	}
	
	@Override
	public boolean equals(StackADT<E> that) {
		if(that ==null) return false;
		if (this.size()!= that.size()) return false;
		Iterator<E> it1 = this.iterator();
		Iterator<E> it2 = that.iterator();
		while(it1.hasNext()) {
			if(!it1.next().equals(it2.next()))return false;
		}
		return true;
	}
	@Override
	public boolean stackOverflow() {
		return false;
		}
	
	private class StackIterator implements Iterator<E>{
		private Object[]copy;
		private int pos = 0;
		
		public StackIterator() {
			copy = toArray();
		}
	}
	
	@Override
	public boolean hasNext() {
		return pos < copy.length;
	}
	
	@SurpressWarnings ("unchecked")
	@Override
	public E next() throws NoSuchElementException{
		if(!hasNext())throw new NoSuchElementException("No more elements in iteration");
		return (E) copy[pos++];
	}
}