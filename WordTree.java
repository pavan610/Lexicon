import java.util.*;
import java.io.*;

public class WordTree
{
	Word root;
	
	public WordTree()
	{
		root = null;
	}
	
	public void insert(String newItem)
	{
		//	Special case: the tree is empty
		if (root == null)
		{
			root = new Word();
			root.insertItem(newItem);	
			return;
		}
		
		//	General case: the tree is not empty
		
		//	find a leaf node to insert data
		Word p = root;
		boolean descending = true;
		while (descending)
		{
			if (p.isLeaf())
			{
				descending = false;
			}
			else 
			{
				int index = 0;	
				while (index < p.getData().size() 
							&& p.getData().get(index).nameOfWord.compareTo(newItem) < 0)
				{
					index++;
				}
				p = p.getChildren().get(index);
			}
		}
	
		if (!p.isFull())
		{
			insertIntoNotFullLeafNode(p, newItem);
		}	
		else
		{
			insertIntoFullLeafNode(p, newItem);
		}
	}
	
	//	insert into a leaf node which is not full
	private void insertIntoNotFullLeafNode(Word node, String newItem)
	{
		node.insertItem(newItem);
	}
	
	private void insertIntoFullLeafNode(Word p, String newItem)
	{
		Object[] returnArray = split(p, newItem, null);
		String mid = (String) returnArray[0];
		Word newSibling = (Word) returnArray[1];
		
		Word parent = p.getParent();
		if (parent == null)	//	p is the root
		{
			root = new Word();
			root.insertChild(0, p);
			p.setParent(root);
			parent = root;
		}
		
		insertUpward(parent, mid, newSibling);
	}
	
	private Object[] split(Word p, String newItem, Word newChild)
	{
		//	creare a new sibling
		Word newSibling = new Word();

		// distribute ITEMS between the two nodes
		
		ArrayList<WordStructure> tempItemList = new ArrayList<WordStructure>();
		for(int i = 0; i < p.getData().size(); i++)
		{
			tempItemList.add(p.getData().get(i));
		}
		int index = 0;
		while(index < p.getData().size() 
			&& p.getData().get(index).nameOfWord.compareTo(newItem) < 0)
		{
			index++;
		}
		tempItemList.add(index, new WordStructure(newItem,newItem.length()));

		p.getData().clear();
		for(int i = 0; i < Word.N/2; i++)
		{
			p.insertItem(tempItemList.get(i).nameOfWord);
		}
		String mid = tempItemList.get(Word.N/2).nameOfWord;
		for(int i = Word.N/2 + 1; i <= Word.N; i++)
		{
			newSibling.insertItem(tempItemList.get(i).nameOfWord);
		}

		if (!p.isLeaf())
		{
			//	distribute CHILDREN between the two nodes
			
			ArrayList<Word> tempChildList = new ArrayList<Word>();
			for(int i = 0; i < p.getChildren().size(); i++)
			{
				tempChildList.add(p.getChildren().get(i));
			}
			tempChildList.add(index +1, newChild);

			p.getChildren().clear();
			for(int i = 0; i <= Word.N/2; i++)
			{
				p.insertChild(i, tempChildList.get(i));
			}

			for(int i = 0; i <= Word.N/2; i++)
			{
				newSibling.insertChild(i, tempChildList.get(Word.N/2 + 1 + i));
			}

			// update PARENTS of child nodes of the two nodes
			
			for(int i = 0; i < p.getChildren().size(); i++)
			{
				p.getChildren().get(i).setParent(p);
			}

			for(int i = 0; i < newSibling.getChildren().size(); i++)
			{
				newSibling.getChildren().get(i).setParent(newSibling);
			}
		}
		
		Object[] returnArray = new Object[2];
		returnArray[0] = mid;
		returnArray[1] = newSibling;
		return returnArray;
	}
	
	private void insertUpward(Word node, String newItem, Word sibling)
	{
		if (!node.isFull())
		{
			node.insertItem(newItem);
			int index = node.getItemIndex(newItem);
			node.insertChild(index + 1, sibling);
			sibling.setParent(node);
		}
		else
		{
			Object[] returnArray = split(node, newItem, sibling);
			String mid = (String) returnArray[0];
			Word newSibling = (Word) returnArray[1];
			
			Word parent = node.getParent();
			if (parent == null)	//	node is the root
			{
				root = new Word();
				root.insertChild(0, node);
				node.setParent(root);
				parent = root;
			}
					
			insertUpward(parent, mid, newSibling);
		}
	}
	
	//	DISPLAY IN INORDER
	public ArrayList<WordStructure> displayInOrder()
	{
		ArrayList<WordStructure> allWordsInOrder = new ArrayList<WordStructure>();
		return displayInOrder(root,allWordsInOrder);
	}

	public ArrayList<WordStructure> displayInOrder(Word localRoot,ArrayList<WordStructure> allWordsInOrder)
	{
		if(localRoot != null)
		{
			int numberOfItems = localRoot.getData().size();
			boolean isNonLeaf = !localRoot.isLeaf();

			for(int index = 0; index < numberOfItems; index++)
			{
				if (isNonLeaf)
				{
					displayInOrder(localRoot.getChild(index),allWordsInOrder);
				}

				// System.out.println(localRoot.getItem(index) + " "+localRoot.getData().get(index).frequency);
				
				// fw.write(localRoot.getItem(index) + " "+localRoot.getData().get(index).frequency+"\n");		
				allWordsInOrder.add(new WordStructure(localRoot.getItem(index),localRoot.getData().get(index).frequency,localRoot.getItem(index).length()));
			}

			if (isNonLeaf)
			{
				displayInOrder(localRoot.getChild(numberOfItems),allWordsInOrder);
			}
		}
		return allWordsInOrder;
   }
	
	public boolean search (String searchItem)
	{	
		return search(root, searchItem);
	}
	
	private boolean search(Word localRoot, String searchItem)
	{
		if (localRoot == null)
		{
			return false;
		}
		else 
		{
			int index = localRoot.getItemIndex(searchItem);	
			if (index != -1 ) 	
			//	element has been found
			{	
				localRoot.getData().get(index).increaseFrequency();
				root = getRoot(localRoot);
				return true;	
			}	
			else if (localRoot.isLeaf())	
			//	not found and is leaf node (element is not in the tree)
			{
				return false;
			}
			else
			//	not found and non-leaf node (search a subtree)
			{
				//	Word nextChild = getNextChild(localRoot, searchItem);
				index = 0;
				while (index < localRoot.getData().size() && 
							localRoot.getData().get(index).nameOfWord.compareTo(searchItem) < 0)
				{
					index++;
				}
				return search(localRoot.getChildren().get(index), searchItem);
			}
		}
   }

   
	
	public Word getRoot(Word s){
		while(true){
			if(s.getParent() == null)
				return s;
			s = s.getParent();
		}
	}

	public String toString()
	{
		if (root == null)
		{
			return null;
		}
		else
		{
			return root.toString();
		}
	}
	
	
	public static void print(String s)
	{
		System.out.println(s);
	}
	
	public static void print(Object obj)
	{
		print(obj.toString());
	}
}
