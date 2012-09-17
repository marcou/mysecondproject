package com.gra.gra;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoneyAsteroid extends Asteroid{

	private int value;	//wartosc monet ktore znajduja sie w tej asteeroidzie
	private int quota;	//ilosc monet ktore zostaja wyrzucone w powietrze
	
	public MoneyAsteroid(List<FlyingObject> objects, float x, float y,
			double speed, double angle, int mass, int radius, int size, int value, int quota) {
		super(objects, x, y, speed, angle, mass, radius, size);
		
		this.value = value;
		this.quota = quota;
	}

	@Override
	public void update() {
		//jesli obiekt dotyka ziemi usun go po czasie "life_timer"
		if(super.isOn_ground()){
			super.setLife(0);
		}
		
		super.setCurrentFrame(super.getCurrentFrame() + 1);
		
		if(super.getCurrentFrame() > super.getFrames()){
			super.setCurrentFrame(0);
		}
		
	}

	@Override
	public void resolveCollision(FlyingObject object) {
		if(object instanceof Asteroid){
			//zmniejsz ich rozmiary
			setSize(getSize() - 1);
			((Asteroid) object).setSize(((Asteroid) object).getSize() - 1);
			
			if(((Asteroid) object).getSize() > 0){
				((Asteroid) object).setRadius(((Asteroid) object).getBasic_radius() * ((Asteroid) object).getSize());
			}
			Random rand = new Random();
			super.setAngle(super.getAngle() - 180);//rand.nextInt(90) - 90);
			((Asteroid) object).setAngle(((Asteroid) object).getAngle() - 180);//rand.nextInt(90) - 90);
		}
		//kolizja z upgradeami
		else if(object instanceof Upgrade){
			((Upgrade) object).setLife(0);
		}
		//kolizja asteroidy z pieniedzmi
		else if(object instanceof Money){
			//((Money) object).setLife(0);
		}
	}

	public ArrayList<FlyingObject> getMoney(float x, float y, double angle){
		ArrayList<FlyingObject> money = new ArrayList<FlyingObject>();
		Random rand = new Random();
		for(int i = 0; i <= quota; i++){//super.getSize(); i++){	//size w tym przydpadku informuje ile monet zostanie wyrzuconych w kosmos
			Money m = new Money(money, x, y, (double)(rand.nextInt(10) + 10), (double)(rand.nextInt(90) - 45 + angle), value, 5);
			money.add(m);
		}
		return money;
	}
}
