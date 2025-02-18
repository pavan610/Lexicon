import java.util.ArrayList;

public class Word
{
	public static final int M = 5;	// order
	public static final int N = M-1;	// Maximum number of nodes
	
	private ArrayList<WordStructure> data;
	private ArrayList<Word> children;
	private int frequency;
	private Word parent;
	
	public Word()
	{
		data = new ArrayList<WordStructure>();
		children = new ArrayList<Word>();
		parent = null;
	}
	
	//	Insert item into the proper place in data
	public void insertItem(String item)
	{
		int index = 0;
		while (index < data.size() && data.get(index).nameOfWord.compareTo(item) < 0)
		{
			index++;
		}
		data.add(index, new WordStructure(item,item.length()));
	}
	
	public void setParent(Word parent)
	{
		this.parent = parent;
	}
	
	public void insertChild(int index, Word child)
	{
		children.add(index, child);
	}

	public boolean isFull()
	{
		return data.size() == N;
	}
	
	public boolean isLeaf()
	{
		return children.size() == 0;
	}

	//getters

	public Word getParent()
	{
		return parent;
	}

	public ArrayList<WordStructure> getData()
	{
		return data;
	}
	
	public ArrayList<Word> getChildren()
	{
		return children;
	}
	
	public String getItem(int index)
	{
		return data.get(index).nameOfWord;
	}
	
	public Word getChild(int index)
	{
		return children.get(index);
	}
	
	public int getItemIndex(String item)
	{
		for(int index = 0; index < data.size(); index++)
		{
			if (data.get(index).nameOfWord.compareTo(item) == 0)
			{
				return index;
			}
		}
		return -1;
	}
	
	public String toString()
	{
		return toString(0);
	}
	
	public String toString(int level)
	{
		String indent = "";
		for(int i = 1; i <= level; i++) indent = indent + "- ";
		
		String s = indent + data.toString() 
			+ " (parent: " + (parent==null? "null": parent.getData()) + ")\n";
		
		for(int i = 0; i < children.size(); i++)
		{
			// s = s + indent + "- " + children.get(i).toString();
			s = s + children.get(i).toString(level + 1);
		}
		return s;
	}
}
