package ch.hcuge.simed.cohortgenericexporter.utilities;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

public class ArrayOperations {
	public static <A> A[] elementRemoval(A[] array, Integer index) {
		if(array.length==0)
			return null;
		List<A> transitionList = new LinkedList<>();
		for(int i = 0; i < array.length; i++) {
			if(i != index)
				transitionList.add(array[i]);
		}
		//Class<A> clazz = null;
		A[] arr = (A[]) Array.newInstance(array[0].getClass(), array.length-1);
		return  transitionList.toArray(arr);
	}
	
	public <E> E[] getArray(Class<E> clazz, int size) {
	    @SuppressWarnings("unchecked")
	    E[] arr = (E[]) Array.newInstance(clazz, size);

	    return arr;
	}
}
