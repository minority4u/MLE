#include "../../Downloads/RestrBolzmMachine_Aufgabe6/include/GL/freeglut.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include "hwFunctions.h"
#include "MnistReader.h"

// graphical output_size
#define width 900
#define height 350

// --- Deep Net
#define NEURONS 28*28+10

#define GREEN 0,1,0
#define BLUE 0,0,1

static unsigned int m_z=12345,m_w=45678;

MnistReader letters;


unsigned int randomGen()
{
    m_z = 36969 * (m_z & 65535) + (m_z >> 16);
    m_w = 18000 * (m_w & 65535) + (m_w >> 16);
    return (m_z << 16) + m_w;
}
void init(double weights[NEURONS][NEURONS]){
	for (int t=0;t<NEURONS;t++){
		for (int neuron=0; neuron<NEURONS; neuron++){
			weights[neuron][t]=randomGen()%2000/1000.0-1.0;
		}
	}
}
void activateForward(double input[], double weights[NEURONS][NEURONS],double output[]){

	// insert code here

}
void activateReconstruction(double input[], double weights[NEURONS][NEURONS],double output[]){

	// insert code here

}

void contrastiveDivergence(double input[], double output[], double reconstructed_input[], double weights[NEURONS][NEURONS])
{

	// insert code here
}


void drawActivity(int xx, int yy, double neuron[],float red, float green, float blue){
	int i=0;
	for (int y=0;y<28;y++){
		for (int x=0;x<28;x++){
			setColor(red*neuron[i],green*neuron[i],blue*neuron[i]);
			drawBox(x*10+xx,y*10+yy,x*10+8+xx,y*10+8+yy);
			i++;
		}
	}
	int y=29;
	for (int x=0;x<10;x++){
		setColor(red*neuron[i],green*neuron[i],blue*neuron[i]);
		drawBox(x*10+xx,y*10+yy,x*10+8+xx,y*10+8+yy);
		i++;
	}

}

void trainOrTestNet(bool train, int maxCount,float red,float green,float blue){
	static double weights[NEURONS][NEURONS];
	static double output[NEURONS];
	static double input[NEURONS];
	static double reconstructed_input[NEURONS];
	int correct = 0;

	char text[1000];

	FILE *outFile;
	outFile = fopen("result.txt","wb");

	if (train){
		init(weights);
	}
	int pattern=0;
    for (int count=1; count<maxCount; count++){
		// --- training phase

		for (int t=0;t<NEURONS-10;t++){
			input[t]=letters.trainImage[pattern%100][t]; // initialize original pattern
		}
		for (int t=NEURONS-10;t<NEURONS;t++){
			input[t]=0;
		}
		if (train){
			// --- use the label also as input!
			if (letters.trainLabel[pattern%100]>=0 && letters.trainLabel[pattern%100]<10){
				input[NEURONS-10+letters.trainLabel[pattern%100]] = 1.0;
			}
		}

		drawActivity(0,0,input,red,green,blue);

		// --- Contrastive divergence
		// Activation
		input[0] = 1;					// bias neuron!
		activateForward(input,weights,output); // positive Phase
		output[0] = 1;					// bias neuron!

		drawActivity(300,0,output,red,green,blue);

		activateReconstruction(reconstructed_input,weights,output); // negative phase/ reconstruction

		drawActivity(600,0,reconstructed_input,red,green,blue);
		if (train){
			contrastiveDivergence(input,output,reconstructed_input,weights);
		}

		if (count%11==0){
			sprintf(text,"Zahl:%d",letters.trainLabel[pattern%100]);
			setColor(1,0,0);
			glPrint(10,340,text);

			sprintf(text,"Trainingsmuster:%d                 Erkennungsrate:%f %%",count,(float)(correct)/(float)(count));
			setColor(1,0,0);
			glPrint(10,320,text);
			for (int t=0;t<10;t++){
				display();
				glutMainLoopEvent();
			}
		}

		if (!train){
			int number = 0;
			for (int t=NEURONS-10;t<NEURONS;t++){
				if (reconstructed_input[t]>reconstructed_input[NEURONS-10+number]){
					number = t-(NEURONS-10);
				}
			}
			if (letters.trainLabel[pattern%100]==number){
				fprintf(outFile,"Muster: %d, Erkannt: %d KORREKT!!!\n",letters.trainLabel[pattern%100],number);
				correct++;
			}
			else{
				fprintf(outFile,"Muster: %d, Erkannt: %d \n",letters.trainLabel[pattern%100],number);
			}
		}

		pattern++;
	}

	fclose(outFile);

}

int main(int argc, char** argv){

	initWindow(argc,argv,width,height,"Restricted Bolzmann Machine");

	trainOrTestNet(true,10000,BLUE);
	trainOrTestNet(false,1000,GREEN);

}
