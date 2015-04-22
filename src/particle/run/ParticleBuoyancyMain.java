package particle.run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import particle.Particle;
import particle.force.ParticleGravityGenerator;
import particle.force.buoyancy.ParticleBuoyancy;
import _math.Real;
import _math.Vector3D;

public class ParticleBuoyancyMain extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final public static void main(String[] args ) {
		new ParticleBuoyancyMain().run();
	}
	
	protected ParticleWorld world;
	protected Particle ball1;
	protected Particle ball2;
	protected PhysicsPanel pnlPhysics = new PhysicsPanel();
	
	public ParticleBuoyancyMain() {
		this.world = new ParticleWorld();
		
		this.ball1 = new Particle( new Real( 10 ) , new Vector3D( new Real( 270 ) , new Real( 150 ), Real.ZERO ) , new Vector3D( new Real( 0 ) , new Real( 0 ) , Real.ZERO ) );
		this.ball2 = new Particle( new Real( 10 ) , new Vector3D( new Real( 210 ) , new Real( 400 ) , Real.ZERO ) , new Vector3D( new Real( 0 ) , Real.ZERO , Real.ZERO ) );
		//this.ball1.setInverseMass( Real.ZERO );
		this.ball1.setDamping( new Real(0.999) );
		this.ball2.setDamping( new Real(0.999) );
		this.world.addParticle( this.ball1 );
		this.world.addParticle( this.ball2 );
		
		ParticleGravityGenerator gravityGenerator = new ParticleGravityGenerator();
		gravityGenerator.addObject( this.ball1 );
		gravityGenerator.addObject( this.ball2 );
		this.world.addForceGenerator( gravityGenerator );
		
		
		
		ParticleBuoyancy buoyancy = new ParticleBuoyancy( new Real( 5 ) , new Real( 300 ) , new Real( 100 ) , new Real( 1 ) );
		buoyancy.addObject( this.ball1 );
		buoyancy.addObject( this.ball2 );
		
		this.world.addForceGenerator( buoyancy );
		
		/*Real dt = Real.ONE.divide( new Real( 1000 ) );
		for ( int i = 0; i < 10000; i++ ) {
			this.world.runPhysics( dt );
			if ( i % 100 == 0 ) {
				//System.out.println( dt.multiply( new Real(i) ) + " seconds : " + ball1 );
				//System.out.println( dt.multiply( new Real(i) ) + " seconds : " + ball2 );
				System.out.println( this.ball1.getVelocity().getY() );
			}
		}*/
		
		add( this.pnlPhysics );
		setSize( 500 , 500 );
		setVisible( true );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	@Override
	public void run() {
		while ( true ) {
			Real dt = Real.ONE.divide( new Real ( 1000 ) );
			this.world.runPhysics( dt );
			this.pnlPhysics.repaint();
			//System.out.println( ball1 );
			//System.out.println( ball2 );
			
			try {
				Thread.sleep( 1 );
			} catch (Exception e) {
				//ignore
			}
		}
	}
	
	private class PhysicsPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public PhysicsPanel() {
			
		}
		
		
		@Override
		public void paint( Graphics graphics ) {
			super.paint( graphics );
			Graphics2D g = ( Graphics2D ) graphics;
			
			g.setColor( Color.blue );
			g.fillRect(0, 400, 500, 500);
			g.setColor( Color.black );
			g.fillOval( (int) ParticleBuoyancyMain.this.ball1.getPosition().getX().value() - 5 , 500 - (int) ParticleBuoyancyMain.this.ball1.getPosition().getY().value() - 5, 10 , 10 );
			g.setColor( Color.red );
			g.fillOval( (int) ParticleBuoyancyMain.this.ball2.getPosition().getX().value() - 5, 500 - (int) ParticleBuoyancyMain.this.ball2.getPosition().getY().value() - 5, 10 , 10 );
			//g.drawLine( (int) ParticleBuoyancyMain.this.ball1.getPosition().getX().value() + 5 , 500 - (int) ParticleBuoyancyMain.this.ball1.getPosition().getY().value() + 5, (int) ParticleBuoyancyMain.this.ball2.getPosition().getX().value() + 5, 500 - (int) ParticleBuoyancyMain.this.ball2.getPosition().getY().value() + 5);
		}
	}
}
