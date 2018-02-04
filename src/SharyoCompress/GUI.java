package SharyoCompress;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import SharyoCompress.LBG.Globals;

import javax.swing.JScrollPane;

public class GUI {

	private JFrame frmSharyo;

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmSharyo.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public GUI() {
		initialize();
	}


	private void initialize() {
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 22));
		frmSharyo = new JFrame();
		frmSharyo.setTitle("SharyoCompress");
		frmSharyo.setBounds(100, 100, 1158, 762);
		frmSharyo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSharyo.getContentPane().setLayout(null);
		
		
		
		
		textArea.setBounds(91, 72, 567, 45);
		frmSharyo.getContentPane().add(textArea);
		

		JScrollPane scrollPane_1 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1.setBounds(91, 146, 570, 103);
		frmSharyo.getContentPane().add(scrollPane_1);
		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		textArea_1.setFont(new Font("Monospaced", Font.PLAIN, 22));
		
		
		
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(91, 581, 579, 121);
		frmSharyo.getContentPane().add(scrollPane);
		JTextArea textArea_2 = new JTextArea();
		scrollPane.setViewportView(textArea_2);
		textArea_2.setFont(new Font("Monospaced", Font.PLAIN, 22));
        //LZ77
		JButton btnNewButton = new JButton("compress");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String path = textArea.getText();
				String input = "";

				FileInputStream fis;
				try {
					fis = new FileInputStream(path);
					ObjectInputStream is=new ObjectInputStream(fis);
					while(is.available()!=0)
					{
						input+=(char)is.read();
					}
					is.close();

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
       
				Vector<LZ77_Tag> tags = LZ77_Process.comp(input);

				try {
					FileOutputStream fos = new FileOutputStream(path);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					textArea_2.setText("");
					for (LZ77_Tag x : tags) {
						String tmp = "<" + x.last + " ," + x.len + " ," + x.nxt + ">\n";

						textArea_2.append(tmp);
						os.write(x.last);
						os.write(x.len);
						os.write(x.nxt);
					}

					os.close();
				} catch (FileNotFoundException e1) {
					String msg = "invalid path";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					String msg = "an error occured";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(154, 385, 166, 51);
		frmSharyo.getContentPane().add(btnNewButton);
        //LZ77
		JButton btnDecompress = new JButton("decompress");
		btnDecompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String path = textArea.getText();
				char nxt = 't';
				int last, len;
				FileInputStream fis;
				Vector<LZ77_Tag> tags=new Vector<LZ77_Tag>();
				try {
					fis = new FileInputStream(path);
					ObjectInputStream is=new ObjectInputStream(fis);
   
					while (is.available()!=0) {
						
						last=(int)is.read();
						len=(int)is.read();
						nxt=(char)is.read();
                       LZ77_Tag t=new LZ77_Tag(last,len,nxt);
                       tags.addElement(t);
					}
					is.close();

				} catch (EOFException e2) {
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				String tmp=LZ77_Process.decomp(tags);
				textArea_2.setText(tmp);
                  String input=tmp;
				try {
					FileOutputStream fos = new FileOutputStream(path);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					os.writeBytes(input);
					os.close();
				} catch (FileNotFoundException e1) {
					String msg = "invalid path";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					String msg = "an error occurred in--> ObjectOutputStream";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				}
				
			}
		});
		btnDecompress.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnDecompress.setBounds(382, 382, 166, 56);
		frmSharyo.getContentPane().add(btnDecompress);

		JLabel lblInput = new JLabel("input");
		lblInput.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblInput.setBounds(28, 107, 68, 66);
		frmSharyo.getContentPane().add(lblInput);

		JLabel lblOutput = new JLabel("output");
		lblOutput.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblOutput.setBounds(12, 583, 78, 33);
		frmSharyo.getContentPane().add(lblOutput);

		JButton btnNewButton_1 = new JButton("save");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String path = textArea.getText();
				String input = textArea_1.getText();

				try {
					FileOutputStream fos = new FileOutputStream(path);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					os.writeBytes(input);
					os.close();
				} catch (FileNotFoundException e1) {
					String msg = "invalid path";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					String msg = "an error occurred in--> ObjectOutputStream";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				}

			}
		});
		
		
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnNewButton_1.setBounds(673, 173, 124, 45);
		frmSharyo.getContentPane().add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Browse");
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path;
				JFileChooser filechooser = new JFileChooser();
				if (filechooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					java.io.File file = filechooser.getSelectedFile();
					path = file.getAbsolutePath();
				} else {
					path = "No file was selected";
				}

				textArea.setText(path.toString());
			}
		});
		btnNewButton_2.setBounds(673, 72, 124, 33);
		frmSharyo.getContentPane().add(btnNewButton_2);

		
		//LZW
		JButton button = new JButton("compress");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = textArea.getText();
				String input = "";

				FileInputStream fis;
				try {
					fis = new FileInputStream(path);
					ObjectInputStream is=new ObjectInputStream(fis);
					while(is.available()!=0)
					{
						input+=(char)is.read();
					}
					is.close();

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Vector<Integer>ret=LZW.comp(input);
				

				try {
					FileOutputStream fos = new FileOutputStream(path);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					textArea_2.setText("");
					for (Integer x : ret) {
						String tmp = x+ "\n";

						textArea_2.append(tmp);
						os.write(x);
					}

					os.close();
				} catch (FileNotFoundException e1) {
					String msg = "invalid path";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					String msg = "an error occured";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				}

				
			}
		});
		button.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button.setBounds(154, 451, 166, 51);
		frmSharyo.getContentPane().add(button);
		//LZW
		JButton button_1 = new JButton("decompress");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = textArea.getText();
				Integer t;
				FileInputStream fis;
				Vector<Integer> tags=new Vector<Integer>();
				try {
					fis = new FileInputStream(path);
					ObjectInputStream is=new ObjectInputStream(fis);
   
					while (is.available()!=0) {
						
						t=(int)is.read();
						
                       tags.addElement(t);
					}
					is.close();

				} catch (EOFException e2) {
					// we expect it so ignore
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				String tmp=LZW.decomp(tags);
				textArea_2.setText(tmp);
                  String input=tmp;
				try {
					FileOutputStream fos = new FileOutputStream(path);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					os.writeBytes(input);
					os.close();
				} catch (FileNotFoundException e1) {
					String msg = "invalid path";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					String msg = "an error occurred in--> ObjectOutputStream";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				}
			}
		});
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_1.setBounds(382, 451, 166, 51);
		frmSharyo.getContentPane().add(button_1);
		
		JLabel lblLz = new JLabel("LZ77");
		lblLz.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblLz.setBounds(74, 369, 68, 66);
		frmSharyo.getContentPane().add(lblLz);
		
		JLabel lblLzw = new JLabel("LZW");
		lblLzw.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblLzw.setBounds(74, 443, 68, 66);
		frmSharyo.getContentPane().add(lblLzw);
		//Huffman
		JButton button_2 = new JButton("compress");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = textArea.getText();
				String input = "";

				FileInputStream fis;
				try {
					fis = new FileInputStream(path);
					ObjectInputStream is=new ObjectInputStream(fis);
					while(is.available()!=0)
					{
						input+=(char)is.read();
					}
					is.close();

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
				String tmp=Huffman.compress(input);
				textArea_2.setText(tmp);
                input=tmp;
				try {
					FileOutputStream fos = new FileOutputStream(path);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					os.writeBytes(input);
					os.close();
				} catch (FileNotFoundException e1) {
					String msg = "invalid path";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					String msg = "an error occurred in--> ObjectOutputStream";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				}
				
				
			}
		});
		button_2.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_2.setBounds(154, 321, 166, 51);
		frmSharyo.getContentPane().add(button_2);
		//Huffman
		JButton button_3 = new JButton("decompress");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = textArea.getText();
				String input = "";

				FileInputStream fis;
				try {
					fis = new FileInputStream(path);
					ObjectInputStream is=new ObjectInputStream(fis);
					while(is.available()!=0)
					{
						input+=(char)is.read();
					}
					is.close();

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
				String tmp=Huffman.decompress(input);
				textArea_2.setText(tmp);
                input=tmp;
				try {
					FileOutputStream fos = new FileOutputStream(path);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					os.writeBytes(input);
					os.close();
				} catch (FileNotFoundException e1) {
					String msg = "invalid path";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					String msg = "an error occurred in--> ObjectOutputStream";
					textArea.setText(msg.toString());
					e1.printStackTrace();
				}
				
				
			}
		});
		button_3.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_3.setBounds(382, 318, 166, 56);
		frmSharyo.getContentPane().add(button_3);
		
		JLabel lblHufman = new JLabel("Hufman");
		lblHufman.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblHufman.setBounds(55, 313, 96, 66);
		frmSharyo.getContentPane().add(lblHufman);
		
		//arithmetic
		JButton button_4 = new JButton("compress");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = textArea.getText();
				String input = "";

				FileInputStream fis;
				try {
					fis = new FileInputStream(path);
					ObjectInputStream is=new ObjectInputStream(fis);
					while(is.available()!=0)
					{
						input+=(char)is.read();
					}
					is.close();

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
				Double tmp=Arithmetic_coding.comp(input);
				String t=tmp.toString();
				textArea_2.setText(t);
				
			}
		});
		button_4.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_4.setBounds(154, 517, 166, 51);
		frmSharyo.getContentPane().add(button_4);
		
		//arithmetic
		JButton button_5 = new JButton("decompress");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String tmp=Arithmetic_coding.decomp();
				textArea_2.setText(tmp);
			}
		});
		button_5.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_5.setBounds(382, 517, 166, 51);
		frmSharyo.getContentPane().add(button_5);
		
		JLabel lblArithmetic = new JLabel("Arithmetic");
		lblArithmetic.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblArithmetic.setBounds(37, 509, 105, 66);
		frmSharyo.getContentPane().add(lblArithmetic);
		
		JLabel lblCompressText = new JLabel("compress text :");
		lblCompressText.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblCompressText.setBounds(12, 250, 204, 66);
		frmSharyo.getContentPane().add(lblCompressText);
		
		JLabel lblCompressImage = new JLabel("compress image :");
		lblCompressImage.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblCompressImage.setBounds(518, 250, 204, 66);
		frmSharyo.getContentPane().add(lblCompressImage);
		
		JLabel lblLbgWithSplitting = new JLabel(" Non Uniform Quantizer \" LBG with Splitting\"");
		lblLbgWithSplitting.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblLbgWithSplitting.setBounds(560, 273, 507, 85);
		frmSharyo.getContentPane().add(lblLbgWithSplitting);
		//LBG
		JButton button_6 = new JButton("compress");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = textArea.getText();
				int[][]tmp=ImageClass.readImage(path);
				String t=textArea_1.getText();
				Globals.B= Integer.parseInt(t);
				LBG.comp(tmp);
			}
		});
		button_6.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_6.setBounds(771, 338, 166, 51);
		frmSharyo.getContentPane().add(button_6);
		
		//LBG
		JButton button_7 = new JButton("decompress");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = textArea.getText();
				int [][]tmp=LBG.decomp();
				ImageClass.writeImage(tmp,path);
				
				
			}
		});
		button_7.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_7.setBounds(962, 333, 166, 56);
		frmSharyo.getContentPane().add(button_7);
		
		JLabel lblnumberOfBits = new JLabel("\"number of bits as input\"");
		lblnumberOfBits.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblnumberOfBits.setBounds(592, 307, 295, 85);
		frmSharyo.getContentPane().add(lblnumberOfBits);
		
		JLabel lblSplitting = new JLabel("  Vector Quantization using Splitting");
		lblSplitting.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblSplitting.setBounds(570, 381, 507, 85);
		frmSharyo.getContentPane().add(lblSplitting);
		
		//Vector Quantization
		JButton button_8 = new JButton("compress");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path = textArea.getText();
				int[][]tmp=ImageClass.readImage(path);
				String t=textArea_1.getText();
				String a[]= new String [2];
				a=t.split(" ");
				VectorQuantization.comp(tmp, Integer.parseInt(a[0]),Integer.parseInt(a[1]));
				
			}
		});
		button_8.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_8.setBounds(771, 451, 166, 51);
		frmSharyo.getContentPane().add(button_8);
		//Vector Quantization
		JButton button_9 = new JButton("decompress");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = textArea.getText();
				int [][]tmp=VectorQuantization.decomp();
				ImageClass.writeImage(tmp,path);
			}
		});
		button_9.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_9.setBounds(962, 448, 166, 56);
		frmSharyo.getContentPane().add(button_9);
		
		JLabel lblFeedForwardCoding = new JLabel("Feed Forward Coding\r\n");
		lblFeedForwardCoding.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblFeedForwardCoding.setBounds(597, 479, 507, 85);
		frmSharyo.getContentPane().add(lblFeedForwardCoding);
		
		//DPCM
		JButton button_10 = new JButton("compress");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String path = textArea.getText();
				int[][]tmp=ImageClass.readImage(path);
				String t=textArea_1.getText();
				DPCM.comp(tmp,Integer.parseInt(t));
			}
		});
		button_10.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_10.setBounds(771, 545, 166, 51);
		frmSharyo.getContentPane().add(button_10);
		
		JButton button_11 = new JButton("decompress");
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = textArea.getText();
				int [][]tmp=DPCM.decomp();
				ImageClass.writeImage(tmp,path);
			}
		});
		button_11.setFont(new Font("Tahoma", Font.PLAIN, 22));
		button_11.setBounds(962, 545, 166, 51);
		frmSharyo.getContentPane().add(button_11);
		
		
		
		 
	}
}
