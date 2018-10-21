package com.cheng.wang.shengshui.utils;


import static com.cheng.wang.shengshui.utils.Constant.ground;
import static com.cheng.wang.shengshui.utils.Constant.startqipan;

public class Utils {
	// 检查是否有五子连起来
	public static boolean isWin() {
		if (baiwin() || hewin())
			return true;
		return false;

	}


	public static boolean hewin() {
		int bai = 0;
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				if (ground[y][x] == 1) {
					bai++;
				}
			}
		}
		if (bai >=4) {
			return false;
		}else {
			return true;
		}
	}




	public static boolean baiwin(){
		if ((ground[0][0]==2&&ground[0][2]==1&&ground[0][4]==1&&ground[1][1]==1&&ground[2][2]==1)||
				(ground[0][2]==2&&ground[0][0]==1&&ground[0][4]==1&&ground[1][2]==1&&ground[2][2]==1)||
				(ground[1][2]==2&&ground[0][2]==1&&ground[1][1]==1&&ground[1][3]==1&&ground[2][2]==1&&ground[3][2]==1)||
				(ground[0][4]==2&&ground[0][0]==1&&ground[0][2]==1&&ground[1][3]==1&&ground[2][2]==1)||
				(ground[1][3]==2&&ground[0][4]==1&&ground[1][2]==1&&ground[1][1]==1&&ground[2][2]==1&&ground[1][3]==1)||
				(ground[1][1]==2&&ground[0][0]==1&&ground[1][2]==1&&ground[1][3]==1&&ground[2][2]==1&&ground[3][3]==1)||


				(ground[1][3]==2&&ground[0][4]==1&&ground[2][3]==1&&ground[3][3]==1&&ground[2][2]==1&&ground[1][3]==1)||
				(ground[3][3]==2&&ground[4][4]==1&&ground[3][2]==1&&ground[3][1]==1&&ground[2][2]==1&&ground[1][1]==1)||
				(ground[3][2]==2&&ground[4][2]==1&&ground[3][1]==1&&ground[3][3]==1&&ground[2][2]==1&&ground[1][2]==1)||
				(ground[4][0]==2&&ground[3][1]==1&&ground[2][2]==1&&ground[4][2]==1&&ground[4][4]==1)||
				(ground[4][2]==2&&ground[4][0]==1&&ground[4][4]==1&&ground[3][2]==1&&ground[2][2]==1)||
				(ground[4][4]==2&&ground[4][0]==1&&ground[4][2]==1&&ground[3][3]==1&&ground[2][2]==1)){
			return true;
		}
		return false;
	}



	public static boolean isLegal(int x, int y) {
		if (((y==0||y==4)&&(x==0||x==2||x==4))||
				((y==1||y==3)&&x>=1&&x<=3)||
				(y==2&&x==2)){
			return true;
		}
		return false;
	}
	public static void initGroup(){
//		shua=true;
		int length =  ground.length;
		for (int i = 0; i < length; i++) {
			for(int j = 0;j < length;j++){
				ground[i][j] = startqipan[i][j];
			}
		}

	}



}
