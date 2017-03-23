package com.example.sortlistview;

import java.util.Comparator;

import com.dev.nutclass.entity.SimpleEntity;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<SimpleEntity> {

	@Override
	public int compare(SimpleEntity o1, SimpleEntity o2) {
		// TODO Auto-generated method stub
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
