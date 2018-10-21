package com.cheng.wang.shengshui.utils;

import java.util.UUID;

public class Constant {
	// 白棋
	public final static int WHITE_CHESS = 1;
	// 黑棋
	public final static int BLACK_CHESS = 2;
	public static boolean isnoPlaySound=true;


	// 对方还未获胜
	public final static int ENEMYNOTWIN = 0;
	// 对方获胜
	public final static int ENEMYWIN = 1;
	// 格子长和高度
	public  static int RECT_R = 26;
//	// 棋子长和高度
	public  static int CHESS_R = 18;
	// 时候是服务器端
	public static boolean serverOrClient = true;
	
//	//屏幕宽度
//	public  static int SCREENWIDTH;
//	//屏幕高度
//	public  static int SCREENHEIGHT;
	/**
	 * 目标设备的蓝牙物理地址
	 * **/
	public static String address ;
	public static String LogTag = "五子棋";
	/**
	 * 用于蓝牙之间通信的表示uuid
	 * **/
	public final static UUID uuid = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	/**
	 * 棋盘
	 * **/
	public static int[][] ground = new int[][] { {  1, 0, 1,0,1 },

			{ 0, 1, 1,1,0 },{ 0, 0, 2,0,0 },{0, 0, 0, 0,0},{  0, 0, 0,0,0 } };
public static int[][] startqipan = new int[][] { {  1, 0, 1,0,1 },

			{ 0, 1, 1,1,0 },{ 0, 0, 2,0,0 },{0, 0, 0, 0, 0},{  0, 0, 0,0,0 } };



	

	

}
