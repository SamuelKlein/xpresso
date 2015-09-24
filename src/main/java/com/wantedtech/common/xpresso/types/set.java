package com.wantedtech.common.xpresso.types;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.wantedtech.common.xpresso.x;
import com.wantedtech.common.xpresso.functional.Function;
import com.wantedtech.common.xpresso.functional.Predicate;
import com.wantedtech.common.xpresso.helpers.Helpers;

public class set<T> implements Iterable<T>,Serializable,Comparable<set<T>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7346997017663439429L;
	HashSet<T> set;
	
	public set(){
		this.set = new HashSet<T>();
	}
	
	public set(Iterable<T> iterable){
		if(iterable instanceof set<?>){
			this.set = ((set<T>)iterable).toHashSet();
		}else{			
			this.set = Helpers.newHashSet(iterable);
		}
	}

	
	public void absorb(Iterable<T> anotherSet){
		if(anotherSet instanceof Set<?>){
			this.set.addAll((Set<T>)anotherSet);	
		}else if(anotherSet instanceof List<?>){
			this.set.addAll((List<T>)anotherSet);
		}else if(anotherSet instanceof set<?>){
			this.set.addAll(((set<T>)anotherSet).toHashSet());
		}else{
			for (T element : anotherSet){
				this.set.add(element);
			}	
		}
	}
	
	public void reject(Iterable<T> anotherSet){
		if(anotherSet instanceof Set<?>){
			this.set.removeAll((Set<T>)anotherSet);	
		}else if(anotherSet instanceof List<?>){
			this.set.removeAll((List<T>)anotherSet);
		}else if(anotherSet instanceof set<?>){
			this.set.removeAll(((set<T>)anotherSet).toHashSet());
		}else{
			for (T element : anotherSet){
				this.set.remove(element);
			}	
		}
	}
	
	public void reflect(Iterable<T> anotherSet){
		if(anotherSet instanceof Set<?>){
			this.set.retainAll((Set<T>)anotherSet);	
		}else if(anotherSet instanceof List<?>){
			this.set.retainAll((List<T>)anotherSet);
		}else if(anotherSet instanceof set<?>){
			this.set.retainAll(((set<T>)anotherSet).toHashSet());
		}else{
			HashSet<T> anotherSetAsSet = Helpers.newHashSet(anotherSet);
			for (T element : this.set){
				if(!anotherSetAsSet.contains(element)){
					this.set.remove(element);
				}
			}	
		}
	}
	
	public set<T> union(Iterable<T> anotherSet){
		HashSet<T> newHashSet = Helpers.newHashSet(this.set);
		if(anotherSet instanceof Set<?>){
			newHashSet.addAll((Set<T>)anotherSet);	
		}else if(anotherSet instanceof List<?>){
			newHashSet.addAll((List<T>)anotherSet);
		}else if(anotherSet instanceof set<?>){
			newHashSet.addAll(((set<T>)anotherSet).toHashSet());
		}else{
			for (T element : anotherSet){
				newHashSet.add(element);
			}	
		}
		return x.set(newHashSet);
	}
	
	@SuppressWarnings("unchecked")
	public set<T> union(T... elements){
		return union(x.set(elements));
	}
	
	@SuppressWarnings("unchecked")
	public set<T> or(T... elements){
		return union(elements);
	}
	
	public set<T> intersection(set<T> anotherSet){
		Set<T> setCopy = Helpers.newHashSet(this.set);
		setCopy.retainAll(anotherSet.toHashSet());
		return x.set(setCopy);
	}
	public set<T> intersection(Set<T> anotherSet){
		Set<T> setCopy = Helpers.newHashSet(this.set);
		setCopy.retainAll(anotherSet);
		return x.set(setCopy);
	}
	@SuppressWarnings("unchecked")
	public set<T> intersection(T... elements){
		return intersection(x.set(elements));
	}
	public set<T> and(set<T> set){
		return intersection(set);
	}
	public set<T> and(Set<T> set){
		return intersection(set);
	}
	@SuppressWarnings("unchecked")
	public set<T> and(T... elements){
		return intersection(elements);
	}
	
	public set<T> difference(set<T> anotherSet){
		HashSet<T> newHashSet = new HashSet<T>();
		for (T element : this.set){
			if (!anotherSet.contains(element)){
				newHashSet.add(element);	
			}
		}
		return x.set(newHashSet);
	}
	public set<T> difference(Set<T> anotherSet){
		HashSet<T> newHashSet = new HashSet<T>();
		for (T element : this.set){
			if (!anotherSet.contains(element)){
				newHashSet.add(element);	
			}
		}
		return x.set(newHashSet);
	}
	@SuppressWarnings("unchecked")
	public set<T> difference(T... elements){
		return difference(x.set(elements));
	}
	public set<T> minus(set<T> set){
		return difference(set);
	}
	public set<T> minus(Set<T> set){
		return difference(set);
	}
	@SuppressWarnings("unchecked")
	public set<T> minus(T... elements){
		return difference(elements);
	}
	
	@Deprecated
	public boolean contains(Object value){
		return this.set.contains(value);
	}
	
	public T get(T value){
		if(this.set.contains(value)){
			this.set.remove(value);
			return value;
		}else{
			throw new IllegalArgumentException("No such element "+value.toString()+" in the set.");
		}
	}
	
	boolean isSubsetOf(set<T> anotherSet){
		if(x.len(anotherSet) >= x.len(this) && x.len(this.minus(anotherSet)) == 0){
			return true;
		}
		return false;
	}
	
	public set<T> put(T value){
		this.set.add(value);
		return this;
	}
	
	public set<T> copy(){
		return new set<T>(this);
	}
	
	public HashSet<T> toHashSet(){
		return Helpers.newHashSet(this.set);
	}
		
	@Override
	public String toString(){
		return "set("+x.String(", ").join(set)+")";
	}
	
    public Iterator<T> iterator(){
        return set.iterator();
    }
    
    public int compareTo(set<T> anotherSet){
    	list<T> firstAsList = x.list(this);
    	list<T> secondAsList = x.list(anotherSet);
    	return firstAsList.compareTo(secondAsList);
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if(o instanceof set<?>){
			try{
				return x.sort(this,x.getHashCode).toString().equals(x.sort((set<T>)o,x.getHashCode).toString());	
			}catch(Exception e){
				return false;
			}
		}
		return false;
	}
	
	public boolean notEquals(Object o) {
		return !equals(o);
	}
}
