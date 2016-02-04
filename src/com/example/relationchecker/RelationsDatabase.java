package com.example.relationchecker;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import android.R.array;
import android.R.integer;
import android.support.v4.util.ArrayMap;


public class RelationsDatabase {
	private ArrayList<Relation> relations ;


	public RelationsDatabase() {
		relations = new ArrayList <Relation>();
		
		relations.add( new Relation("Starcraft","is a", "Game" , 1,1));
		relations.add(new Relation("Starcraft","was developped by", "Blizzard" , 1,1));
	}
	


	public ArrayList<Relation> getRelations() {
		return relations;
	}



	public void setRelations(ArrayList<Relation> relations) {
		this.relations = relations;
	}



	public Relation getRandomRelation(){

		Relation randomRelation;
		Random rand= new Random();
		int randomValue = rand.nextInt(relations.size());
		randomRelation = relations.get(randomValue);
		return randomRelation;
	}

	public LinkedList<Relation> getRelationsBelowConfidence(int threshold){

		LinkedList<Relation> retVal = new LinkedList<Relation>();
		Iterator iterator = relations.iterator();
		while(iterator.hasNext()){
			Relation currentRel=(Relation) (iterator.next());
			if (currentRel.getConfidence()<=threshold){
				retVal.add(currentRel);
			}
		}
		return retVal;
	}

}

