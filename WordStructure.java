import java.util.*;


public class WordStructure{
	String nameOfWord;
	int length;
	int frequency;
	ArrayList<String> neighbours = new ArrayList<String>();

	WordStructure(String item,int length){
		this.nameOfWord = item;
		this.frequency = 1;
		this.length = length;
	}

	WordStructure(String item,int frequency,int length){
		this.nameOfWord = item;
		this.frequency = frequency;
		this.length = length;
	}

	public void increaseFrequency(){

		this.frequency++;

	}

	public void addNeighbours(String neighbour){
		this.neighbours.add(neighbour);
	}

	public String toString(){
		return this.nameOfWord + " " + this.frequency + " "+this.length +" " + this.neighbours + "\n";
	}

}

