
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.school.proto.analyser.AbstractAnalyser;
import com.school.proto.analyser.impl.AnalyserImpl;

public class Analyser extends javax.swing.JFrame {

	File file = null;
	PrintWriter writer = null;

	public Analyser() {
		super("HTTP HLS Protocol Analyser");
		//call to initcomponents(), which contains design
		initComponents();
	}

	private void initComponents() {
		
		jLabel1 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
//below line to exit application
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		jLabel1.setText("File URL");
		getContentPane().add(jLabel1);
		                 //x  y   wid  height
		jLabel1.setBounds(70, 40, 150, 20);

		getContentPane().add(jTextField1);
		jTextField1.setBounds(210, 40, 230, 26);
		//jTextField1.setText("http://146.186.90.203:8080/Arris/ipad.m3u8"); //Remove this

		jButton1.setText("Log File Location");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		getContentPane().add(jButton1);
		jButton1.setBounds(210, 100, 180, 29);

		jButton2.setText("Analyse");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		getContentPane().add(jButton2);
		jButton2.setBounds(210, 150, 180, 29);

		jButton3.setText("View Log");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					jButton3ActionPerformed(evt);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		});
		getContentPane().add(jButton3);
		jButton3.setBounds(210, 200, 180, 29);

		pack();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			try {
				writer = new PrintWriter(new FileOutputStream(file));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} 
	}

	

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
			int i = 0;
			String inputLine;
			StringBuffer logContents= new StringBuffer("");
			while ((inputLine = in.readLine()) != null) {
				
				logContents.append(inputLine).append("\n");
			}
			
			 java.awt.EventQueue.invokeLater(new Runnable() {
		            public void run() {
		            	Log log = new Log();
		            	log.setText(logContents.toString());
		                log.setVisible(true);
		            }
		        });
			in.close();
		}
	}
	
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		URL fileUrl;
		String baseUrl = "";
		String level1Url = "";
		String level2Url = "";
		String level3Url = "";
		String level4Url = "";
		String level5Url = "";
		AbstractAnalyser analyser = new AnalyserImpl();
		try {
			level1Url = jTextField1.getText();
			fileUrl = new URL(level1Url);

			BufferedReader in = new BufferedReader(new InputStreamReader(fileUrl.openStream()));
			int i = 0;
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				
				if(i==2) {
					baseUrl = level1Url.substring(0,level1Url.lastIndexOf("/"))+"/";
					level2Url = baseUrl+inputLine;
					System.out.println("Second level playList URL: "+level2Url);
				}
				
				if(i==4) {
					level3Url = level1Url.substring(0,level1Url.lastIndexOf("/"))+"/"+inputLine;
					System.out.println("Third level playList URL: "+level3Url);
				}
				if(i==6) {
					level4Url = level1Url.substring(0,level1Url.lastIndexOf("/"))+"/"+inputLine;
					System.out.println("Fourth level playList URL: "+level4Url);
				}
				if(i==8) {
					level5Url = level1Url.substring(0,level1Url.lastIndexOf("/"))+"/"+inputLine;
					System.out.println("Fifth level playList URL: "+level5Url);
				}
				i++;
			}
			analyser.analyseLevel1(level1Url, baseUrl,writer);
			analyser.analyseLevel2(level2Url, baseUrl,writer);
			analyser.analyseLevel2(level3Url, baseUrl,writer);
			analyser.analyseLevel2(level4Url, baseUrl,writer);
			analyser.analyseLevel2(level5Url, baseUrl,writer);
			
			
			writer.close();
			in.close();
			JOptionPane.showMessageDialog(null, "Analysis Done");
		} catch (Exception ex) {
			System.out.println("error");
		}
	}

	public static void main(String args[]) {
		Analyser mainFrame = new Analyser();
		mainFrame.setSize(600, 350);
		mainFrame.setVisible(true);

	}


	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JTextField jTextField1;
	// End of variables declaration
}
