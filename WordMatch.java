import java.io.*;
import java.util.*;

public class WordMatch{
	FileWriter fw;
	File textFiles,outputFile,patternsFile,matchesFile;
	ArrayList<File> inputFiles = new ArrayList<File>();
	ArrayList<WordStructure> allWordsInOrder = new ArrayList<WordStructure>();
	WordTree tree = new WordTree();

	WordMatch(String[] files){
		try {
			textFiles = new File(files[0]);
			outputFile = new File(files[1]);
			patternsFile = new File(files[2]);
			matchesFile = new File(files[3]);

			Scanner fileReader = new Scanner(textFiles);
			while(fileReader.hasNext()){
				String f = fileReader.nextLine();
				inputFiles.add(new File(f));
			}
			for(int i = 0;i<inputFiles.size();i++){
				readWords(inputFiles.get(i));
			}
			fw = new FileWriter(matchesFile);		
			
			allWordsInOrder = tree.displayInOrder();
			System.out.println("Finding neighbours .......");
			allWordsInOrder = returnNeighbours(allWordsInOrder);
			fw.write(tree.toString());
			fw.close();
			fw = new FileWriter(outputFile);
			for(int i =0;i<allWordsInOrder.size();i++){
				fw.write(allWordsInOrder.get(i).toString());
			}
			
			fw.close();

		}catch(Exception e){
			System.out.println(e);
		}

	}

	private void readWords(File f){
		try{
			Scanner s = new Scanner(f);
			while(s.hasNext()){
				String word = s.next();
				word = word.replaceAll("[^a-zA-Z]", "");//removing unwanted characters
				word = word.toLowerCase();//converting all words to lower case
				if(word.equals("")){//ignoring empty words
					continue;
				}
				if(!tree.search(word)){
					tree.insert(word);	
				}
			}
			s.close();//closing f
		}catch(FileNotFoundException e){
			System.out.println("File "+f.toString()+" Not Found");
		}
	}

	//this method finds neighbours of the words and returns arraylist of words
	public ArrayList<WordStructure> returnNeighbours(ArrayList<WordStructure> a){
		for(int i = 0;i<a.size();i++){
			for(int j=i+1;j<a.size();j++){
				// if(i == j)
				// 	continue;
				if(a.get(i).nameOfWord.length() == a.get(j).nameOfWord.length()){
					if(checkNeighbours(a.get(i).nameOfWord,a.get(j).nameOfWord)){
						//if true add to neighbours
						a.get(i).addNeighbours(a.get(j).nameOfWord);
						a.get(j).addNeighbours(a.get(i).nameOfWord);
					}
				}
			}
		}
		return a;
	}

	//this method check whether the words are neighbours or not
	private boolean checkNeighbours(String a,String b){
		int differed = 1;
		for(int i = 0;i<a.length();i++){
			if(a.charAt(i) != b.charAt(i))
				differed--;
			if(differed < 0)
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		if(args.length != 4){
			System.out.println("Pass CommandLine arguments");
			System.exit(1);
		}else{
			System.out.println("Input Files :"+args[0]+"  "+args[2]);
			System.out.println("Please Wait....It May Take Some Time");
			new WordMatch(args);
			System.out.println("Output Files :"+args[1]+"  "+args[3]);	
		}
		long stop = System.currentTimeMillis();
		System.out.println((stop - start)/1000+"");
	}
}