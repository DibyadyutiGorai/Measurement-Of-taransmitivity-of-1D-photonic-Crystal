package Photonic;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;
import flanagan.complex.ComplexMatrix;
import flanagan.complex.Complex;
import flanagan.math.*;
import org.math.plot.*;
import org.math.plot.plotObjects.*;
import static java.lang.Math.*;

@SuppressWarnings("serial")
public class Graph_Plot extends JFrame
{
	double j,k,p,l,n1,n2,d3,d4,angle,b1,b2,bw,pv,ex;
    double[] TT1,TT11,lambda,dd1,dd2,dd3,ar2;
    double TTM11[][];
	float ripple;
    int ctr,N;
	int[] ar1;
	String bandwidth;
    Plot2DPanel Graph_plot=new Plot2DPanel();
    boolean bo;
    

	public Graph_Plot(){}

    public void Graph_cal(int a,double sp[],int ar1[],double ar2[], int ctr,boolean bo)
    {
    	N=a;
    	n1=sp[0];
    	n2=sp[1];
    	d3=sp[2];
    	d4=sp[3];
    	angle=sp[4];
    	this.bo=bo;
    	this.ctr=ctr;
    	this.ar1=ar1;
    	this.ar2=ar2;
		
		if(bo)
        {
			b1=graphCal_single(0);
			b2=graphCal_single(1);
			System.out.println(b2+" "+b1);
			if((b1-b2)<0) bw=b2-b1;
			else bw=b1-b2;
			bw=((3E-4)/bw);
			DecimalFormat df=new DecimalFormat("#.000");
			bandwidth=df.format(bw);
			
			Graph_plot.addLinePlot("plot", lambda, TT11);
		} 
        else
        {
     	    if(ctr==1)
			{
     		  graphCal_multi();
              Graph_plot.addLegend("SOUTH");
              Graph_plot.addLinePlot("plot 1",new Color(102,102,255), lambda,dd1);
            }
			if(ctr==2)
			{
            	graphCal_multi();
                Graph_plot.addLegend("SOUTH");
                Graph_plot.addLinePlot("plot 1",new Color(102,102,255), lambda,dd1);
                Graph_plot.addLinePlot("plot 2",new Color(255,102,102), lambda, dd2);
            }
			if(ctr==3)
			{
            	graphCal_multi();
                Graph_plot.addLegend("SOUTH");
                Graph_plot.addLinePlot("plot 1",new Color(102,102,255), lambda,dd1);
                Graph_plot.addLinePlot("plot 2",new Color(255,102,102), lambda, dd2);
                Graph_plot.addLinePlot("plot 3",new Color(0,204,102), lambda, dd3);

            } 
        }
		BaseLabel title = new BaseLabel(" Graph plot ", Color.GREEN, 0.5, 1.1);
        title.setFont(new Font("Tahoma", Font.BOLD, 14));
        Graph_plot.addPlotable(title);
        Graph_plot.setAxisLabels("lambda", "Transmitivity");
        Graph_plot.plotCanvas.setBackground(Color.WHITE);
        Graph_plot.getAxis(0).setColor(Color.BLUE.darker());
        Graph_plot.getAxis(0).setLabelPosition(0.5, -0.1);
        Graph_plot.getAxis(1).setColor(Color.BLUE.darker());
        Graph_plot.getAxis(1).setLabelPosition(-0.1, 0.5);
    }
     
	
	@SuppressWarnings("unused")
	public double graphCal_single(int n)
	{
		j = 1.3E-6;
		k = 1.75E-6;
		p = (k - j) / 1000;
		TT1= new double[1001];
		TT11 = new double[1001];
		lambda= new double[1001];
		double small=0,var=0,lvar=0;
		for (int i = 0; i < 1002; i++)
	    {
		   double k1,k2;
		   if(i!=1001)
		   {
			   lambda[i] = j;
			   k1 = (2 * PI * n1 / lambda[i]);
			   k2 = (2 * PI * n2 / lambda[i]);
		   }
		   else
		   {
			   ex=1.5E-6;
			   k1 = (2 * PI * n1 / ex);
			   k2 = (2 * PI * n2 / ex);
		   }
		   j = j + p;
		   double theta1 = angle * PI / 180, theta2 = Math.asin(((n2 * Math.sin(theta1)) / n1));
		   double d31 = d3*(1E-6);
		   double d41 = d4*(1E-6);
		   double a1 = Math.cos(theta1), a2 = Math.cos(theta2);
		   double d1 = d31 / a1, d2 = d41 / a2;
           double r12 = ((n1 * a1) - (n2 * a2))/((n1 * a1) + (n2 * a2));
           double r21 = -r12;
           double t12 = Math.sqrt(Math.abs(1 - r12 * r12));
		   double t21 = Math.sqrt(Math.abs(1 - r21 * r21));

		   Matrix A = new Matrix(new double[][]{{1, r12}, {r12, 1}});
		   Matrix B = new Matrix(new double[][]{{1, r21}, {r21, 1}});

		   Matrix M12 = A.times(1 / t12);
		   Matrix M21 = B.times(1 / t21);

           
           double t = -(1 / (t12 * t21));
		   
		   Complex eye1 = new Complex(Math.cos(k1 * d1), Math.sin(k1 * d1));
		   Complex eye2 = new Complex(Math.cos(k1 * d1), -Math.sin(k1 * d1));

		   Complex eye11 = eye1.times(r21);
		   Complex eye22 = eye2.times(r21);

		   Complex eye3 = new Complex(Math.cos(k2 * d2), Math.sin(k2 * d2));
		   Complex eye4 = new Complex(Math.cos(k2 * d2), -Math.sin(k2 * d2));

		   Complex eye33 = eye3.times(r12);
		   Complex eye44 = eye4.times(r12);

		   Complex e1 = (eye1.times(eye3)).plus(eye22.times(eye33)).times(t);
		   Complex e2 = (eye1.times(eye44)).plus(eye22.times(eye4)).times(t);
		   Complex e3 = (eye11.times(eye3)).plus(eye2.times(eye33)).times(t);
		   Complex e4 = (eye11.times(eye44)).plus(eye2.times(eye4)).times(t);

		   ComplexMatrix ML1 = new ComplexMatrix(new flanagan.complex.Complex[][]{{e1,e2},{e3,e4}});

		   Complex e5 = (eye3.times(eye1)).plus(eye44.times(eye11)).times(t);
		   Complex e6 = (eye3.times(eye22)).plus(eye44.times(eye2)).times(t);
		   Complex e7 = (eye33.times(eye1)).plus(eye4.times(eye11)).times(t);
		   Complex e8 = (eye33.times(eye22)).plus(eye4.times(eye2)).times(t);

		   ComplexMatrix ML2 = new ComplexMatrix(new flanagan.complex.Complex[][]{{e5,e6},{e7,e8}});

		   Complex t11 = new Complex(ML1.getElementCopy(0,0));
		   Complex t22 = new Complex(ML1.getElementCopy(0,1));
		   Complex t33 = new Complex(ML1.getElementCopy(1,0));
		   Complex t44 = new Complex(ML1.getElementCopy(1,1));
		  
           ComplexMatrix K = new ComplexMatrix(ML1.copy());

		   for(int z=0;z<N;z++) 
		   {
			   ML1 = ML1.times(K);
		   }
		   ComplexMatrix Mtot1 = new ComplexMatrix(ML1.copy());

		   Complex t1 = new Complex(Mtot1.getElementCopy(0,0));
		   Complex t2 = new Complex(Mtot1.getElementCopy(0,1));
		   Complex t3 = new Complex(Mtot1.getElementCopy(1,0));
		   Complex t4 = new Complex(Mtot1.getElementCopy(1,1));
		   Complex tt1 = t1.inverse();                             
		   Complex T1 = tt1.pow(2.0);
		   if(i!=1001)
		   {
			   TT1[i] = T1.abs();
			   TT11[i] = Math.log(TT1[i]);
			   var=TT11[i];
		   }
		   else
		   {
			 ripple=(float)Math.log(T1.abs());
		   }
		 
		   switch(n)
		   {
			   case 0:
				   if(i!=1001&&var<small) 
					{
						small=var;
						lvar=lambda[i];
					}
				   break;
			   case 1:
				   double f1=b1-(0.04E-6);
				   double f2=b1+(0.04E-6);
				   if((i!=1001)&&((lambda[i]<=f1)||(lambda[i]>=f2)))
				   {
					    if(var<small) 
						{
							small=var;
							lvar=lambda[i];
						}
					}
					break;
			   
		   }
		   
	    }  
	   return lvar;
	}  
            
	@SuppressWarnings("unused")
	public void graphCal_multi()
	{
	    TT1= new double[1001];
		TTM11 = new double[10][1001];
		lambda = new double[1001];
		k = 1.75E-6;
		for(int ct=1;ct<=ctr;ct++)
		{
			j = 1.3E-6;
	        p = (k - j) / 1000;
			for (int i = 0; i < 1001; i++)
			{
				lambda[i] = j;
				j = j + p;
			
				double theta1 = (ar2[5*(ct-1)+5]) * PI / 180,      theta2 = Math.asin(((ar2[5*(ct-1)+2] * Math.sin(theta1)) / ar2[5*(ct-1)+1]));
				double d31 = ar2[5*(ct-1)+3]*(1E-6);
				double d41 = ar2[5*(ct-1)+4]*(1E-6);
				double a1 = Math.cos(theta1), a2 = Math.cos(theta2);
                double d1 = d31 / a1, d2 = d41 / a2;
                double r12 = ((ar2[5*(ct-1)+1] * a1) - (ar2[5*(ct-1)+2]* a2))/((ar2[5*(ct-1)+1] * a1) + (ar2[5*(ct-1)+2] * a2));
				double r21 = -r12;
                double t12 = Math.sqrt(Math.abs(1 - r12 * r12));
				double t21 = Math.sqrt(Math.abs(1 - r21 * r21));

				Matrix A = new Matrix(new double[][]{{1, r12}, {r12, 1}});
				Matrix B = new Matrix(new double[][]{{1, r21}, {r21, 1}});

				Matrix M12 = A.times(1 / t12);
				Matrix M21 = B.times(1 / t21);

                double k1 = (2 * PI * ar2[5*(ct-1)+1]/ lambda[i]);
				double k2 = (2 * PI * ar2[5*(ct-1)+2]/ lambda[i]);
                double t = -(1 / (t12 * t21));

				Complex eye1 = new Complex(Math.cos(k1 * d1), Math.sin(k1 * d1));
				Complex eye2 = new Complex(Math.cos(k1 * d1), -Math.sin(k1 * d1));

				Complex eye11 = eye1.times(r21);
				Complex eye22 = eye2.times(r21);

				Complex eye3 = new Complex(Math.cos(k2 * d2), Math.sin(k2 * d2));
				Complex eye4 = new Complex(Math.cos(k2 * d2), -Math.sin(k2 * d2));

				Complex eye33 = eye3.times(r12);
				Complex eye44 = eye4.times(r12);

				Complex e1 = (eye1.times(eye3)).plus(eye22.times(eye33)).times(t);
				Complex e2 = (eye1.times(eye44)).plus(eye22.times(eye4)).times(t);
				Complex e3 = (eye11.times(eye3)).plus(eye2.times(eye33)).times(t);
				Complex e4 = (eye11.times(eye44)).plus(eye2.times(eye4)).times(t);

				ComplexMatrix ML1 = new ComplexMatrix(new flanagan.complex.Complex[][]{{e1,e2},{e3,e4}});

				Complex e5 = (eye3.times(eye1)).plus(eye44.times(eye11)).times(t);
				Complex e6 = (eye3.times(eye22)).plus(eye44.times(eye2)).times(t);
				Complex e7 = (eye33.times(eye1)).plus(eye4.times(eye11)).times(t);
				Complex e8 = (eye33.times(eye22)).plus(eye4.times(eye2)).times(t);

				ComplexMatrix ML2 = new ComplexMatrix(new flanagan.complex.Complex[][]{{e5,e6},{e7,e8}});

				Complex t11 = new Complex(ML1.getElementCopy(0,0));
				Complex t22 = new Complex(ML1.getElementCopy(0,1));
				Complex t33 = new Complex(ML1.getElementCopy(1,0));
				Complex t44 = new Complex(ML1.getElementCopy(1,1));
				ComplexMatrix K = new ComplexMatrix(ML1.copy());

				for(int z=0;z<ar1[ct];z++)
				{
					ML1 = ML1.times(K);
				}
				ComplexMatrix Mtot1 = new ComplexMatrix(ML1.copy());
                Complex t1 = new Complex(Mtot1.getElementCopy(0,0));
				Complex t2 = new Complex(Mtot1.getElementCopy(0,1));
				Complex t3 = new Complex(Mtot1.getElementCopy(1,0));
				Complex t4 = new Complex(Mtot1.getElementCopy(1,1));
				Complex tt1 = t1.inverse();                               
				Complex T1 = tt1.pow(2.0);
				TT1[i] = T1.abs();
				TTM11[ct][i] = Math.log(TT1[i]);
			}
			if(ctr==1)
			{
				
				dd1=TTM11[1];
			}
			if(ctr==2)
			{
				dd2=TTM11[2];
			}
			if(ctr==3)
			{
				dd3=TTM11[3];
			}
		}
	}
}

    
