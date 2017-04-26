#ifndef HW_FUNCTIONS
#define HW_FUNCTIONS

#include "letters.h"


unsigned int window_width = 512, window_height = 512;
int mousex,mousey,mousek;
float *pixel;
unsigned char keyValue;
float red,green,blue;




void myMouseMove(int x, int y){
	mousex=x;
	mousey=y;
}
void myMouse(int button, int state, int x, int y)
{
	mousek=0;
	mousex=x;
	mousey=y;
	if (state==0){
		mousek=button+1;
	}
}
void setColor(float rr, float gg, float bb)
{
	red   = rr;
	green = gg;
	blue  = bb;
}
void setPixel(int x, int y)
{
	int adress = (y*window_width + x)*3;
	if ((int)(adress/3) < window_width * window_height){
		pixel[adress]   = red;
		pixel[adress+1] = green;
		pixel[adress+2] = blue;
	}

}
void clrScr(float r=0,float g=0, float b=0)
{
	int i=0;
	for (int y = 0;y<window_height;y++){
		for (int x=0;x<window_width;x++){
			pixel[i++]=r;
			pixel[i++]=g;
			pixel[i++]=b;
		}
	}
}
void drawLine(int x1,int y1,int x2, int y2){
	float xcounter;
	float ycounter;
	float distx;
	float disty;
	float help;
	float neg;
	float derivative;
	
	distx = x2-x1;
	disty = y2-y1;
	
	neg=-1;
	if (distx<0){
	    distx = distx * neg;
	}
	if (disty<0){
		disty = disty * neg;
	}
	if (distx>disty){
		// screen.drawLine with x counter
		if (x2<x1){ // swap x1,x2 and y1,y2
			help = x2;
			x2 = x1;
			x1 = help;
			help = y2;
			y2 = y1;
			y1 = help;
		}
		ycounter   =  y1;
		derivative = (y2-y1)/(distx);
		for (xcounter=x1; xcounter<x2;xcounter++){
		   setPixel(xcounter,ycounter);
		   ycounter = ycounter + derivative;
		}				
		return;
	}
	else{
		// screen.drawLine with x counter
		if (y2<y1){ // swap x1,x2 and y1,y2
			help = x2;
			x2 = x1;
			x1 = help;
			help = y2;
			y2 = y1;
			y1 = help;
		}
		xcounter   =  x1;
		derivative = (x2-x1)/(disty);
		for (ycounter=y1;ycounter<y2;ycounter++){
		   setPixel(xcounter,ycounter);
		   xcounter=xcounter+derivative;
		}
		return;
	}	
}


void display()
{
	//clear buffer/
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glRasterPos2i(-1.0,1.0);
	glPixelZoom(1.0,-1.0);
	//draw "pixel" array
	glDrawPixels(window_width ,window_height, GL_RGB, GL_FLOAT, pixel);

	//buffer swap
	glutSwapBuffers();
}
void keyboard(unsigned char key, int x, int y)
{
  keyValue = key;
  switch (key)
  {
    case '\x1B':
      exit(EXIT_SUCCESS);
      break;
  }
}
void mouse(int key, int updown, int x, int y)
{
	
	if (key==0 && updown==0){
		mousek|=1;
	}
	if (key==2 && updown==0){
		mousek|=2;
	}
	if (key==0 && updown==1){
		mousek&=2;
	}
	if (key==2 && updown==1){
		mousek&=1;
	}
//	printf("%d, %d, %d\n",mousex,mousey,mousek);
}

void initWindow(int argc, char**argv, int xSize,int ySize, char *title)
{
	
	glutInit(&argc, argv);
	window_width = xSize;
	window_height = ySize;
	const int size = window_width * window_height;
	pixel = new float[size * 3];
	
	red = 1.0f;
	green = 1.0f;
	blue = 1.0f;


	//display mode
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	//window size
	glutInitWindowSize(window_width,window_height);
	glutCreateWindow(title);

	//display funktion wird nur benötigt wenn man glut den "mainloop" übergibt
	//glutDisplayFunc(display);
	glutKeyboardFunc(&keyboard);
	glutMouseFunc(&mouse);

	glutMotionFunc(myMouseMove);
    glutPassiveMotionFunc(myMouseMove);
    glutMouseFunc(myMouse);

	//depth test an sich unnötig, kostet jedoch keine performance könnte man nacher benutzen um text zu "zeichnen"
	glEnable(GL_DEPTH_TEST);
	//clear screen
	glClearColor(0.0, 0.0, 0.0, 1.0);

}


void glPrint(int xx,int yy, char*text)
{
	int index=0,x,y,xxx;
	float rr=red;
	float gg=green;
	float bb=blue;

	xxx = xx;
	while(text[index]!=0){

		if (text[index]=='\n'){
			xx  = xxx;
//			yy -= 15;
		}
		for (y=0;y<15;y++){
			for (x=0;x<10;x++){
				float xxx = (float)((xx+x));
				float yyy = (float)((yy-y));
				unsigned char b = text[index];

				if (letter[b][15-y][x]!=' '){
					setPixel(xxx,yyy);
				}
				else{
					setColor(0,0,0);
					setPixel(xxx,yyy);
					setColor(rr,gg,bb);

				}
			
			}
			
		}
		xx = xx + 12;
		index++;
	}
}


void drawBox(int x0, int y0, int x1, int y1)
{
	for (int t=x0; t<x1;t++){
		drawLine(t,y0,t,y1);
	}
}




#endif // HW_FUNCTIONS