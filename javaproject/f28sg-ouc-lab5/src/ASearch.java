public class ASearch {

	private Entry[] catalogue;
	private int current;

	/*
	 * Assume 10 entries
	 */
	public ASearch() {
		catalogue = new Entry[10];
		current = 0;
	}

	/*
	 * Ignores adding if full (should really be handled by exception...)
	 */
	public void addEntry(Entry e) {
		if (current < 10) {
			
			catalogue[current++] = e;
		}
	}

	/*
	 * Part 2: complete implementation
	 */
	/**
	 * Uses linear search to look up a given name in the catalogue and returns the
	 * number if the name is in the catalogue. Otherwise it returns -1.
	 * 
	 * TODO Where N is the number of entries in the catalogue the (worst case)
	 * complexity is:
	 *
	 * O(N)
	 * 
	 * Because: I need to look through the array and compare the name one by one
	 * which may need n operations due to the length of the array
	 * 
	 * @param name is the person name to look for in the catalogue
	 * @return the number of that person, otherwise -1 to indicate an error
	 */
	public int linearSearch(String name) {
		for(int i=0;i<catalogue.length;i++) {
			if(catalogue[i]==null) {
				break;
			}
			if(catalogue[i].getName().equals(name)) {
				return catalogue[i].getNumber();
			}
		}
		return -1;
	}

	/*
	 * Part 4: complete implementation
	 */
	/**
	 * Uses binary search to look up a given name in the catalogue and returns the
	 * number if the name is in the catalogue. Otherwise it returns -1.
	 * 
	 * TODO Where N is the number of entries in the catalogue the (worst case)
	 * complexity is:
	 *
	 * O(log n)
	 * 
	 * Because: Binary search works by repeatedly dividing the search interval in
	 * half. In each recursive call, the problem size (the range of indices to
	 * search) is reduced by half. This results in a logarithmic number of recursive
	 * calls relative to the number of elements (n) in the catalogue.
	 * 
	 * @param first the array index of the start of search space
	 * @param last  the array index of the end of the search space
	 * @param name  the person name being searched for
	 * @return the persons phone number if their name is found or -1 otherwise
	 */
	private int binarySearch(int first, int last, String name) {
		if (first > last) {
			return -1;
		}
		int middle=(first+last)/2;
		if(catalogue[middle].getName().equals(name)) {
			return catalogue[middle].getNumber();
		}else if(catalogue[middle].getName().compareTo(name)>0) {
			return binarySearch(first,middle-1,name);
		}else {
			return binarySearch(middle+1,last,name);
		}
	}

	// helper method exposed to the programmer
	public int binarySearch(String name) {
		return binarySearch(0,catalogue.length,name);
	}

}
