

class MnistReader
{
public:
	FILE *trainImages;
	FILE *trainLabels;
	FILE *testImages;
	FILE *testLabels;

	char **trainImage;
	char *trainLabel;
	MnistReader(){
		trainImage = new char* [10000];
		trainLabel = new char  [10000];

		trainImages = fopen("train-images-idx3-ubyte","rb");
		trainLabels = fopen("train-labels-idx1-ubyte","rb");
		for (int t=0;t<4*4;t++){
			fgetc(trainImages);
			if (t%2==0)
				fgetc(trainLabels);
		}

		for (int t=0;t<1000;t++){
			trainImage[t] = new char[28*28];
			trainLabel[t] = fgetc(trainLabels);

			//printf("\nZahl=%d\n",trainLabel[t]);
			for (int r=0;r<28*28;r++){
				trainImage[t][r]=fgetc(trainImages);
				if (trainImage[t][r]!=0){
					trainImage[t][r]=1;
				}
				//trainImage[t][r]-=0.5;
/*				printf("%d",trainImage[t][r]);
				if (r%28==0){
					printf("\n");
				}
*/			}

		}
		fclose(trainLabels);
		fclose(trainImages);
	}

};