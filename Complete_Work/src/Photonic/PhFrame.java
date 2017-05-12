package Photonic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class PhFrame extends Graph_Plot implements ActionListener 
{
	JMenuBar menuBar;
	JPanel contentPanel;
	JMenuItem menuItem1,menuItem2,menuItem3;
	JMenu menu,menu1;
	JScrollPane westPanel , eastPanel;
	JInternalFrame internalFrame;
	JLayeredPane layeredPane1,layeredPane2;
	JTextField tf[];
	JLabel label[];
	JLabel lblNewLabel,BW,PV,result;
	JButton button;
	String str[]={"New","Open","Save","Save as","Exit"};
	String s[] = {"  n1", "  n2", "  d1", "  d2", "  Ɵ", "  N"};
	String s1[] = {"in between 1 to 1.6 ", "in between 1 to 1.2 ", "in between 1 to 5 micrometer ", "in between 0.2 to 1.5 micrometer ","in between 0 to 30 degree "," 7,11 or 15 "};
	double n1,n2,d3,d4;
	double[] db = new double[100];
	double single_plot[]=new double[6];
	int M1,ctr=0;;
	int n[] = new int[50];
	boolean singlegraph,multigraph;
	Graph_Plot gp;
	    
	public PhFrame()
	{
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		menuBar = new JMenuBar();
		menu = new JMenu("Options");
		menuItem1 = new JMenuItem("Single Graph View");
		menuItem1.setFocusable(true);
		menu.add(menuItem1);
		menuItem2 = new JMenuItem("Multi Graph View");
		menuItem2.setFocusable(true);
		menu.add(menuItem2);
		menu.addSeparator();
		menuItem3 = new JMenuItem("Exit");
		menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
		menu.add(menuItem3);
		westPanel = new JScrollPane();
		
		singleDisplay();
		
		menuItem1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				singleDisplay();
			}
		});
		menuItem2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				multiDisplay();	
			}
		});
		menuItem3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				System.exit(0);
			}
		});
		menuBar.add(menu);
	    setJMenuBar(menuBar);
     
    	internalFrame = new JInternalFrame("THE GRAPHICAL PLOT");
    	internalFrame.setBorder(new LineBorder(new Color(176, 224, 200), 5));
    	internalFrame.add(Graph_plot);
		internalFrame.setVisible(true);
    	eastPanel = new JScrollPane(internalFrame);
		
    	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, westPanel,eastPanel);
        splitPane.setDividerLocation(255);
        contentPanel.add(splitPane, BorderLayout.CENTER);
        setContentPane(contentPanel);
     
        addWindowListener(new WindowAdapter() 
		{
            public void windowClosing(WindowEvent e) 
			{
                System.exit(0);
            }
        });
      
        setLocation(100, 100);
        setSize(1030, 590);
        setTitle("Transmitivity Of Photonic Crystal");
        setVisible(true);

    } 
    
	void singleDisplay()
    {
		multigraph=false;
		singlegraph=true;
		menuItem1.setBackground(new Color(176, 224, 200));
		menuItem2.setBackground(null);
	    tf=new JTextField[6];
	    label = new JLabel[6];
	    layeredPane1 = new JLayeredPane();
	 	layeredPane1.setOpaque(true);
	 	layeredPane1.setBackground(new Color(153, 255, 204));
		lblNewLabel = new JLabel("    SET INPUT VALUES");
		lblNewLabel.setForeground(new Color(0, 0, 153));
		lblNewLabel.setBackground(new Color(153, 255, 204));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBounds(22, 20, 205, 32);
		layeredPane1.add(lblNewLabel);
	
		for(int i=0;i<6;i++)
		{
			label[i] = new JLabel(s[i]);
	        label[i].setFont(new Font("Yu Gothic UI Semilight", Font.BOLD, 17));
	        label[i].setForeground(new Color(0, 0, 153));
	        label[i].setOpaque(true);//true for putting background color
	        label[i].setBackground(new Color(153, 255, 204));
	        label[i].setBounds(22, (i%2==0?(80+ 37 * i):(75 + 37 * i))  , 38, 24);
	        label[i].setToolTipText("Value of"+s[i]+" must be "+s1[i]);
	        tf[i] = new JTextField();
	 		tf[i].setBounds(80, (i%2==0?(80+ 37 * i):(75 + 37 * i))  , 110, 24);
	 		tf[i].setColumns(10);
	 		if(i==4) tf[i].setText("10");
	        layeredPane1.add(label[i]);
	        layeredPane1.add(tf[i]);
		}
		button = new JButton("GET PLOT");
		button.setToolTipText("CLICK AND GET THE GRAPHICAL PLOT");
		button.setBounds(22, 310, 110, 45);
		layeredPane1.add(button);
		
		result = new JLabel("   RESULT");
		result.setForeground(new Color(0, 0, 153));
		lblNewLabel.setBackground(new Color(153, 255, 204));
		result.setOpaque(true);
		result.setFont(new Font("Tahoma", Font.BOLD, 17));
		result.setBounds(22, 370, 100, 35);
		result.setBackground(new Color(153, 255, 204));
		layeredPane1.add(result);
		
		BW = new JLabel(" BAND WIDTH :");
		BW.setFont(new Font("Tahoma", Font.BOLD, 13));
		BW.setBackground(new Color(255, 255, 204));
		BW.setOpaque(true);
		BW.setBounds(22, 420, 188, 35);
		layeredPane1.add(BW);
		
		PV = new JLabel(" RIPPLE :");
		PV.setFont(new Font("Tahoma", Font.BOLD, 13));
		PV.setBackground(new Color(255, 255, 204));
		PV.setOpaque(true);
		PV.setBounds(22, 470, 188, 35);
		layeredPane1.add(PV);
	    button.addActionListener(this);
	    westPanel.setViewportView(layeredPane1);
	}  
	
	void multiDisplay()
    {
	
		multigraph=true;
	    singlegraph=false;
	    menuItem2.setBackground(new Color(176, 224, 200));
	    menuItem1.setBackground(null);
	    tf=new JTextField[6];
	    label = new JLabel[6];
	    layeredPane2 = new JLayeredPane();
	    layeredPane2.setOpaque(true);
	    layeredPane2.setBackground(new Color(153, 255, 204));
	    lblNewLabel = new JLabel("    SET INPUT VALUES");
		lblNewLabel.setForeground(new Color(0, 0, 153));
		lblNewLabel.setBackground(new Color(153, 255, 204));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBounds(22, 20, 205, 32);
		layeredPane2.add(lblNewLabel);
	
		for(int i=0;i<6;i++)
		{
			label[i] = new JLabel(s[i]);
	        label[i].setFont(new Font("Yu Gothic UI Semilight", Font.BOLD, 17));
	        label[i].setForeground(new Color(0, 0, 153));
	        label[i].setOpaque(true);
	        label[i].setBackground(new Color(153, 255, 204));
	        label[i].setBounds(22, (80 + 50 * i) , 38, 24);
	        label[i].setToolTipText("Value of"+s[i]+" must be "+s1[i]);
	        tf[i] = new JTextField();
	 		tf[i].setBounds(80, (80 + 50 * i)  , 110, 24);
	 		tf[i].setColumns(10);
	 		if(i==4) tf[i].setText("10");
	        layeredPane2.add(label[i]);
	        layeredPane2.add(tf[i]);
		}
		button = new JButton("GET PLOT");
		button.setToolTipText("CLICK AND GET THE GRAPHICAL PLOT");
		button.setBounds(22, 385, 110, 45);
		layeredPane2.add(button);
	    button.addActionListener(this);
	    westPanel.setViewportView(layeredPane2); 
	}  
	
	public void actionPerformed(ActionEvent ae)
	{
		for(int i=0;i<6;i++)
	    {
			try
	    	{
				if(i==5)  M1 =Integer.parseInt(tf[5].getText());
	    		else
    			{
	    			single_plot[i]=Double.valueOf(tf[i].getText());
	    		}
	    	}
		    catch(Exception ee)
		    {
		    	
				if(i==5) JOptionPane.showMessageDialog(this,"Must fill TextFiled of 'N'","Error",JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(this,"Must fill TextFiled of"+s[i],"Error",JOptionPane.ERROR_MESSAGE);
			    return; 
		    }
	    }
	    boolean B[]=new boolean[6];
	    B[0]=(single_plot[0]>=1&&single_plot[0]<=1.6); B[1]=(single_plot[1]>=1&&single_plot[1]<=1.2); B[2]=(single_plot[2]>=1&&single_plot[2]<=5);
	    B[3]=(single_plot[3]>=0.2&&single_plot[3]<=1.5);B[4]=(single_plot[4]>=0&&single_plot[4]<=30);B[5]=(M1==11||M1==7||M1==15);
	    
	    for(int i=0;i<6;i++)
	    {
	    	if(!B[i])
	    	{
		    	try
		    	{
		    		throw new ArithmeticException();
		    	}
		    	catch(ArithmeticException ae1)
		    	{
		    		JOptionPane.showMessageDialog(this,s[i]+" must be "+ s1[i],"Error",JOptionPane.ERROR_MESSAGE);
		            return;
		    	}
	    	}
	    }
	    
		if(ae.getSource()==button)
		{
			internalFrame.setBounds(294, 30, 627, 461);
			if(singlegraph)
			{
				Graph_plot.removeAllPlots();
				Graph_plot.removeLegend(); 
				Graph_cal(M1,single_plot,n,db,ctr,singlegraph);
				BW.setText(" BAND WIDTH : "+bandwidth+" THz");
				PV.setText(" RIPPLE : "+Float.toString(ripple)+" db");
			}
			if(multigraph)
			{
				ctr++;
				if(ctr>3)ctr=1;
				n[ctr]=M1;
				db[5*(ctr-1)+1]=single_plot[0];
				db[5*(ctr-1)+2]=single_plot[1];
				db[5*(ctr-1)+3]=single_plot[2];
				db[5*(ctr-1)+4]=single_plot[3];
				db[5*(ctr-1)+5]=single_plot[4];
				 
				Graph_plot.removeAllPlots();
				Graph_plot.removeLegend();
				Graph_cal(M1,single_plot,n,db,ctr,singlegraph);
			}
		}
	}
	
    public static void main(String[] args) 
	{
        try 
		{
			new PhFrame();
        } 
		catch(Exception ex) 
		{
            ex.printStackTrace();
        }
    } 
}


