/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpblog.aps.system.services.blog.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jpblog.aps.system.services.blog.model.GroupStatistic;

public class BlogStatisticsUtil {
	
	public static void removeOccurrence(Collection<String> allowedGroups, List<GroupStatistic> groupStatistics) {
		boolean removed = false;
		// Ciclo al contrario per poter rimuovere le statistiche 
		for (int i=groupStatistics.size()-1; i>=0; i--) {
			GroupStatistic groupStatistic = groupStatistics.get(i);
			if (matchAtLeastOneGroup(allowedGroups, groupStatistic.getGroups())) {
				// Aggiorno le occorrenze
				int occurrences = groupStatistic.getOccurrences()-1;
				groupStatistic.setOccurrences(occurrences);
				if (occurrences<=0) {
					// Se ho azzerato le occorrenze, rimuovo le statistiche per il gruppo
					groupStatistics.remove(i);
				}
				removed = true;
			}
		}
		if (removed) {
			Collections.sort(groupStatistics);
		}
	}
	
	public static int getOccurrences(Collection<String> allowedGroups, List<GroupStatistic> groupStatistics) {
		int occurrences = 0;
		if (groupStatistics.size()>0) {
			if (allowedGroups.contains(Group.ADMINS_GROUP_NAME)) {
				occurrences = groupStatistics.get(0).getOccurrences();
			} else {
				int groupsSize = allowedGroups.size();
				Iterator<GroupStatistic> groups = groupStatistics.iterator();
				while (groups.hasNext()) {
					GroupStatistic groupStat = groups.next();
					if (groupsSize>=groupStat.getGroups().size() && allowedGroups.containsAll(groupStat.getGroups())) {
						occurrences = groupStat.getOccurrences();
						break;
					}
				}
			}
		}
		return occurrences;
	}
	
	public static void addOccurrence(Collection<String> allowedGroups, List<GroupStatistic> groupStatistics) {
		String[] array = allowedGroups.toArray(new String[0]);
		List<Set<String>> combinations = combineArrayElements(array);
		if (groupStatistics.isEmpty()) {
			Iterator<Set<String>> combIter = combinations.iterator();
			while (combIter.hasNext()) {
				Set<String> combination = combIter.next();
				addOccurrence(combination, groupStatistics, 1);
			}
		} else {
			Iterator<GroupStatistic> groupsIter = groupStatistics.iterator();
			while (groupsIter.hasNext()) {
				GroupStatistic groupStatistic = groupsIter.next();
				if (matchAtLeastOneGroup(allowedGroups, groupStatistic.getGroups())) {
					groupStatistic.setOccurrences(groupStatistic.getOccurrences()+1);
					if (allowedGroups.containsAll(groupStatistic.getGroups())) {
						// Ho trovato una combinazione - Procedo con la rimozione
						removeCombination(combinations, groupStatistic.getGroups());
					}
				}
			}
			if (!combinations.isEmpty()) {
				List<GroupStatistic> newGroupCombinations = new ArrayList<GroupStatistic>();
				Iterator<Set<String>> combinationsIter = combinations.iterator();
				while (combinationsIter.hasNext()) {
					Set<String> combination = combinationsIter.next();
					int occurrences = getCombinationOccurrences(combination, groupStatistics);
					addOccurrence(combination, newGroupCombinations, occurrences);
				}
				groupStatistics.addAll(newGroupCombinations);
			}
			Collections.sort(groupStatistics);
		}
	}
	
	private static boolean matchAtLeastOneGroup(Collection<String> allowedGroups, Collection<String> groups) {
		boolean contained = false;
		Iterator<String> allowedGroupsIter = allowedGroups.iterator();
		while (allowedGroupsIter.hasNext()) {
			if (groups.contains(allowedGroupsIter.next())) {
				contained = true;
				break;
			}
		}
		return contained;
	}
	
	private static void removeCombination(List<Set<String>> combinations, Collection<String> combination) {
		int combSize = combination.size();
		int index = 0;
		Iterator<Set<String>> combinationsIter = combinations.iterator();
		while (combinationsIter.hasNext()) {
			Set<String> current = combinationsIter.next();
			if (current.size()==combSize && current.containsAll(combination)) {
				combinations.remove(index);
				break;
			}
			index++;
		}
	}
	
	private static void addOccurrence(List<Set<String>> combinations, List<GroupStatistic> groupStatistics) {
		if (groupStatistics.isEmpty()) {
			Iterator<Set<String>> combIter = combinations.iterator();
			while (combIter.hasNext()) {
				Set<String> combination = combIter.next();
				addOccurrence(combination, groupStatistics, 1);
			}
		} else {
			List<GroupStatistic> newGroupCombinations = new ArrayList<GroupStatistic>();
			Iterator<Set<String>> combIter = combinations.iterator();
			while (combIter.hasNext()) {
				Set<String> combination = combIter.next();
				int groupsSize = combination.size();
				boolean found = false;
				Iterator<GroupStatistic> groupsIter = groupStatistics.iterator();
				while (groupsIter.hasNext()) {
					GroupStatistic groupStat = groupsIter.next();
					if (groupsSize==groupStat.getGroups().size() && groupStat.getGroups().containsAll(combination)) {
						groupStat.setOccurrences(groupStat.getOccurrences()+1);
						found = true;
						break;
					}
				}
				if (!found) {
					int occurrences = getCombinationOccurrences(combination, groupStatistics);
					addOccurrence(combination, newGroupCombinations, occurrences);
				}
			}
			if (newGroupCombinations.size()>0) {
				groupStatistics.addAll(newGroupCombinations);
			}
			Collections.sort(groupStatistics);
		}
	}
	
	private static int getCombinationOccurrences(Set<String> combination, List<GroupStatistic> groupStatistics) {
		int occurrences = 1;
		int groupsSize = combination.size();
		Iterator<GroupStatistic> groupsIter = groupStatistics.iterator();
		while (groupsIter.hasNext()) {
			GroupStatistic groupStat = groupsIter.next();
			Collection<String> statGroups = groupStat.getGroups();
			if (statGroups.size()<groupsSize && combination.containsAll(statGroups)) {
				occurrences = groupStat.getOccurrences();
				break;
			}
		}
		return occurrences;
	}
	
	private static void addOccurrence(Set<String> combination, List<GroupStatistic> groupStatistics, int size) {
		GroupStatistic groupStat = new GroupStatistic();
		groupStat.setGroups(combination);
		groupStat.setOccurrences(size);
		groupStatistics.add(groupStat);
	}
	
	private static List<Set<String>> combineArrayElements(String[] array) {
		List<Set<String>> allCombinations = new ArrayList<Set<String>>();
		for (int i=0; i<array.length; i++) {
			Set<String> combination = new TreeSet<String>();
			combination.add(array[i]);
			allCombinations.add(combination);
			combineArrayElements(array, i+1, combination, allCombinations);
		}
		return allCombinations;
	}
	
	private static void combineArrayElements(String[] array, int start, Set<String> startCombination, List<Set<String>> allCombinations) {
		for (int i=start; i<array.length; i++) {
			Set<String> combination = new TreeSet<String>(startCombination);
			combination.add(array[i]);
			allCombinations.add(combination);
			combineArrayElements(array, i+1, combination, allCombinations);
		}
	}
	
}