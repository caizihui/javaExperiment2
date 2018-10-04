package 炉石传说1;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * 英雄类
 * @author 蔡子辉
 *
 */
class Hero			
{
	/**
	 * 英雄的生命值，初始值为30
	 */
	int healthPoint = 30;			
	/**
	 * 英雄的攻击力，为固定值0，故用final修饰，同时表明英雄不具有攻击力
	 */
	final int attackValue = 0;		
	
	
	/**
	 * 判断英雄对象自身是否死亡的方法
	 * @return boolean
	 */
	boolean isDead()				
	{
		if(healthPoint <= 0)
			return true;
		
		return false;
	}
}


/**
 * 随从类
 * @author 蔡子辉
 *
 */
class Follower		
{
	/**
	 * 随从的生命值，具体值由构造时给定的值决定
	 */
	int healthPoint;			
	/**
	 * 随从的攻击力，为固定值，故用final修饰，具体值由构造时给定的值决定
	 */
	final int attackValue;		
	
	/**
	 * 构造方法
	 * @param healthPoint 所构造的随从对象的初始生命值
	 * @param attackValue 所构造的随从对象的攻击力
	 */
	Follower(int healthPoint,int attackValue)
	{
		this.healthPoint = healthPoint;
		this.attackValue = attackValue;
	}
	
	/**
	 * 判断随从对象自身是否死亡
	 * @return boolean
	 */
	boolean isDead()		
	{
		if(healthPoint <= 0)
			return true;
		
		return false;
	}
	
	/**
	 * 随从攻击随从
	 * @param rival 被攻击的随从对象
	 */
	void attack(Follower rival)		
	{
		this.healthPoint -= rival.attackValue;
		
		rival.healthPoint -= this.attackValue;
	}

	/**
	 * 随从攻击英雄
	 * @param rival 被攻击的英雄对象
	 */
	void attack(Hero rival)
	{
		this.healthPoint -= rival.attackValue;
		
		rival.healthPoint -= this.attackValue;
	}
}

/**
 * 战斗类
 * @author 蔡子辉
 *
 */
class Battle	
{
	/**
	 * 第一方的英雄
	 */
	Hero hero1;	
	/**
	 * 第一方的随从
	 */
	LinkedList<Follower> followerQueue1;
	
	/**
	 * 第二方的英雄
	 */
	Hero hero2;
	/**
	 * 第二方的随从
	 */
	LinkedList<Follower> followerQueue2;
	
	/**
	 * 当前作战方
	 */
	boolean firstIsBattling = true;		
	/**
	 * 战斗结果
	 */
	Byte result = 0;		
	
	
	/**
	 * 构造一场战役
	 */
	Battle()			
	{
		hero1 = new Hero();
		followerQueue1 = new LinkedList();
		
		hero2 = new Hero();
		followerQueue2 = new LinkedList();
	}
	
	/**
	 * 战斗类型之一--召唤随从
	 * @param location 被召唤的随从放入随从队里的位置
	 * @param attackValue 被召唤的随从的攻击力
	 * @param healthPoint 被召唤的随从的生命值
	 */
	private void summon(int location,int attackValue,int healthPoint)
	{
		if(firstIsBattling)		//1方召唤一个随从
			followerQueue1.add(location-1,new Follower(healthPoint,attackValue));
			
		else
			followerQueue2.add(location-1,new Follower(healthPoint,attackValue));
	}

	/**
	 * 战斗类型之一--攻击对方
	 * @param attackerLocation 攻击者的位置
	 * @param defenderLocation 防御者的位置
	 */
	private void attack(int attackerLocation,int defenderLocation)
	{
		if(firstIsBattling)		//作战方为1方
		{
			if(defenderLocation == 0)	//攻击英雄
				followerQueue1.get(attackerLocation-1).attack(hero2);
				
			else						//攻击随从
				followerQueue1.get(attackerLocation-1).attack(followerQueue2.get(defenderLocation-1));
			
			
			//检查一对作战后的状况
			//检查1队：
			if(followerQueue1.get(attackerLocation-1).isDead())		//随从死了
				followerQueue1.remove(attackerLocation-1);			//从随从队列中取出	
			
			//检查2队:
			if(defenderLocation == 0)
			{
				if(hero2.isDead())		//英雄2死了,1对胜利
					result = 1;
			}
				
			
			else
			{
				if(followerQueue2.get(defenderLocation-1).isDead())//队2里的随从死了，移除
					followerQueue2.remove(defenderLocation-1);
			}
				
		}
		
		else			//作战方为2方
		{
			if(defenderLocation == 0)
				followerQueue2.get(attackerLocation-1).attack(hero1);
				
			else
				followerQueue2.get(attackerLocation-1).attack(followerQueue1.get(defenderLocation-1));
				
				
			//检查一对作战后的状况
			//检查1队：
			if(defenderLocation == 0)
			{
				if(hero1.isDead())
					result = -1;
			}
				
			else
			{
				if(followerQueue1.get(defenderLocation-1).isDead())
					followerQueue1.remove(defenderLocation-1);
			}
					
			//检查2队
			if(followerQueue2.get(attackerLocation-1).isDead())
				followerQueue2.remove(attackerLocation-1);
		}
	}
	
	/**
	 * 交换当前作战方
	 */
	private void end()
	{
		firstIsBattling = !firstIsBattling;
	}
	
	/**
	 * 战役开始
	 * @param battleName 即将进行的战役的每一个战斗的类型
	 */
	void beginBattle(String battleName[][])
	{
		int i;
		
		for(i=0 ; i<battleName.length ; i++)
		{
			if(battleName[i][0].equals("summon"))
				summon(Integer.parseInt(battleName[i][1]),Integer.parseInt(battleName[i][2]),Integer.parseInt(battleName[i][3]));
			
			else if(battleName[i][0].equals("attack"))
				attack(Integer.parseInt(battleName[i][1]),Integer.parseInt(battleName[i][2]));
			
			else
				end();
		}
		
	}
	
	/**
	 * 向战场外部展示战况
	 */
	void displayResult()
	{
		int i;
		int size1 = followerQueue1.size(),size2 = followerQueue2.size();
		
		System.out.println(result);
		
		System.out.println(hero1.healthPoint);
		System.out.print(size1);
		for(i=0 ; i<size1 ; i++)
			System.out.print(" "+followerQueue1.get(i).healthPoint);
		System.out.println();
			
		
		System.out.println(hero2.healthPoint);
		System.out.print(size2);
		for(i=0 ; i<size2 ; i++)
			System.out.print(" "+followerQueue2.get(i).healthPoint);
		System.out.println();
	}
}

/**
 * 主类
 * @author 蔡子辉
 *
 */
public class Main
{
	/**
	 * 主方法，程序的入口
	 * @param args 入口参数
	 */
	public static void main(String [] args)
	{
		Scanner input = new Scanner(System.in);
		String battleName[][] = new String[input.nextInt()][4];
		int i;
		
		for(i=0 ; i<battleName.length ; i++)
		{
			battleName[i][0] = input.next();
			
			if(battleName[i][0].equals("summon"))
			{
				battleName[i][1] = input.next();
				battleName[i][2] = input.next();
				battleName[i][3] = input.next();
			}
			
			else if(battleName[i][0].equals("attack"))
			{
				battleName[i][1] = input.next();
				battleName[i][2] = input.next();
			}
		}
		
		//构建战场：
		Battle battle = new Battle();
		
		//开始战斗：
		battle.beginBattle(battleName);
		
		//最终战况：
		battle.displayResult();
	}
}
