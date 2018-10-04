package ¯ʯ��˵1;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Ӣ����
 * @author ���ӻ�
 *
 */
class Hero			
{
	/**
	 * Ӣ�۵�����ֵ����ʼֵΪ30
	 */
	int healthPoint = 30;			
	/**
	 * Ӣ�۵Ĺ�������Ϊ�̶�ֵ0������final���Σ�ͬʱ����Ӣ�۲����й�����
	 */
	final int attackValue = 0;		
	
	
	/**
	 * �ж�Ӣ�۶��������Ƿ������ķ���
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
 * �����
 * @author ���ӻ�
 *
 */
class Follower		
{
	/**
	 * ��ӵ�����ֵ������ֵ�ɹ���ʱ������ֵ����
	 */
	int healthPoint;			
	/**
	 * ��ӵĹ�������Ϊ�̶�ֵ������final���Σ�����ֵ�ɹ���ʱ������ֵ����
	 */
	final int attackValue;		
	
	/**
	 * ���췽��
	 * @param healthPoint ���������Ӷ���ĳ�ʼ����ֵ
	 * @param attackValue ���������Ӷ���Ĺ�����
	 */
	Follower(int healthPoint,int attackValue)
	{
		this.healthPoint = healthPoint;
		this.attackValue = attackValue;
	}
	
	/**
	 * �ж���Ӷ��������Ƿ�����
	 * @return boolean
	 */
	boolean isDead()		
	{
		if(healthPoint <= 0)
			return true;
		
		return false;
	}
	
	/**
	 * ��ӹ������
	 * @param rival ����������Ӷ���
	 */
	void attack(Follower rival)		
	{
		this.healthPoint -= rival.attackValue;
		
		rival.healthPoint -= this.attackValue;
	}

	/**
	 * ��ӹ���Ӣ��
	 * @param rival ��������Ӣ�۶���
	 */
	void attack(Hero rival)
	{
		this.healthPoint -= rival.attackValue;
		
		rival.healthPoint -= this.attackValue;
	}
}

/**
 * ս����
 * @author ���ӻ�
 *
 */
class Battle	
{
	/**
	 * ��һ����Ӣ��
	 */
	Hero hero1;	
	/**
	 * ��һ�������
	 */
	LinkedList<Follower> followerQueue1;
	
	/**
	 * �ڶ�����Ӣ��
	 */
	Hero hero2;
	/**
	 * �ڶ��������
	 */
	LinkedList<Follower> followerQueue2;
	
	/**
	 * ��ǰ��ս��
	 */
	boolean firstIsBattling = true;		
	/**
	 * ս�����
	 */
	Byte result = 0;		
	
	
	/**
	 * ����һ��ս��
	 */
	Battle()			
	{
		hero1 = new Hero();
		followerQueue1 = new LinkedList();
		
		hero2 = new Hero();
		followerQueue2 = new LinkedList();
	}
	
	/**
	 * ս������֮һ--�ٻ����
	 * @param location ���ٻ�����ӷ�����Ӷ����λ��
	 * @param attackValue ���ٻ�����ӵĹ�����
	 * @param healthPoint ���ٻ�����ӵ�����ֵ
	 */
	private void summon(int location,int attackValue,int healthPoint)
	{
		if(firstIsBattling)		//1���ٻ�һ�����
			followerQueue1.add(location-1,new Follower(healthPoint,attackValue));
			
		else
			followerQueue2.add(location-1,new Follower(healthPoint,attackValue));
	}

	/**
	 * ս������֮һ--�����Է�
	 * @param attackerLocation �����ߵ�λ��
	 * @param defenderLocation �����ߵ�λ��
	 */
	private void attack(int attackerLocation,int defenderLocation)
	{
		if(firstIsBattling)		//��ս��Ϊ1��
		{
			if(defenderLocation == 0)	//����Ӣ��
				followerQueue1.get(attackerLocation-1).attack(hero2);
				
			else						//�������
				followerQueue1.get(attackerLocation-1).attack(followerQueue2.get(defenderLocation-1));
			
			
			//���һ����ս���״��
			//���1�ӣ�
			if(followerQueue1.get(attackerLocation-1).isDead())		//�������
				followerQueue1.remove(attackerLocation-1);			//����Ӷ�����ȡ��	
			
			//���2��:
			if(defenderLocation == 0)
			{
				if(hero2.isDead())		//Ӣ��2����,1��ʤ��
					result = 1;
			}
				
			
			else
			{
				if(followerQueue2.get(defenderLocation-1).isDead())//��2���������ˣ��Ƴ�
					followerQueue2.remove(defenderLocation-1);
			}
				
		}
		
		else			//��ս��Ϊ2��
		{
			if(defenderLocation == 0)
				followerQueue2.get(attackerLocation-1).attack(hero1);
				
			else
				followerQueue2.get(attackerLocation-1).attack(followerQueue1.get(defenderLocation-1));
				
				
			//���һ����ս���״��
			//���1�ӣ�
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
					
			//���2��
			if(followerQueue2.get(attackerLocation-1).isDead())
				followerQueue2.remove(attackerLocation-1);
		}
	}
	
	/**
	 * ������ǰ��ս��
	 */
	private void end()
	{
		firstIsBattling = !firstIsBattling;
	}
	
	/**
	 * ս�ۿ�ʼ
	 * @param battleName �������е�ս�۵�ÿһ��ս��������
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
	 * ��ս���ⲿչʾս��
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
 * ����
 * @author ���ӻ�
 *
 */
public class Main
{
	/**
	 * ����������������
	 * @param args ��ڲ���
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
		
		//����ս����
		Battle battle = new Battle();
		
		//��ʼս����
		battle.beginBattle(battleName);
		
		//����ս����
		battle.displayResult();
	}
}
