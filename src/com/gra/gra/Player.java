package com.gra.gra;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * 
 * @author Szpada
 *
 *	gracz a wlasciwie kulka ktora kontroluje gracz
 */
public class Player {
	/*-----------------------------------------------------------------------------------------------------*
	 * Startowe wartosci do ktorych bedzie sie wracalo po uplynieciu czasu dzialania modyfikatora(upgradeu)*
	 *-----------------------------------------------------------------------------------------------------*/
	private int default_multiplier = 1;
	private int default_radius;
	private double default_speed;
	private double default_jump_power;
	private double default_sucking_range = 25.0;
	
	//mnozniki (upgradeowe) do ziemi
	private double earth_gravity_multiplier = 1.0;	//mno¿nik grawitacji
	private double earth_radius_multiplier = 1;		//mnoznik promienia ziemi  	
	
	//tajmery logiczne dla playera, ziemi, armagedonu i deszczu pieniedzy
	private long timer = 0;	//czas po ktorym przestaja dzialac upgrady
	private long earth_timer = 0;
	private long armagedon_timer = 0;
	private long money_rain_timer = 0;
	private long immortality_timer = 0;
	
	//flaga ktorej ustawienie na true sprawia ze zmienuly sie statystyki ziemi
	private boolean earth_stats_changed = false;
	
	private double speed = 5.0;
	private double jump_power = 20.0;
	private double current_jump_power;
	
	private double x_speed;		//predkosc w plaszczyznie X
	private double y_speed;		//predkosc w plaszczyznie Y
	
	private float x;
	private float y;
	
	private int mass;
	private double radius;		//promien naszego obiektu
	private double angle;		//kat - godzina 6 to 90stopni, 12 270
	
	private float earth_x;		//pozycja X srodka ziemi
	private float earth_y;		//pozycja Y srodka ziemi
	private int earth_radius;	//promien okregu po jakim ma siê poruszaæ nasz obiekt
	private boolean on_ground = true;	//czy dotyka ziemi
	
	private Paint paint;
	
	private long points = 1000;	//punkty gracza
	private int multiplier = 1;		//mnoznik punktow
	
	private boolean armagedon = false;
	private boolean money_rain = false;
	
	private int life = 3;			//zycie gracza MAX = 3 smierc przy Life = 0
	
	private double sucking_range;	//ile od gracza moze znajdowac sie moneta zeby ja zassal
	
	private int currentFrame;
	
	private boolean immortal = false;
	
	
	public Player(float x, float y,  int mass, int radius, int degree){
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.mass = mass;
		this.angle = degree;
		
		this.default_jump_power = jump_power;
		this.default_multiplier = multiplier;
		this.default_radius = radius;
		this.default_speed = speed;
		this.sucking_range = default_sucking_range;
		
		paint = new Paint();
		paint.setColor(Color.YELLOW);
	}
	
	public void onDraw(Canvas canvas){
		if(immortal){
			paint.setColor(Color.BLUE);
		}
		else{
			paint.setColor(Color.YELLOW);
		}
		canvas.drawCircle(x, y, (float) radius, paint);
		
		//dekrementacja tajmerow
		if(timer > 0){
			timer--;
		}
		else{
			//zresetowanie dzialania upgradeow
			timer = 0;
			this.multiplier = default_multiplier;
			this.speed = default_speed;
			this.jump_power = default_jump_power;
			this.radius = default_radius;
			this.sucking_range = default_sucking_range;
		}
		if(immortality_timer > 0){
			immortality_timer--;
		}
		else{
			immortality_timer = 0;
			immortal = false;
		}
	}
	
	public void set_earth(float x, float y, int radius){
		this.earth_x = x;
		this.earth_y = y;
		this.earth_radius = radius;
	}
	
	public void setUpgrade(long time, double radius, double multiplier, double speed, double jump_power, double sucking_range, boolean immortality){
		this.timer = time;
		if(immortality){
			this.immortality_timer = time;
		}
		this.radius *= radius;
		this.multiplier *= multiplier;
		this.speed *= speed;
		this.jump_power *= jump_power;
		this.sucking_range *= sucking_range;
		this.immortal = immortality;
	}
	
	//Ruch				zgodnie lub przeciwnie do wskazowek zegara
	public void move(boolean clockwise){
		double distance; 	//zmienna przechowujaca dystans kulki od planety (potrzebna do ruchu w powietrzu)
		int jumping_speed = (int)speed;//(int)speed/2 + 1;
		//zgodnie z ruchem wskazowek zegara
		if(clockwise){
			//gracz jest na ziemi
			if(on_ground){
				if(angle + speed < 360){
					this.angle += this.speed;
				}
				else if(angle + speed >= 360){
					this.angle = angle - 360 + speed;
				}
				this.x = (float)(		Math.cos(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
				this.y = (float)(		Math.sin(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
			}
			//gracz jest w powietrzu
			else{
				
				if(angle + jumping_speed < 360){
					this.angle += jumping_speed;
				}
				else if(angle + jumping_speed >= 360){
					this.angle = angle - 360 + jumping_speed;
				}
				distance = Math.pow((Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2)),0.5);
				this.x = (float)(		Math.cos(Math.toRadians(this.angle)) * (distance) + this.earth_x		);
				this.y = (float)(		Math.sin(Math.toRadians(this.angle)) * (distance) + this.earth_y		);
			}
		}
		//przeciwnie do ruchu wskazowek zegara
		else{
			//gracz jest na ziemi
			if(on_ground){
				if(angle - speed > 0){
					this.angle -= this.speed;
				}
				else if(angle - speed <= 0){
					this.angle = angle + 360 - speed;
				}
				this.x = (float)(		Math.cos(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
				this.y = (float)(		Math.sin(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
			}
			//gracz jest w powietrzu
			else{
				if(angle - jumping_speed > 0){
					this.angle -= jumping_speed;
				}
				else if(angle - jumping_speed <= 0){
					this.angle = angle + 360 - jumping_speed;
				}
				distance = Math.pow((Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2)),0.5);
				this.x = (float)(		Math.cos(Math.toRadians(this.angle)) * (distance) + this.earth_x		);
				this.y = (float)(		Math.sin(Math.toRadians(this.angle)) * (distance) + this.earth_y		);
			}
		}
	}
	
	public void resolveGravity(double gravity, int mass, int radius){
		//ZREZYGNOWALEM Z GRAWITACJI ZALEZNEJ OD ODLEGLOSCI BO PLAYER WYLATUJE W KOSMOS
		this.earth_radius = radius;
		double power = gravity;
		//jesli sila wyrzutu jest mniejsza od grawitacji
		if(current_jump_power <= 0){
			current_jump_power -= power;
			//spadamy z sila power - current_jump_power
			resolvePower(-current_jump_power, false);
		}
		else{
			current_jump_power -= power;
			resolvePower(current_jump_power, true);
		}
	}
	
	public void jump(){
		current_jump_power = jump_power;
		on_ground = false;
	}
	//w pierwszej zmiennej znajduje sie sila z jaka
	//gracz sie unosi/opada (druga zmienna)
	//sila jest wypadkowa grawitacji i sily wyskoku
	public void resolvePower(double power, boolean move_up){	
		x_speed = Math.cos(Math.toRadians(angle)) * power;	//obliczenie skladowej X						
		y_speed = Math.sin(Math.toRadians(angle)) * power;	//obliczenie skladowej Y
		
		if(!move_up){
			x_speed = -x_speed;
			y_speed = -y_speed;
		}
		
		this.x += x_speed;
		this.y += y_speed;
		
		//jesli kulka spadnie "za nisko" ustawaimy ja na wyjsciawa pozycje
		if(Math.pow((Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2)),0.5) < this.radius + this.earth_radius){
			on_ground = true;
			this.x = (float) (		Math.cos(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
			this.y = (float) (		Math.sin(Math.toRadians(this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
		}
	}
	
	public boolean checkCollision(float x, float y, int radius, boolean money) {
		double distance = this.radius + radius;
		if(money){
			distance += this.sucking_range;
		}
		if(Math.pow(Math.pow(x - this.x,2) + Math.pow(y - this.y,2),0.5) <= distance){
			return true;
		}
		return false;
	}

	public void resolveCollision(FlyingObject object) {
		//kolizja gracza z asteroida
		if(object instanceof Asteroid){
			((Asteroid) object).setLife(0);
			if(!immortal){
				setLife(getLife()-1);
				immortal(40);
			}
		}
		//kolizja gracza z pieniedzmi
		else if(object instanceof Money){
			setPoints(getPoints() + ((Money) object).getPoints() * multiplier);
			((Money) object).setLife(0);
		}
		//kolizja gracza z upgradem
		else if(object instanceof Upgrade){
			//jesli ktorys modyfikator dotyczy ziemi ustawiamy odpowiednia flage na true
			if(((Upgrade) object).getEarth_gravity() < 1.0 || ((Upgrade) object).getEarth_gravity() > 1.0 || ((Upgrade) object).getEarth_radius() < 1 || ((Upgrade) object).getEarth_radius() > 1){
				this.earth_gravity_multiplier = ((Upgrade) object).getEarth_gravity();
				this.earth_radius_multiplier = ((Upgrade) object).get_earth_radius();
				this.earth_timer = ((Upgrade) object).getTime();
				this.earth_stats_changed = true;
			}
			//jesli upgrade dotyczy gracza
			if(((Upgrade) object).getPlayer_jump_power() < 1.0 || ((Upgrade) object).getPlayer_jump_power() > 1.0 
					|| ((Upgrade) object).getPlayer_point_multiplier() < 1 || ((Upgrade) object).getPlayer_point_multiplier() > 1 
					|| ((Upgrade) object).getPlayer_radius() < 1 || ((Upgrade) object).getPlayer_radius() > 1 
					|| ((Upgrade) object).getPlayer_speed() < 1 || ((Upgrade) object).getPlayer_speed() > 1
					|| ((Upgrade) object).getPlayer_sucking_range() < 1 || ((Upgrade) object).getPlayer_sucking_range() > 1
					|| ((Upgrade) object).isPlayer_immortality()){
				//zresetowanie poprzednich upgradeow
				resetUpgrade();
				//ustawienie nowych upgradeow playerowi
				setUpgrade(((Upgrade) object).getTime(), ((Upgrade) object).getPlayer_radius(), ((Upgrade) object).getPlayer_point_multiplier(), ((Upgrade) object).getPlayer_speed(), ((Upgrade) object).getPlayer_jump_power(), ((Upgrade) object).getPlayer_sucking_range(), ((Upgrade) object).isPlayer_immortality());
			}
			//jesli upgrade jest typu armagedon
			if(((Upgrade) object).isArmagedon()){
				armagedon = true;
				armagedon_timer = ((Upgrade) object).getTime();
			}
			if(((Upgrade) object).isMoney_rain()){
				money_rain = true;
				money_rain_timer = ((Upgrade) object).getTime();
			}
			((Upgrade) object).setLife(0);
		}
		//kolizja z kolcem wystajacym z ziemi
		else if(object instanceof GroundEnemy){
			if(!immortal){
				setLife(getLife()-1);
				immortal(40);
			}
		}
	}
	
	public void immortal(long time){
		this.immortal = true;
		this.immortality_timer = time;
	}
	
	public void setPoints(long points){
		this.points = points;
	}
	
	public long getPoints(){
		return points;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getMass() {
		return mass;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public float getEarth_x() {
		return earth_x;
	}

	public void setEarth_x(float earth_x) {
		this.earth_x = earth_x;
	}

	public float getEarth_y() {
		return earth_y;
	}

	public void setEarth_y(float earth_y) {
		this.earth_y = earth_y;
	}

	public int getEarth_radius() {
		return earth_radius;
	}

	public void setEarth_radius(int earth_radius) {
		this.earth_radius = earth_radius;
	}

	public boolean isOn_ground() {
		return on_ground;
	}

	public void setOn_ground(boolean on_ground) {
		this.on_ground = on_ground;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public boolean isEarth_stats_changed() {
		return earth_stats_changed;
	}

	public void setEarth_stats_changed(boolean earth_stats_changed) {
		this.earth_stats_changed = earth_stats_changed;
	}

	public double getEarth_gravity_multiplier() {
		return earth_gravity_multiplier;
	}

	public void setEarth_gravity_multiplier(double earth_gravity_multiplier) {
		this.earth_gravity_multiplier = earth_gravity_multiplier;
	}

	public double getEarth_radius_multiplier() {
		return earth_radius_multiplier;
	}

	public void setEarth_radius_multiplier(double earth_radius_multiplier) {
		this.earth_radius_multiplier = earth_radius_multiplier;
	}

	public long getTimer() {
		return timer;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}
	public void resetUpgrade(){
		this.multiplier = default_multiplier;
		this.speed = default_speed;
		this.jump_power = default_jump_power;
		this.radius = default_radius; 
	}
	public long getEarthTimer(){
		return this.earth_timer;
	}
	public int getLife(){
		return this.life;
	}
	public void setLife(int life){
		this.life = life;
	}

	public boolean isArmagedon() {
		return armagedon;
	}

	public void setArmagedon(boolean armagedon) {
		this.armagedon = armagedon;
	}

	public boolean isMoney_rain() {
		return money_rain;
	}

	public void setMoney_rain(boolean money_rain) {
		this.money_rain = money_rain;
	}

	public long getArmagedon_timer() {
		return armagedon_timer;
	}

	public void setArmagedon_timer(long armagedon_timer) {
		this.armagedon_timer = armagedon_timer;
	}

	public long getMoney_rain_timer() {
		return money_rain_timer;
	}

	public void setMoney_rain_timer(long money_rain_timer) {
		this.money_rain_timer = money_rain_timer;
	}
	
	public long getImmortality_timer() {
		return immortality_timer;
	}

	public void setImmortality_timer(long immortality_timer) {
		this.immortality_timer = immortality_timer;
	}

	public boolean isImmortal() {
		return immortal;
	}

	public void setImmortal(boolean immortal) {
		this.immortal = immortal;
	}

	public Unit PlayerToUnit(){
		return new Unit(this.x, this.y, (int)this.angle, this.speed, this.mass, (int) this.radius, this.currentFrame, this.earth_gravity_multiplier, this.earth_radius_multiplier,this.timer, this.earth_timer, this.armagedon_timer, this.money_rain_timer, this.earth_stats_changed, this.current_jump_power, this.on_ground, this.points, this.multiplier, this.armagedon, this.money_rain, this.life, this.sucking_range);
	}
	public void UnitToPlayer(Unit u){
		this.angle = u.getAngle();
		this.armagedon = u.isArmagedon();
		this.armagedon_timer = u.getArmagedon_timer();
		this.current_jump_power = u.getCurrent_jump_power();
		this.currentFrame = u.getCurrentFrame();
		this.earth_gravity_multiplier = u.getEarth_gravity_multiplier();
		this.earth_timer = u.getEarth_timer();
		this.life = u.getLife();
		this.money_rain = u.isMoney_rain();
		this.money_rain_timer = u.getMoney_rain_timer();
		this.multiplier = u.getMultiplier();
		this.on_ground = u.isOn_ground();
		this.points = u.getPoints();
		this.speed = u.getSpeed();
		this.sucking_range = u.getSucking_range();
		this.timer = u.getTimer();
		this.x = u.getX();
		this.y = u.getY();
	}
}
