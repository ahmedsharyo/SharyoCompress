package SharyoCompress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;


public class VectorQuantization {
	
	public static class Node
	{
		double c[][]=null;
		Node (int sz)
		{
			 c=new double [sz][sz];
			 for(int i=0;i<sz;i++)
					for(int j=0;j<sz;j++)c[i][j]=0.0;
		}
	}
	
	
	static Node sum (Node a,Node b,int sz)
	{
		Node res =new Node(sz);
		for(int i=0;i<sz;i++)
			for(int j=0;j<sz;j++)
				res.c[i][j]=a.c[i][j]+b.c[i][j];
		return res;
	}
	
	static double abs(double x)
	{
		if(x<0.0)return (-1)*x;
		return x;
	}
	
	static double dist (Node a,Node b,int sz)
	{
		double res=0.0;
		for(int i=0;i<sz;i++)
			for(int j=0;j<sz;j++)
				res+=abs(a.c[i][j]-b.c[i][j]);
		return res;
	}
	
	static Node avg (Vector<Node> a,int sz)
	{
		Node res =new Node(sz);
		for(int i=0;i<a.size();i++)res=sum(res,a.elementAt(i),sz);
		
		for(int i=0;i<sz;i++)
			for(int j=0;j<sz;j++)res.c[i][j]/=(a.size()+0.0);
		
      return res;
	}

	public static void comp(int [][]a,int block_sz,int nov)
	{
		
		int N=a.length-a.length%block_sz;
		int M=a[0].length-a[0].length%block_sz;
		int n=N/block_sz;
		int m=M/block_sz;
		Node b[][]=new Node [n][m];
		Vector<Node>q=new Vector<Node>(); 
		Vector<Node>sq=new Vector<Node>(); 

		for(int i=0,k=0;i<N;i+=block_sz,k++)
		{
			for(int j=0,l=0;j<M;j+=block_sz,l++)
			{
				Node tmp=new Node(block_sz);
				
				for(int r=i,o=0;r<i+block_sz;r++,o++)
				{
					for(int z=j,p=0;z<j+block_sz;z++,p++ )
					{
						tmp.c[o][p]=a[r][z];
					}
				}
				
				b[k][l]=new Node(block_sz);
				b[k][l].c=tmp.c;
			}
		}
		
		Node av=new Node(block_sz);
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)
			{
				av=sum(av,b[i][j],block_sz);
			}
		}
		
		for(int i=0;i<block_sz;i++)
			for(int j=0;j<block_sz;j++)av.c[i][j]/=(n*m+0.0);
		
		q.addElement(av);
		int split=(int)(Math.log(nov)/Math.log(2));
		while(split>0)
		{
			while(q.size()>0)
			{
				Node tmp=new Node(block_sz);
				Node tmp1=new Node(block_sz);
				Node tmp2=new Node(block_sz);
				tmp=q.get(0);
				for(int i=0;i<block_sz;i++) {
					for(int j=0;j<block_sz;j++)
					{
						double t=tmp.c[i][j];
						if(t!=(int)t)
						{
							tmp1.c[i][j]=Math.floor(t);
							tmp2.c[i][j]=Math.ceil(t);
						}
						else
						{
							tmp1.c[i][j]=t-1;
							tmp2.c[i][j]=t+1;
						}
					}
				}
				
				sq.addElement(tmp1);
				sq.addElement(tmp2);

				q.remove(0);
			}
			
			HashMap<Integer, Vector<Node> > mp = new HashMap<Integer, Vector<Node> > ();
			
			for(int i=0;i<n;i++)
			{
				for(int j=0;j<m;j++)
				{
					double mnd=1000000000.0;
					int ind=0;
					for(int k=0;k<sq.size();k++)
					{
						double d=dist(sq.get(k),b[i][j],block_sz);
						if(d<mnd)
						{
							mnd=d;
							ind=k;
						}
					}
					
				
					Vector<Node>temp=new Vector<Node>(); 
					if(mp.get(ind)==null)mp.put(ind, temp);
					temp=mp.get(ind);
					temp.addElement(b[i][j]);
					mp.put(ind, temp);
				}
			}
			
		   
			for(int k=0;k<sq.size();k++)
			{
				Vector<Node>temp=new Vector<Node>(); 
				if(mp.get(k)==null)mp.put(k, temp);
				temp=mp.get(k);
				Node tmp =new Node(block_sz);
				tmp=avg(temp,block_sz);
				q.addElement(tmp);
			}
            sq.clear();
            mp.clear();
			
            split--;
		}
		
		int to_write[][]=new int [n][m];
		
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)
			{
				double mnd=1000000000.0;
				int ind=0;
				for(int k=0;k<q.size();k++)
				{
					double d=dist(q.get(k),b[i][j],block_sz);
					if(d<mnd)
					{
						mnd=d;
						ind=k;
						
					}
				}
				to_write[i][j]=ind;

			}
		}
		
		     // write
				String path = "Image.txt";
				try {
					FileOutputStream fos = new FileOutputStream(path);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					
					os.writeInt(n);
					os.writeInt(m);
					os.writeInt(block_sz);
					os.writeInt(nov);
					
					
					
					
					for(int k=0;k<nov;k++)
					{
						for(int i=0;i<block_sz;i++)
							for(int j=0;j<block_sz;j++)
								os.writeDouble(q.get(k).c[i][j]);
					}
					
					for(int i=0;i<n;i++)
					{
						for(int j=0;j<m;j++)
						{
							os.writeInt(to_write[i][j]);
						}
						
					}
					
					os.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		
	
	}
	
	
	
	
	
	
	
	public static  int [][] decomp()
	{
		
		 String path = "Image.txt";

			int n=0,m=0,block_sz=0,nov=0;
			Node [] q=null;
            int [][]b=null;
			
			FileInputStream fis;
			try {
				fis = new FileInputStream(path);
				ObjectInputStream is = new ObjectInputStream(fis);
				
				n=is.readInt();
				m=is.readInt();
				block_sz=is.readInt();
				nov=is.readInt();

				q=new Node[nov];
	            b=new int [n][m];
	            for(int k=0;k<nov;k++)
				{
	            	q[k]=new Node(block_sz);
	            	
					for(int i=0;i<block_sz;i++)
						for(int j=0;j<block_sz;j++)
						{

							q[k].c[i][j]=is.readDouble();
						}	
				}
	            
	            for(int i=0;i<n;i++)
				{
					for(int j=0;j<m;j++)
					{
						b[i][j]=is.readInt();

					}
					
				}
				
				is.close();

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  int N=n*block_sz;
		  int M=m*block_sz;
		 int [][] a=new int [N][M];
		 
		 for(int i=0,k=0;i<N;i+=block_sz,k++)
			{
				for(int j=0,l=0;j<M;j+=block_sz,l++)
				{
					Node tmp=new Node(block_sz);
					int t=b[k][l];
					
					tmp=q[t];
					
					for(int r=i,o=0;r<i+block_sz;r++,o++)
					{
						for(int z=j,p=0;z<j+block_sz;z++,p++ )
						{
							a[r][z]=(int)tmp.c[o][p];
						}
					}
					
				}
			}
		 
		return a;
	}

}
